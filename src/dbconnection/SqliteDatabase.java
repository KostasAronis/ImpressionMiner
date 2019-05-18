package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class SqliteDatabase 
{
    private static Connection con = null;

    public Connection GetConnection() {
        if (con != null)
            return con;

        try 
        {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:ImpressionMiner.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return con;
    }

    public void CloseConnection() throws SQLException
    {
        con.close();
    }

    public void CreateTables()
    {

    }

    // public <T> List<T> queryAndCollect(String query, Collector col) throws SQLException 
    // {
    //     List<T> rows = new ArrayList<>();
    //     Statement stmt = con.createStatement();
    //     ResultSet rs = stmt.executeQuery(query);
    //     while (rs.next()) {
    //         //rows.add((T)col.collect(rs));
    //         //rows.add(rs.getObject(columnLabel));

    //     }
    //     rs.close();
    //     stmt.close();        
    //     return rows;
    // } 
}