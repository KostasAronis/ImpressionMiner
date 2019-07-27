package models;
import java.util.List;

public class WorkWord {
    public Integer id;
    public Integer WorkId;
    public String word;
    public Integer count;
    public Double impression;

    public WorkWord()
    {

    }

    public WorkWord(Integer id,Integer workId, String word){
        this.id = id;
        this.WorkId=workId;
        this.word=word;
    }

}