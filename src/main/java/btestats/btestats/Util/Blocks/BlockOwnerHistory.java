package btestats.btestats.Util.Blocks;

import btestats.btestats.BTEStats;
import btestats.btestats.Database.BlockOwnerCollection;
import com.mongodb.lang.Nullable;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.concurrent.ConcurrentHashMap;

public class BlockOwnerHistory {

    private ConcurrentHashMap<Location, String> blockOwnerHistory;
    private final BlockOwnerCollection blockOwnerCollection;
    private final BTEStats plugin;

    public BlockOwnerHistory(BTEStats plugin, BlockOwnerCollection blockOwnerCollection){
        this.blockOwnerCollection = blockOwnerCollection;
        this.plugin = plugin;
        this.blockOwnerHistory = this.blockOwnerCollection.get();
    }

    @Nullable
    public String get(Block block){
        Location location = block.getLocation();
        return this.blockOwnerHistory.getOrDefault(location, null);
    }

    public void set(Block block, String owner){
        Location location = block.getLocation();
        this.blockOwnerHistory.put(location, owner);
        this.blockOwnerCollection.addToBuffer(location, owner);
    }

    public void remove(Block block){
        Location location = block.getLocation();
        this.blockOwnerHistory.remove(location);
        this.blockOwnerCollection.removeFromBuffer(location);
    }
}
