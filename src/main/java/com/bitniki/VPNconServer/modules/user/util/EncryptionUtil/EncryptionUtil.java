package com.bitniki.VPNconServer.modules.user.util.EncryptionUtil;

/**
 * Утилита для кодирования и декодирования строки.
 * Закодированная строка является URL безопасной.
 */
public interface EncryptionUtil {
    /**
     * Кодирование строки.
     * Закодированная строка является URL безопасной.
     * @param originalString Строка, которую следует закодировать.
     * @return URL безопасная закодированная строка.
     */
    String encode(String originalString);

    /**
     * Декодирование строки.
     * @param encryptedString Строка, которую следует декодировать.
     * @return декодированная строка.
     */
    String decode(String encryptedString);
}
