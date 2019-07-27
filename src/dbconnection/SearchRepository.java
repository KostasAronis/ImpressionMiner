package dbconnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.Search;
import models.TargetWebsite;
import models.Word;
import models.Work;

public class SearchRepository implements IRepository<Search> {
    private SqliteDatabase _db;
    private IRepository<Work> _workRepo;

    public SearchRepository() {
        _db = new SqliteDatabase();
        _workRepo = new WorkRepository();
    }

    @Override
    public List<Search> GetAll() {
        List<Search> searches = new ArrayList<Search>();
        try {
            String query = "Select Id, Timestamp from Search ";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Search search = ConvertToSearch(rs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ParseException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return searches;
    }

    @Override
    public Search GetById(Integer id) {
        Search search = new Search();
        try {
            String query = "Select Search.Id,Search.Timestamp,Work.Id " + " FROM Search "
                    + " LEFT JOIN Work ON Work.Id = Search.WorkId " + " Where Search.Id = ? ";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.isFirst())
                    search = ConvertToSearch(rs);

                search.works.add(_workRepo.GetById(rs.getInt(3)));
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ParseException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return search;
    }

    @Override
    public Search Insert(Search arg) {
        Integer idColVar = GetLastSearchId();
        String query = "Insert INTO Search (Id,Timestamp) VALUES(?,?)";
        String relateQuery = "Insert INTO SearchWork (SearchId, WorkId) Values(?,?)";
        try {
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, idColVar);
            stmt.setString(2, arg.timestamp.toString());

            Integer success = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        for (Work work : arg.works) {
            try{
                work = _workRepo.Insert(work);
                PreparedStatement stmt = _db.GetConnection().prepareStatement(relateQuery);
                stmt.setInt(1, idColVar);
                stmt.setInt(2, work.id);
                Integer success = stmt.executeUpdate();
                stmt.close();
                
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        arg.id = idColVar;
        return arg;
    }

    @Override
    public boolean Update(Search arg) {
        return false;
    }

    @Override
    public boolean Delete(Integer id) {
        return false;
    }
    
    private List<Work> GatherWorks(Integer SearchId){
        List<Work> works = new ArrayList<>();
        String relatedQuery = "Select WorkId From SearchWork Where SearchId=?";
        try 
        {   PreparedStatement stmt = _db.GetConnection().prepareStatement(relatedQuery);
            stmt.setInt(1, SearchId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Integer workId = rs.getInt(1);
                Work work = _workRepo.GetById(workId);
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
    private Integer GetLastSearchId() {
        Integer nextID = null;
        try {
            String query = "Select IFNULL(max(id),0)  FROM Search";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                nextID = rs.getInt(1) + 1;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return nextID;
    }

    private Search ConvertToSearch(ResultSet rs) throws SQLException, ParseException {
        Search search = new Search();
        search.id = rs.getInt(1);
        search.timestamp = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(rs.getString(2));
        search.works = GatherWorks(search.id);
        return search;
    }
}