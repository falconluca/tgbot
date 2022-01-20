package me.shaohsiung.dao;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.model.WordSpec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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

            // TODO
            WordSpec.WordExplanation wordExplanation1 = explanationList.get(0);
            WordSpec.WordExplanation wordExplanation2 = explanationList.get(1);

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
}
