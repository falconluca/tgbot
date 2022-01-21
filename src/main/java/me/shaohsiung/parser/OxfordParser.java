package me.shaohsiung.parser;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.response.OxfordResponse;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class OxfordParser {
    private final Elements rows;

    private OxfordParser(Elements rows) {
        this.rows = rows;
    }

    public static OxfordParser of(String html) {
        if (StringUtils.isBlank(html)) {
            log.warn("html is black, generate a non content Oxford parser");
            return new OxfordParser(new Elements());
        }

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("//*[@id=\"entryContent\"]");
        if (rows.isEmpty()) {
            log.warn("page cannot match the pattern, generate a non content Oxford parser");
            return new OxfordParser(new Elements());
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
