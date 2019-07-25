package models;

import java.util.Date;
//import java.sql.Date;
import java.util.List;

public class Search 
{
    public Integer id;
    public Date timestamp;
    public List<Work> works;

    public Search() { }
    public Search (Date timestamp,List<Work> works)
    {
        this.timestamp = timestamp;
        this.works = works;
    }
    public Search (Integer id,Date timestamp,List<Work> works)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.works = works;
    }
}