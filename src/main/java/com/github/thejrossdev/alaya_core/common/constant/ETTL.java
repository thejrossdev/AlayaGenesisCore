package com.github.thejrossdev.alaya_core.common.constant;

public enum ETTL {
    DEFAULT(-1L),
    NONE(0L),
    SHORT(300L),
    MID(600L),
    LONG(1000L);

    private final long ttl;

    ETTL(long ttl) {
        this.ttl = ttl;
    }

    public long getTTL() {
        return ttl;
    }
}
