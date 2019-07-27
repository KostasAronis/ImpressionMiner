package app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dbconnection.IRepository;
import dbconnection.RepositoryFactory;
import dbconnection.SearchRepository;
import dbconnection.SqliteDatabase;
import dbconnection.WordRepository;
import dbconnection.WorkRepository;
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
    private static List<TargetWebsite> targetUrlsDB = new ArrayList<TargetWebsite>();
    private static List<Word> keywordsDB = new ArrayList<Word>();
    private static IRepository<TargetWebsite> targetWebsiteRepo;
    private static IRepository<Word> wordRepo;
    private static IRepository<Work> workRepo;
    private static IRepository<Search> searchRepo;
    public static void main(String[] args) throws Exception 
    {
        targetWebsiteRepo = RepositoryFactory.GetRepository(TargetWebsite.class);
        wordRepo = RepositoryFactory.GetRepository(Word.class);
        workRepo = RepositoryFactory.GetRepository(Work.class);
        searchRepo = RepositoryFactory.GetRepository(Search.class);
        targetUrlsDB = targetWebsiteRepo.GetAll();
        keywordsDB = wordRepo.GetAll();
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
            menu.addItem(new menu.MenuItem("View statistics", this, "viewStatistics"));
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
        Word newWord = new Word(input);
        Word newWordWithId = wordRepo.Insert(newWord);
        keywordsDB.add(newWordWithId);
        manageKeywords();
    }
    public void showKeywords(){
        if (keywordsDB.size()==0){
            System.out.println("No keywords are currently stored in db.");
            manageKeywords();
            return;
        }
        System.out.println("Current keywords are:");
        for (Word word : keywordsDB){
            System.out.println(word.id + " --- " + word.word);
        }
        System.out.println("Enter the id of a word to delete it or B to go back");
        String inputID = System.console().readLine();
        if (!inputID.toUpperCase().equals("B")) {
            try{
                Integer id = Integer.parseInt(inputID);
                if(wordRepo.Delete(id)){
                    keywordsDB.removeIf(w->w.id==id);
                }
            } catch (Exception ex){
                System.out.println("Incorrect id!");
            }
        }
        manageKeywords();
    }
    public void manageTargets(){
        if(manageTargetsMenu==null){
            Menu menu = new Menu();
            menu.setTitle("Manage Target Websites");
            menu.addItem(new MenuItem("Add Target",this, "addTarget"));
            menu.addItem(new MenuItem("Show Targets",this, "showTargets"));
            menu.addItem(new MenuItem("MainMenu", this, "mainMenu"));
            manageTargetsMenu=menu;
        }
        manageTargetsMenu.execute();
    }
    public void addTarget(){
        System.out.print("Enter a new target url:");
        String input = System.console().readLine();
        TargetWebsite newTarget = new TargetWebsite(input);
        TargetWebsite newTargetWithId = targetWebsiteRepo.Insert(newTarget);
        targetUrlsDB.add(newTargetWithId);
        manageTargets();
    }
    public void showTargets(){
        if (targetUrlsDB.size()==0){
            System.out.println("No target urls are currently stored in db.");
            manageTargets();
            return;
        }
        System.out.println("Current target websites are:");
        for ( TargetWebsite target : targetUrlsDB ) {
            System.out.println(target.getId() + " --- " + target.getUrl());
        }
        System.out.println("Enter the id of a target url to delete it or B to go back");
        String inputID = System.console().readLine();
        if (!inputID.toUpperCase().equals("B")) {
            try{
                Integer id = Integer.parseInt(inputID);
                if(targetWebsiteRepo.Delete(id)){
                    targetUrlsDB.removeIf(t->t.getId()==id);
                }
            } catch (Exception ex){
                System.out.println("Incorrect id!");
            }
        }
        manageTargets();
    }
    Supervisor s;
    public void startTheSearch() 
    {
        String err="";
        if (keywordsDB.size() == 0 ){
            err+="Cannot start a search without any keywords.";
        }
        if (targetUrlsDB.size() == 0 ){
            err+="Cannot start a search without any target urls.";
        }
        if (err!=""){
            System.out.println(err);
            mainMenu.execute();
            return;
        }
        Menu menu = new Menu();
        menu.setTitle("*** Processing ***");
        menu.addItem(new menu.MenuItem("Pause", this, "pause"));
        Search search = GenerateSearch(targetUrlsDB, keywordsDB);
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

    public void viewStatistics(){
        Menu menu = new Menu();
        menu.setTitle("*** Statistics ***");
        List<Search> searches = searchRepo.GetAll();
        menu.addItem(new menu.MenuItem("Export to CSV", this, "exportCSV"));
        menu.addItem(new MenuItem("MainMenu", this, "mainMenu"));
        menu.execute();
    }
    public void exportCSV(){

    }

    static Search GenerateSearch(List<TargetWebsite> targetWebsites, List<Word> words){
        List<Work> works = new ArrayList<Work>();
        for (TargetWebsite targetWebsite : targetWebsites) {
            List<WorkWord> workWords = new ArrayList<>();
            for (Word word : words){
                WorkWord ww = new WorkWord();
                ww.count=0;
                ww.impression=0d;
                ww.word = word.word;
                workWords.add(ww);
            }
            Work work = new Work(targetWebsite, workWords);
            works.add(work);
        }
        Search s = new Search(new Date(), works);
        return s;
    }
}