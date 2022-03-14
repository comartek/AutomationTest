package core.utils.evaluationManager;

import core.utils.assertExt.AssertExt;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;

import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akarpenko on 03.11.2017.
 */
public class EvaluationManager {

    // конструктор EvaluationManager() инициализирует поле retMap файлом dataPool.json
    private EvaluationManager() {
        //resetPool();
    }


    private final static String GROOVY_SCRIPT_PATTERN = "(?s)\\%\\{(?<script>.*?)\\}\\%";
//    TRY SWAPPING THESE PATTERNS IN CASE OF PROBLEMS WITH PARSING
//    private final static String GROOVY_SCRIPT_PATTERN = "\\%\\{(?<script>.*?)\\}\\%";
    public final static Pattern GROOVY_SCRIPT_PATTERN_COMPILED = Pattern.compile(GROOVY_SCRIPT_PATTERN);


    //private final static Properties properties = new Properties();


    private static ThreadLocal<Map<Object, Object>> variables = new ThreadLocal<Map<Object, Object>>(){
        protected HashMap initialValue( ) {
            return new HashMap<Object, Object>();
        }
    };

    private static Map<String, String> variable_types = new HashMap<String, String>();
        

    public static ThreadLocal<Map<Object, Object>> variablesMeta = new ThreadLocal<Map<Object, Object>>(){
        protected HashMap initialValue( ) {
            return new HashMap<Object, Object>();
        }
    };

    public static ThreadLocal<Map<Object, Object>> scenarioVariablesDB = new ThreadLocal<Map<Object, Object>>(){
        protected HashMap initialValue( ) {
            return new HashMap<Object, Object>();
        }
    };

    public static Map<Object, Object> getVariables(){
        return variables.get();
    }

    public static Map<String, String> getVariableTypes(){
        return variable_types;
    }

    public static void setVariable(String name, Object value) {
        variables.get().put(name, value);
    }

    public static void setVariables(Map<String, String> variablesMap) {
        variables.get().putAll(variablesMap);
    }

    public static Object getVariable(String name) {
        return variables.get().get(name);
    }

    public static void clearVariables(){
        variables.get().clear();
    }

    private static final GroovyShell groovyShell=new GroovyShell();

    private static Object evalGroovy(String expression) {
        for (Map.Entry<Object, Object> entry : variables.get().entrySet()) {
            if (entry.getValue() != null) {
                String variable_name=entry.getKey().toString();
                String variable_value=entry.getValue().toString();

                if(variable_value.isEmpty()){
                    groovyShell.setVariable(variable_name, "");
                    continue;
                }

                if (variable_types.containsKey(variable_name)){
                    switch (variable_types.get(variable_name).toLowerCase()){
                        case "string":
                            groovyShell.setVariable(variable_name, variable_value);
                            continue;
                        case "integer":
                        case "long":
                            groovyShell.setVariable(variable_name, Long.valueOf(deleteTrailingL(variable_value)));
                            continue;
                        case "double":
                            groovyShell.setVariable(variable_name, Double.valueOf(variable_value));
                            continue;
                        case "boolean":
                            groovyShell.setVariable(variable_name, Boolean.valueOf(variable_value));
                            continue;
                    }
                    
                }

                groovyShell.setVariable(variable_name, toNumberOrElseReturnOriginalObject(entry.getValue()));
            }
        }
        Object result=groovyShell.evaluate("import java.text.*;import java.math.*;import static core.utils.evaluationManager.ScriptUtils.*;"+expression);
        return result;
    }

    public static Object toNumberOrElseReturnOriginalObject(Object value){
        String valueAsString = value.toString();
        if (!valueAsString.isEmpty() && NumberUtils.isNumber(valueAsString)) {
            String valueAsString_abs = deleteLeadingDash(valueAsString);
            if (StringUtils.isNumeric(deleteTrailingL(valueAsString_abs))) {
                if (valueAsString_abs.startsWith("0")&&!(valueAsString_abs.equals("0")))
                    return value;
                else
                    return Long.valueOf(deleteTrailingL(valueAsString));
            } else
                return Double.valueOf(valueAsString);
        } else
            return value;
    }

    private static String deleteLeadingDash(String original){
        return original.startsWith("-") ? original.substring(1) : original;
    }
    private static String deleteTrailingL(String original){
        return original.endsWith("L") || original.endsWith("l") ? original.substring(0, original.length() - 1) : original;
    }

    public static <T> T evalVariable(String param) {
        try {
            if (param.trim().matches(".*" + GROOVY_SCRIPT_PATTERN + ".*")) {
                Matcher groovyMatcher = GROOVY_SCRIPT_PATTERN_COMPILED.matcher(param);
                StringBuffer groovySB = new StringBuffer();
                while (groovyMatcher.find()) {
                    String value = String.valueOf(evalGroovy(groovyMatcher.group("script")));
                    //do quoteReplacement to avoid exceptions caused by \ and $ characters in value
                    String value_quote=Matcher.quoteReplacement(value);
                    groovyMatcher.appendReplacement(groovySB, value_quote);
                }
                groovyMatcher.appendTail(groovySB);
                T result = evalVariable(groovySB.toString());
                Logger.getRootLogger().info(String.format("[Parameter evaluation output]: %s -> %s",param,result));
                return result;
            }
            return (T)param;
        } catch (Exception e) {
            e.printStackTrace();
//            try {
//                Thread.sleep(30000);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            Assert.fail(String.format("Parsing error of parameter: [%s]\n%s", param, Arrays.toString(e.getStackTrace())));
//            return null;
            throw new AssertionError(String.format("Parsing error of parameter: [%s].\nCaused by:\n%s", param, e.getMessage()), e);
        }
    }

    public static ExamplesTable evaluateExampleTable(ExamplesTable table){
        List<Map<String, String>> newRows=new ArrayList<>();

        for (Map<String, String> row : table.getRows()) {
            Map<String,String> newRow=new LinkedHashMap<>();
            for (String header:table.getHeaders()) {
                String value = evalVariable(row.get(header));
                newRow.put(header,value);
            }
            newRows.add(newRow);
        }
        return (new ExamplesTable("")).withRows(newRows);
    }



}
