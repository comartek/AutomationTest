package core.utils.evaluationManager;

import core.applications.Application;
import core.applications.ApplicationManager;
import core.utils.datapool.DataPool;
import core.utils.lang.LangUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.utils.lang.LangUtils.getDefaultLanguage;
import static org.junit.Assert.assertTrue;

/**
 * Created by Akarpenko on 03.11.2017.
 */

// Functions for groovy scripts in stories
public class ScriptUtils {
    /**********  regexp  **********/
    public static String regex(Object value, String mask){
        return regex(value, mask,false);
    }
    public static String regex(Object value, String mask, boolean isMultiline){
        return regex(value, mask,1,isMultiline);
    }

    public static String regex(Object value, String mask, int groupNumber){
        return regex(value, mask,groupNumber,false);
    }

    public static String regex(Object value, String mask,int groupNumber, boolean isMultiline) {

        Pattern pattern = isMultiline ?
                Pattern.compile(mask, Pattern.DOTALL) :
                Pattern.compile(mask);
        Matcher matcher = pattern.matcher(value.toString());

        boolean flag = matcher.find();
        assertTrue("No match for pattern:'" + mask + "' in string:" + value, flag);
        if (flag) {
            return matcher.group(groupNumber);
        } else {
            return null;
        }
    }

    /**********  lang  **********/
    public static String lang(String en_string, String vi_string){
        LangUtils.Language language = getDefaultLanguage();
        Application activeApplication = ApplicationManager.getInstance().get().ActiveApplication;
        if(activeApplication != null){
            language = activeApplication.language;
        }
        switch (language){
            case EN_US:
                return en_string;
            case VI_VN:
                return vi_string;
            default:
                throw new RuntimeException("Error in ScriptUtils.lang method");
        }
    }

    /**********  meta + metaIgnoreCase  **********/
    public static String meta(String variable) {
        Map<Object, Object> map = EvaluationManager.variablesMeta.get();
        Object metaValue = map.get(variable);
        if(metaValue == null){
            throw new RuntimeException(String.format("Meta variable with name '%s' has not been found in the scenario meta parameters: '%s'", variable, map.keySet().toString()));
        }
        return metaValue.toString();
    }

    public static String metaIgnoreCase(String variable) {
        Map<Object, Object> map = EvaluationManager.variablesMeta.get();
        for(Object metaKey : map.keySet()){
            if (metaKey.toString().equalsIgnoreCase(variable)){
                return map.get(metaKey).toString();
            }
        }
        throw new RuntimeException(String.format("Meta variable with name '%s' has not been found in the scenario meta parameters: '%s'", variable, map.keySet().toString()));
    }

    /**********  localDB  **********/
    public static String localDB(String variable) {
        Map<Object, Object> map = EvaluationManager.scenarioVariablesDB.get();
        if(map.containsKey(variable)){
            return map.get(variable).toString();
        } else {
            throw new RuntimeException(String.format("Parameter with variable name '%s' has not been found in the DB for the chosen dataset (existing variable names: %s)", variable, Arrays.toString(map.keySet().toArray())));
        }
    }

    /**********  env  **********/
    public static String env(String variable){
        return System.getProperty(variable);
    }

    /**********  msg + msgFrom  **********/
    public static String msg(String id,Object... args){
        String dictionary=System.getProperty("message_dictionary");
        if (StringUtils.isEmpty(dictionary))
            dictionary = "messages_t24";
        return msgFrom(dictionary, id,args);
    }

    public static String msgFrom(String dictionary,String id,Object... args){
        Properties props = new Properties();
        InputStream input = null;
        if (!dictionary.startsWith("dictionaries/"))
            dictionary = "dictionaries/"+dictionary;
        if (!dictionary.endsWith(".properties"))
            dictionary = dictionary+".properties";
        try {

            input = new FileInputStream(dictionary);
            // load a properties file
//            props.load(input);
            InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader buffReader = new BufferedReader(isr);
            // load a properties file
            props.load(buffReader);

            String msg=props.getProperty(id);
            String msg_format = String.format(msg,args);
            return msg_format;


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**********  now  **********/
    public static String now() {
        return now("");
    }

    public static String now(String format) {
        Calendar cl = Calendar.getInstance();
        if (format.isEmpty())
            format = "dd.MM.yyyy";
        DateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return formatter.format(cl.getTime());
    }

    /**********  date  **********/
    public static String date(Object input,String shift) throws ParseException {
        return date(input, shift,"","");
    }

    public static String date(Object input,String shift,String format) throws ParseException{
        return date(input, shift,format,"");
    }

    public static String date(Object input,String shift,String inputFormat,String outputFormat) throws ParseException {
        String pattern = "([\\+\\-])\\s*(\\d+)\\s*([YyMdHhmsS])\\s*$";

        Calendar cl = Calendar.getInstance();
        if (inputFormat.isEmpty())
            inputFormat = "dd.MM.yyyy";
        if (outputFormat.isEmpty())
            outputFormat = inputFormat;
        DateFormat formatter = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        cl.setTime(formatter.parse(input.toString()));

        if(!shift.trim().equals("")) {
            String[] shiftSubPatterns = shift.split(",");
            for (String shiftSubPattern : shiftSubPatterns) {
                shiftSubPattern = shiftSubPattern.trim();
                Matcher match = Pattern.compile(pattern).matcher(shiftSubPattern);
                String sign = null;
                String amount = null;
                String points = null;
                if (match.find()) {
                    sign = match.group(1);
                    amount = match.group(2);
                    points = match.group(3);
                }
                if (sign == null) {
                    throw new RuntimeException("Shift pattern '" + shiftSubPattern + "' is not correct. Pattern should begin with sign ('+' or '-'), following by number (0-N), following by interval type ('Y' or 'y' - year; 'M' - month; 'd' - day of month; 'H' - hour (0-23); 'h' - hour (1-12); 'm' - minute; 's' - second; 'S' - millisecond). * Spaces between sign, number and type are allowed ('+ 7 d') but not necessary ('+7d')");
                } else {
                    int fld = -1;
                    switch (points) {
                        case "Y":
                            fld = Calendar.YEAR;
                            break;
                        case "y":
                            fld = Calendar.YEAR;
                            break;
                        case "M":
                            fld = Calendar.MONTH;
                            break;
                        case "d":
                            fld = Calendar.DATE;
                            break;
                        case "H":
                            fld = Calendar.HOUR_OF_DAY;
                            break;
                        case "h":
                            fld = Calendar.HOUR;
                            break;
                        case "m":
                            fld = Calendar.MINUTE;
                            break;
                        case "s":
                            fld = Calendar.SECOND;
                            break;
                        case "S":
                            fld = Calendar.MILLISECOND;
                            break;
                    }
                    cl.add(fld, (sign.equals("-") ? -1 : 1) * Integer.valueOf(amount));
                }
            }
        }

        formatter = new SimpleDateFormat(outputFormat,  Locale.ENGLISH);
        return formatter.format(cl.getTime());
    }

    /**********  dp  **********/
    public static Object dp(String var){
        Object value = DataPool.getInstance().getPool().get(var);
        if(value == null){
            throw new RuntimeException(String.format("Datapool variable '%s' has not been found in the datapool section '%s' (section type = %s)", var, DataPool.currentSectionName, DataPool.currentDatapoolSectionType));
        }
        return EvaluationManager.toNumberOrElseReturnOriginalObject(String.valueOf(value));
    }

    /**********  random  **********/
    public static Integer random(Object min,Object max) {
        int _min = Integer.parseInt(min.toString());
        int _max = Integer.parseInt(max.toString());
        Random rn = new Random();
        int range = _max - _min + 1;
        int randomNum =  rn.nextInt(range) + _min;
        return randomNum;
    }

    /**********  formatNumber + formatNumberAndRound +  formatNumberAndFloor  **********/
    public static String formatNumber(Object number) {
        return NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(number.toString()));
    }

    public static String formatNumber(Object number, String currency) {
        return formatNumber(number, currency, RoundingMode.HALF_UP);
    }

    public static String formatNumber(Object number, RoundingMode roundingMode) {
        Double d = Double.parseDouble(number.toString());
        if(roundingMode == RoundingMode.HALF_UP){
            return formatNumber((int)Math.round(d));
        } else if(roundingMode == RoundingMode.FLOOR){
            return formatNumber((int)Math.floor(d));
        }else if(roundingMode == RoundingMode.UP){
            return formatNumber((int)Math.ceil(d));
        } else {
            throw new UnsupportedOperationException("formatNumber(Object number, RoundingMode roundingMode) method - is not implemented for roundingMode = '" + roundingMode.toString() + "' contact framework developer");
        }
    }

    public static String formatNumber(Object number, String currency, RoundingMode roundingMode) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        String pattern=null;
        switch (currency){
            case "VND":
                pattern = "###,###";
                break;
            case "USD":
            case "EUR":
                pattern = "###,##0.00";
                break;
        }
        DecimalFormat format = new DecimalFormat(pattern,symbols);
        format.setRoundingMode(roundingMode);
        return format.format(Double.parseDouble(number.toString()));
    }

//    public static String formatNumberOCB(String number,String currency) {
//        switch (currency){
//            case "VND":
//                return "";
//        }
//    }

    public static String formatNumberAndRound (Object number){
        return formatNumber(number, RoundingMode.HALF_UP);
    }
    public static String formatNumberAndRound (Object number,String currency){
        return formatNumber(number, currency, RoundingMode.HALF_UP);
    }
    public static String formatNumberAndRoundUp (Object number){
        return formatNumber(number, RoundingMode.UP);
    }
    public static String formatNumberAndRoundUp (Object number,String currency){
        return formatNumber(number, currency, RoundingMode.UP);
    }

    public static String formatNumberAndFloor (Object number) {
        return formatNumber(number, RoundingMode.FLOOR);
    }
    public static String formatNumberAndFloor (Object number,String currency) {
        return formatNumber(number, currency, RoundingMode.FLOOR);
    }

    /**********  unformatMoneyNumber  **********/
    public static Object unformatMoneyNumber(Object number) {
        String numberWithoutComas = number.toString().replace(",", "");
        return EvaluationManager.toNumberOrElseReturnOriginalObject(numberWithoutComas);
    }

    public static Object unformatMoneyNumber(Object number, String currency) {

        String value = number.toString();
        String pattern = String.format("(.*) %s", currency);
        Matcher match = Pattern.compile(pattern).matcher(value);
        if (match.find()) {
            value = match.group(1);
        }

        pattern = String.format("%s(.*)", currency);
        match = Pattern.compile(pattern).matcher(value);
        if (match.find()) {
            value = match.group(1);
        }

        return unformatMoneyNumber(value.trim());
    }

    /**********  round  **********/
    public static String round(Object number) {
        Double d = Double.parseDouble(number.toString());
        return String.valueOf((int)Math.round(d));
    }

    public static String roundWithDecimals(Object number, int digitsAfterDecimalPoint) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String pattern = "##." + StringUtils.repeat("0", digitsAfterDecimalPoint);
        DecimalFormat format = new DecimalFormat(pattern,symbols);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(Double.parseDouble(number.toString()));
    }

    /**********  currencyMark  **********/
    public static String currencyMark(String currencyCode) {
        switch (currencyCode){
            case "VND":
                return "₫";
            case "USD":
                return "$";
            case "EUR":
                return "€";
            default :
                return "";
        }

//        for (Locale locale : NumberFormat.getAvailableLocales()) {
//            Currency currency = NumberFormat.getCurrencyInstance(locale).getCurrency();
//            if (currency.getCurrencyCode().equals(currencyCode))
//                return currency.getSymbol(locale);
//        }
    }

    /**********  toInteger  **********/
    public static Integer toInteger(Object value) {
        if (value instanceof Integer)
            return (Integer)value;
        return ((BigDecimal) value).intValue();
    }

    /**********  toDouble  **********/
    public static Double toDouble(Object value) {
        if (value instanceof Double)
            return (Double)value;
        return Double.valueOf(value.toString());
    }

    /**********  maskCard  **********/
    public static String maskCard(Object number) {
        int unmaskedLengthOneSide=4;
        String numberAsString = number.toString();
        return numberAsString.substring(0,unmaskedLengthOneSide) + StringUtils.repeat("X", numberAsString.length() - unmaskedLengthOneSide*2) + numberAsString.substring(numberAsString.length()-unmaskedLengthOneSide);
    }

    /**********  str  **********/
    //    Method to retrieve true string value of variable, for those vars that get auto-cast to double/int in scripts
    public static String str(String var) {
        return String.valueOf(EvaluationManager.getVariable(var));
    }

    /**********  xpath + xpathFrom  **********/
    public static String xpath(String id,Object... args){
        String dictionary=System.getProperty("xpath_dictionary");
        if (StringUtils.isEmpty(dictionary))
            dictionary = "xpath_t24";
        return xpathFrom(dictionary, id,args);
    }

    public static String xpathFrom(String dictionary,String id,Object... args){
        Properties props = new Properties();
        InputStream input = null;
        if (!dictionary.startsWith("dictionaries/"))
            dictionary = "dictionaries/"+dictionary;
        if (!dictionary.endsWith(".properties"))
            dictionary = dictionary+".properties";
        try {

            input = new FileInputStream(dictionary);
            InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader buffReader = new BufferedReader(isr);
            props.load(buffReader);

            String xpath=props.getProperty(id);
            String xpath_format = String.format(xpath,args);
            return xpath_format;


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**********  String  **********/
    public static String sortABC(String stringSort) {
        char []arr = stringSort.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }


}
