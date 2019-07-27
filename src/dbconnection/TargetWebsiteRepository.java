package dbconnection;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.TargetWebsite;

public class TargetWebsiteRepository implements IRepository<TargetWebsite> {

    private SqliteDatabase _db;
    public TargetWebsiteRepository()
    {
        _db = new  SqliteDatabase();
    }

    @Override
    public List<TargetWebsite> GetAll() {
        List<TargetWebsite> tWebsites = new ArrayList<TargetWebsite>();
        try 
        {
            String query = "Select * FROM TargetWebsite ";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                tWebsites.add(ConvertToTargetWebsite(rs));
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return tWebsites;
    }

    @Override
    public TargetWebsite GetById(Integer id) {
        TargetWebsite tw = new TargetWebsite();
        try 
        {
            String query = "Select * FROM TargetWebsite Where Id = ? ";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                tw = ConvertToTargetWebsite(rs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return tw;
    }

    @Override
    public TargetWebsite Insert(TargetWebsite arg) 
    {
        TargetWebsite tw = new TargetWebsite();
        try {
            String query = "Insert INTO TargetWebsite (Url) VALUES(?)";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setString(1, arg.getUrl());
            Integer success = stmt.executeUpdate();
            Integer idColVar = null;
            ResultSet  rs = stmt.getGeneratedKeys();
            if(success == 1)
            {
                while (rs.next()) 
                {
                     idColVar = rs.getInt(1);     
                }
            }
            rs.close();
            stmt.close();

            tw = this.GetById(idColVar);
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return tw;
    }

    @Override
    public boolean Update(TargetWebsite arg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean Delete(Integer id) {
        try 
        {
            String query = "DELETE FROM TargetWebsite Where Id = ?";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            Integer result = stmt.executeUpdate();
            stmt.close();
            return result==1;
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
    }

    private TargetWebsite ConvertToTargetWebsite(ResultSet rs) throws SQLException
    {
        return new TargetWebsite
        (
            rs.getInt("Id"),
            rs.getString("Url")
        );
    }

}