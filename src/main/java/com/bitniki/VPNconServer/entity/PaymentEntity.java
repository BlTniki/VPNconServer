package com.bitniki.VPNconServer.entity;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String billId;
    private Long userId;
    private Long subsId;

    public PaymentEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubsId() {
        return subsId;
    }

    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }
}
