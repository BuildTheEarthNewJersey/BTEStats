package net.btenj.btestats.commands;

import net.btenj.btestats.storage.Players;

public class GetPlayerStats {
    /* TODO Implement
    1. Get player object data from PlayerData.java collection. Returns number of blocks placed and login streaks. Admins should be the only one that can see last login time.
     */

    private Players playerDB;

    public GetPlayerStats(Players playerDB){
        this.playerDB = playerDB;
    }
}
