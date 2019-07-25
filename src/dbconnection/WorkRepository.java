package dbconnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.TargetWebsite;
import models.Word;
import models.Work;

public class WorkRepository implements IRepository<Work>
{
    private SqliteDatabase _db;

    public WorkRepository()
    {
        _db = new SqliteDatabase();
    }

    @Override
    public List<Work> GetAll() {
        List<Work> works = new ArrayList<Work>();
        Work work = new Work();
        Integer lastWorkId = -1;
        try 
        {
            String query = "Select Work.Id,TargetWebsite.Id,TargetWebsite.Url,"
            + "Word.Id,Word.Word,Word.Count,Word.Impression "
            + "FROM Work "
            + "LEFT JOIN TargetWebsite ON Work.TargetWebsiteId = TargetWebsite.Id "
            + "LEFT JOIN Word ON Work.WordId = Word.Id";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                if (lastWorkId == rs.getInt(1))
                {
                    work.words.add(ConvertToWord(rs));
                    lastWorkId = rs.getInt(1);
                }
                else
                {
                    if(work.id != null)
                        works.add(work);

                    work = ConvertToWork(rs);
                    work.words.add(ConvertToWord(rs));
                    lastWorkId = rs.getInt(1);
                }  
            }
            works.add(work);
            rs.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return works;
    }

    @Override
    public Work GetById(Integer id) 
    {
        Work work = new Work();
        try 
        {
            String query = "Select Work.Id,TargetWebsite.Id,TargetWebsite.Url,"
            + "Word.Id,Word.Word,Word.Count,Word.Impression "
            + "FROM Work "
            + "LEFT JOIN TargetWebsite ON Work.TargetWebsiteId = TargetWebsite.Id "
            + "LEFT JOIN Word ON Work.WordId = Word.Id "
            + "Where Work.Id = ?";
            PreparedStatement stmt =  _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                if (rs.isFirst())
                    work = ConvertToWork(rs);

                work.words.add(ConvertToWord(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return work;
    }

    @Override
    public Work Insert(Work arg) 
    {
        Integer idColVar = GetLastWorkId();
        String query = "Insert INTO Work (Id,WordId,TargetWebsiteId) VALUES(?,?,?)";
        for (Word word : arg.words) 
        {
            try 
            {
                PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
                stmt.setInt(1, idColVar);
                stmt.setInt(2, word.id);
                stmt.setInt(3, arg.targetWebsite.getId());
                    
                Integer success = stmt.executeUpdate();
                stmt.close();
            } 
            catch (SQLException e) 
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
            
        }
        arg.id = idColVar;
        return arg;
    }

    @Override
    public boolean Update(Work arg) {
        return false;
    }

    @Override
    public boolean Delete(Work arg) {
        return false;
    }

    //#region Helpers
    private Integer GetLastWorkId()
    {
        Integer nextID = null;
        try 
        {
            String query = "Select IFNULL(max(id))  FROM Work";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                nextID = rs.getInt(1) + 1;
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return nextID;
    }

    private Work ConvertToWork(ResultSet rs) throws SQLException 
    {
        Work work = new Work();
        work.id = rs.getInt(1);
        work.targetWebsite = new TargetWebsite();
        work.targetWebsite.setId(rs.getInt(2));
        work.targetWebsite.setUrl(rs.getString(3));
        work.words = new ArrayList<Word>();
        return work;
    }

    private Word ConvertToWord(ResultSet rs) throws SQLException 
    {
        return new Word
        (
            rs.getInt(4),
            rs.getString(5),
            rs.getInt(6),
            rs.getDouble(7)
        );
    }
    //#endregion
   


}