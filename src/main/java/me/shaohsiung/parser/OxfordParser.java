package me.shaohsiung.parser;

import me.shaohsiung.model.OxfordWord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OxfordParser {
    private final Elements rows;

    public OxfordParser(Elements initRows) {
        Assert.notNull(initRows, "initRows");
        rows = initRows;
    }

    public static OxfordParser of(String html) {
        Assert.hasText(html, "html must not be blank.");

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("//*[@id=\"entryContent\"]");
        if (rows.isEmpty()) {
            throw new RuntimeException("html parse failed");
        }

        Element el = rows.get(0);
        return new OxfordParser(el.select("ol li"));
    }

    public void parse() {
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        List<OxfordWord> words = new ArrayList<>();
        for (Element el : rows) {
            OxfordWord word = new OxfordWord();
            String text = el.select("span.def").text();
            word.setDefine(text);

            List<String> sen = new ArrayList<>();
            Elements sentences = el.select("span.x-gs span.x-g");
            for (Element sentence : sentences) {
                String t1 = sentence.text();
                sen.add(t1);
            }
            word.setSentences(sen);

            words.add(word);
        }

        System.out.println(words);
    }
}
