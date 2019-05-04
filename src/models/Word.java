package models;

public class Word {
    public Integer id;
    public String word;
    public Integer count;
    public Integer impression;
    public Word(String word){
        this.word=word;
    }
    public static IWordFactory simpleWordMaker(){
        return new SimpleWordMaker();
    }
}
class SimpleWordMaker implements IWordFactory{
    @Override
    public Word CreateWord(String word) {
        return new Word(word);
    }
}