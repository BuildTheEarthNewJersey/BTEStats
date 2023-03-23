package btestats.btestats.Events;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.Players;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class RemoveBlockPlace implements Listener {

    private final BTEStats plugin;

    public RemoveBlockPlace(BTEStats plugin){
        this.plugin = plugin;
    }/**/

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (block.getMetadata("owner").size() == 0){
            return;
        }
        String uuidMetadata = block.getMetadata("owner").get(0).asString();
        if (!uuidMetadata.equals(uuid)){
            Players.updateBlocksPlaced(uuidMetadata, -1);
            return;
        }
        block.removeMetadata("owner", plugin);
        Players.updateBlocksPlaced(uuid, -1);
    }
}
