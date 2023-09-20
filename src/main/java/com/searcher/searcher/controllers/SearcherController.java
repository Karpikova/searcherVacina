package com.searcher.searcher.controllers;

import com.searcher.searcher.AllResult;
import com.searcher.searcher.SearcherByWord;
import com.searcher.searcher.TypoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SearcherController {

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity doSearch(@RequestParam(value = "word", defaultValue = "World") String word) throws IOException, InterruptedException {

        TypoService wordTypoService = new TypoService(word);
        wordTypoService.generateTypos();
        int found = 0;

        System.out.println("Поиск по " + word);
        SearcherByWord searcherByWord = new SearcherByWord(word, word, found);
        found = found + searcherByWord.search();

        //int i = 0;
        for (String typoWord : wordTypoService.getTypoWords()) {
            System.out.println("Поиск по " + typoWord);
            searcherByWord = new SearcherByWord(typoWord, word, found);
            found = found + searcherByWord.search();
            Thread.sleep(5000);
            //  i++;
            //  if (i>5) break; //for the fast test
        }
        AllResult.setWhoIsDoneByWord(word);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
