package btestats.btestats.Events.handlers;

import btestats.btestats.BTEStats;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class  OwnerPersistentStorageHandler {
    private static final String KEY = "owner";

    private static PersistentDataContainer getContainer(BTEStats plugin, Block block){
        return new CustomBlockData(block, plugin);
    }

    private static NamespacedKey getKey(BTEStats plugin){
        return new NamespacedKey(plugin, KEY);
    }
    public static String get(BTEStats plugin, Block block){
        NamespacedKey key = getKey(plugin);
        PersistentDataContainer container = getContainer(plugin, block);
        return container.get(key, PersistentDataType.STRING);
    }

    public static void set(BTEStats plugin, Block block, String owner){
        NamespacedKey key = getKey(plugin);
        PersistentDataContainer container = getContainer(plugin, block);
        container.set(key, PersistentDataType.STRING, owner);
    }

    public static void remove(BTEStats plugin, Block block){
        NamespacedKey key = getKey(plugin);
        PersistentDataContainer container = getContainer(plugin, block);
        container.remove(key);
    }
}
