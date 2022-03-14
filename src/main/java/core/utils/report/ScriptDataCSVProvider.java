package core.utils.report;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.CSVWriter;
import core.utils.evaluationManager.EvaluationManager;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Meta;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScriptDataCSVProvider implements ScriptDataIOProvider {

    private CSVWriter getCsvWriter(String filename,boolean append) throws IOException{
        return new CSVWriter(new FileWriter(filename,append), ';',CSVWriter.DEFAULT_QUOTE_CHARACTER,CSVWriter.RFC4180_LINE_END);
    }

    @Override
    public ExamplesTable generateDatasets(Meta meta) {
        ExamplesTable table = new ExamplesTable("");

        try {
//            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(meta.getProperty("input_file")));
            CSVReaderHeaderAware reader = (CSVReaderHeaderAware)new CSVReaderHeaderAwareBuilder(new FileReader(meta.getProperty("input_file"))).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            String table_as_string="|dataset|\n";
            int i=0;
            while (reader.readNext()!=null){
                table_as_string+=String.format("|Dataset_%s|\n",i);
                i++;
            }
            if (!table_as_string.equals("|dataset|\n"))
                table=new ExamplesTable(table_as_string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    @Override
    public Map<Object, Object> loadDataset(String filename, String dataset) {
        HashMap<Object, Object> datasetMap = new HashMap<>();
        try {

            CSVReaderHeaderAware reader = (CSVReaderHeaderAware)new CSVReaderHeaderAwareBuilder(
                    new InputStreamReader(new BOMInputStream(new FileInputStream(filename))))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').withQuoteChar('"').withStrictQuotes(false).build()).build();

            int datasetIndex= Integer.parseInt(dataset.split("_")[1]);
            reader.skip(datasetIndex);
            Map<String, String> readerMap = reader.readMap();
            for(String name:readerMap.keySet()){
                datasetMap.put(name, readerMap.get(name));
            }
        } catch (Exception e) {
            Logger.getRootLogger().error(e.toString());
        }
        return datasetMap;
    }

    @Override
    public void prepareOutputFile(Meta meta) {
        if (meta.hasProperty("output_file")) {
            try (CSVWriter writer = getCsvWriter(meta.getProperty("output_file"),false)) {
                if (meta.hasProperty("output_variables")) {
                    String[] header = meta.getProperty("output_variables").split(",");
                    writer.writeNext(header);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void appendOutputFile(Meta meta,String result) {
        if (meta.hasProperty("output_file")) {

            try (CSVWriter writer = getCsvWriter(meta.getProperty("output_file"),true)) {
                if (meta.hasProperty("output_variables")) {
                    String[] vars = Arrays.stream(meta.getProperty("output_variables").split(",")).map(var -> EvaluationManager.getVariable(var).toString()).toArray(String[]::new);
                    writer.writeNext(vars);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

