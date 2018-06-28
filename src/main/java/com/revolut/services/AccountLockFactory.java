package com.revolut.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountLockFactory {

    private static final Map<Long, Object> locks = new ConcurrentHashMap<>();

    public static Object getLock(Long id) {
        locks.putIfAbsent(id, new Object());
        return locks.get(id);
    }
}
