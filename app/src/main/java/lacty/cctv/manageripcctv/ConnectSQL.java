package lacty.cctv.manageripcctv;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectSQL {
    private static String ip = "192.168.30.123";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "IPManagerCCTV";
    private static String username = "sa";
    private static String password = "Killua21608";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private Connection connection;
    private Statement statement;

    public ConnectSQL() {
        connection = null;
        statement = null;
        // khai bao bat buoc de ket noi SQL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection() {
        if (connection == null) {
            // ket noi SQL
            try {
                Class.forName(Classes);
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // use for insert, delete, update
    public int execUpdateQuery(String stringQuery) {
        try {
            statement = connection.createStatement();
            int result = statement.executeUpdate(stringQuery);
            statement.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<String> getArraySeclect(String select){
        ArrayList<String> arraylist=new ArrayList<>();
        try{
            if(connection!=null){
                statement=connection.createStatement();
                ResultSet result=statement.executeQuery(select);
                while(result.next()){
                    arraylist.add(result.getString(1));
                }
                result.close();
                statement.close();
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arraylist;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void closeStatement() throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }
}
