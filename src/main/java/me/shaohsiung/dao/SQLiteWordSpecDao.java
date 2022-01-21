package me.shaohsiung.dao;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.model.WordSpec;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SQLiteWordSpecDao implements WordSpecDao {
    private static final String PERSIST_SQL = "INSERT INTO word_spec(word, create_at, american_pronunciation, " +
            "british_pronunciation, google_pronunciation, img_list, video_list, explanation_1, explanation_2) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    @Override
    public void persist(WordSpec wordSpec) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            conn.setAutoCommit(false);
            
            PreparedStatement pstmt = conn.prepareStatement(PERSIST_SQL);
            pstmt.setString(1, wordSpec.getWord());
            // https://stackoverflow.com/a/45104131/9076327
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(wordSpec.getCreateAt()));
            pstmt.setString(3, wordSpec.getAmericanPronunciation());
            pstmt.setString(4, wordSpec.getBritishPronunciation());
            pstmt.setString(5, wordSpec.getGooglePronunciation());
            List<String> imgList = wordSpec.getImgList();
            pstmt.setString(6, String.join(",", imgList));
            List<String> videoList = wordSpec.getVideoList();
            pstmt.setString(7, String.join(",", videoList));
            List<WordSpec.WordExplanation> explanationList = wordSpec.getExplanationList();

            String exp1 = composeWordExplanation(explanationList.get(0));
            if (StringUtils.isNotBlank(exp1)) {
                pstmt.setString(8, exp1);
            }
            String exp2 = composeWordExplanation(explanationList.get(1));
            if (StringUtils.isNotBlank(exp2)) {
                pstmt.setString(9, exp2);
            }

            pstmt.executeUpdate();
            pstmt.close();
            conn.commit();
        }
        catch (Exception e) {
            log.error(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        finally {
            try {
                conn.close();
            } 
            catch (SQLException e) {
                log.error("sqlite conn close failed", e);
            }
        }
        log.info("Opened database successfully");
    }

    private String composeWordExplanation(WordSpec.WordExplanation explanation) {
        String type = explanation.getType();
        String expl = explanation.getExplanation();
        String examples = explanation.getExamples()
                .stream()
                .filter(StringUtils::isNotBlank)
                .limit(2)
                .collect(Collectors.joining(","));
        return String.format("%s: %s\n%s", type, expl, examples);
    }
}
