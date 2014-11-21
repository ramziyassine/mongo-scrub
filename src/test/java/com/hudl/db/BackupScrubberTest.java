package com.hudl.db;

import com.google.inject.Guice;
import org.junit.Test;

import java.io.File;

/**
 *
 */
public class BackupScrubberTest {

    @Test
    public void testNormal() {
        BackupScrubber scrubber = Guice.createInjector().getInstance(BackupScrubber.class);
        scrubber.scrub(new File("/Users/ramzi/Desktop/users.bson"));
    }
}
