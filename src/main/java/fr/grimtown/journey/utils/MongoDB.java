package fr.grimtown.journey.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.quests.classes.Event;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.util.Collections;
import java.util.HashMap;

public class MongoDB {
    /**
     * Load DataStores
     */
    public static HashMap<String, Datastore> getDatastoreMap(Configuration config) {
        HashMap<String, Datastore> datastoreMap = new HashMap<>();
        for (Object dbName : config.getStringList("data.mongo.databases")) {
            if (GamePlugin.DEBUG_ERRORS) Bukkit.getLogger().info("[Mongo] Load db: " + dbName.toString());
            datastoreMap.put(dbName.toString(), setDatastore(config, dbName.toString()).startSession());
        }
        if (GamePlugin.DEBUG_ERRORS) Bukkit.getLogger().info("[Mongo] " + datastoreMap.size() + " db loaded");
        return datastoreMap;
    }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    private static Datastore setDatastore(Configuration config, String dbName) {
        MongoCredential credential = MongoCredential.createCredential(
                config.getString("data.mongo.user"),
                config.getString("data.mongo.db"),
                config.getString("data.mongo.password").toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(config.getString("data.mongo.host"), config.getInt("data.mongo.port")))))
                .credential(credential)
                .build();
        Datastore datastore = Morphia.createDatastore(MongoClients.create(settings), dbName, MapperOptions.builder()
                .enablePolymorphicQueries(true)
                .build());
        datastore.getMapper().map(Event.class);
        datastore.ensureIndexes();
        datastore.ensureCaps();
        datastore.enableDocumentValidation();
        return datastore;
    }
}
