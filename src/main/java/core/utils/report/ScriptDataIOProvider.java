package core.utils.report;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.Meta;

import java.util.Map;

public interface ScriptDataIOProvider {

    ScriptDataIOProvider instance=new ScriptDataXLSXProvider();

    ExamplesTable generateDatasets(Meta meta);

    Map<Object,Object> loadDataset(String filename, String dataset);

    void prepareOutputFile(Meta meta);

    void appendOutputFile(Meta meta,String result);

}
