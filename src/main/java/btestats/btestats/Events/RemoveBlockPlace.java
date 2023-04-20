package btestats.btestats.Events;
import btestats.btestats.Database.Players;
import btestats.btestats.Util.Blocks.BlockOwnerHistory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
public class RemoveBlockPlace implements Listener {
    private final Players playerDB;

    private final BlockOwnerHistory blockOwnerHistory;

    public RemoveBlockPlace(Players playerDB, BlockOwnerHistory blockOwnerHistory){
        this.playerDB = playerDB;
        this.blockOwnerHistory = blockOwnerHistory;
    }/**/

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String owner = this.blockOwnerHistory.get(block);

        // Do not update if nobody broke the block
        if (owner == null){
            return;
        }

        // Another user breaks someone else's block
        if (!owner.equals(uuid)){
            this.playerDB.updateBlocksPlaced(uuid, -1);
            return;
        }

        this.blockOwnerHistory.remove(block);
        this.playerDB.updateBlocksPlaced(uuid, -1);
    }
}
