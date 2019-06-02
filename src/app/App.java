package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dbconnection.SqliteDatabase;
import menu.Menu;
import menu.MenuItem;
import models.*;
import worker.Supervisor;
import worker.Worker;
import scraper.Parser;
import scraper.Evaluator;
public class App {
    private static List<String> urls = new ArrayList<String>(Arrays.asList(new String[]{ 
        "https://www.in.gr",
        "https://finance.yahoo.com/tech/",
        "https://www.theverge.com/tech",
        "https://www.bbc.com/news/technology",
        "https://www.cnet.com/news/",
        "https://www.gadgetsnow.com/tech-news",
        "https://www.technewsworld.com/"
    }));
    private static List<String> searchWords = new ArrayList<String>(Arrays.asList(new String[]{
        "Apple",
        "Samsung"
    }));
    public static void main(String[] args) throws Exception {
        new App().mainMenu();
    }
    Menu mainMenu;
    Menu manageKeywordsMenu;
    Menu manageTargetsMenu;
    Menu addKeywordMenu;
    Menu showKeywordsMenu;
    public void mainMenu() 
    {
        if (mainMenu==null){
            Menu menu = new Menu();
            menu.setTitle("Impression Miner Main Menu");
            menu.addItem(new MenuItem("Manage Keywords",this, "manageKeywords"));
            menu.addItem(new MenuItem("Manage Target Websites", this, "manageTargets"));
            menu.addItem(new menu.MenuItem("Start the search", this, "startTheSearch"));
            mainMenu=menu;
        }
        mainMenu.execute();
    }
    public void manageKeywords(){
        if(manageKeywordsMenu == null){
            Menu menu = new Menu();
            menu.setTitle("Manage Keywords");
            menu.addItem(new MenuItem("Add Keyword",this, "addKeyword"));
            menu.addItem(new MenuItem("Show Keywords",this, "showKeywords"));
            menu.addItem(new MenuItem("MainMenu", this, "mainMenu"));
            manageKeywordsMenu=menu;
        }
        manageKeywordsMenu.execute();
    }
    public void addKeyword(){
        System.out.print("Enter a new word:");
        String input = System.console().readLine();
        searchWords.add(input);
        manageKeywords();
    }
    public void showKeywords(){
        System.out.println("Current keywords are:");
        for (String word : searchWords){
            System.out.println(word);
        }
        manageKeywords();
    }
    public void manageTargets(){
        if(manageTargetsMenu==null){
            Menu menu = new Menu();
            menu.setTitle("Manage Target Websites");
            manageTargetsMenu=menu;
        }
        manageTargetsMenu.execute();
    }
    public void addTarget(){
        System.out.print("Enter a new target url:");
        String input = System.console().readLine();
        urls.add(input);
        manageKeywords();
    }
    public void showTargets(){
        System.out.println("Current target websites are:");
        for (String url : urls){
            System.out.println(url);
        }
        manageKeywords();
    }
    Supervisor s;
    public void startTheSearch() 
    {
        Menu menu = new Menu();
        menu.setTitle("*** Processing ***");
        menu.addItem(new menu.MenuItem("Pause", this, "pause"));
        Search search = GenerateSearch(urls, searchWords, new SimpleWorkMaker(), new SimpleWordMaker());
        Parser p = new Parser();
        Evaluator e = new Evaluator();
        s = new Supervisor(search, p, e);
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
    static Search GenerateSearch(List<String> urls, List<String> searchWords, IWorkFactory workMaker, IWordFactory wordMaker){
        Search s = new Search();
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
        s.works=works;
        s.timestamp=new Date();
        return s;
    }
}