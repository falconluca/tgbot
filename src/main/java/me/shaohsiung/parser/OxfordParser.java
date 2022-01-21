package me.shaohsiung.parser;

import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.response.OxfordResponse;
import me.shaohsiung.util.AssertUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 牛津词典页面解析器
 */
public class OxfordParser {
    private final Elements rows;

    public OxfordParser(Elements initRows) {
        AssertUtils.notNull(initRows, "initRows");
        rows = initRows;
    }

    public static OxfordParser of(String html) {
        AssertUtils.hasText(html, "html must not be blank.");

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("//*[@id=\"entryContent\"]");
        if (rows.isEmpty()) {
            throw new RuntimeException("html parse failed");
        }

        Element el = rows.get(0);
        return new OxfordParser(el.select("ol li"));
    }

    public List<BaseResponse> parse() {
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }

        List<BaseResponse> words = new ArrayList<>();
        for (Element el : rows) {
            OxfordResponse word = new OxfordResponse();
            String text = el.select("span.def").text();
            word.setDefine(text);
            
            String type = el.select("span.gram-g").text();
            word.setType(type);

            List<String> examples = new ArrayList<>();
            Elements exampleElements = el.select("span.x-gs span.x-g");
            for (Element exampleElement : exampleElements) {
                examples.add(exampleElement.text());
            }
            word.setExamples(examples);

            words.add(word);
        }

        return words;
    }
}
