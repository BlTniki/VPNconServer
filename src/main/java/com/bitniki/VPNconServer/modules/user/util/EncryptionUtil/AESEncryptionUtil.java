package com.bitniki.VPNconServer.modules.user.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
public class AESEncryptionUtil implements EncryptionUtil {
    private final String SECRET_KEY;

    @Override
    public String encode(String originalString) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedBytes = cipher.doFinal(originalString.getBytes());
            byte[] urlSafeEncodedBytes = Base64.getUrlEncoder().encode(encryptedBytes);
            return new String(urlSafeEncodedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String decode(String encryptedString) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] urlSafeDecodedBytes = Base64.getUrlDecoder().decode(encryptedString);
            byte[] decryptedBytes = cipher.doFinal(urlSafeDecodedBytes);
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
