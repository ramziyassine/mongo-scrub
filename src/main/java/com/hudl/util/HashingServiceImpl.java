package com.hudl.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.inject.Singleton;

import java.nio.charset.Charset;

/**
 *
 */
@Singleton
public class HashingServiceImpl implements HashingService {
    @Override
    public String hash(String data) {
        HashCode hashCode = Hashing.sha512().hashString(data, Charset.defaultCharset());
        return hashCode.toString();
    }
}
