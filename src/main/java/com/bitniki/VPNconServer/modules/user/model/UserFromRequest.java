package com.bitniki.VPNconServer.modules.user.model;

import lombok.*;

/**
 * Model for request body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFromRequest {
    private String login;
    private String password;
}
