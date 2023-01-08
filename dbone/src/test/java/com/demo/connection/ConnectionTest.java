package com.demo.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(ConnectionConst.url, ConnectionConst.id, ConnectionConst.pw);
        Connection con2 = DriverManager.getConnection(ConnectionConst.url, ConnectionConst.id, ConnectionConst.pw);

        System.out.println(con1);
        System.out.println(con2);
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConst.url);
        dataSource.setUsername(ConnectionConst.id);
        dataSource.setPassword(ConnectionConst.pw);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        dataSource.getConnection();


    }
}
