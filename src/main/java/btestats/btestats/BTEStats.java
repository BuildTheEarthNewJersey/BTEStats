package btestats.btestats;

import btestats.btestats.Commands.GetPlayerStats;
import btestats.btestats.Database.Players;
import btestats.btestats.Mongo.MongoConnection;
import com.mongodb.client.MongoDatabase;
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
        
        // Plugin startup logic
        //Starts up a Mongo Connection and retrieves the database
        mongoURI = this.getConfig().getString("mongo-uri");
        MongoDatabase mongo = new MongoConnection(mongoURI).getConnection();

        // Register Plugins
        Bukkit.getServer().getPluginManager().registerEvents(new AddBlockPlace(this, new Players(mongo)), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RemoveBlockPlace(this, new Players(mongo)), this);

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
