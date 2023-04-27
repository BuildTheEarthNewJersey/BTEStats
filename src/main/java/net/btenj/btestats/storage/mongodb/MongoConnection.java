package net.btenj.btestats.storage.mongodb;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    /*TODO Implement:
    1. Mongo DB Connection, where mongo db url and auth is in the plugin config file
    2. BTEStats.java calls Mongo DB Connection and passes the mongo object into other database objects
    3. Database objects get passed into command objects
    4. Create function to start mongo connection and stop mongo connection
     */

    private String mongoUri;
    public MongoConnection(String mongoUri){
        this.mongoUri=mongoUri;
    }
    public MongoDatabase getConnection(){
        MongoClient client = MongoClients.create(mongoUri);
        MongoDatabase database = client.getDatabase("BTEstats");
        return database;
    }





}
