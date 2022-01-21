package me.shaohsiung.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 词汇明细
 */
public class WordSpec {
    /**
     * 词汇
     */
    private String word;

    /**
     * 词汇的创建时间
     */
    private LocalDateTime createAt;

    /**
     * 词汇的含义
     */
    private List<WordExplanation> explanationList;
    
    /**
     * 美音
     */
    private String americanPronunciation;

    /**
     * 英音
     */
    private String britishPronunciation;

    /**
     * Google 携带口型图片的发音
     */
    private String googlePronunciation;
    
    /**
     * 与该词汇有关的图片
     */
    private List<String> imgList;

    /**
     * 涉及该词汇有关的视频
     */
    private List<String> videoList;
    
    public String beautyFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append(word).append("\n")
                .append("美音: ").append(americanPronunciation).append("\n")
                .append("英音: ").append(britishPronunciation).append("\n")
                .append("Google: ").append(googlePronunciation).append("\n")
                .append("video: ").append(videoList.get(0)).append("\n");
        
        WordExplanation exp1 = explanationList.get(0);
        if (exp1 != null) {
            builder.append(exp1.formatString());
        }
        WordExplanation exp2 = explanationList.get(0);
        if (exp2 != null) {
            builder.append(exp2.formatString());
        }
        return builder.toString();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public List<WordExplanation> getExplanationList() {
        return explanationList;
    }

    public void setExplanationList(List<WordExplanation> explanationList) {
        this.explanationList = explanationList;
    }

    public String getAmericanPronunciation() {
        return americanPronunciation;
    }

    public void setAmericanPronunciation(String americanPronunciation) {
        this.americanPronunciation = americanPronunciation;
    }

    public String getBritishPronunciation() {
        return britishPronunciation;
    }

    public void setBritishPronunciation(String britishPronunciation) {
        this.britishPronunciation = britishPronunciation;
    }

    public String getGooglePronunciation() {
        return googlePronunciation;
    }

    public void setGooglePronunciation(String googlePronunciation) {
        this.googlePronunciation = googlePronunciation;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }

    public static class WordExplanation {
        /**
         * 英英解释
         */
        private String explanation;

        /**
         * 例句
         */
        private List<String> examples;

        /**
         * 词性：动词、形容词等
         */
        private String type;
        
        public String firstExample() {
            if (examples == null || examples.isEmpty()) {
                return null;
            }
            return examples.get(0);
        }

        public String secondExample() {
            if (examples == null || examples.isEmpty()) {
                return null;
            }
            return examples.get(1);
        }
        
        public String formatString() {
            StringBuilder builder = new StringBuilder();
            builder.append("exp1: ").append(type).append(" ").append(explanation).append("\n");
            
            // 仅显示2种解释
            if (firstExample() != null) {
                builder.append("- ").append(firstExample()).append("\n");
            }
            if (secondExample() != null) {
                builder.append("- ").append(secondExample()).append("\n");
            }
            return builder.toString();
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
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
    }
}
