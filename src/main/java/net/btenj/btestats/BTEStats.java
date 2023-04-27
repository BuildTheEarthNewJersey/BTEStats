package net.btenj.btestats;

import net.btenj.btestats.storage.BlockOwnerCollection;
import net.btenj.btestats.storage.Players;
import net.btenj.btestats.events.BlockOwnerCollectionFlush;
import net.btenj.btestats.storage.mongodb.MongoConnection;
import net.btenj.btestats.utils.blocks.BlockOwnerHistory;
import com.mongodb.client.MongoDatabase;
import net.btenj.btestats.events.AddBlockPlace;
import net.btenj.btestats.events.RemoveBlockPlace;
import org.bukkit.plugin.java.JavaPlugin;

public final class BTEStats extends JavaPlugin {
    private String mongoURI;
    private BlockOwnerHistory blockOwnerHistory;
    private BlockOwnerCollection blockOwnerCollection;

    private Players players;
    @Override
    public void onEnable() {
        /* TODO
            1. load config
            2. start API/Router
            3. Start Mongo/MongoConnection
            4. Instantiate DB classes
            5. Instantiate and Register commands
         */
        
        // Plugin startup logic

        //Starts up a Mongo Connection and retrieves the database
        mongoURI = this.getConfig().getString("mongo-uri");
        MongoDatabase mongo = new MongoConnection(mongoURI).getConnection();

        // Register Plugins

        blockOwnerCollection = new BlockOwnerCollection(mongo, this);
        blockOwnerHistory = new BlockOwnerHistory(this, blockOwnerCollection);
        players = new Players(mongo);
        getServer().getPluginManager().registerEvents(new AddBlockPlace( players, blockOwnerHistory), this);
        getServer().getPluginManager().registerEvents(new RemoveBlockPlace(players, blockOwnerHistory), this);
        getServer().getPluginManager().registerEvents(new BlockOwnerCollectionFlush(blockOwnerCollection, players), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        /* TODO
           1. close mongo connection
           2. shutdown api
         */
    }
}
