package com.ducle.chat_service.service.id_generator.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.service.id_generator.IdGenerator;

import jakarta.annotation.PostConstruct;

@Service
@ConfigurationProperties(prefix = "snowflake")
public class Snowflake implements IdGenerator {

    // private static final long TIMESTAMP_BITS = 41L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long MACHINE_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

    private static final long CUSTOM_EPOCH = 1577836800000L;

    private long datacenterId;
    private long machineId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    @PostConstruct
    private void init() {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("Datacenter ID must be between 0 and %d", MAX_DATACENTER_ID));
        }
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
    }

    @Override
    public synchronized long generateId() {
        long currentTimestamp = currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - CUSTOM_EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = currentTimeMillis();
        }
        return currentTimestamp;
    }

    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
