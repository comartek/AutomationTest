package core.utils.file;

import org.junit.Assert;
import sun.security.action.GetPropertyAction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.util.ArrayList;

public class FileUtils {

    public static String readFileTextualContent(String filePath){
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);

            ArrayList<String> resultList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultList.add(line);
            }
            reader.close();

            String res = String.join("\n", resultList.toArray(new String[]{}));
            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeTextIntoFile(String filePath, boolean overwriteFileContent, String contentToWrite){
        contentToWrite = contentToWrite.replace("\n", "\r\n");
        try {
            FileWriter writer = new FileWriter(filePath, !overwriteFileContent);
            writer.write(contentToWrite);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createTmpFile(String fileName, String fileExtension){
            fileExtension = fileExtension.startsWith(".") ? fileExtension : "." + fileExtension;
            return createTmpFile(fileName + fileExtension);
    }

    public static String createTmpFile(String fileNameWIthExtension){
        try{
            final File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
            Path path = Paths.get(tmpdir + File.separator + fileNameWIthExtension);
            Files.createFile(path);
            return path.toString();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static File findFileInDefaultDownloadsFolderByName(String fileName){
        File[] files = null;
        String location = System.getProperty("user.home") + "\\Downloads";

        System.out.println("Searching file '" + fileName + "' in directory: " + location);
        for (int i = 0; i < 60; i++) {
            System.out.println("Iteration #" + i);
            File directory = new File(location);
            files = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.matches(fileName + ".+");
                }
            });
            try {
            if (files == null) Assert.fail("File was not found");
            if (files.length > 1) Assert.fail("More than 1 file found");
            if (files.length > 0 && files[0].exists()) {
                if (!files[0].getAbsolutePath().endsWith(".crdownload")) {
                    System.out.println(String.format("File '%s' was found", files[0].getAbsolutePath()));
                    break;
                }
                else Thread.sleep(1000);
            } else {
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return files[0];
    }
}
