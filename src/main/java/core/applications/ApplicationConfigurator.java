package core.applications;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * This is core of framework.
 * Class ApplicationManager parse file applications.json with necessary settings for different browsers.
 */
public class ApplicationConfigurator {

    private static ThreadLocal<ApplicationConfigurator> instance = new ThreadLocal<>();

    /**
     * Private constructor.  Called while first launch.
     * @see ApplicationConfigurator#loadConfig()
     */
    private ApplicationConfigurator(){
        loadConfig();
    }

    /**
     * Basic method of singleton. (ThreadLocal for multithreading support)
     */
    public static ThreadLocal<ApplicationConfigurator> getInstance() {
        if (instance.get() == null)
        {
            instance.set(new ApplicationConfigurator());
        }
        return instance;
    }

    /**
     * List of Applications.
     * @see Application
     */
    public Map<String,Application> Applications;

    /**
     * List of Applications as objects from json.
     */
    public List<Object> jApps;

    /**
     * Basic method of ApplicationConfigurator. It parses file application.json and creates list of Application
     */
    public void loadConfig() {

        try {
            Type type = new TypeToken<List<Object>>() {}.getType();
            InputStream stream = new FileInputStream("./applications.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            jApps = new Gson().fromJson(br, type);

            Applications = new LinkedTreeMap<>();

            for (Object jApp : jApps) {
                Application application = new Application();

                application.name = ((LinkedTreeMap<String, Object>) jApp).get("name").toString();
                application.drivername = ((LinkedTreeMap<String, Object>) jApp).get("drivername").toString();
                LinkedTreeMap<String, Object> capabilitiesFields =
                        (LinkedTreeMap<String, Object>) ((LinkedTreeMap<String, Object>) jApp).get("capabilities");

                DesiredCapabilities capabilities = new DesiredCapabilities();

                for (Map.Entry<String, Object> capabilitiesField : capabilitiesFields.entrySet()) {
                    capabilities.setCapability(capabilitiesField.getKey(), capabilitiesField.getValue());
                }
                application.capabilities = capabilities;
                Applications.put(application.name,application);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("applications.json parse error!");
        }

    }
}
