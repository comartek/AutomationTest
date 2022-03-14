package core.utils.datapool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import core.utils.report.MetaParser;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Akarpenko on 02.11.2017.
 */
public class DataPool {
    private static DataPool instance;
    private LinkedTreeMap<String,Object> retMap;
    private LinkedTreeMap<String,Object> currentRetMap;
    public static SectionType currentDatapoolSectionType;
    public static String currentSectionName;

    // конструктор DataPool() инициализирует поле retMap файлом dataPool.json
    private DataPool() {
        resetPool();
    }

    public static DataPool getInstance() {
        if (instance == null) {
            instance = new DataPool();
            currentDatapoolSectionType = SectionType.ROOT;
            currentSectionName = "";
        }
        return instance;
    }

    public LinkedTreeMap<String,Object> getPool() {
        return currentRetMap;
    }

    public LinkedTreeMap<String,Object> switchToSection(String sectionName) {
        currentDatapoolSectionType = SectionType.SECTION_WITH_DATASET;
        currentSectionName = sectionName;
        //datapool structure is 2-level:
        // 1 - section name, as written in scripts in "When switch to datapool section" steps. Usually relates to script chain name
        // 2 - dataset name. Used to split data inside a section for correct transfer of parameters between scripts in chain when executing on multiple datasets.
        //     If current dataset is null (unnamed dataset), second level is omitted
        currentRetMap = (LinkedTreeMap<String, Object>) retMap.get(sectionName);
        if (currentRetMap ==null) {
            retMap.put(sectionName, new LinkedTreeMap<String, Object>());
            savePool();
            currentRetMap =(LinkedTreeMap<String, Object>) retMap.get(sectionName);
        }
        if (MetaParser.getCurrentDataset()!=null){
            String subSectionName=MetaParser.getCurrentDataset();
            LinkedTreeMap<String, Object> subSectionMap = (LinkedTreeMap<String, Object>)currentRetMap.get(subSectionName);

            if (subSectionMap ==null) {
                currentRetMap.put(subSectionName, new LinkedTreeMap<String, Object>());
                savePool();
                currentRetMap =(LinkedTreeMap<String, Object>) currentRetMap.get(subSectionName);
            }
            else currentRetMap = subSectionMap;
        }
        return currentRetMap;
    }

    public LinkedTreeMap<String,Object> switchToSectionWithoutDataset(String sectionName) {
        currentDatapoolSectionType = SectionType.SECTION_WITHOUT_DATASET;
        currentSectionName = sectionName;
        currentRetMap = (LinkedTreeMap<String, Object>) retMap.get(sectionName);
        if (currentRetMap ==null) {
            retMap.put(sectionName, new LinkedTreeMap<String, Object>());
            savePool();
            currentRetMap =(LinkedTreeMap<String, Object>) retMap.get(sectionName);
        }
        return currentRetMap;
    }

    public LinkedTreeMap<String,Object> switchToRoot() {
        currentDatapoolSectionType = SectionType.ROOT;
        currentSectionName = "";
        currentRetMap = retMap;
        return currentRetMap;
    }

    public void savePool() {
        try {
            File myFile = new File("./dataPool.json");
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            myOutWriter.append(gson.toJson(retMap));
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {

        }

    }

    public  void resetPool()
    {
        try {
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            InputStream stream = new FileInputStream("./dataPool.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            /* читаем dataPool.json в BufferedReader (говорим ему что dataPool.json в кодировке UTF-8)
            в строке ниже передаём конструктору:
            - .json в виде br и
            - объект типа класс ClassPool.class, который является описанием того как из .json сделать наш ClassPool */
            retMap = new Gson().fromJson(br, type);
            currentRetMap = retMap;

        } catch (Exception e) {

        }

    }

    public enum SectionType{ROOT, SECTION_WITH_DATASET, SECTION_WITHOUT_DATASET}
}
