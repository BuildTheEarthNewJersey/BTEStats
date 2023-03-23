package btestats.btestats;

import btestats.btestats.Commands.GetPlayerStats;
import btestats.btestats.Database.Players;
import btestats.btestats.Mongo.MongoConnection;
import org.bukkit.command.CommandExecutor;
import btestats.btestats.Events.AddBlockPlace;
import btestats.btestats.Events.RemoveBlockPlace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BTEStats extends JavaPlugin {
    private String mongoURI;
    @Override
    public void onEnable() {
        /* TODO
            1. load config
            2. start API/Router
            3. Start Mongo/MongoConnection
            4. Instantiate DB classes
            5. Instantiate and Register commands
         */
        // Register Plugins
        Bukkit.getServer().getPluginManager().registerEvents(new AddBlockPlace(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RemoveBlockPlace(this), this);
        
        // Plugin startup logic
        //Starts up a Mongo Connection and retrieves the database
        mongoURI = this.getConfig().getString("mongo-uri");
        MongoConnection mongo = new MongoConnection(mongoURI);
        mongo.getConnection();
        Players playerDB = new Players(mongo.getConnection());
        getCommand("test").setExecutor((CommandExecutor) new GetPlayerStats(playerDB));
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
