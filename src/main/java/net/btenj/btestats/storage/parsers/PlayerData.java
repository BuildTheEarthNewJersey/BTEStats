package net.btenj.btestats.storage.parsers;

import java.util.UUID;

public class PlayerData {
  public int blocksPlaced;
  public long lastLogin;
  public int loginStreak;

  public PlayerData(
    String uuid,
    int blocksPlaced,
    long lastLogin,
    int loginStreak
  ) {
    if (!(uuid.equals(UUID.fromString(uuid).toString()))) {
      throw new IllegalArgumentException("Invalid UUID");
    }
    this.blocksPlaced = blocksPlaced;
    this.lastLogin = lastLogin;
    this.loginStreak = loginStreak;
  }
}
