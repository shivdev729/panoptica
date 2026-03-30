package com.artifactexplorer.admin.dto;

import java.time.Instant;

 // admin/dto/AuthResponse.java
public record AuthResponse(
    String token,
    String adminId,
    String username,
    String role,
    Instant expiresAt
) {} 
