package net.btenj.btestats.events;

import net.btenj.btestats.storage.BlockOwnerCollection;
import net.btenj.btestats.storage.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class BlockOwnerCollectionFlush implements Listener {
  /*
   * Flushes block history into DB every world save
   */

  private final BlockOwnerCollection blockOwnerCollection;
  private final Players player;

  public BlockOwnerCollectionFlush(
    BlockOwnerCollection blockOwnerCollection,
    Players player
  ) {
    this.blockOwnerCollection = blockOwnerCollection;
    this.player = player;
  }

  @EventHandler
  public void onWorldSave(WorldSaveEvent event) {
    blockOwnerCollection.flush();
    player.flush();
  }
}
