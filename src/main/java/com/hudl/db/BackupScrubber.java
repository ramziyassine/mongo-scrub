package com.hudl.db;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.inject.Singleton;
import org.bson.*;

import javax.inject.Inject;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Singleton
public class BackupScrubber {


    private final Scruber scrubber;

    @Inject
    public BackupScrubber(Scruber scrubber) {
        this.scrubber = scrubber;
    }

    public void scrub(File bsonFile) {

        if (!bsonFile.isAbsolute()) {
            bsonFile = bsonFile.getAbsoluteFile();
        }

        String newFileName = new StringBuilder()//
                .append(Files.getNameWithoutExtension(bsonFile.getPath()))//
                .append("-scrubbed.bson")//
                .toString();

        scrubTo(bsonFile, bsonFile.getParentFile(), newFileName);
    }

    /**
     * @param bsonFile
     * @param destinationPath
     */
    private void scrubTo(File bsonFile, File destinationPath, String fileName) {

        Preconditions.checkArgument(bsonFile != null);
        Preconditions.checkArgument(bsonFile.exists());
        Preconditions.checkArgument(destinationPath != null);
        Preconditions.checkArgument(destinationPath.exists());
        Preconditions.checkArgument(destinationPath.isDirectory());
        Preconditions.checkArgument(fileName != null && !fileName.isEmpty());


        List<BSONObject> dbObjects = load(bsonFile);
        List<BSONObject> scrubbedObjects = scrub(dbObjects);
        writeBson(scrubbedObjects, destinationPath, fileName);
    }

    private void writeBson(List<BSONObject> bsonObjects, File destinationPath, String fileName) {
        File path = new File(destinationPath.getPath(), fileName);
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(path, false));
            BSONEncoder encoder = new BasicBSONEncoder();
            int counter = 0;
            for (BSONObject bson : bsonObjects) {
                byte[] bytes = encoder.encode(bson);
                outputStream.write(bytes);
                ++counter;

                if (counter % 20 == 0) {
                    outputStream.flush();
                }
            }

        } catch (Exception e) {
            throw Throwables.propagate(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                    System.out.println("Scrubbed files written to: " + destinationPath.getAbsolutePath() + File.separator + fileName);
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    private List<BSONObject> scrub(List<BSONObject> bsonObjects) {

        List<BSONObject> result = Lists.newArrayListWithExpectedSize(bsonObjects.size());
        for (BSONObject bsonObject : bsonObjects) {
            Map<String, Object> map = bsonObject.toMap();
            Map newMap = scrub(map);

            BSONObject scrubbedBson = new BasicBSONObject();
            scrubbedBson.putAll(newMap);
            result.add(scrubbedBson);
        }

        return result;
    }


    private List<BSONObject> load(File bsonFile) {
        BSONDecoder decoder = new BasicBSONDecoder();
        List<BSONObject> result = Lists.newArrayList();

        InputStream inputStream = null;
        int counter = 0;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(bsonFile));

            while (inputStream.available() != 0) {
                BSONObject o = decoder.readObject(inputStream);
                result.add(o);
            }
            return result;


        } catch (Exception e) {
            throw Throwables.propagate(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore.
                }
            }
        }
    }


    private Map scrub(Map<String, Object> map) {

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