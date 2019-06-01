package dbconnection;

import java.sql.SQLException;
import java.sql.Statement;

import models.Work;

public class WorkRepository<T> implements IRepositoryFactory<T, Work>
{

    @Override
    public T Get(Work arg) 
    {
        try 
        {
            SqliteDatabase db = new SqliteDatabase();
            Statement stmt =  db.GetConnection().createStatement();
            String query = "Select * FROM ";
            stmt.executeQuery(query);
            stmt.close();
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    @Override
    public T Insert(Work arg) {
        return null;
    }

    @Override
    public boolean Update(Work arg) {
        return false;
    }

    @Override
    public boolean Delete(Work arg) {
        return false;
    }


}