package com.hudl.utils;

import com.google.inject.Guice;
import com.hudl.util.HashingService;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Junit for {@link HashingService}
 */
public class HashingServiceTest {


    @Test
    public void testNormal() {
        HashingService service = Guice.createInjector().getInstance(HashingService.class);
        String hash = service.hash("Germany 7 - Brazil 1");
        assertNotNull(hash);
        assertTrue(!hash.isEmpty());
    }

    @Test
    public void testConsistency() {
        HashingService service = Guice.createInjector().getInstance(HashingService.class);
        String hash1 = service.hash("WorldCup 2022 in Qatar");
        String hash2 = service.hash("WorldCup 2022 in Qatar");
        assertEquals(hash1, hash2);
    }
}
