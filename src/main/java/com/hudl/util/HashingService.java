package com.hudl.util;

import com.google.inject.ImplementedBy;

@ImplementedBy(HashingServiceImpl.class)
public interface HashingService {

    public String hash(String data);
}
