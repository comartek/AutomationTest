package core.sql.drivers;

import core.sql.SQLConnectionParams;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akarpenko on 21.02.2018.
 */
public abstract class SQLDriverBase {

    protected SQLConnectionParams connectionParams;
    protected abstract String getConnectionString();

    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultSet;

    protected List<Map<String,String>> lastQueryResults;

    public SQLDriverBase(SQLConnectionParams _connection){
        this.connectionParams = _connection;
    }


    protected void connect() {
        try {
            if (connection!=null && !connection.isClosed())
                return;
            connection = DriverManager.getConnection(getConnectionString(), connectionParams.login, connectionParams.password);
            statement = connection.createStatement();
        } catch (Exception e) {

            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void disconnect(){
        try{
            if (connection!=null && !connection.isClosed())
            connection.close();
        }
        catch (Exception e){
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public ResultSet executeQuery(String query) {
        connect();
        try {
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
        //disconnect();
        return resultSet;
    }
    public void executeCallable(String query) {
        connect();
        try {
            CallableStatement state= connection.prepareCall(query);
            state.execute();
        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
//        disconnect();
    }


    public List<Map<String,String>> executeQueryWithResults(String query) {
        connect();
        try {
            resultSet = statement.executeQuery(query);
            lastQueryResults=convertResultSet(resultSet);
        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
//        disconnect();
        return lastQueryResults;
    }

    public void executeUpdate(String query) {
        connect();
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private List<Map<String,String>> convertResultSet(ResultSet result)
    {
        List<Map<String,String>> list=new ArrayList<>();

        try {
            ResultSetMetaData metaData = result.getMetaData();

            while (result.next()) {
                Map<String,String> row=new HashMap();
                for (int i=0;i<metaData.getColumnCount();i++)
                {
                    row.put(metaData.getColumnLabel(i+1),result.getString(i+1));
                }
                list.add(row);
            }
            return list;
        }
        catch(Exception e)
        {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> getLastQueryResults() {
        return lastQueryResults;
    }


}
