package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap;

import models.Work;
import scraper.IEvaluator;
import scraper.IParser;

public class Supervisor implements Runnable{

    private Map<Worker,Thread> workers=new HashMap<Worker,Thread>();
    public void pause(){
        for (Thread t : workers.values()){
            t.suspend();
        }
        // for (Worker w : workers.keySet()){
        //     w.pause();
        // }
    }
    public void resume(){
        for (Thread t : workers.values()){
            t.resume();
        }
        // for (Worker w : workers.keySet()){
        //     w.resume();
        // }
    }
    public Supervisor(List<Work> works, IParser p, IEvaluator e){
        for (Work w : works) {
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
    }
}