package dbconnection;

import models.Word;

public class WordRepository implements IRepositoryFactory<Word,Word>
{

    @Override
    public Word Get(Word arg) {
        return null;
    }

    @Override
    public Word Insert(Word arg) {
        return null;
    }

    @Override
    public boolean Update(Word arg) {
        return false;
    }

    @Override
    public boolean Delete(Word arg) {
        return false;
    }

}