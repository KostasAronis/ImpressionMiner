package dbconnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Word;

public class WordRepository implements IRepository<Word>
{
    private SqliteDatabase _db;
    public WordRepository()
    {
        _db = new SqliteDatabase();
    }

    @Override
    public List<Word> GetAll() {
        List<Word> words = new ArrayList<Word>();
        try 
        {
            String query = "Select * FROM Word";
            Statement stmt = _db.GetConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) 
            {
                words.add(ConvertToWord(rs));
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


    @Override
    public Word GetById(Integer id) {
        Word word = new Word();
        try 
        {
            String query = "Select * FROM Word Where Id = ?";
            PreparedStatement stmt =  _db.GetConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                word = ConvertToWord(rs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return word;
    }

    @Override
    public Word Insert(Word arg) {
        try {
            String query = "Insert INTO Word (Word) VALUES(?)";
            PreparedStatement stmt = _db.GetConnection().prepareStatement(query);
            stmt.setString(1, arg.word);
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
            arg = this.GetById(idColVar);
        } catch (SQLException e) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return arg;
    }

    @Override
    public boolean Update(Word arg) {
        return false;
    }

    @Override
    public boolean Delete(Integer id) {
        try 
        {
            String query = "DELETE FROM Word Where Id = ?";
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

    //#region Helpers
    private Word ConvertToWord(ResultSet rs) throws SQLException {
        return new Word
        (
            rs.getInt("Id"),
            rs.getString("Word")
        );
    }
    //#endregion
}