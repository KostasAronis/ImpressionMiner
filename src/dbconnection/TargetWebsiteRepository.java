package dbconnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import models.TargetWebsite;

public class TargetWebsiteRepository<T> implements IRepositoryFactory<T,TargetWebsite>
{

    @Override
    public T Get(TargetWebsite arg) {
        return null;
    }

    @Override
    public T Insert(TargetWebsite arg) {
        try 
        {
            SqliteDatabase db = new SqliteDatabase();
            String query = "Insert INTO TARGETWEBSITE VALUES(?,?)";
            PreparedStatement stmt =  db.GetConnection().prepareStatement(query);
            stmt.setString(1,arg.getUrl());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    @Override
    public boolean Update(TargetWebsite arg) {
        return false;
    }

    @Override
    public boolean Delete(TargetWebsite arg) {
        return false;
    }

    
}