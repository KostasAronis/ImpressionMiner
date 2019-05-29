package app;

import java.util.ArrayList;
import java.util.List;

import dbconnection.SqliteDatabase;
import dbconnection.TestThread;
import dbconnection.TestThread2;
import menu.Menu;
import menu.MenuItem;
import models.*;
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
        //System.out.println("Hello Java");
        // List<Work> works = GenerateWork(urls, searchWords, Work.simpleWorkMaker(), Word.simpleWordMaker());
        // Parser p = new Parser();
        // Evaluator e = new Evaluator();
        // for (Work w : works) {
        //     Worker worker = new Worker(p,e,w);
        //     Thread t = new Thread(worker);
        //     t.start();
        // }
    }
    private void mainMenu() 
    {
        Menu menu = new Menu();
        menu.setTitle("Impression Miner Main Menu");
        menu.addItem(new MenuItem("Option A", this, "subMenuA"));
        menu.addItem(new MenuItem("Option B", this, "subMenuB"));
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

    static List<Work> GenerateWork(String[] urls, String[] searchWords, IWorkFactory workMaker, IWordFactory wordMaker){
        // List<Work> works = new ArrayList<Work>();
        // for (String url : urls) {
        //     List<Word> words = new ArrayList<Word>();
        //     for (String searchWord : searchWords){
        //         Word word = wordMaker.CreateWord(searchWord);
        //         words.add(word);
        //     }
        //     Work work = workMaker.CreateWork(url, words);
        //     works.add(work);
        // }
        // return works;
        return null;
    }
}