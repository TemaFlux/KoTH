package me.mattyhd0.koth.mysql;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Builder
public class MySQL {
    @NonNull private final String host;
    @NonNull private final String database;
    @NonNull private final String user;
    @NonNull private final String password;
    @NonNull private final Integer port;

    private final LoadingCache<Integer, Connection> cache = CacheBuilder.newBuilder()
    .expireAfterAccess(10L, TimeUnit.SECONDS)
    .removalListener(removalNotification -> {
        try {
            if (removalNotification.getValue() != null) {
                ((Connection) removalNotification.getValue()).close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }).build(new CacheLoader<Integer, Connection>() {
        @Override public Connection load(Integer unused) throws Exception {
            return createConnection();
        }
    });

    @SneakyThrows
    public void update(String update, Object... objs) {
        try (Connection connection = cache.get(1); PreparedStatement p = connection.prepareStatement(update)) {
            setArguments(objs, p);
            p.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public ResultSet query(String query, Object... objs) {
        try {
            Connection connection = cache.get(1);
            PreparedStatement p = connection.prepareStatement(query);

            setArguments(objs, p);
            return p.executeQuery();
        } catch (SQLException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setArguments(Object[] objs, PreparedStatement p) throws SQLException {
        for (int i = 0; i < objs.length; ++i) {
            Object obj = objs[i];

            if (obj instanceof String) {
                p.setString(i + 1, (String) obj);
            } else if (obj instanceof Integer) {
                p.setInt(i + 1, (Integer) obj);
            } else if (obj instanceof Date) {
                p.setDate(i + 1, (Date) obj);
            } else if (obj instanceof Timestamp) {
                p.setTimestamp(i + 1, (Timestamp) obj);
            } else if (obj instanceof Boolean) {
                p.setBoolean(i + 1, (Boolean) obj);
            } else if (obj instanceof Float) {
                p.setFloat(i + 1, (Float) obj);
            } else if (obj instanceof Double) {
                p.setDouble(i + 1, (Double) obj);
            } else if (obj instanceof Long) {
                p.setLong(i + 1, (Long) obj);
            }
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }
}
