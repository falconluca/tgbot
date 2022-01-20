package me.shaohsiung.model;

public class DeletedMemo extends Memo {
    DeletedMemo(Long deleteMemoId) {
        super(deleteMemoId, null, null, null, null, null, null, null);
    }
    
    public static ArModel of(Long deleteMemoId) {
        return new DeletedMemo(deleteMemoId);
    }

    @Override
    public String telegramMessage() {
        return "🔥 Memo #" + id + " has deleted!";
    }
}
