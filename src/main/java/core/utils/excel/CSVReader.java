package core.utils.excel;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FunctionalInterface
public interface CSVReader {


    void ourImplementation(String line);

    static void csvReader(File csvFile, CSVReader csvReader) {
        String line;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),"UTF-8"));

            while ((line = br.readLine()) != null) {
                csvReader.ourImplementation(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String buildLine(String line, String regex, ArrayList<String> listOfRegexConditionsForReplace) {
        line = line.replace("\uFEFF", "");
        line = line.replace("\"", "");
        Pattern p = Pattern.compile(regex);
        Matcher m;
        m = p.matcher(line);
        if (m.matches()) {
            if (listOfRegexConditionsForReplace != null) {
                for (int i = 0; i < listOfRegexConditionsForReplace.size(); i++) {
                    line = line.replaceAll(listOfRegexConditionsForReplace.get(i), "");
                }
            }
        }
        else if (!m.matches()) {
            line = null;
        }
        return line;
    }
}