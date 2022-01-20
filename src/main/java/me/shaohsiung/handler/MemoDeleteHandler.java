package me.shaohsiung.handler;

import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.MemoMessageContext;
import me.shaohsiung.model.ArModel;
import me.shaohsiung.model.DeletedMemo;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemoDeleteHandler implements CommandHandler {
    private final static String DELETE_MEMO_SQL = "DELETE FROM memo WHERE id = ? AND confirm_code = ?";
    
    private final JdbcTemplate jdbcTemplate;

    public MemoDeleteHandler() {
        jdbcTemplate = JdbcFactory.getJdbcTemplate();
    }
    
    @Override
    public ArModel handle(ExtMessageContext c) {
        MemoMessageContext ctx = (MemoMessageContext) c;
        Long id = Long.valueOf(ctx.getDeleteMemoId());
        String confirmCode = ctx.getConfirmCode();
        jdbcTemplate.update(DELETE_MEMO_SQL, id, confirmCode);
        return DeletedMemo.of(id);
    }
}
