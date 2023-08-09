package com.bitniki.VPNconServer.modules.role;

/**
 * Разрешения, которые проверяются у пользователя при запросе.
 */
public enum Permission {
    Personal("personal"),
    Any("any"),
    User_READ("user:read"),
    User_WRITE("user:write"),
    Peer_READ("peer:read"),
    Peer_WRITE("peer:write"),
    Host_READ("host:read"),
    Host_WRITE("host:write"),
    Metacode_USE("metacode:use"),
    Metacode_GEN("metacode:gen"),
    Subscription_READ("subscription:read"),
    Subscription_WRITE("subscription:write"),
    UserSubscription_READ("user_subscription:read"),
    UserSubscription_WRITE("user_subscription:write"),
    Mail_READ("mail:read"),
    Mail_WRITE("mail:write"),
    PAYMENT_READ("payment:read"),
    PAYMENT_WRITE("payment:write"),
    REMINDER_READ("reminder:read"),
    REMINDER_write("reminder:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
