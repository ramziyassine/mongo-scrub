package com.hudl.db;

public enum ScrubStrategy {
    NULL,
    SHUFFLE,
    MASK,
    SUBSTITUTION,
    PASSWORD,
    NONE;
}