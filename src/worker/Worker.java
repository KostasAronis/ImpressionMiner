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
    public Worker(IParser parser, IEvaluator evaluator, Work work){
        this.work=work;
        this.parser=parser;
        this.evaluator=evaluator;
    }

    @Override
    public void run() {
        try {
            String text = this.parser.getPageText(work.url);
            System.out.println("Parsed: "+work.url);
            for(Word word : work.words){
                word.count=this.evaluator.getWordCount(text, word.word);
                word.impression=this.evaluator.getImpression(text, word.word);
                System.out.println("Evaluated: "+word.word+" in: "+work.url+", found: "+word.count.toString());
            }
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}