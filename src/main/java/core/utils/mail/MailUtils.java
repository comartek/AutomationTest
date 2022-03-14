package core.utils.mail;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import core.sql.SQLManager;
import core.steps.sql.SQLSubSteps;
import core.utils.evaluationManager.EvaluationManager;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;

import static core.utils.timeout.TimeoutUtils.sleep;

public class MailUtils {

    public static String user = "AplanaVPBank@gmail.com";
    public static String password = "FFIHASH1";

//    //FOR DEBUGGING PURPOSES
//    public static void main(String[] args) {
//
//        long startDate = System.currentTimeMillis();
//
//        long maxWaitTimeInSeconds = 300;
//        Date dateFrom = new Date(System.currentTimeMillis() - 180 * 60 * 1000);
//        Date dateTo = new Date(System.currentTimeMillis() + maxWaitTimeInSeconds * 1000);
//
//        System.out.println(getOneTimePassword(dateFrom, dateTo, maxWaitTimeInSeconds));
//        double elapsedTimeInSeconds = (System.currentTimeMillis() - startDate) / 1000.0;
//        System.out.println("Elapsed time (seconds) = " + elapsedTimeInSeconds);
//    }


    public static String getOneTimePasswordFromService(String username,Date startDate, Date endDate, long maxWaitTimeInSeconds) throws IOException {
        long maxDateTimeInMillis = System.currentTimeMillis() + maxWaitTimeInSeconds * 1000;
        while(System.currentTimeMillis() <= maxDateTimeInMillis) {
            HttpClient client = new DefaultHttpClient();
            String url = System.getProperty("otp.customMailServer.url");//"aplanavpbat.hldns.ru:8080/otp/latest";
            HttpGet request = new HttpGet(String.format("http://%s/otp/latest/%s", url, username));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                OtpRecord record=null;
                try {
                    Gson gson = new Gson();
                    record = gson.fromJson(rd, OtpRecord.class);
                    Instant receive_datetime = Instant.from(OffsetDateTime.parse(record.date));
                    Instant start=startDate.toInstant();
                    Instant end = endDate.toInstant();
                    if (receive_datetime.isAfter(start)&&receive_datetime.isBefore(end))
                        return record.otp;
                    else
                        System.err.println(String.format("Latest found OTP for specified user is beyond set date interval"));
                }
                catch (JsonParseException ex){
                    System.err.println(String.format("Unable to parse server response"));
                }
            }
            else {
                System.err.println(String.format("OTP for specified user not found on server"));
            }

            int timeoutInSeconds = 10;
            sleep(timeoutInSeconds);
        }

        return null;
    }

    public static String getOneTimePasswordFromDatabase(String username,Date startDate, Date endDate, long maxWaitTimeInSeconds) throws IOException {
        long maxDateTimeInMillis = System.currentTimeMillis() + maxWaitTimeInSeconds * 1000;
        while (System.currentTimeMillis() <= maxDateTimeInMillis) {
            String query = String.format("SELECT TOP 1 * FROM OTPData WHERE otpMethod='EMAIL' and otpReceiver like '%s%%' ORDER BY otpTime DESC", username);
            List<Map<String, String>> results = SQLManager.getInstance().get().OTP_DB.executeQueryWithResults(query);

            if (results.size() > 0) {
                Map<String, String> result = results.get(0);
                String time_local=result.get("lastUpdateTime").split("\\.")[0];
                Instant receive_datetime = Instant.from(OffsetDateTime.of(
                        (LocalDateTime.parse(time_local, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                        ZoneOffset.ofHours(Integer.parseInt(System.getProperty("otp.otpDb.timeOffset")))
                ));
                Instant start = startDate.toInstant();
                Instant end = endDate.toInstant();
                if (receive_datetime.isAfter(start) && receive_datetime.isBefore(end))
                    return result.get("otpNumber");
                else
                    System.err.println(String.format("Latest found OTP for specified user is beyond set date interval"));
            } else {
                System.err.println(String.format("OTP for specified user not found on server"));
            }
            int timeoutInSeconds = 10;
            sleep(timeoutInSeconds);
        }
        return null;
    }

    public static String getOneTimePasswordFromDatabaseWithMethod(String username,Date startDate, Date endDate, long maxWaitTimeInSeconds) throws IOException {
        long maxDateTimeInMillis = System.currentTimeMillis() + maxWaitTimeInSeconds * 1000;
        String email;
        String phone;

        SQLSubSteps sqlSubSteps = new SQLSubSteps();
        sqlSubSteps.stepEmailAddressIsReceivedByParameterAndSavedToVariable(username, "email");
        email = (String) EvaluationManager.getVariable("email");
        sqlSubSteps.stepPhoneNumberIsReceivedByParameterAndSavedToVariable(username, "phone");
        phone = (String) EvaluationManager.getVariable("phone");
        sqlSubSteps.stepOtpMethodIsReceivedByParameterAndSavedToVariable(username, "otpMethod");
        String OTPmethod = (String) EvaluationManager.getVariable("otpMethod");

        while (System.currentTimeMillis() <= maxDateTimeInMillis) {
            String query;
            if (OTPmethod.toLowerCase().equals("sms")) {
                query = String.format("SELECT TOP 1 * FROM OTPData WHERE otpMethod='SMS' and otpReceiver like '%s%%' ORDER BY otpTime DESC", phone.trim());
                System.out.println(query);
            }
            else if (OTPmethod.toLowerCase().equals("email")) {
                query = String.format("SELECT TOP 1 * FROM OTPData WHERE otpMethod='EMAIL' and otpReceiver like '%s%%' ORDER BY otpTime DESC", email.trim());
                System.out.println(query);
            }
            else {
                System.err.println(String.format("OTP method is invalid. It should be equal \'sms\' or \'email\'"));
                query = null;
                System.out.println(query);
            }

            List<Map<String, String>> results = SQLManager.getInstance().get().OTP_DB.executeQueryWithResults(query);

            if (results.size() > 0) {
                Map<String, String> result = results.get(0);
                String time_local=result.get("lastUpdateTime").split("\\.")[0];
                Instant receive_datetime = Instant.from(OffsetDateTime.of(
                        (LocalDateTime.parse(time_local, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                        ZoneOffset.ofHours(Integer.parseInt(System.getProperty("otp.otpDb.timeOffset")))
                ));
                Instant start = startDate.toInstant();
                Instant end = endDate.toInstant();
                Logger.getRootLogger().info(String.format("start = %s",start));
                Logger.getRootLogger().info(String.format("end = %s",end));
                Logger.getRootLogger().info(String.format("receive = %s",receive_datetime));
                Logger.getRootLogger().info(String.format("otp = %s",result.get("otpNumber")));
                if (receive_datetime.isAfter(start) && receive_datetime.isBefore(end))
                    return result.get("otpNumber");
                else
                    System.err.println(String.format("Latest found OTP for specified user is beyond set date interval"));
            } else {
                System.err.println(String.format("OTP for specified user not found on server. Check that OTP method is valid for this user"));
            }
            int timeoutInSeconds = 10;
            sleep(timeoutInSeconds);
        }
        return null;
    }

    public static String getOneTimePasswordFromGmail(Date startDate, Date endDate, long maxWaitTimeInSeconds){

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        MailUtils gmail = new MailUtils();
        String expectedSubject = "Mat khau OTP cho giao dich i2b";
//        String pattern = "Mật khẩu OTP được thiết lập dành riêng cho giao dịch i2b hiện tại là <b>(.*)</b>.";
        String pattern = "Mật khẩu OTP được thiết lập dành riêng cho giao dịch i2b hiện tại là (?:<b>)?(\\d+)(?:</b>)?.";
        ArrayList<String> otpValues = new ArrayList<>();

        System.out.println("Start: " + (new Date()).toString());
        long maxDateTimeInMillis = System.currentTimeMillis() + maxWaitTimeInSeconds * 1000;
        while(System.currentTimeMillis() <= maxDateTimeInMillis){

            try {
                Session session = Session.getDefaultInstance(props, null);

                Store store = session.getStore("imaps");
                store.connect("smtp.gmail.com", user, password);

                Folder inbox = store.getFolder("inbox");
                inbox.open(Folder.READ_ONLY);

                ReceivedDateTerm receivedDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, startDate);
                SearchTerm receivedDateTerm2 = new SearchTerm() {
                    @Override
                    public boolean match(Message message) {
                        try {
                            Date receivedDate = message.getReceivedDate();
                            if(!(receivedDate.before(startDate) || receivedDate.after(endDate))) {
                                return true;
                            }
                        } catch (MessagingException ex) {
                            ex.printStackTrace();
                        }
                        return false;
                    }
                };

                SubjectTerm subjectTerm = new SubjectTerm(expectedSubject);
                SearchTerm searchTerm1 = new AndTerm(receivedDateTerm, subjectTerm);
                SearchTerm searchTermFinal = new AndTerm(searchTerm1, receivedDateTerm2);
                Message[] messages = inbox.search(searchTermFinal);

                for (int i = messages.length - 1; i >= 0; i--) {
                    String content = getTextFromMessage(messages[i]);
                    Matcher match = Pattern.compile(pattern).matcher(content.toString());
                    if (match.find()) {
                        String oneTimePassword = match.group(1);
                        otpValues.add(oneTimePassword);
                    }
                }

                inbox.close(true);
                store.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(otpValues.size() > 0) {
                break;
            } else {
                System.out.println(String.format("No OTP e-mails has been found yet (date from = %s; date to = %s). Current date = %s", startDate.toString(), endDate.toString(), (new Date()).toString()));
                int timeoutInSeconds = 5;
                long timeLeftInMilis = System.currentTimeMillis() + timeoutInSeconds * 1000;
                if(timeLeftInMilis <= maxDateTimeInMillis) {
                    sleep(timeoutInSeconds);
                } else {
                    System.out.println("break without sleeping, no reason to wait " + timeoutInSeconds + " seconds because timeLeftInSeconds = " + (maxDateTimeInMillis - timeLeftInMilis - timeoutInSeconds * 1000) / 1000.0);
                    break;
                }
            }
        }

        if(otpValues.size() == 0) {
            throw new RuntimeException(String.format("Could not get One Time Password: dateFrom = %s, dateTo = %s, maxWaitTimeInSeconds = %s, mailbox user = %s", startDate.toString(), endDate.toString(), maxWaitTimeInSeconds, user));
        } else if (otpValues.size() >= 2) {
            String errMessage = "More than one found 'one time password' value: " + Arrays.toString(otpValues.toArray()) + "\nNeed to find the way how to get only one 'one time password'";
            System.err.println(errMessage);
            //throw new RuntimeException(errMessage);
        }

        return otpValues.get(0);
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + html;
                //result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
    public static String getContentOutlookMail(){
        String content = "";
        String protocol = "imaps";
        String user = "thuynguyen.it.ptit@gmail.com";
        String host = "imap.gmail.com";
        String pass = "";
        String port = "993";
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol",protocol);
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.user",user);
        properties.setProperty("mail.imap.host",host);
        properties.setProperty("mail.imap.port",port);

        properties.setProperty("mail.imap.auth.plain.disable", "true");
        properties.setProperty("mail.imap.auth.gssapi.disable", "true");
        properties.setProperty("mail.imap.auth.ntlm.disable", "true");
        properties.setProperty("mail.imap.ssl.enable", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getInstance(properties, auth);
//        Session mailSession = Session.getInstance(properties);

        try {
            Store mailStore = mailSession.getStore(protocol);
            mailStore.connect(host, user, pass);
            Folder folder = mailStore.getDefaultFolder();
            Folder inbox = folder.getFolder("Me Only");
            inbox.open(Folder.READ_WRITE);
            Folder destFolder = mailStore.getFolder("TEST FOLDER");
            if (!destFolder.exists()){
                destFolder.create(Folder.HOLDS_MESSAGES);
            }
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseen = new FlagTerm(seen, false);
            Message inMassages[] = inbox.search(unseen);
            if(inMassages.length != 0){
                inbox.copyMessages(inMassages, destFolder);
                for (int i = 0; i < inMassages.length; i++){
                    String isubject = inMassages[i].getSubject();
                    MimeMultipart contentMineMul = (MimeMultipart) inMassages[i].getContent();
                    String icontent = getTextFromMimeMultipart(contentMineMul);
                    content = "Subject: "+isubject+"\nContent: "+icontent;
                    inMassages[i].setFlag(Flags.Flag.DELETED, true);
                }
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }catch (MessagingException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return content;
    }

    public static class SMTPAuthenticator extends javax.mail.Authenticator
    {

        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = "";
            String password = "";
            return new PasswordAuthentication(username, password);
        }
    }

}

