package models;

import java.util.List;


public class Work {
    public Integer id;
    public TargetWebsite targetWebsite;
    public List<WorkWord> words;

    public Work()
    {
        
    }
    public Work(TargetWebsite targetWebsite, List<WorkWord> words){
        this.targetWebsite = targetWebsite;
        this.words=words;
    }
    public Work(Integer id,TargetWebsite targetWebsite, List<WorkWord> words)
    {
        this.id = id;
        this.targetWebsite = targetWebsite;
        this.words = words;
    }
}
