package worker;

import java.io.IOException;

import models.Word;
import models.Work;
import models.WorkWord;
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

    //Pauses worker thread
    public void pause()
    {
        this.paused=true;
    }

    //Resumes worker thread
    public void resume()
    {
        synchronized (this.pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

    @Override
    public void run() {
        try 
        {
            //Finds the text of the website for each worker and searches for all the words that the worker holds inside this text
            String text = this.parser.getPageText(work.targetWebsite.getUrl());
            System.out.println("Parsed: "+ work.targetWebsite.getUrl());
            for(WorkWord word : work.words){
                word.count=this.evaluator.getWordCount(text, word.word);
                word.impression=this.evaluator.getImpression(text, word.word);
                //THIS WOULD BE SAVING RESULT TO DB
                System.out.println("Evaluated: "+word.word+" in: "+work.targetWebsite.getUrl()+", found: "+word.count.toString() + " scored: " + word.impression.toString());
            }
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    @Deprecated
    public void run2() {
        try {
            String text = this.parser.getPageText(work.targetWebsite.getUrl());
            System.out.println("Parsed: "+ work.targetWebsite.getUrl());
            for(WorkWord word : work.words){
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
                System.out.println("Evaluated: "+word.word+" in: "+work.targetWebsite.getUrl()+", found: "+word.count.toString() + " scored: " + word.impression.toString());
            }
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}