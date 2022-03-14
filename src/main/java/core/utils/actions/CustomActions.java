package core.utils.actions;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.utils.timeout.TimeoutUtils.sleep;
import static core.steps.BaseSteps.getActiveDriver;

public class CustomActions {

    public static void moveCursorToTheTopLeftCornerInTheOCBApp() {
        if(System.getProperty("testDB.schema").contains("ocb")){
            moveCursorToTheTopLeftCorner();
        }
    }

    public static void moveCursorToTheTopLeftCorner(){
        int xOffset = -1000;
        int yOffset = -1000;
        Actions actions = new Actions(getActiveDriver());
        try{
            actions.moveByOffset(xOffset,yOffset).build().perform(); //move cursor to 0,0
            sleep(100, TimeUnit.MILLISECONDS); //instead of .pause(Duration.ofMillis(100))
        } catch (MoveTargetOutOfBoundsException e){
            String errMessage = e.getMessage();
            String pattern = "\\(([^,]+), ([^\\)]+)\\) is out of bounds";
            Matcher match = Pattern.compile(pattern).matcher(errMessage);
            if (match.find()) {
                xOffset = xOffset - Double.valueOf(match.group(1)).intValue() + 1;
                yOffset = yOffset - Double.valueOf(match.group(2)).intValue() + 1;
                actions.moveByOffset(xOffset,yOffset).build().perform(); //move cursor to 0,0
                sleep(100, TimeUnit.MILLISECONDS); //instead of .pause(Duration.ofMillis(100))
            }
        }
    }

    public static boolean mouseMove(int x, int y){
        Robot robot = null;
        try {
            robot = new Robot();
            robot.mouseMove(x, y);
            return true;
        } catch (AWTException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}

