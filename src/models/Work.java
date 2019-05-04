package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Work {
    public Integer id;
    public String url;
    public Date startDate;
    public Date endDate;
    public Status status;
    public List<Word> words;
    public Work(String url, List<Word> words){
        this.startDate = new Date();
        this.url = url;
        this.status=Status.PENDING;
        this.words=words;
    }
    public static IWorkFactory simpleWorkMaker(){
        return new SimpleWorkMaker();
    }
}
class SimpleWorkMaker implements IWorkFactory{
    @Override
    public Work CreateWork(String url, List<Word> words) {
        return new Work(url, words);
    }
}