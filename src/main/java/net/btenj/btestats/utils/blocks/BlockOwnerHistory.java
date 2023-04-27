package net.btenj.btestats.utils.blocks;

import com.mongodb.lang.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import net.btenj.btestats.storage.BlockOwnerCollection;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockOwnerHistory {
  private final ConcurrentHashMap<Location, String> blockOwnerHistory;
  private final BlockOwnerCollection blockOwnerCollection;

  public BlockOwnerHistory(BlockOwnerCollection blockOwnerCollection) {
    this.blockOwnerCollection = blockOwnerCollection;
    this.blockOwnerHistory = this.blockOwnerCollection.get();
  }

  @Nullable
  public String get(Block block) {
    Location location = block.getLocation();
    return this.blockOwnerHistory.getOrDefault(location, null);
  }

  public void set(Block block, String owner) {
    Location location = block.getLocation();
    this.blockOwnerHistory.put(location, owner);
    this.blockOwnerCollection.addToBuffer(location, owner);
  }

  public void remove(Block block) {
    Location location = block.getLocation();
    this.blockOwnerHistory.remove(location);
    this.blockOwnerCollection.removeFromBuffer(location);
  }
}
