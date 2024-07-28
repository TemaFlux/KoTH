package me.mattyhd0.koth.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mattyhd0.koth.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class KOTHApi
implements IKOTHApi {
    private final MySQL mySQL;

    @Override
    public boolean doesUserExist(UUID uuid) {
        try (ResultSet rs = mySQL.query("SELECT count(*) AS count FROM KOTHapi WHERE uuid = ?", uuid.toString())) {
            return rs != null && rs.next() && rs.getInt("count") != 0;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void initPlayer(UUID uuid) {
        mySQL.update("INSERT INTO KOTHapi (uuid, points) VALUES (?, ?)", uuid.toString(), 1);
    }

    @Override
    public int getPoints(UUID uuid) {
        try (ResultSet rs = mySQL.query("SELECT points FROM KOTHapi WHERE uuid = ?", uuid.toString())) {
            if (rs == null || !rs.next()) return -1;
            return rs.getInt("points");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void addPoint(@NonNull UUID uuid) {
        if (doesUserExist(uuid)) {
            int point = getPoints(uuid) + 1;

            setPoints(uuid, point);
            KOTHCache.points.put(uuid, point);
        } else {
            initPlayer(uuid);
            KOTHCache.points.put(uuid, 1);
        }
    }

    private void setPoints(UUID uuid, int point) {
        mySQL.update("UPDATE KOTHapi SET points = ? WHERE uuid = ?", point, uuid.toString());
    }

    @Override
    public void createTable() {
        mySQL.update("CREATE TABLE IF NOT EXISTS KOTHapi (uuid VARCHAR(100), points INT(100))");
    }
}
