package net.btenj.btestats.events;
import net.btenj.btestats.storage.Players;
import net.btenj.btestats.utils.blocks.BlockOwnerHistory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AddBlockPlace implements Listener {
    private final Players playerDB;
    private final BlockOwnerHistory blockOwnerHistory;

    public AddBlockPlace(Players playerDB, BlockOwnerHistory blockOwnerHistory){
        this.playerDB = playerDB;
        this.blockOwnerHistory = blockOwnerHistory;
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String owner = blockOwnerHistory.get(block);

        // If Block was placed by somebody else
        if (owner != null && owner.equals(uuid)){
            return;
        }

        blockOwnerHistory.set(block, uuid);

        this.playerDB.updateBlocksPlaced(uuid, 1);
    }
}
