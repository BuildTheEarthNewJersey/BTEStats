package btestats.btestats.Events;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.Players;
import btestats.btestats.Util.Blocks.BlockOwnerHistory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
public class RemoveBlockPlace implements Listener {

    private final BTEStats plugin;
    private final Players playerDB;

    public RemoveBlockPlace(BTEStats plugin, Players playerDB){
        this.plugin = plugin;
        this.playerDB = playerDB;
    }/**/

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String owner = BlockOwnerHistory.get(plugin, block);

        // Do not update if nobody broke the block
        if (owner == null){
            return;
        }

        // Another user breaks someone else's block
        if (!owner.equals(uuid)){
            this.playerDB.updateBlocksPlaced(uuid, -1);
            return;
        }

        BlockOwnerHistory.remove(plugin, block);
        this.playerDB.updateBlocksPlaced(uuid, -1);
    }
}
