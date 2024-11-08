package org.example;

import java.util.concurrent.atomic.AtomicLong;

public class SnowflakeIdGenerator {

    private static final long EPOCH = 1288834974657L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static long workerId;
    private static long dataCenterId;
    private static AtomicLong sequence = new AtomicLong(0);
    private static long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKER_ID);
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data Center ID must be between 0 and " + MAX_DATA_CENTER_ID);
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp != lastTimestamp) {
            sequence.set(0); // Reset sequence for a new timestamp
            lastTimestamp = timestamp;
        } else {
            sequence.incrementAndGet();
            if (sequence.get() > MAX_SEQUENCE) {
                timestamp = waitForNextMillis(lastTimestamp);
            }
        }

        return (timestamp - EPOCH) << (WORKER_ID_BITS + DATA_CENTER_ID_BITS + SEQUENCE_BITS) |
                (dataCenterId << (WORKER_ID_BITS + SEQUENCE_BITS)) |
                (workerId << SEQUENCE_BITS) |
                sequence.get();
    }

    private static long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
