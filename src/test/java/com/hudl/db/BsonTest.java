package com.hudl.db;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.hudl.util.HashingService;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.junit.Test;

import javax.inject.Inject;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @user ramzi
 */
public class BsonTest {

    @Test
    public void test() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DBCursor users = mongoClient.getDB("test").getCollection("users").find();
            Scruber scrubber = Guice.createInjector().getInstance(Scruber.class);
            while (users.hasNext()) {
                DBObject object = users.next();
                System.out.println("original db object: " + object);
                Map<String, Object> map = object.toMap();
                Map newMap = scrub(map, scrubber);
                object.putAll(newMap);
                System.out.println("Scrubbed db object: " + object);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    private Map scrub(Map<String, Object> map, Scruber scrubber) {

        Map<String, Object> scrubbedMapped = Maps.newHashMapWithExpectedSize(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object newValue = value;
            if (value instanceof String) {
                newValue = scrubber.scrub(key, (String) value);

            }
            scrubbedMapped.put(key, newValue);
        }
        return scrubbedMapped;

    }
}
