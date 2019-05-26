package worker;

import java.util.ArrayList;
import java.util.List;
import models.Work;
import scraper.IEvaluator;
import scraper.IParser;

public class Supervisor implements Runnable{

    private List<Thread> workers=new ArrayList<Thread>();
    public Supervisor(List<Work> works, IParser p, IEvaluator e){
        for (Work w : works) {
            Object pauseLock = new Object();
            Worker worker = new Worker(p, e, w, pauseLock);
            Thread t = new Thread(worker);
            workers.add(t);
        }
    }
    @Override
    public void run() {
        for (Thread t : workers) {
            t.start();
        }
    }
}