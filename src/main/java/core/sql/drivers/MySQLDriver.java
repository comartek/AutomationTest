package core.sql.drivers;

import core.sql.DBType;
import core.sql.SQLConnectionParams;

/**
 * Created by Akarpenko on 21.02.2018.
 */
public class MySQLDriver extends SQLDriverBase {

    public MySQLDriver(SQLConnectionParams _connection){
        super(_connection);

        if (connectionParams.type != DBType.MySQL){
            throw new RuntimeException("Connection type does not match this driver");
        }
    }

    @Override
    protected String getConnectionString() {
        return String.format(
                "jdbc:mysql://%s/%s?user=%s&password=%s" +
                        "&useUnicode=true&useJDBCCompliantTimezoneShift=true&" +
                        "useLegacyDatetimeCode=false&serverTimezone=UTC",
                connectionParams.host, connectionParams.schema, connectionParams.login, connectionParams.password);
    }

}
