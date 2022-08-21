package org.example;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledDataSource extends PGSimpleDataSource {
    private final Queue<Connection> poll;

    public PooledDataSource(String url, String userName, String password) {
        this.poll = new ConcurrentLinkedQueue<>();
        this.setUrl(url);
        this.setUser(userName);
        this.setPassword(password);
        this.initPool();
    }

    @SneakyThrows
    private void initPool() {
        for (int i = 0; i < 10; i++) {
            Connection connection = super.getConnection();
            poll.add(new ConnectionProxy(poll, connection));
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return poll.poll();
    }
}
