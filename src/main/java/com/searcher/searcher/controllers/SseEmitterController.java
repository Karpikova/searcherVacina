package com.searcher.searcher.controllers;

import com.searcher.searcher.AllResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class SseEmitterController {
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @GetMapping("/foundPages")
    public ResponseEntity<SseEmitter> foundPages(@RequestParam(value = "word") String word) {
        if (AllResult.getWhoIsDoneByWord(word)) {
            return getFoundPagesFinished(word);
        } else {
            return getFoundPagesInProcess(word);
        }
    }

    private ResponseEntity getFoundPagesInProcess(String word) {
        String text = "Я нашел страниц: " + AllResult.getSizeOfResultByWord(word);
        SseEmitter emitter = createEmitter(text);
        return new ResponseEntity(emitter, HttpStatus.OK);
    }

    private ResponseEntity getFoundPagesFinished(String word) {
        String text = "Поиск завершен. Я нашел " + AllResult.getSizeOfResultByWord(word) + " страниц. <br>";
        String allResult = AllResult.getAllResultForDisplayByWord(word);
        SseEmitter emitter = createEmitter(text + allResult);
        return new ResponseEntity(emitter, HttpStatus.OK);
    }

    private SseEmitter createEmitter(String text) {
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            try {
                emitter.send(text);
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/handledWords")
    public ResponseEntity<SseEmitter> handledWords(@RequestParam(value = "word") String word) {
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            try {
                Thread.sleep(3000);
                emitter.send(AllResult.getProcessedWordsForDisplayByWord(word));
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
//            return new ResponseEntity(emitter, HttpStatus.NO_CONTENT);
        return new ResponseEntity(emitter, HttpStatus.OK);
    }
}