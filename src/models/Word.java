package models;

public class Word {
    public Integer id;
    public String word;
    public Integer count;
    public Double impression;
    public Word(String word){
        this.word=word;
    }
}