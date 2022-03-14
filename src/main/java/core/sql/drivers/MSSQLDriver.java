package core.sql.drivers;

import core.sql.DBType;
import core.sql.SQLConnectionParams;

/**
 * Created by Akarpenko on 29.03.2018.
 */
public class MSSQLDriver extends SQLDriverBase {

    public MSSQLDriver(SQLConnectionParams _connection){
        super(_connection);

        if (connectionParams.type != DBType.MSSQL){
            throw new RuntimeException("Connection type does not match this driver");
        }
    }

    @Override
    protected String getConnectionString() {
        return String.format(
                "jdbc:sqlserver://%s:%s;"
                        + "database=%s;"
                        + "user=%s;"
                        + "password=%s;"
                        + "loginTimeout=30;",
                connectionParams.host,connectionParams.port,connectionParams.schema,
                connectionParams.login, connectionParams.password);
    }

}
