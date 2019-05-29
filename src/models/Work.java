package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Work {
    public Integer id;
    public TargetWebsite targetWebsite;
    public Date startDate;
    public Date endDate;
    public Status status;
    public List<Word> words;
    public Work(TargetWebsite targetWebsite, List<Word> words){
        this.startDate = new Date();
        this.targetWebsite = targetWebsite;
        this.status=Status.PENDING;
        this.words=words;
    }
    public static IWorkFactory simpleWorkMaker()
    {
        return new SimpleWorkMaker();
    }
}
