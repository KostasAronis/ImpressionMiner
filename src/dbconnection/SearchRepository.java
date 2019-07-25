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
        Search search = new Search();
        Integer lastSearchId = -1;
        Integer lastWorkId = -1;
        try {
            String query = "Select Search.Id,Search.Timestamp,Search.WorkId from Search ";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                if (lastSearchId == rs.getInt(1)) 
                {
                    if (lastWorkId == rs.getInt(3)) 
                    {
                        search.works.add(_workRepo.GetById(rs.getInt(3)));
                    }

                    lastWorkId = rs.getInt(3);
                    lastSearchId = rs.getInt(1);
                } 
                else 
                {
                    if (search.id != null)
                        searches.add(search);

                    search = ConvertToSearch(rs);
                    search.works.add(_workRepo.GetById(rs.getInt(3)));

                    lastWorkId = rs.getInt(3);
                    lastSearchId = rs.getInt(1);
                }
            }
            searches.add(search);
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
            String query = "Select Search.Id,Search.Timestamp,Work.Id " 
                            + " FROM Search "
                            + " LEFT JOIN Work ON Work.Id = Search.WorkId " 
                            + " Where Search.Id = ? ";
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
        String query = "Insert INTO Search (Id,Timestamp,WorkId) VALUES(?,?,?)";
        for (Work work : arg.works) {
            try {
                PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
                stmt.setInt(1, idColVar);
                stmt.setString(2, arg.timestamp.toString());
                stmt.setInt(3, work.id);

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
    public boolean Delete(Search arg) {
        return false;
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
        search.timestamp = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH).parse(rs.getString(2));
        search.works = new ArrayList<Work>();
        return search;
    }
}