package models;

public class SimpleWordMaker implements IWordFactory
{
    @Override
    public Word CreateWord(String word) {
        return new Word(word);
    }
}