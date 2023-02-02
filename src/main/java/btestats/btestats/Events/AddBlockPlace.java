package btestats.btestats.Events;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.Players;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class AddBlockPlace implements Listener {

    private final BTEStats plugin;

    public AddBlockPlace(BTEStats plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (block.getMetadata("owner").size() != 0){
            return;
        }
        String uuidMetadata = block.getMetadata("owner").get(0).asString();
        if (uuidMetadata.equals(uuid)){
            return;
        }
        block.setMetadata("owner", new FixedMetadataValue(plugin, player.getUniqueId()));
        Players.updateBlocksPlaced(uuid, 1);
    }
}
