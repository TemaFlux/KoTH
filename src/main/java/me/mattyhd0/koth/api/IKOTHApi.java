package me.mattyhd0.koth.api;

import java.util.UUID;

public interface IKOTHApi {
    void initPlayer(UUID uuid);
    boolean doesUserExist(UUID uuid);

    int getPoints(UUID uuid);
    void addPoint(UUID uuid);

    void createTable();
}
