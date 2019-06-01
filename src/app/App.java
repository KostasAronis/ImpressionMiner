package app;

import java.util.ArrayList;
import java.util.List;

import dbconnection.SqliteDatabase;
import dbconnection.TestThread;
import dbconnection.TestThread2;
import menu.Menu;
import menu.MenuItem;
import models.*;
import worker.Supervisor;
import worker.Worker;
import scraper.Parser;
import scraper.Evaluator;
public class App {
    private static final String[] urls = { 
        "https://www.in.gr",
        "https://finance.yahoo.com/tech/",
        "https://www.theverge.com/tech",
        "https://www.bbc.com/news/technology",
        "https://www.cnet.com/news/",
        "https://www.gadgetsnow.com/tech-news",
        "https://www.technewsworld.com/"
    };
    private static final String[] searchWords= { 
        "Apple",
        "Samsung"
    };
    public static void main(String[] args) throws Exception {
        new App().mainMenu();
    }
    private void mainMenu() 
    {
        Menu menu = new Menu();
        menu.setTitle("Impression Miner Main Menu");
        menu.addItem(new MenuItem("Option A", this, "subMenuA"));
        menu.addItem(new MenuItem("Option B", this, "subMenuB"));
        menu.addItem(new menu.MenuItem("Do the work", this, "doTheWork"));
        menu.execute();
    }
    public void subMenuA() 
    {
        Menu menu = new Menu();
        menu.setTitle("*** Sub Menu A ***");
        menu.addItem(new MenuItem("Option Aa"));
        menu.addItem(new MenuItem("Long Task",this,"LongTimeMethod"));
        menu.execute();
    }
    
    public void subMenuB() 
    {
        Menu menu = new Menu();
        menu.setTitle("*** Sub Menu B ***");
        menu.execute();
    }

    public void LongTimeMethod()
    {
       TestThread ts = new TestThread();
       TestThread2 ts2 = new TestThread2();
       ts.start();
       ts2.start();
    }
    Supervisor s;
    public void doTheWork() 
    {
        Menu menu = new Menu();
        menu.setTitle("*** Processing ***");
        menu.addItem(new menu.MenuItem("Pause", this, "pause"));
        List<Work> works = GenerateWork(urls, searchWords, Work.simpleWorkMaker(), Word.simpleWordMaker());
        Parser p = new Parser();
        Evaluator e = new Evaluator();
        s = new Supervisor(works, p, e);
        Thread t = new Thread(s);
        t.start();
        menu.execute();
    }
    public void pause(){
        Menu menu = new Menu();
        menu.setTitle("*** Paused ***");
        menu.addItem(new menu.MenuItem("Resume", this, "resume"));
        s.pause();
        menu.execute();
    }
    public void resume(){
        Menu menu = new Menu();
        menu.setTitle("*** Processing ***");
        menu.addItem(new menu.MenuItem("Pause", this, "pause"));
        s.resume();
        menu.execute();
    }
    static List<Work> GenerateWork(String[] urls, String[] searchWords, IWorkFactory workMaker, IWordFactory wordMaker){
        List<Work> works = new ArrayList<Work>();
        for (String url : urls) {
            TargetWebsite tw = new TargetWebsite(0, url);
            List<Word> words = new ArrayList<Word>();
            for (String searchWord : searchWords){
                Word word = wordMaker.CreateWord(searchWord);
                words.add(word);
            }
            Work work = workMaker.CreateWork(tw, words);
            works.add(work);
        }
        return works;
    }
}