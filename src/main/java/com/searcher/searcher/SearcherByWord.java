package com.searcher.searcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//не предназначен для распределения по потокам
public class SearcherByWord {

    private static final Logger LOGGER = LogManager.getLogger(SearcherByWord.class);
    private final static String BASE_URL = "https://???";//TODO
    private final static String SEARCH_BASE_URL = "/epz/order/extendedsearch/results.html?";
    private final static String ALL_ELEMENTS_TAG = "div.search-registry-entry-block";
    private final static String PURCHASE_OBJECT_TAG = "div.registry-entry__body-value";
    private final static String PURCHASE_NUMBER_TAG = "div.registry-entry__header-mid__number";
    private final static String PURCHASE_CUSTOMER_TAG = "div.registry-entry__body-href";
    private final static String LINK_TAG = "div.registry-entry__header-mid__number > a";
    private final static int MAX_TRY_COUNT_OF_ONE_PAGE = 10;
    private final static int MAX_TRY_COUNT_OF_MISTAKEN_PAGES = 10;
    private final String word;
    private final String originalWord;
    private Set<Result> resultSet = new HashSet<>();
    private int counter;
    private int count;
    private int pageNumber = 1;

    public SearcherByWord(String word, String originalWord, int counter) {
        this.word = word;
        this.originalWord = originalWord;
        this.counter = counter;
    }

    public int search() {
        Elements select;
        boolean endOfSearchIsAchieved = false;
        int tryCountCommon = 0;
        do {
            Optional<Document> document = getHtml(pageNumber);
            if (document.isPresent()) {
                select = document.get().select(ALL_ELEMENTS_TAG);
                if (select.isEmpty()) {
                    endOfSearchIsAchieved = true;
                } else {
                    count = processSelect(select);
                }
            } else {
                tryCountCommon++;
            }
        }
        while (!endOfSearchIsAchieved && tryCountCommon < MAX_TRY_COUNT_OF_MISTAKEN_PAGES);

        AllResult.addProcessedWord(originalWord, word);
        return count;
    }

    private int processSelect(Elements select) {
        for (Element purchase : select) {
            createResult(purchase);
            count++;
        }
        AllResult.addSetOfResult(originalWord, resultSet);
        resultSet.clear();
        pageNumber++;
        return count;
    }

    private void createResult(Element purchase) {
        counter++;
        String purchaseObject = purchase.select(PURCHASE_OBJECT_TAG).text();
        String number = purchase.select(PURCHASE_NUMBER_TAG).text();
        String customer = purchase.select(PURCHASE_CUSTOMER_TAG).text();
        String link = BASE_URL + purchase.select(LINK_TAG).first().attr("href");
        Result result = new Result(purchaseObject, number, customer, link);
        resultSet.add(result);
        printToConsole(result);
    }

    private void printToConsole(Result result) {
        System.out.println(counter + ". " + result.getNumber());
        System.out.println("Объект закупки: " + result.getPurchaseObject());
        System.out.println("Заказчик: " + result.getCustomer());
        System.out.println("Ссылка: " + BASE_URL + result.getLink());
        System.out.println("");
    }

    private Optional<Document> getHtml(int pageNumber) {
        String url = createUrl(pageNumber);
        LOGGER.info("А вот и " + url);
        int tryCount = 0;
        Document document = null;
        do {
            try {
                document = Jsoup.connect(url).maxBodySize(0).get();
            } catch (Exception ex) {
                LOGGER.error("Ошибка запроса " + url);
                tryCount++;
                if (tryCount == MAX_TRY_COUNT_OF_ONE_PAGE) {
                    AllResult.addMistakenLink(word, url);
                }
            }
        } while (document == null && tryCount <= MAX_TRY_COUNT_OF_ONE_PAGE);
        return Optional.of(document);
    }

    private String createUrl(int page) {
        return BASE_URL + SEARCH_BASE_URL + "searchString=" + word +
                "&morphology=on" +
                "&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F" +
                "&pageNumber=" + page +
                "&sortDirection=false" +
                "&recordsPerPage=_50" +
                "&showLotsInfoHidden=false" +
                "&sortBy=UPDATE_DATE" +
                "&fz44=on" +
                "&fz223=on" +
                "&pc=on" +
                "&currencyIdGeneral=-1";
    }
}
