package scraper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EvaluationWeights{
/*
        The following three resources are used in the evaluation algorithm
        and are provided as an example, they are in no way complete, well weighted
        or even remotely sufficient.
    */
    static Map<String,Double> wordValue = new HashMap<String,Double>()
    {
        {
            put("good", 1.0);
            put("bad", -1.0);
            put("great", 5.0);
            put("terrible", -5.0);
            put("awesome", 10.0);
            put("awful", -10.0);
        }
    };
    static Map<String,Double> wordΜultiplier=new HashMap<String,Double>()
    {
        {
            put("really", 2.0);
            put("very", 3.0);
            put("not", -1.0);
        }
    };
    static List<String> noiseWords = new ArrayList<String>(){
        {
            add("the");
            add("that");
            add("all");
        }
    };
}

public class Evaluator implements IEvaluator {
    
    //Counts the given word appearances inside the given text
    @Override
    public Integer getWordCount(String text, String word) {
        Pattern pattern = Pattern.compile(word);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find())
            count++;
        return count;
    }

    //Returns a double number that simulates the "bad" or "good" meaning of a 
    //word with the positive results to mean good and negative bad
    @Override
    public Double getImpression(String text, String word) {
        Double score=0.0;
        for (String sentence : this.getSentences(text,word)) {
            List<String> sentenceWords=this.getSentenceWords(sentence);
            Double sentenceValue = this.getSentenceValue(sentenceWords, EvaluationWeights.wordValue, EvaluationWeights.wordΜultiplier, EvaluationWeights.noiseWords);
            score+=sentenceValue;
        }
        return score;
    }

    //Gets all sentences that contains our word with regex pattern
    private List<String> getSentences(String text,String word){
        List<String> sentences = new ArrayList<String>();
        String regex = "[^.]* "+word+" [^.]*\\.";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find())
            sentences.add(matcher.group());
        return sentences;
    }

    //Splits a sentence that contains our word into a list of words
    private List<String> getSentenceWords(String sentence){
        String[] words = sentence.split("\\W+"); // seperate the sentence into words
        List<String> sentenceWords = Arrays.asList(words);
        return sentenceWords;
    }

    //calculates the words value inside the sentence depending on evalution weights that we declared previously
    private Double getSentenceValue(List<String> sentence,
                                    Map<String,Double> wordValue,
                                    Map<String,Double> wordΜultiplier,
                                    List<String> noiseWords){
        Double score = 0.0;
        Double multiplier = 1.0;
        Double wordGap = 0.0;
        for (String word : sentence) {
            if(wordValue.containsKey(word)){
                score += (multiplier/1+wordGap)*wordValue.get(word);
                multiplier=1.0;
                wordGap=0.0;
            } else {
                if(wordΜultiplier.containsKey(word)){
                    multiplier*=wordΜultiplier.get(word);
                } else {
                    if(!noiseWords.contains(word)){
                        wordGap+=1;
                    }
                }
            }
        }
        return score;
    }
}
