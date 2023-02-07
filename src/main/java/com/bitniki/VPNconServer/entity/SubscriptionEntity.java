package com.bitniki.VPNconServer.entity;

import com.bitniki.VPNconServer.role.Role;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private Integer priceInRub = 0;
    @Column(nullable = false)
    private Integer peersAvailable = 0;
    @Column(nullable = false)
    private Integer days = 0;

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
