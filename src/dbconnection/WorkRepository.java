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
import models.WorkWord;

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
        try 
        {
            String query = "Select Work.Id,TargetWebsite.Id,TargetWebsite.Url "
            + " FROM Work "
            + " LEFT JOIN TargetWebsite ON Work.TargetWebsiteId = TargetWebsite.Id ";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                Work work = ConvertToWork(rs);
                works.add(work);
            }
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
            String query = 
            "Select Work.Id, TargetWebsite.Id, TargetWebsite.Url "
            + " FROM Work "
            + " LEFT JOIN TargetWebsite ON Work.TargetWebsiteId = TargetWebsite.Id "
            + " Where Work.Id = ? ";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                if (rs.isFirst())
                    work = ConvertToWork(rs);
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
        String query = "Insert INTO Work (Id,TargetWebsiteId) VALUES(?,?)";
        try 
        {
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, idColVar);
            stmt.setInt(2, arg.targetWebsite.getId());

            Integer success = stmt.executeUpdate();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        arg.id = idColVar;
        arg = InsertWords(arg);
        return arg;
    }

    @Override
    public boolean Update(Work arg) {
        return false;
    }

    @Override
    public boolean Delete(Integer id) {
        return false;
    }

    //#region Helpers
    private Integer GetLastWorkId()
    {
        Integer nextID = null;
        try 
        {
            String query = "Select IFNULL(max(id),0) FROM Work";
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
    private Integer GetLastWorkWordId()
    {
        Integer nextID = null;
        try 
        {
            String query = "Select IFNULL(max(id),0) FROM WorkWord";
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
        work.words = GatherWords(work.id);
        return work;
    }
    private Work InsertWords(Work arg) {
        List<WorkWord> wordsWithIds=new ArrayList<>();
        String query = "Insert INTO WorkWord (Id, WorkId, Word, Count, Impression) VALUES(?,?,?,?,?)";
        for (WorkWord word : arg.words) {
            Integer idColVar = GetLastWorkWordId();
            try 
            {
                PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
                stmt.setInt(1, idColVar);
                stmt.setInt(2, arg.id);
                stmt.setString(3, word.word);
                stmt.setInt(4, word.count);
                stmt.setDouble(5, word.impression);
                word.id=idColVar;
                word.WorkId=arg.id;
                Integer success = stmt.executeUpdate();
                stmt.close();
            } 
            catch (SQLException e) 
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
        }
        return arg;
    }

    private WorkWord ConvertToWord(ResultSet rs) throws SQLException 
    {
        WorkWord word = new WorkWord();
        word.id = rs.getInt(1);
        word.WorkId = rs.getInt(2);
        word.word = rs.getString(3);
        word.count = rs.getInt(4);
        word.impression = rs.getDouble(5);
        return word;
    }
    private List<WorkWord> GatherWords(Integer workId){
        List<WorkWord> words = new ArrayList<WorkWord>();
        try 
        {
            String query = "Select Id, WorkId, Word, Count, Impression "
            + " FROM WorkWord "
            + " Where WorkId=? ";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, workId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                WorkWord word = ConvertToWord(rs);
                words.add(word);
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException e) 
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return words;
    }
    //#endregion
}