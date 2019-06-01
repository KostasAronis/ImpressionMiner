package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Work {
    public Integer id;
    public TargetWebsite targetWebsite;
    public List<Word> words;

    public Work(TargetWebsite targetWebsite, List<Word> words){
        this.targetWebsite = targetWebsite;
        this.words=words;
    }
}
