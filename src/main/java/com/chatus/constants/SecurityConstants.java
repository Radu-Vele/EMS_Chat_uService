package com.chatus.constants;

public class SecurityConstants {
    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:3000",
            "http://localhost:5173",
            "http://172.20.0.6:80",
            "http://localhost:80",
            "http://localhost"
    };
    public static final String[] NO_AUTH_REQUIRED_PATTERNS = {"/ws/**",
            "/user/create",
    }; // TODO: add security for web socket as well

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {"/findAdminAvailable",
            "/user/getById",
            "/message/**",
            "/user/edit",
            "/group/*",
            "/user/addNewChat",
            "/chatDb/*"
    };
    public static final String[] ADMIN_AUTH_REQUIRED_PATTERNS = {"/getActiveSessions",
            "/user/delete"
    };
}
