package me.shaohsiung.parser;

import me.shaohsiung.model.BaseModel;
import me.shaohsiung.model.LongmanWord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongmanParser {
    private final Elements rows;

    public LongmanParser(Elements initRows) {
        Assert.notNull(initRows, "initRows");
        rows = initRows;
    }

    public static LongmanParser of(String html) {
        Assert.hasText(html, "html must not be blank.");

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("/html/body/div[2]/div[2]/div[2]");
        if (rows.isEmpty()) {
            throw new RuntimeException("html parse failed");
        }

        Element el = rows.get(0);
        return new LongmanParser(el.select(".dictionary span.dictentry"));
    }
    
    public List<BaseModel> parse() {
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyList();
        }

        List<BaseModel> words = new ArrayList<>();
        for (Element el : rows) {
            LongmanWord word = new LongmanWord();
            String define = el.select("span.DEF").text();
            word.setDefine(define);

            words.add(word);
        }
        return words;
    }
}
