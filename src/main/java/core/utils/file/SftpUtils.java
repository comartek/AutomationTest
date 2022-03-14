package core.utils.file;


import com.jcraft.jsch.*;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.report.ReportUtils;
import org.junit.Assert;

import java.io.*;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.utils.file.FileUtils.createTmpFile;
import static core.utils.file.FileUtils.readFileTextualContent;
import static core.utils.file.FileUtils.writeTextIntoFile;

public class SftpUtils {
    private static Session session;
    private static Channel channel;
    private static ChannelSftp channelSftp;

    public static String generateDot48FileForT24SftpServer(String fileNameWithExtension, String fileContent){
        String tmpFilePath = createTmpFile(fileNameWithExtension);
        writeTextIntoFile(tmpFilePath, true, fileContent);
        return tmpFilePath;
    }

    public static String generateIncorrectDot48FileForT24SftpServer(){
        String SftpExampleText = readFileTextualContent("VCB_UATest_negative.048");
        String nowToString = ScriptUtils.now("ddMMyyHHmmss");
        String tmpFilePath = createTmpFile("UATest_" + nowToString, ".48");
        //String SftpNewText = SftpExampleText.replaceAll(":20:AT.*\n", ":20:AT" + nowToString + "\n");
        writeTextIntoFile(tmpFilePath, true, SftpExampleText);
        return tmpFilePath;
    }


    public static void sendFileToT24SftpServer(String filePath, String SftpWorkingDir) {
        setSftpConnection(SftpWorkingDir);
        String connectionInfo = String.format("Host: %s, Port: %s, User: %s, Sftp Working Directory: %s", session.getHost(), session.getPort(), session.getUserName(), SftpWorkingDir);
//        System.out.println("preparing the host information for sftp.");
        try {
            File f = new File(filePath);
            channelSftp.put(new FileInputStream(f), f.getName());
            ReportUtils.attachMessage("File is sent to T24 sftp server", connectionInfo);
        } catch (Exception ex) {
            throw new RuntimeException("Exception found while sending file by sftp (" + connectionInfo + ")", ex);
//            System.out.println("Exception found while sending file by sftp." + ex.toString());
        }
        finally{
            closeSftpConnection();
        }
    }

    public static void isSftpFileExist(String SftpWorkingDir, String pattern) {
        Boolean isFind = false;
        setSftpConnection(SftpWorkingDir);
        Vector<ChannelSftp.LsEntry> list = null;
        try {
            list = channelSftp.ls("/t24data/t24eoy/t24if/atm/CARD.ISS.BP/*");
            for (ChannelSftp.LsEntry entry : list) {
                Matcher matcher = Pattern.compile(pattern).matcher(entry.getFilename());
                if (matcher.find()) {
                    isFind = true;
                    break;
                }
            }
        } catch (SftpException e) {
            closeSftpConnection();
            throw new RuntimeException("Exception found while find sftp file path", e);
        }
        finally{
            closeSftpConnection();
        }
        Assert.assertTrue(String.format("file is not fount in directoru %s by patern %s ",SftpWorkingDir,pattern),isFind);
    }
    private static void closeSftpConnection() {
        if (channelSftp!=null&&channelSftp.isConnected())
            channelSftp.exit();
//            System.out.println("sftp Channel exited.");
        if (channel!=null&&channel.isConnected())
            channel.disconnect();
//            System.out.println("Channel disconnected.");
        if (session!=null&&session.isConnected())
            session.disconnect();
//            System.out.println("Host Session disconnected.");
    }
    private static void setSftpConnection(String SftpWorkingDir){

        String SFTPHOST = System.getProperty("SFTPHOST");
        int SFTPPORT = Integer.parseInt(System.getProperty("SFTPPORT"));
        String SFTPUSER = System.getProperty("SFTPUSER");
        String SFTPPASS = System.getProperty("SFTPPASS");
        String connectionInfo = String.format("Host: %s, Port: %s, User: %s", SFTPHOST, SFTPPORT, SFTPUSER);
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
//            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
//            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SftpWorkingDir);
        }
        catch (Exception ex) {
            closeSftpConnection();
            throw new RuntimeException("Exception found while connecting to sftp (" + connectionInfo + ")", ex);
        }
    }
}








