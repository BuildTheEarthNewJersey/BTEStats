package net.btenj.btestats;

import com.mongodb.client.MongoDatabase;
import net.btenj.btestats.events.AddBlockPlace;
import net.btenj.btestats.events.BlockOwnerCollectionFlush;
import net.btenj.btestats.events.RemoveBlockPlace;
import net.btenj.btestats.storage.BlockOwnerCollection;
import net.btenj.btestats.storage.Players;
import net.btenj.btestats.storage.mongodb.MongoConnection;
import net.btenj.btestats.utils.blocks.BlockOwnerHistory;
import org.bukkit.plugin.java.JavaPlugin;

public final class BTEStats extends JavaPlugin {

  @Override
  public void onEnable() {
    //Starts up a Mongo Connection and retrieves the database
    String mongoURI = this.getConfig().getString("mongo-uri");
    MongoDatabase mongo = new MongoConnection(mongoURI).getConnection();

    //Register Events
    BlockOwnerCollection blockOwnerCollection = new BlockOwnerCollection(
      mongo,
      this
    );
    BlockOwnerHistory blockOwnerHistory = new BlockOwnerHistory(
      blockOwnerCollection
    );
    Players players = new Players(mongo);
    getServer()
      .getPluginManager()
      .registerEvents(new AddBlockPlace(players, blockOwnerHistory), this);
    getServer()
      .getPluginManager()
      .registerEvents(new RemoveBlockPlace(players, blockOwnerHistory), this);
    getServer()
      .getPluginManager()
      .registerEvents(
        new BlockOwnerCollectionFlush(blockOwnerCollection, players),
        this
      );
  }

  @Override
  public void onDisable() {}
}
