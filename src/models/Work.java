package models;

import java.sql.Date;
import java.util.List;

public class Work {
    String url;
    Date startDate;
    Date endDate;
    String status;
    List<Word> words;
}