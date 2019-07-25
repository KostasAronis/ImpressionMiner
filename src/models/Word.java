package models;

public class Word {
    public Integer id;
    public String word;
    public Integer count;
    public Double impression;

    public Word()
    {

    }
    
    public Word(String word){
        this.word=word;
    }

    public Word(Integer id,String word,Integer count,Double impression){
        this.id = id;
        this.word=word;
        this.count = count;
        this.impression = impression;
    }

}