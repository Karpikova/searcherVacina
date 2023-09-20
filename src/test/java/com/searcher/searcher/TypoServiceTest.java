package com.searcher.searcher;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Set;

public class TypoServiceTest extends TestCase {

    public void testTypoWithWrongButton() {
        TypoService cat = new TypoService("Кот");
        cat.generateTyposWithWrongButton();
        Set<String> typoWords = cat.getTypoWords();
        Assert.assertEquals(40, typoWords.size()); //дублей нет
        Assert.assertTrue(typoWords.contains("еот"));
        Assert.assertTrue(typoWords.contains("вот"));
        Assert.assertTrue(typoWords.contains("уот"));

        Assert.assertTrue(typoWords.contains("екот"));
        Assert.assertTrue(typoWords.contains("вкот"));
        Assert.assertTrue(typoWords.contains("укот"));

        Assert.assertTrue(typoWords.contains("кеот"));
        Assert.assertTrue(typoWords.contains("квот"));
        Assert.assertTrue(typoWords.contains("куот"));
    }

    public void testTypoWithoutWrongButton() {
        TypoService cat = new TypoService("Кот");
        cat.generateTyposWithoutWrongButton();
        Set<String> typoWords = cat.getTypoWords();
        Assert.assertEquals(8, typoWords.size());
        Assert.assertTrue(typoWords.contains("от"));
        Assert.assertTrue(typoWords.contains("кт"));
        Assert.assertTrue(typoWords.contains("ко"));

        Assert.assertTrue(typoWords.contains("ккот"));
        Assert.assertTrue(typoWords.contains("коот"));
        Assert.assertTrue(typoWords.contains("котт"));

        Assert.assertTrue(typoWords.contains("окт"));
        Assert.assertTrue(typoWords.contains("кто"));
    }
}