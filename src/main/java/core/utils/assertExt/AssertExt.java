package core.utils.assertExt;

import org.hamcrest.Matcher;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class AssertExt {

    private static boolean softAssertModeDefaultValue = false;
    private static boolean softAssertMode = softAssertModeDefaultValue;
    private static boolean softAssertModePreviousValue = softAssertModeDefaultValue;
    private static int countInterceptedErrors = 0;
    private static LinkedList<List<Throwable>> interceptedErrorsList = new LinkedList<List<Throwable>>();

    /*********************************************
     ************  Getters / setters *************
     *********************************************/
    public static void setSoftAssertMode(boolean newSoftAssertMode){
        softAssertModePreviousValue = softAssertMode;
        softAssertMode = newSoftAssertMode;
    }

    public static void setPreviousSoftAssertMode(){
        setSoftAssertMode(softAssertModePreviousValue);
    }

    public static boolean getSoftAssertMode(){
        return softAssertMode;
    }

    public static void setCountInterceptedErrors(int newCountInterceptedErrors){
        countInterceptedErrors = newCountInterceptedErrors;
    }

    public static void setSoftAssertModeToDefaultValue(){
        softAssertMode = softAssertModeDefaultValue;
    }

    public static int getCountInterceptedErrors(){
        return countInterceptedErrors;
    }

    public static LinkedList<List<Throwable>> getInterceptedErrorsList(){
        return interceptedErrorsList;
    }

    public static void clearInterceptedErrorsList(){
        interceptedErrorsList.clear();
    }
}
