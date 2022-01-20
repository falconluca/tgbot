package me.shaohsiung.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class JdbcUtils {
    private static final JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = new JdbcTemplate(getHikariDataSource());
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private static DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("DRIVER");
        dataSource.setUrl("JDBC_URL");
        dataSource.setUsername("USERNAME");
        dataSource.setPassword("PASSWORD");
        return dataSource;
    }

    // https://github.com/brettwooldridge/HikariCP
    private static DataSource getHikariDataSource() {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;
        config.setJdbcUrl("jdbc:mysql://localhost:3306/watch-dog-bot?useUnicode=true&serverTimezone=GMT");
        config.setUsername("root");
        config.setPassword("root");
        /*
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        */
        ds = new HikariDataSource(config);
        return ds;
    }
}
