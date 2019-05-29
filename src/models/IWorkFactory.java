package models;

import java.util.List;

public interface IWorkFactory {
    public Work CreateWork(TargetWebsite targetWebsite, List<Word> words);
}