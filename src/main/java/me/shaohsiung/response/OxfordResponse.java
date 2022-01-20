package me.shaohsiung.response;

import me.shaohsiung.model.WordSpec;
import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 牛津词典页面的解析数据
 */
public class OxfordResponse extends BaseResponse {
    private String define;
    
    private List<String> examples;

    public OxfordResponse() {
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

    @Override
    public void attach(WordSpec wordSpec) {
        List<WordSpec.WordExplanation> explanationList = wordSpec.getExplanationList();
        if (explanationList == null) {
            explanationList = new ArrayList<>();
        }

        WordSpec.WordExplanation exp = new WordSpec.WordExplanation();
        exp.setExamples(examples);
//        exp.setType(type);
        exp.setExplanation(define);
        explanationList.add(exp);
    }
}
