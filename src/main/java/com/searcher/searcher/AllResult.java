package com.searcher.searcher;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AllResult {
    private static Map<String, Set<Result>> allResult = new ConcurrentHashMap();
    private static Map<String, LinkedList<String>> processedWords = new ConcurrentHashMap();
    private static Map<String, LinkedList<String>> mistakenLinks = new ConcurrentHashMap();
    private static Map<String, String> processedWordsForDisplay = new ConcurrentHashMap();
    //todo убери булеан
    private static Map<String, Boolean> whoIsDone = new ConcurrentHashMap();

    public static void setWhoIsDoneByWord(String word) {
        whoIsDone.put(word, true);
    }

    public static boolean getWhoIsDoneByWord(String word) {
        Boolean result = whoIsDone.get(word);
        return result == null ? false : true;
    }

    public static String getProcessedWordsForDisplayByWord(String word) {
        String res = processedWordsForDisplay.get(word);
        return res == null ? "" : res;
    }

    public static String getAllResultForDisplayByWord(String word) {
        Set<Result> results = allResult.get(word);
        if (results == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            results.forEach(q -> sb.append(q.getNumber()).append(". ").append(q.getLink()).append("<br>"));
            return sb.toString();
        }
    }

    public static int getSizeOfResultByWord(String word) {
        Set<Result> results = allResult.get(word);
        return results == null ? 0 : results.size();
    }


    public static int getSizeOfMistakenLinks(String word) {
        LinkedList<String> results = mistakenLinks.get(word);
        return results == null ? 0 : results.size();
    }

    //merge это конечно здорово, но тогда надо решать вопрос очистки resultSet в методе search
    public static void addSetOfResult(String word, Set<Result> result) {
        Set<Result> current = allResult.get(word);
        if (current == null) {
            Set<Result> newResult = new HashSet<>();
            newResult.addAll(result);
            allResult.put(word, newResult);
        } else {
            allResult.get(word).addAll(result);
        }
    }

    public static void processedWordsByWordInColumn(String word) {
        LinkedList<String> current = processedWords.get(word);
        if (current != null) {
            StringBuilder wordsInColumn = new StringBuilder();
            current.stream().forEach(q -> wordsInColumn.append(q).append("<br>"));
            processedWordsForDisplay.put(word, wordsInColumn.toString());
        } else {
            processedWordsForDisplay.put(word, "");
        }
    }

    public static void addProcessedWord(String word, String processedWord) {
        LinkedList<String> current = processedWords.get(word);
        if (current != null) {
            current.add(processedWord);
        } else {
            LinkedList<String> newList = new LinkedList<>();
            newList.add(processedWord);
            processedWords.put(word, newList);
        }
        processedWordsByWordInColumn(word);
    }

    public static void addMistakenLink(String word, String link) {
        LinkedList<String> current = mistakenLinks.get(word);
        if (current != null) {
            current.add(link);
        } else {
            LinkedList<String> newList = new LinkedList<>();
            newList.add(link);
            mistakenLinks.put(word, newList);
        }
    }
}
