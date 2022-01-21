package me.shaohsiung.response;

import me.shaohsiung.model.WordSpec;
import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 朗文词典页面的解析数据
 */
public class LongmanResponse extends BaseResponse {
    private String define;
    
    private List<String> examples;
    
    private String type;

    public LongmanResponse() {
        super(UuidUtils.uuid(), LocalDateTime.now());
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void attach(WordSpec wordSpec) {
        List<WordSpec.WordExplanation> explanationList = wordSpec.getExplanationList();
        if (explanationList == null) {
            explanationList = new ArrayList<>();
        }

        WordSpec.WordExplanation exp = new WordSpec.WordExplanation();
        exp.setExplanation(define);
        exp.setType(type);
        exp.setExamples(examples);
        explanationList.add(exp);
        
        wordSpec.setExplanationList(explanationList);
    }
}
