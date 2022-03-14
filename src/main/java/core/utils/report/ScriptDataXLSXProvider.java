package core.utils.report;

import core.utils.evaluationManager.EvaluationManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Meta;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ScriptDataXLSXProvider  implements ScriptDataIOProvider{

    private static String DATASET_NAME_HEADER = "DATASET_NAME";

    Map<String,Map<Object,Object>> datasets;

    @Override
    public ExamplesTable generateDatasets(Meta meta) {
        ExamplesTable table = new ExamplesTable("");

        datasets = new HashMap<>();

        String result = "";
        InputStream in = null;
        XSSFWorkbook wb = null;
        try {
            in = new FileInputStream(meta.getProperty("input_file"));
            wb = new XSSFWorkbook(in);

            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();
            ArrayList<String> columns = new ArrayList<>();
            boolean hasDatasetNameColumn=false;

            // read header row until first blank field
            for (int i=0;i<row.getLastCellNum();i++){
                Cell cell = row.getCell(i,Row.CREATE_NULL_AS_BLANK);
                String paramHeader =cell.getStringCellValue();
                if (StringUtils.isNotBlank(paramHeader)) {
                    if (paramHeader.equalsIgnoreCase(DATASET_NAME_HEADER))
                        hasDatasetNameColumn=true;
                    else
                        columns.add(paramHeader);
                }
                else
                    break;
            }

            // read data rows until first entirely empty row (all cells below headers do not exist, are null or " ")


            String table_as_string="|dataset|\n";
            int i=0;
            while (rows.hasNext()){
                row = rows.next();

                String datasetName;
                if (hasDatasetNameColumn) {
                    datasetName = row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (StringUtils.isEmpty(datasetName))
                        datasetName = String.format("Dataset_%s",i);
                }
                else
                    datasetName = String.format("Dataset_%s",i);


                DataFormatter dataFormatter = new DataFormatter();
                HashMap<Object, Object> datasetMap = new HashMap<>();
                for (int j=0;j<columns.size();j++){
                    Cell cell = row.getCell(
                            hasDatasetNameColumn?j+1:j,
                            Row.CREATE_NULL_AS_BLANK);//cells.next();

                    String value=dataFormatter.formatCellValue(cell);
                    System.out.println(columns.get(j)+"="+value);

                    datasetMap.put(columns.get(j),value);
                }

                if (datasetMap.entrySet().stream().allMatch(entry->StringUtils.isBlank(entry.getValue().toString())))
                    continue;
                else {
                    datasets.put(datasetName, datasetMap);
                    table_as_string+=String.format("|%s|\n",datasetName);
                    i++;
                }
            }
            if (!table_as_string.equals("|dataset|\n"))
                table=new ExamplesTable(table_as_string);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return table;
    }

    @Override
    public Map<Object,Object> loadDataset(String filename, String dataset) {
        return datasets.get(dataset);
    }

    @Override
    public void prepareOutputFile(Meta meta) {
        if (meta.hasProperty("output_file")) {
            try {
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet();
                XSSFRow row = sheet.createRow(0);

                ArrayList<String> header = new ArrayList<>();
                if (meta.hasProperty("output_variables")) {
                    List<String> output_variables = Arrays.asList(meta.getProperty("output_variables").split(","));
                    for (int i = 0; i < output_variables.size(); i++){
                        output_variables.set(i, output_variables.get(i).trim());
                    }
                    header.addAll(output_variables);
                }
                header.add("SCRIPT_STATUS");

                for(int i=0;i<header.size();i++){
                    XSSFCell cell = row.createCell(i);
                    cell.setCellValue(header.get(i));
                }

                wb.write(new FileOutputStream(meta.getProperty("output_file")));
                wb.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void appendOutputFile(Meta meta,String result) {
        if (meta.hasProperty("output_file")) {
            try {
                FileInputStream in = new FileInputStream(meta.getProperty("output_file"));
                XSSFWorkbook wb = new XSSFWorkbook(in);
                XSSFSheet sheet = wb.getSheetAt(0);
                int numRows=sheet.getPhysicalNumberOfRows();
                XSSFRow row = sheet.createRow(numRows);

                ArrayList<String> vars = new ArrayList<>();
                if (meta.hasProperty("output_variables")) {
//                    List<String> output_variables = Arrays.stream(meta.getProperty("output_variables").split(",")).map(var -> EvaluationManager.getVariable(var.trim()).toString()).collect(Collectors.toList());
                    List<String> output_variables_names = Arrays.asList(meta.getProperty("output_variables").split(","));
                    for (int i = 0; i < output_variables_names.size(); i++){
                        output_variables_names.set(i, output_variables_names.get(i).trim());
                    }

                    List<String> output_variables_values = output_variables_names.stream().map(var -> {
                        Object value = EvaluationManager.getVariable(var.trim());
                        return value == null ? "" : value.toString();
                    }).collect(Collectors.toList());
                    vars.addAll(output_variables_values);
                }
                vars.add(result);

                for(int i=0;i<vars.size();i++){
                    XSSFCell cell = row.createCell(i);
                    cell.setCellValue(vars.get(i));
                }

                wb.write(new FileOutputStream(meta.getProperty("output_file")));
                wb.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
