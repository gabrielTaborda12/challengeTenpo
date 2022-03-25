package com.tenpo.utils;

import com.tenpo.service.impl.ChallengeServiceImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsCrypto {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsCrypto.class);

    public static String encrypt(String text) {
        String base64EncryptedString = "";
        try {
            byte[] plainTextBytes = text.getBytes("utf-8");
            base64EncryptedString = Base64.encodeBase64URLSafeString(plainTextBytes);
        } catch (Exception ex) {
            LOGGER.error("Error al encriptar: ({})",text);
            LOGGER.error("Exception message: ({})",ex.getMessage());
        }
        return base64EncryptedString;
    }

}
