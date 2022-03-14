package core.utils.lang;

import core.applications.ApplicationManager;

public class LangUtils {

    public static Language getCurrAppLanguage(){
        return ApplicationManager.getInstance().get().ActiveApplication.language;
    }
    public static void setCurrAppLanguage(LangUtils.Language lang){
        ApplicationManager.getInstance().get().ActiveApplication.language = lang;
    }

    public static Language getDefaultLanguage(){
        return Language.valueOf(System.getProperty("lang"));
    }

    public static String getLanguageAsString(){

        if (getDefaultLanguage().equals(Language.EN_US))
            return "English";
        else if (getDefaultLanguage().equals(Language.VI_VN))
            return "Vietnamese";
        else
            return null;
    }



    public enum Language{EN_US, VI_VN}

}
