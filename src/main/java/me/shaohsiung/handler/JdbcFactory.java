package me.shaohsiung.handler;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public class JdbcFactory {
    private static final JdbcTemplate jdbcTemplate;
    
    private static final TransactionTemplate transactionTemplate;
    
    private static final SimpleJdbcInsert simpleJdbcInsert;
    
    private static final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {
        jdbcTemplate = new JdbcTemplate(getHikariDataSource());
        
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(getHikariDataSource());
        transactionTemplate = new TransactionTemplate(transactionManager);

        simpleJdbcInsert = new SimpleJdbcInsert(getHikariDataSource());

        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getHikariDataSource());
    }
    
    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public static TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public static SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }
    
    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
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
