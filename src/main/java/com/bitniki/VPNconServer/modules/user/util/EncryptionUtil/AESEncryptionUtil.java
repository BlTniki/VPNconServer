package com.bitniki.VPNconServer.modules.user.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
public class AESEncryptionUtil implements EncryptionUtil {
    private final String SECRET_KEY;

    public String encode(String originalString) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(originalString.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public String decode(String encryptedString) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        AESEncryptionUtil encryptor = new AESEncryptionUtil("ThisIsASecretKey");
        String originalString = "Hello, AES!";
        String encodedString = encryptor.encode(originalString);
        System.out.println("Encoded String: " + encodedString);
        String decodedString = encryptor.decode(encodedString);
        System.out.println("Decoded String: " + decodedString);
    }
}
