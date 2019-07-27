package models;

import java.util.List;

public class SimpleWorkMaker implements IWorkFactory {
    @Override
    public Work CreateWork(TargetWebsite targetWebsite, List<WorkWord> words) 
    {
        return new Work(targetWebsite, words);
    }
}