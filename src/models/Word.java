package models;

public class Word {
    public Integer id;
    public String word;

    public Word()
    {

    }
    
    public Word(String word){
        this.word=word;
    }

    public Word(Integer id,String word){
        this.id = id;
        this.word=word;
    }

}