package core.utils.report;

import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.VideoFormatKeys.*;


public class VideoCaptureUtils {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!!!!   CODEC: https://assets.techsmith.com/Downloads/TSCC.msi    !!!!!!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private static ScreenRecorder screenRecorder = null;
    private static Boolean screenCaptureActivated = null;
    private static Boolean addVideoTimestampIntoAllureSteps = null;
    private static Boolean copyMovieIntoTheTmpDir = null;
    private static HashSet<String> copiedFilesPaths = new HashSet<>();
    private static HashMap<String,String> filesToRename = new HashMap<>();



    public static void startScreenRecording(String textToReplace_ScreenRecording_InTheFileName){
        startScreenRecording();

        String filePathBeforeRename = getLastScreenRecordFilePath();
        String filePathAfterRename = getScreenRecordingFilePathAfterRename(filePathBeforeRename, textToReplace_ScreenRecording_InTheFileName);
        filesToRename.put(filePathBeforeRename, filePathAfterRename);
    }

    public static void startScreenRecording(){
        try {
            if(screenRecorder == null)
                setUpScreenRecorder();
            screenRecorder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopScreenRecording(){
        try {
            screenRecorder.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //replace "ScreenRecording" with specified text
        String filePath = getLastScreenRecordFilePath();
        String newFilePath = filesToRename.get(filePath);
        if(newFilePath != null){
            File file = new File(filePath);
            if(file.exists())
                file.renameTo(new File(newFilePath));
        }

        System.out.println();
        System.out.println("VIDEO IS CREATED: " + (newFilePath != null ? newFilePath : filePath));

        //copy file if needed
        if(isCopyMovieIntoTheTmpDirActivated())
            copyMovieIntoTheTmpDir();
    }

    public static void copyMovieIntoTheTmpDir(){
        String filePath = getLastScreenRecordFilePath();
        String replacedFilePath = filesToRename.get(filePath);
        filePath = replacedFilePath != null ? replacedFilePath : filePath;

        File screenRecordFilePath = new File(filePath);
        File tmpFolder = new File(getTmpVideoFolderPath());
        String tmpFilePath = tmpFolder.getAbsolutePath() + File.separator + screenRecordFilePath.getName();
        //Avoiding double copy
        if(!copiedFilesPaths.contains(tmpFilePath)) {
            try {
                FileUtils.copyFileToDirectory(screenRecordFilePath, tmpFolder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("VIDEO IS COPIED INTO TMP FOLDER: " + tmpFilePath);
            copiedFilesPaths.add(tmpFilePath);
        }
    }

    public static String getCurrentRecordTimeStamp(){
        long timeElapsedInMillis = System.currentTimeMillis() - screenRecorder.getStartTime();
        long hours = timeElapsedInMillis / 1000 / 3600;
        long minutes = (timeElapsedInMillis - hours * 1000 * 3600) / 1000 / 60;
        long seconds = (timeElapsedInMillis - hours * 1000 * 3600 - minutes *  1000 * 60) / 1000;
        return (hours > 0 ? hours + ":" : "") + (minutes > 9 ? minutes : "0" + minutes) + ":" + (seconds > 9 ? seconds : "0" + seconds);
    }

    public static String getLastScreenRecordFilePath(){
        List<File> createdMovieFiles = screenRecorder.getCreatedMovieFiles();
        return createdMovieFiles.get(createdMovieFiles.size() - 1).getAbsolutePath();
    }

    public static String getTmpVideoFolderPath(){
        return System.getProperty("java.io.tmpdir") + File.separator + "screenrecorder";
    }

    public static String getScreenRecordingFilePathAfterRename(String screenRecordingFilePathBeforeRename, String textToReplace_ScreenRecording_InTheFileName){
        File file = new File(screenRecordingFilePathBeforeRename);
        return file.getParent() + File.separator + file.getName().replace("ScreenRecording", textToReplace_ScreenRecording_InTheFileName.replaceAll("[\\\\/:*?\"<>|]", "_"));
    }



    public static boolean isScreenCaptureActivated(){
        if(screenCaptureActivated == null){
            screenCaptureActivated = Boolean.valueOf(System.getProperty("activateVideoRecorder"));
        }
        return screenCaptureActivated;
    }
    public static boolean isAddVideoTimestampIntoAllureStepsActivated(){
        if(addVideoTimestampIntoAllureSteps == null){
            addVideoTimestampIntoAllureSteps = Boolean.valueOf(System.getProperty("addVideoTimestampIntoAllureSteps"));
        }
        return addVideoTimestampIntoAllureSteps;
    }
    public static boolean isCopyMovieIntoTheTmpDirActivated(){
        if(copyMovieIntoTheTmpDir == null){
            copyMovieIntoTheTmpDir = Boolean.valueOf(System.getProperty("copyMovieIntoTheTmpDir"));
        }
        return copyMovieIntoTheTmpDir;
    }




    private static void setUpScreenRecorder() {
        //Create a instance of GraphicsConfiguration to get the Graphics configuration
        //of the Screen. This is needed for ScreenRecorder class.
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        //Create a instance of ScreenRecorder with the required configurations (video recording only starts with "screenRecorder.start();")
        //the higher frame rate the smoother picture will be, however file size will be bigger (recommended 10 - 15)
        //QualityKey and KeyFrameIntervalKey can also have a big impact on the quality as well as file size
        int frameRate = 15;
        File movieFolder = new File(System.getProperty("movieFolder"));

        try {
            screenRecorder = new ScreenRecorder(gc,
                    (Rectangle)null,
                    new Format(
                            MediaTypeKey, FormatKeys.MediaType.FILE,
                            MimeTypeKey, MIME_AVI),
                    new Format(
                            MediaTypeKey, FormatKeys.MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, (int)24,
                            FrameRateKey, Rational.valueOf(frameRate),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, (int) (30 * 30)), // 1 key frame every 30 seconds
                    new Format(
                            MediaTypeKey, MediaType.VIDEO,
                            EncodingKey,"black",
                            FrameRateKey, Rational.valueOf(30)),
                    null,
                    movieFolder
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
