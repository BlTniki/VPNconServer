package com.bitniki.VPNconServer.role;

public enum Permission {
    GenToken("gen_token"),
    Personal("personal"),
    Any("any"),
    User_READ("user:read"),
    User_WRITE("user:write"),
    Peer_READ("peer:read"),
    Peer_WRITE("peer:write"),
    Host_READ("host:read"),
    Host_WRITE("host:write"),
    Subscription_READ("subscription:read"),
    Subscription_WRITE("subscription:write"),
    Mail_READ("mail:read"),
    Mail_WRITE("mail:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
