package core.pages;

import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;

import java.util.Set;

/**
 * PageFactory class allows to get class of page.
 */
public class PageFactory {

    /**
     * This method gets the class of page (with help of reflection) by its string name and initializes this page with webdriver.
     * @see BasePage
     * @param pageClassname string name of page
     * @param driver webdriver to initialization page
     */
    public static BasePage getPage(String pageClassname, WebDriver driver){

        Reflections reflections = new Reflections("core.pages");
        Set<Class<? extends BasePage>> allPages = reflections.getSubTypesOf(BasePage.class);

        String fakeExceptionMessage = "fake Exception";
        Exception lastException = new Exception(fakeExceptionMessage);
        for (Class<? extends BasePage> currentPage :allPages)
        {
            if (currentPage.getName().endsWith("."+pageClassname))
                try {
                    BasePage page = currentPage.getConstructor().newInstance();
                    page.initialize(driver);
                    return page;
                }catch (Exception e){
                    e.printStackTrace();
                    lastException = e;
                }

        }

        //return null;
        if(fakeExceptionMessage.equals(lastException.getMessage())){
            throw new RuntimeException(String.format("Could not getPage with name '%s' (probably page with specified name does not exist in the project)", pageClassname));
        } else {
            throw new RuntimeException(String.format("Could not getPage with name '%s' (the page with specified name was found in the project but an Exception was caught while trying to instantiate and initialize this page... last caught Exception is in the following message: %s)", pageClassname, lastException), lastException);
        }
    }

}
