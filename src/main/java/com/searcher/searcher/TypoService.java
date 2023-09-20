package com.searcher.searcher;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO не сделана замена на английские буквы
@Getter
public class TypoService {
    private final String word;
    private final Set<String> typoWords = new HashSet<>();

    public TypoService(String word) {
        this.word = word.toLowerCase();
    }

    public void generateTypos() {
        generateTyposWithWrongButton();
        generateTyposWithoutWrongButton();
    }

    public void generateTyposWithWrongButton() {
        for (int i = 0; i < word.length(); i++) {
            List<String> characters = Keyboard.BUTTONS.get(word.substring(i, i + 1));
            if (characters != null) {
                StringBuilder wordSb = new StringBuilder();
                for (String c : characters) {
                    resetStringBuilder(wordSb);
                    typoWords.add(wordSb.replace(i, i + 1, c).toString());
                    resetStringBuilder(wordSb);
                    typoWords.add(wordSb.insert(i, c).toString());
                    resetStringBuilder(wordSb);
                    typoWords.add(wordSb.insert(i + 1, c).toString());
                }
            }
        }
    }

    private void resetStringBuilder(StringBuilder wordSb) {
        wordSb.setLength(0);
        wordSb.append(word);
    }

    public void generateTyposWithoutWrongButton() {
        StringBuilder wordSb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            resetStringBuilder(wordSb);
            String letter = word.substring(i, i + 1);
            typoWords.add(wordSb.insert(i, letter).toString());
            typoWords.add(new StringBuilder()
                    .append(word.substring(0, i)).append(word.substring(i + 1)).toString());
        }
        for (int i = 0; i < word.length() - 1; i++) {
            String letterFirst = word.substring(i, i + 1);
            String letterSecond = word.substring(i + 1, i + 2);
            typoWords.add(new StringBuilder()
                    .append(word.substring(0, i))
                    .append(letterSecond)
                    .append(letterFirst)
                    .append(word.substring(i + 2))
                    .toString());
        }
    }

}
