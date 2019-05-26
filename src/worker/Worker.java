package worker;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

import models.Word;
import models.Work;
import scraper.IEvaluator;
import scraper.IParser;

public class Worker implements Runnable{
    private Work work;
    private IEvaluator evaluator;
    private IParser parser;
    private Object pauseLock;
    private Boolean paused;
    public Worker(IParser parser, IEvaluator evaluator, Work work, Object pauseLock){
        this.work=work;
        this.parser=parser;
        this.evaluator=evaluator;
        this.pauseLock=pauseLock;
        this.paused=false;
    }
    public void pause(){
        this.paused=true;
    }
    public void resume(){
        synchronized (this.pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
    @Override
    public void run() {
        try {
            String text = this.parser.getPageText(work.url);
            System.out.println("Parsed: "+work.url);
            for(Word word : work.words){
                synchronized (pauseLock) {
                    if (paused){
                        try{
                            synchronized(pauseLock){
                                this.pauseLock.wait();
                            }
                        } catch (InterruptedException e){
                            //HANDLE InterruptedException
                            e.printStackTrace();
                        }
                    }
                    word.count=this.evaluator.getWordCount(text, word.word);
                    word.impression=this.evaluator.getImpression(text, word.word);
                }
                //THIS WOULD BE SAVING RESULT TO DB
                System.out.println("Evaluated: "+word.word+" in: "+work.url+", found: "+word.count.toString() + " scored: " + word.impression.toString());
            }
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}