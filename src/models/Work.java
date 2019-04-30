package models;

import java.sql.Date;
import java.util.List;

public class Work {
    Integer id;
    String url;
    Date startDate;
    Date endDate;
    String status;
    List<Word> words;
    public void W(){
        this.id=4;
        this.words.get(1).count=4;
    }
}