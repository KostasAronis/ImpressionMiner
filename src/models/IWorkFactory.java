package models;

import java.util.List;

public interface IWorkFactory {
    public Work CreateWork(String url, List<Word> words);
}