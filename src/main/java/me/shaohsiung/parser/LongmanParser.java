package me.shaohsiung.parser;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.response.LongmanResponse;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class LongmanParser {
    private final Elements rows;

    private LongmanParser(Elements rows) {
        this.rows = rows;
    }

    public static LongmanParser of(String html) {
        if (StringUtils.isBlank(html)) {
            log.warn("html is black, generate a non content Longman parser");
            return new LongmanParser(new Elements());
        }

        Document document = Jsoup.parse(html);
        Elements rows = document.selectXpath("/html/body/div[2]/div[2]/div[2]");
        if (rows.isEmpty()) {
            log.warn("page cannot match the pattern, generate a non content Longman parser");
            return new LongmanParser(new Elements());
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
