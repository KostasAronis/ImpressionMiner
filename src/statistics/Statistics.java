package statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import models.Search;
import models.Word;
import models.Work;
import models.WorkWord;

public class Statistics {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

    public static boolean writeTimeTableInCSV(List<Search> searches, String fileName){
        if(searches.isEmpty()){
            return false;
        }
        Map<String, Map<String, WorkWord>> timeData = getTimeData(searches);
        List<List<String>> rows = getTimeTableRows(timeData);
        return rowsToCSV(rows, fileName);
    }
    public static boolean rowsToCSV(List<List<String>>rows, String fileName){
        FileWriter csvWriter;
        try {
            csvWriter = new FileWriter(fileName);
            for (List<String> rowData : rows) {
                csvWriter.append(String.join(",", rowData));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Something went wrong while opening the file");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static List<List<String>> getTimeTableRows(Map<String, Map<String, WorkWord>> timeData){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        headers.add("Date");
        for(String word : timeData.values().stream().findAny().orElseThrow().keySet()){
            headers.add(word);
        }
        rows.add(headers);
        Iterator<String> dateIterator = timeData.keySet().stream().sorted().iterator();
        while (dateIterator.hasNext()){
            String dateStr = dateIterator.next();
            Map<String,WorkWord> dateData = timeData.get(dateStr);
            List<String> dateRow = new ArrayList<>();
            for(String s : headers){
                if(s.equals("Date")){
                    dateRow.add(dateStr);
                } else {
                    WorkWord wordData = dateData.get(s);
                    if (wordData == null){
                        dateRow.add("N/A");
                    } else {
                        dateRow.add(wordData.count.toString()+" ["+wordData.impression.toString()+"]");
                    }
                }
            }
            rows.add(dateRow);
        }
        return rows;
    }
    public static Map<String, Map<String, WorkWord>> getTimeData(List<Search> searches){
        Collections.sort(searches,(s1, s2) -> s1.timestamp.compareTo(s2.timestamp));
        Map<String, Map<String, WorkWord>> timeData = new TreeMap<>();
        Set<String> totalWords = new HashSet<>();
        Date startDate=null;
        Date endDate=null;
        startDate = searches.get(0).timestamp;
        endDate = searches.get(searches.size()-1).timestamp;
        long days = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
        for (int i=0;i<=days;i++){
            Date newDate = addDays(startDate, i);
            String newDateStr = dateFormat.format(newDate);
            timeData.put(newDateStr, new TreeMap<>());
        }
        for (Search s : searches){
            String sDateStr = dateFormat.format(s.timestamp);
            Map<String, WorkWord> dateMap = timeData.get(sDateStr);
            for (Work work: s.works) {
                if (work.words==null){
                    System.out.print("ASDF");
                }
                for (WorkWord word : work.words){
                    if(word==null){
                        System.out.print("ASDF");
                    }
                    if(word.word == null){
                        System.out.print("ASDF");
                    }
                    if(dateMap.containsKey(word.word)){
                        dateMap.get(word.word).count+=word.count;
                        dateMap.get(word.word).impression+=word.impression;
                    } else {
                        if(!totalWords.contains(word.word)){
                            totalWords.add(word.word);
                        }
                        WorkWord dummywWorkWord = new WorkWord();
                        dummywWorkWord.count=word.count;
                        dummywWorkWord.impression = word.impression;
                        dateMap.put(word.word, dummywWorkWord);
                    }
                }
            }
        }
        for (String word : totalWords){
            for (String date : timeData.keySet()){
                if (!timeData.get(date).containsKey(word)){
                    timeData.get(date).put(word, null);
                }
            }
        }
        return timeData;
    }
    private static Date addDays(Date date, Integer days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static List<String> getReportTable(Search s){
        Integer largestWord=0;
        Map<String, WorkWord> wordMap = Statistics.getSummary(s);
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
        for (WorkWord word : wordMap.values()){
            String str = String.format(leftAlignFormat, word.word, word.count, word.impression);
            table.add(str);
        }
        table.add(rowSeperator);
        return table;
    }
    public static Map<String, WorkWord> getSummary(Search s){
        Map<String, WorkWord> wordMap = new HashMap<String, WorkWord>();
        for (Work w : s.works) {
            for (WorkWord word: w.words){
                if(wordMap.containsKey(word.word)){
                    WorkWord oldWord = wordMap.get(word.word);
                    oldWord.count=oldWord.count+word.count;
                    oldWord.impression=oldWord.impression+word.impression;
                } else {
                    WorkWord newWord = new WorkWord();
                    newWord.word = word.word;
                    newWord.count = word.count;
                    newWord.impression = word.impression;
                    wordMap.put(word.word, newWord);
                }
            }
        }
        return wordMap;
    }

}