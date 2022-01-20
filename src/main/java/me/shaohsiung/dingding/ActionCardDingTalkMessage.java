package me.shaohsiung.dingding;

import java.util.Collections;
import java.util.List;

public class ActionCardDingTalkMessage extends AbstractDingMessage {
    private ActionCard actionCard;

    public ActionCardDingTalkMessage(String title, String text, String url) {
        super("actionCard");
        // TODO url not exist
        Button btn = new Button("立即处理", url);
        this.actionCard = new ActionCard(title, text, Collections.singletonList(btn));
    }

    public ActionCard getActionCard() {
        return actionCard;
    }

    public void setActionCard(ActionCard actionCard) {
        this.actionCard = actionCard;
    }
    
    static class Button {
        private String title;
        private String actionURL;

        public Button(String title, String actionURL) {
            this.title = title;
            this.actionURL = actionURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getActionURL() {
            return actionURL;
        }

        public void setActionURL(String actionURL) {
            this.actionURL = actionURL;
        }
    }

    static class ActionCard {
        private String title;

        private String text;
        
        private String btnOrientation;
        
        private List<Button> btns;

        public ActionCard(String title, String text, List<Button> btns) {
            this.title = title;
            this.text = text;
            this.btnOrientation = "0";
            this.btns = btns;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getBtnOrientation() {
            return btnOrientation;
        }

        public void setBtnOrientation(String btnOrientation) {
            this.btnOrientation = btnOrientation;
        }

        public List<Button> getBtns() {
            return btns;
        }

        public void setBtns(List<Button> btns) {
            this.btns = btns;
        }
    }
}
