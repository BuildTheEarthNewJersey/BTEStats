package btestats.btestats.Database;

import btestats.btestats.BTEStats;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BlockOwnerCollection {
    /*
     * Handles database call for Block Owner History
     */
    private final MongoCollection<Document> collection;
    private static final String OWNER_KEY = "owner";
    private static final char UNIT_SEPARATOR = 0x1E;

    private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);

    private final BTEStats plugin;
    private ConcurrentHashMap<Location, String> buffer;

    public BlockOwnerCollection(MongoDatabase database, BTEStats plugin){
        this.collection = database.getCollection("blockOwnerCollection");
        this.buffer = new ConcurrentHashMap<>();
        this.plugin = plugin;
    }

    public void addToBuffer(Location location, String owner){
        this.buffer.put(location, owner);
    }

    public void removeFromBuffer(Location location){
        this.buffer.remove(location);
    }

    private String locationToString(Location location){
        return location.getWorld().getName() + UNIT_SEPARATOR + location.getBlockX() + UNIT_SEPARATOR + location.getBlockY() + UNIT_SEPARATOR + location.getBlockZ() + UNIT_SEPARATOR + location.getPitch() + UNIT_SEPARATOR + location.getYaw();
    }

    private Location stringToLocation(String locationString){
        List<String> separatedString = Arrays.asList(locationString.split(String.valueOf(UNIT_SEPARATOR)));

        if (separatedString.size() != 6){
            throw new IllegalArgumentException("Invalid location string: " + locationString);
        }

        World world = this.plugin.getServer().getWorld(separatedString.get(0));
        double x = Double.parseDouble(separatedString.get(1));
        double y = Double.parseDouble(separatedString.get(2));
        double z = Double.parseDouble(separatedString.get(3));
        float pitch = Float.parseFloat(separatedString.get(4));
        float yaw = Float.parseFloat(separatedString.get(5));

        return new Location(world, x, y, z, pitch, yaw);

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
                result.put(this.stringToLocation(document.get("_id").toString()), document.get(OWNER_KEY).toString())
        );

        return result;
    }

    public void flush(){
        /*
        Flush buffer into DB and wipe buffer
         */
        this.buffer.forEach((location, owner) ->
                collection.updateOne(Filters.eq("_id", this.locationToString(location)), Updates.set(OWNER_KEY, owner), UPSERT)
                );
        this.buffer = new ConcurrentHashMap<>();
    }

    
}
