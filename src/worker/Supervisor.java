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
import statistics.Statistics;

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
    public void start(){
        thread.start();
    }
    public void join(){
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("Supervisor thread interrupted");
            e.printStackTrace();
        }
    }
    public Thread thread;
    public boolean done;
    public Supervisor(Search s, IParser p, IEvaluator e){
        _search=s;
        _searchRepo = new SearchRepository();
        thread = new Thread(this);
        done = false;
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
        this.done = true;
    }
    private void report(Search s){
        System.out.println("Final results");
        for (String line : Statistics.getReportTable(s)){
            System.out.println(line);
        }
    }
    private void storeResultsInDB(Search s){
        _searchRepo.Insert(s);
        System.out.println("Results stored in db.");
    }
}