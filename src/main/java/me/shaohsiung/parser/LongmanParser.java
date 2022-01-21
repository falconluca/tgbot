package me.shaohsiung.parser;

import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.response.LongmanResponse;
import me.shaohsiung.util.AssertUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 朗文词典页面解析器
 */
public class LongmanParser {
    private final Elements rows;

    public LongmanParser(Elements initRows) {
        AssertUtils.notNull(initRows, "initRows");
        rows = initRows;
    }

    public static LongmanParser of(String html) {
        AssertUtils.hasText(html, "html must not be blank.");

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("/html/body/div[2]/div[2]/div[2]");
        if (rows.isEmpty()) {
            throw new RuntimeException("html parse failed");
        }

        Element el = rows.get(0);
        return new LongmanParser(el.select(".dictionary span.dictentry"));
    }
    
    public List<BaseResponse> parse() {
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        List<BaseResponse> words = new ArrayList<>();
        for (Element el : rows) {
            LongmanResponse word = new LongmanResponse();
            String define = el.select("span.DEF").text();
            word.setDefine(define);

            String type = el.select("span.POS").text();
            word.setType(type);
            
            List<String> examples = new ArrayList<>();
            Elements exampleElements = el.select("span.EXAMPLE");
            for (Element exampleElement : exampleElements) {
                examples.add(exampleElement.text());
            }
            word.setExamples(examples);

            words.add(word);
        }
        return words;
    }
}
