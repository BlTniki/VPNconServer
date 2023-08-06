package com.bitniki.VPNconServer.modules.user.util.EncryptionUtil;

public interface EncryptionUtil {
    String encode(String originalString);
    String decode(String encryptedString);
}
