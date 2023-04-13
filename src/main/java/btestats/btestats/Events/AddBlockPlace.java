package btestats.btestats.Events;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.Players;
import btestats.btestats.Util.Blocks.BlockOwnerHistory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AddBlockPlace implements Listener {

    private final BTEStats plugin;
    private final Players playerDB;

    public AddBlockPlace(BTEStats plugin, Players playerDB){
        this.plugin = plugin;
        this.playerDB = playerDB;
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String owner = BlockOwnerHistory.get(plugin, block);

        // If Block was placed by somebody else
        if (owner != null && owner.equals(uuid)){
            return;
        }

        BlockOwnerHistory.set(plugin, block, uuid);

        this.playerDB.updateBlocksPlaced(uuid, 1);
    }
}
