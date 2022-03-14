package core.sql;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import core.sql.drivers.MSSQLDriver;
import core.sql.drivers.MySQLDriver;
import core.sql.drivers.OracleDriver;
import core.sql.drivers.SQLDriverBase;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Akarpenko on 20.02.2018.
 */
public class SQLManager {

    public MySQLDriver LocalDB;
    public OracleDriver ESB_UAT_DB;
    public OracleDriver OCB_UAT_DB;
    public OracleDriver OCB_SIT_DB;
    public MSSQLDriver OTP_DB;
    public OracleDriver LOS_DB;
    public OracleDriver W4_DB;
    public MSSQLDriver VPB_LOYALTY_DEV;
    public OracleDriver RLOS_UAT2_DB;


    private static ThreadLocal<SQLManager> instance = new ThreadLocal<>();

    private SQLManager(){
        loadConnections();

        LocalDB = new MySQLDriver(getConnection("LocalDB"));
        ESB_UAT_DB = new OracleDriver(getConnection("ESB_UAT_DB"));
        OCB_UAT_DB = new OracleDriver(getConnection("OCB_UAT_DB"));
        OCB_SIT_DB = new OracleDriver(getConnection("OCB_SIT_DB"));
        OTP_DB = new MSSQLDriver(getConnection("OTP_DB"));
        LOS_DB = new OracleDriver(getConnection("LOS_DB"));
        W4_DB = new OracleDriver(getConnection("W4_DB"));
        VPB_LOYALTY_DEV = new MSSQLDriver(getConnection("VPB_LOYALTY_DEV"));
        RLOS_UAT2_DB = new OracleDriver(getConnection("RLOS_UAT2_DB"));
    }

    public static ThreadLocal<SQLManager> getInstance() {
        if (instance.get() == null)
        {
            instance.set(new SQLManager());
        }
        return instance;
    }

    private Map<String,SQLConnectionParams> Connections;

    private void loadConnections(){

        try {
            Type type = new TypeToken<List<Object>>() {}.getType();
            InputStream stream = new FileInputStream("./SQLConnections.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            List<Object> jApps = new Gson().fromJson(br, type);

            Connections = new LinkedTreeMap<>();

            for (Object jApp : jApps) {
                SQLConnectionParams connection = new SQLConnectionParams();

                connection.name = ((LinkedTreeMap<String, Object>) jApp).get("name").toString();
                if (connection.name.equals("LocalDB")) {
                    String schema_prop = System.getProperty("testDB.schema");
                    if (schema_prop != null && !StringUtils.isEmpty(schema_prop))
                        connection.schema = schema_prop;
                    else
                        connection.schema = ((LinkedTreeMap<String, Object>) jApp).get("schema").toString();
                } else
                    connection.schema = ((LinkedTreeMap<String, Object>) jApp).get("schema").toString();
                connection.host = ((LinkedTreeMap<String, Object>) jApp).get("host").toString();
                connection.port = ((LinkedTreeMap<String, Object>) jApp).get("port").toString();
                connection.serviceName = ((LinkedTreeMap<String, Object>) jApp).get("serviceName").toString();
                connection.login = ((LinkedTreeMap<String, Object>) jApp).get("login").toString();
                connection.password = ((LinkedTreeMap<String, Object>) jApp).get("password").toString();
                connection.type = DBType.valueOf(((LinkedTreeMap<String, Object>) jApp).get("type").toString());

                Connections.put(connection.name, connection);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("SQLConnections.json parse error!");
        }

    }

    private SQLConnectionParams getConnection(String name){
        return Connections.get(name);
    }

    public SQLDriverBase getSqlDriver(String name)
    {
        try {
            return (SQLDriverBase)(this.getClass().getField(name).get(this));
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Unknown database: %s",name));
        }

    }

    public void closeConnections(){
        OCB_UAT_DB.disconnect();
        OCB_SIT_DB.disconnect();
        ESB_UAT_DB.disconnect();
        OTP_DB.disconnect();
        LOS_DB.disconnect();
        LocalDB.disconnect();
        VPB_LOYALTY_DEV.disconnect();
        RLOS_UAT2_DB.disconnect();
    }



}
