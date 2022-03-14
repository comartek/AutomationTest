package core.utils.report;


// Java Program to Capture full Image of Screen
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;


public class FullDesktopScreenshot {

    public static byte[] getScreenshotAsBytes(){
        BufferedImage image = getScreenshotAsBufferedImage();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveScreenshot(String path){
        BufferedImage image = getScreenshotAsBufferedImage();
        try {
            ImageIO.write(image, "jpg", new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getScreenshotAsBufferedImage(){
        try{
            Robot r = new Robot();
            Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return r.createScreenCapture(capture);
        } catch (AWTException e){
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args)
    {
        try {
            Thread.sleep(120);
            Robot r = new Robot();

            // It saves screenshot to desired path
            String path = "D:// Shot.jpg";

            // Used to get ScreenSize and capture image
            Rectangle capture =
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage Image = r.createScreenCapture(capture);
            ImageIO.write(Image, "jpg", new File(path));
            System.out.println("Screenshot saved");
        }
        catch (AWTException | IOException | InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
