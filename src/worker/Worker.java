package worker;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

import models.Word;
import models.Work;
import scraper.IEvaluator;
import scraper.IParser;

public class Worker implements Runnable{
    private List<Work> works;
    private IEvaluator evaluator;
    private IParser parser;
    public Worker(IParser parser, IEvaluator evaluator, List<Work> works){
        this.works=works;
        this.parser=parser;
        this.evaluator=evaluator;
    }

    @Override
    public void run() {
        for (Work work : this.works) {
            try {
                Document doc = this.parser.readUrl(work.url);
                for(Word word : work.words){
                    word.count=this.evaluator.getWordCount(doc, word.word);
                    word.impression=this.evaluator.getImpression(doc, word.word);
                }
            } catch (IOException e) {
                //TODO: handle exception
                e.printStackTrace();
            }
            
        }
    }
}