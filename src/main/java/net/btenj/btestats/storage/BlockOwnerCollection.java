package net.btenj.btestats.storage;

import net.btenj.btestats.BTEStats;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BlockOwnerCollection implements Bufferable {
    /*
     * Handles database call for Block Owner History
     */
    private final MongoCollection<Document> collection;
    private static final String WORLD_KEY = "world";

    private static final String OWNER_KEY = "owner";
    private static final String X_KEY = "x";
    private static final String Y_KEY = "y";

    private static final String Z_KEY = "z";
    private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);

    private final BTEStats plugin;
    private HashMap<Location, String> addBuffer;
    private HashSet<Location> removeBuffer;

    private final Object BUFFER_MUX = new Object();

    public BlockOwnerCollection(MongoDatabase database, BTEStats plugin){
        this.collection = database.getCollection("blockOwnerCollection");
        this.addBuffer = new HashMap<>();
        this.removeBuffer = new HashSet<>();
        this.plugin = plugin;
    }

    public void addToBuffer(Location location, String owner){
        synchronized (BUFFER_MUX) {
            this.removeBuffer.remove(location);
            this.addBuffer.put(location, owner);
        }
    }

    public void removeFromBuffer(Location location){
        synchronized (BUFFER_MUX) {
            this.addBuffer.remove(location);
            this.removeBuffer.add(location);
        }
    }

    public ConcurrentHashMap<Location, String> get(){
        /*
        Gets all entries from DB
         */

        if (this.collection.estimatedDocumentCount() == 0) {
            return new ConcurrentHashMap<>();
        }

        ConcurrentHashMap<Location, String> result = new ConcurrentHashMap<>();
        collection.find().forEach((Consumer<? super Document>) (document) ->
                result.put(
                        new Location(
                                this.plugin.getServer().getWorld(document.get(WORLD_KEY).toString()),
                                (Double) document.get(X_KEY),
                                (Double) document.get(Y_KEY),
                                (Double) document.get(Z_KEY)
                        ),
                        document.get(OWNER_KEY).toString()
                )
        );

        return result;
    }

    private static Bson generateLocationFilter(Location location){
        return Filters.and(
                Filters.eq(WORLD_KEY, location.getWorld().getName()),
                Filters.eq(X_KEY, location.getX()),
                Filters.eq(Y_KEY, location.getY()),
                Filters.eq(Z_KEY, location.getZ())
        );
    }

    public void flush(){
        /*
        Flush buffer into DB and wipe buffer
         */
        synchronized (BUFFER_MUX) {
            this.removeBuffer.forEach((location) ->
                    collection.deleteOne(
                            BlockOwnerCollection.generateLocationFilter(location)
                    )
            );

            this.addBuffer.forEach((location, owner) ->
                    collection.updateOne(BlockOwnerCollection.generateLocationFilter(location), Updates.set(OWNER_KEY, owner), UPSERT)
            );

            this.addBuffer = new HashMap<>();
            this.removeBuffer = new HashSet<>();
        }
    }

    
}
