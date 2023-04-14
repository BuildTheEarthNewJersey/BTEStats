package btestats.btestats.Events;

import btestats.btestats.Database.BlockOwnerCollection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class BlockOwnerCollectionFlush implements Listener {
    /*
     * Flushes block history into DB every world save
     */

    private final BlockOwnerCollection blockOwnerCollection;
    public BlockOwnerCollectionFlush(BlockOwnerCollection blockOwnerCollection) {
        this.blockOwnerCollection = blockOwnerCollection;
    }

    @EventHandler
    public void onWorldSave( WorldSaveEvent event ) {
        blockOwnerCollection.flush();
    }

}
