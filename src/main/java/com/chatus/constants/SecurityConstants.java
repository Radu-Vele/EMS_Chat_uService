package com.chatus.constants;

public class SecurityConstants {
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:5173", "http://localhost:8081"};
    public static final String[] NO_AUTH_REQUIRED_PATTERNS = {"/ws/**"}; // TODO: add security for web socket as well

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {"/findAdminAvailable"};
    public static final String[] ADMIN_AUTH_REQUIRED_PATTERNS = {"/getActiveSessions"};
}
