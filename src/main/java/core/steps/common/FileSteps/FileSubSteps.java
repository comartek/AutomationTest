package core.steps.common.FileSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.steps.common.UtilSteps.UtilSubSteps;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

public class FileSubSteps {

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("download file from URL \"{url}\" and save to path \"{path}\"")
    public void stepDownloadFileAndSaveToPath(String url, String path) {

        try {
            URL url1 = new URL(url);
            File file=new File(path);
            FileUtils.copyURLToFile(url1,file);
        }
        catch (MalformedURLException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("PDF file \"{file}\" contains \"{pageCount}\" pages")
    public void stepPdfFileContainsPages(String file, int pageCount) {
        try {
            File pdf=new File(file);
            PDDocument document = PDDocument.load(pdf);
            int count=document.getNumberOfPages();

            Assert.assertEquals(pageCount, count);

        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("PDF file \"{file}\" contains text \"{text}\" on page \"{pageNum}\"")
    public void stepPdfFileContainsTextOnPage(String file, String text, int pageNum) {
        try {
            File pdf=new File(file);
            PDDocument document = PDDocument.load(pdf);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(pageNum);
            stripper.setEndPage(pageNum);
            String parsedText = stripper.getText(document);

            Assert.assertTrue(String.format("PDF does not contain text '%s' on page '%s'",text,pageNum),parsedText.contains(text));

        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("remove file \"{file}\" if it exists")
    public void stepRemoveFileIfExists(String file) {
        File xx = new File(file);
        if (xx.exists()) {
            xx.delete();
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("file \"{file}\" exists")
    public void stepFileExists(String file) {
        File xx = new File(file);
        Assert.assertTrue(String.format("File %s does not exist",file),xx.exists());
    }

    /* template for image comparison step
    @StepExt("image comparison ")
    public void method{
        //(new FileSubSteps()).stepDownloadFileAndSaveToPath(
        //getActiveDriver().findElement(By.xpath("//table[@id='datadisplay']//img")).getAttribute("src"),
        //        "download.jpg")
        //

        new File("download.jpg").length()
    }
    */
    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("absolute path of a framework file \"{frameworkFile}\" is saved to variable \"{variable}\"")
    public void stepAbsolutePathOfAFrameworkFileIsSavedToVariable(String frameworkFile, String variable){
        File file = new File(frameworkFile);
        Assert.assertTrue(String.format("File %s does not exist",file), file.exists());
        String absolutePath = file.getAbsolutePath();
        new UtilSubSteps().stepSaveVariable(absolutePath, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("create CSV file for overdraft UPL and saved to path \"{path}\"")
    public void stepCreateCSVFileForUPL(String path){
        String template = "STT,Cif,T?n K,CMND,S?T,Email,T?ng d? n? cho vay kh?ng TS?B c?a KH t?i VPBank,T?ng d? n? cho vay kh?ng TS?B c?a KH t?i cÿc TCTD khÿc,S? ti?n cho vay t?i ?a,L?i su?t,Th?i h?n vay˜˜,Ng?y b?t ??u,Ng?y k?t thœc˜,Ng?y tr? n? Kho?n vay c? s? ,DAO CBB LD c? s?,Book CN qu?n l? kho?n vay,K?nh Kh? d?ng,MAX_TENOR,CHANNEL_T24,??I T??NG VAY,LD C? S?\n" +
                "1,%s,CUS CRE ACC SHORT ACC,%s,%s,%s,0,0,%s,25,%s,%s,%s, ,10463,VN0010325,1,12,CFT,HOPNHAT_UPL_PR,1";
        String evalCif = evalVariable("%{cif}%");
        String evalPassportID = evalVariable("%{passportID}%");
        String evalPhone = evalVariable("%{phone}%");
        String evalEmail = evalVariable("%{email}%");
        String evalApprovalLimit = evalVariable("%{ApprovalLimit}%");
        String evalTerm = evalVariable("%{term}%");
        String evalStartDate = evalVariable("%{date(now(),'-1d','','dd/MM/yyyy')}%");
        String evalEndDate = evalVariable("%{date(now(),'+6M','','dd/MM/yyyy')}%");
        String newString = String.format(template, evalCif, evalPassportID, evalPhone, evalEmail, evalApprovalLimit, evalTerm, evalStartDate, evalEndDate);
        System.out.println(newString);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(newString.getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                fos.close();
            }catch (Exception ex1){
                ex1.printStackTrace();
            }
        }
    }
}
