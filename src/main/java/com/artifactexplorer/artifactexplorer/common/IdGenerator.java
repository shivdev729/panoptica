package com.artifactexplorer.common;

import java.util.UUID;

import org.springframework.stereotype.Component;

 // common/IdGenerator.java
@Component
public class IdGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

    // optional: short readable IDs for specific entities
    public String generate(String prefix) {
        String short_uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
        return prefix + "-" + short_uuid;
    }
} 
