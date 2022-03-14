package core.sql.drivers;

import core.sql.DBType;
import core.sql.SQLConnectionParams;

/**
 * Created by Akarpenko on 21.02.2018.
 */
public class OracleDriver extends SQLDriverBase {

    public OracleDriver(SQLConnectionParams _connection){
        super(_connection);

        if (connectionParams.type != DBType.Oracle){
            throw new RuntimeException("Connection type does not match this driver");
        }
    }

    @Override
    protected String getConnectionString() {
        return String.format(
                "jdbc:oracle:thin:@(DESCRIPTION=" +
                        "(ADDRESS_LIST=" +
                        "(ADDRESS=(PROTOCOL=TCP)" +
                        "(HOST=%s)" +
                        "(PORT=%s)" +
                        ")" +
                        ")" +
                        "(CONNECT_DATA=" +
                        "(SERVICE_NAME=%s)" +
                        "(SERVER=DEDICATED)" +
                        ")" +
                        ")", connectionParams.host, connectionParams.port, connectionParams.serviceName);
    }

}
