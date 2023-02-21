package com.bitniki.VPNconServer.modules.subscription.model;

import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.user.role.Role;

public class Subscription {
    private Long id;
    private Role role;
    private Integer priceInRub = 0;
    private Integer peersAvailable = 0;
    private Integer days = 0;

    public static Subscription toModel(SubscriptionEntity entity) {
        return new Subscription(entity);
    }

    public Subscription() {
    }

    public Subscription(SubscriptionEntity entity) {
        this.setId(entity.getId());
        this.setRole(entity.getRole());
        this.setPriceInRub(entity.getPriceInRub());
        this.setPeersAvailable(entity.getPeersAvailable());
        this.setDays(entity.getDays());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getPriceInRub() {
        return priceInRub;
    }

    public void setPriceInRub(Integer priceInRub) {
        this.priceInRub = priceInRub;
    }

    public Integer getPeersAvailable() {
        return peersAvailable;
    }

    public void setPeersAvailable(Integer peersAvailable) {
        this.peersAvailable = peersAvailable;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
