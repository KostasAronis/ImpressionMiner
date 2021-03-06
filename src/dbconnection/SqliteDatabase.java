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
            if(TablesExist() == false)
            {
                CreateTargetWebSiteTable();
                CreateWordTable();
                CreateWorkTable();
                CreateSearchTable();
                CreateWorkWordTable();
                CreateSearchWorkTable();
            }
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

    private boolean TablesExist()  throws SQLException
    {
        
        Statement stmt = GetConnection().createStatement();
        String sql = "SELECT * FROM sqlite_master Where type = 'table' ";
        ResultSet rs =  stmt.executeQuery(sql);
        Integer tableCount = 0;
        while ( rs.next() ) 
        {
            tableCount += 1;
        }
        stmt.close();

        if (tableCount == 6)
            return true;
        else
            return false;
    }

    private void CreateTargetWebSiteTable()
    {
        String query = "CREATE TABLE IF NOT EXISTS TargetWebSite "
                    + "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Url           TEXT      NOT NULL)";
        CreateTable(query);
    }
    private void CreateWorkTable()
    {
        String query = "CREATE TABLE IF NOT EXISTS Work "
        + "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "TargetWebsiteId INTEGER)";
        CreateTable(query);
    }
    private void CreateWordTable()
    {
        String query = "CREATE TABLE IF NOT EXISTS Word "
        + "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "Word           TEXT      NOT NULL)";
        CreateTable(query);
    }
    private void CreateSearchTable()
    {
        String query = "CREATE TABLE IF NOT EXISTS Search "
        + "(Id              INTEGER     NOT NULL,"
        + "Timestamp         DATE       NOT NULL )";
        CreateTable(query);
    }
    private void CreateSearchWorkTable() {
        String query = "CREATE TABLE IF NOT EXISTS SearchWork "
        + "("
        + "SearchId         INTEGER     NOT NULL,"
        + "WorkId           INTEGER     NOT NULL,"
        + "FOREIGN KEY(SearchId) REFERENCES Search(Id)"
        + "FOREIGN KEY(WorkId) REFERENCES Work(Id)"+
        ")";
        CreateTable(query);
    }

    private void CreateWorkWordTable() {
        String query = "CREATE TABLE IF NOT EXISTS WorkWord "
        + "(Id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "WorkId         INTEGER     NOT NULL,"
        + "Word         STRING     NOT NULL,"
        + "Count       INTEGER      ,"
        + "Impression  REAL      ,"
        + "FOREIGN KEY(WorkId) REFERENCES Work(Id)"+
        ")";
        CreateTable(query);
    }
    private void CreateTable(String query)
    {
        try 
        {
            Statement stmt = GetConnection().createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
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