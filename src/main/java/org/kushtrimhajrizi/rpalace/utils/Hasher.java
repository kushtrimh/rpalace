package org.kushtrimhajrizi.rpalace.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.HashException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * @author Kushtrim Hajrizi
 */
public class Hasher {

    private static final Logger logger = LogManager.getLogger(Hasher.class);

    private Hasher() {}

    public static String hashSha256(String data) throws HashException {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedTokenBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedTokenBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            throw new HashException("Could not hash data", e);
        }
    }
}
