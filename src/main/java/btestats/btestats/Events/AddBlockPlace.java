package btestats.btestats.Events;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.Players;
import java.util.List;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

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

        List<MetadataValue> owner = block.getMetadata("owner");

        // If Block was placed by somebody else
        if (owner.size() > 0 && owner.get(0).asString().equals(uuid)){
            return;
        }

        block.setMetadata("owner", new FixedMetadataValue(plugin, player.getUniqueId()));
        this.playerDB.updateBlocksPlaced(uuid, 1); //TODO
    }
}
