package me.shaohsiung.handler;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.MemoMessageContext;
import me.shaohsiung.model.ArModel;
import me.shaohsiung.model.ListMemo;
import me.shaohsiung.model.Memo;
import me.shaohsiung.model.MemoTag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class MemoListHandler implements CommandHandler {
    private final JdbcTemplate jdbcTemplate;
    
    private final static String MEMO_LIST_SQL = "SELECT id, created_at, content, url, confirm_code FROM memo " +
            "ORDER BY created_at DESC LIMIT ? OFFSET ?";
    
    private final static String MEMO_TAG_LIST_IN_SQL = "SELECT t.id AS tagId, rel.memo_id AS memoId, t.`value` " +
            "FROM memo_tag_rel rel LEFT JOIN memo_tag t ON rel.tag_id = t.id WHERE rel.memo_id IN (:memoIds)";
    
    private final static String TOTAL_MEMO_SQL = "SELECT COUNT(*) FROM memo";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public MemoListHandler() {
        this.jdbcTemplate = JdbcFactory.getJdbcTemplate();
        namedParameterJdbcTemplate = JdbcFactory.getNamedParameterJdbcTemplate();
    }

    @Override
    public ArModel handle(ExtMessageContext c) {
        // 获取memo列表
        MemoMessageContext ctx = (MemoMessageContext) c;
        int size = ctx.getSize();
        int offset = ctx.getOffset();
        List<Memo> memos = jdbcTemplate.query(MEMO_LIST_SQL, (ps) -> {
            ps.setInt(1, size);
            ps.setInt(2, offset);
        }, (rs, rowNum) -> Memo
                .builder()
                .id(rs.getLong("id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .content(rs.getString("content"))
                .url(rs.getString("url"))
                .confirmCode(rs.getString("confirm_code"))
                .build());
        if (CollectionUtils.isEmpty(memos)) {
            ListMemo emptyMemos = ListMemo.of(Collections.emptyList(), ctx.getPage(), size, 0);
            emptyMemos.enableSendMessage2DingTalk();
            return emptyMemos;
        }
        
        log.info("memos: {}", memos);

        // 获取标签
        Map<Long, List<MemoTag>> tagTable = new HashMap<>(memos.size());
        for (Memo memo : memos) {
            tagTable.put(memo.getId(), new ArrayList<>());
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memoIds", tagTable.keySet());
        namedParameterJdbcTemplate.query(MEMO_TAG_LIST_IN_SQL, params,
                (r, n) -> {
                    long memoId = r.getLong("memoId");
                    List<MemoTag> tags = tagTable.get(memoId);
                    MemoTag memoTag = new MemoTag(r.getLong("tagId"), r.getString("value"));
                    tags.add(memoTag);
                    tagTable.put(memoId, tags);
                    return null;
                });
        for (Memo memo : memos) {
            List<MemoTag> memoTags = tagTable.get(memo.getId());
            memo.setTags(memoTags);
        }

        Integer totalMemo = jdbcTemplate.queryForObject(TOTAL_MEMO_SQL, Integer.class);
        
        ListMemo listMemo = ListMemo.of(memos, ctx.getPage(), size, totalMemo);
        listMemo.enableSendMessage2DingTalk();
        return listMemo;
    }
}
