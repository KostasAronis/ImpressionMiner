package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Search;
import models.Word;
import models.Work;
import scraper.IEvaluator;
import scraper.IParser;

public class Supervisor implements Runnable{

    private Map<Worker,Thread> workers=new HashMap<Worker,Thread>();
    private Search _search;
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
    public Supervisor(Search s, IParser p, IEvaluator e){
        _search=s;
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
    }
    private void report(Search s){
        System.out.println("Final results");
        for (String line : getReportTable(s)){
            System.out.println(line);
        }

    }
    private List<String> getReportTable(Search s){
        Integer largestWord=0;
        Map<String, Word> wordMap = getSummary(s);
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
        for (Word word : wordMap.values()){
            String str = String.format(leftAlignFormat, word.word, word.count, word.impression);
            table.add(str);
        }
        table.add(rowSeperator);
        return table;
    }
    private Map<String, Word> getSummary(Search s){
        Map<String, Word> wordMap = new HashMap<String, Word>();
        for (Work w : s.works) {
            for (Word word: w.words){
                if(wordMap.containsKey(word.word)){
                    Word oldWord = wordMap.get(word.word);
                    Word newWord = new Word( word.word );
                    newWord.count=oldWord.count+word.count;
                    newWord.impression=oldWord.impression+word.impression;
                    wordMap.replace(word.word, newWord);
                } else {
                    Word newWord = new Word( word.word );
                    newWord.count = word.count;
                    newWord.impression = word.impression;
                    wordMap.put(word.word, newWord);
                }
            }
        }
        return wordMap;
    }
}