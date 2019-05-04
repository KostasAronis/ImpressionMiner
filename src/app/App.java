package app;

import java.util.ArrayList;
import java.util.List;
import models.*;
public class App {
    private static final String[] urls = { 
        "www.in.gr",
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
        System.out.println("Hello Java");
        List<Work> works = GenerateWork(urls, searchWords, Work.simpleWorkMaker(), Word.simpleWordMaker());
    }
    static List<Work> GenerateWork(String[] urls, String[] searchWords, IWorkFactory workMaker, IWordFactory wordMaker){
        List<Work> works = new ArrayList<Work>();
        for (String url : urls) {
            List<Word> words = new ArrayList<Word>();
            for (String searchWord : searchWords){
                Word word = wordMaker.CreateWord(searchWord);
                words.add(word);
            }
            Work work = workMaker.CreateWork(url, words);
        }
        return works;
    }
}