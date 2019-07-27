package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbconnection.IRepository;
import dbconnection.RepositoryFactory;
import dbconnection.SearchRepository;
import models.Search;
import models.Word;
import models.Work;
import models.WorkWord;
import scraper.IEvaluator;
import scraper.IParser;

public class Supervisor implements Runnable{

    private Map<Worker,Thread> workers=new HashMap<Worker,Thread>();
    private Search _search;
    private IRepository<Search> _searchRepo;
    public void pause(){
        for (Thread t : workers.values()){
            t.suspend();
        }
    }
    public void resume(){
        for (Thread t : workers.values()){
            t.resume();
        }
    }
    public Supervisor(Search s, IParser p, IEvaluator e){
        _search=s;
        _searchRepo = new SearchRepository();
        for (Work w : s.works){
            Object pauseLock = new Object();
            Worker worker = new Worker(p, e, w, pauseLock);
            Thread t = new Thread(worker);
            workers.put(worker, t);
        }
    }
    @Override
    public void run() {
        for (Thread t : workers.values()) {
            t.start();
        }
        for (Thread t : workers.values()) {
            try{
                t.join();
            } catch (InterruptedException e){
                System.out.println("Worker thread interrupted!");
                e.printStackTrace();
            }
        }
        report(_search);
        storeResultsInDB(_search);
        System.out.println("Results stored in db.");
    }
    private void report(Search s){
        System.out.println("Final results");
        for (String line : getReportTable(s)){
            System.out.println(line);
        }
    }
    private void storeResultsInDB(Search s){
        _searchRepo.Insert(s);
    }
    private List<String> getReportTable(Search s){
        Integer largestWord=0;
        Map<String, WorkWord> wordMap = getSummary(s);
        for (String w : wordMap.keySet()){
            if (w.length()>largestWord){
                largestWord = w.length();
            }
        }
        largestWord = largestWord < 4 ? 4 : largestWord;
        String leftAlignFormat = "| %-"+largestWord.toString()+"s | %-5d | %-10f |%n";
        List<String> table = new ArrayList<String>();
        String rowSeperator = String.format("+%s+-------+------------+%n","-".repeat(largestWord+2));
        String titleRow = String.format("| Word %s| Count | Impression |%n"," ".repeat(largestWord-4));
        table.add(rowSeperator);
        table.add(titleRow);
        table.add(rowSeperator);
        for (WorkWord word : wordMap.values()){
            String str = String.format(leftAlignFormat, word.word, word.count, word.impression);
            table.add(str);
        }
        table.add(rowSeperator);
        return table;
    }
    private Map<String, WorkWord> getSummary(Search s){
        Map<String, WorkWord> wordMap = new HashMap<String, WorkWord>();
        for (Work w : s.works) {
            for (WorkWord word: w.words){
                if(wordMap.containsKey(word.word)){
                    WorkWord oldWord = wordMap.get(word.word);
                    oldWord.count=oldWord.count+word.count;
                    oldWord.impression=oldWord.impression+word.impression;
                } else {
                    WorkWord newWord = new WorkWord();
                    newWord.word = word.word;
                    newWord.count = word.count;
                    newWord.impression = word.impression;
                    wordMap.put(word.word, newWord);
                }
            }
        }
        return wordMap;
    }
}