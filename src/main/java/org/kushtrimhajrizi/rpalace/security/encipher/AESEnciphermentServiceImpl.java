package org.kushtrimhajrizi.rpalace.security.encipher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.EnciphermentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class AESEnciphermentServiceImpl implements AESEnciphermentService {

    private static final Logger logger = LogManager.getLogger(AESEnciphermentServiceImpl.class);

    private static final int IV_LENGTH = 16;

    @Value("${rpalace.encipher.file}")
    private String keyFilepath;

    private byte[] key;

    @PostConstruct
    public void init() throws IOException {
        String keyData = Files.readString(Path.of(keyFilepath));
        key = Base64.getDecoder().decode(keyData);
    }

    @Override
    public String encrypt(String data) {
        byte[] iv = generateIv();
        var ivParameterSpec = new IvParameterSpec(iv);

        var keySpec = new SecretKeySpec(key, "AES");
        byte[] encryptedData = transform(Cipher.ENCRYPT_MODE,
                data.getBytes(StandardCharsets.UTF_8),
                keySpec,
                ivParameterSpec);

        var ivAndEncryptedData = new byte[IV_LENGTH + encryptedData.length];
        System.arraycopy(iv, 0, ivAndEncryptedData, 0, IV_LENGTH);
        System.arraycopy(encryptedData, 0, ivAndEncryptedData, IV_LENGTH, encryptedData.length);
        return Base64.getEncoder().encodeToString(ivAndEncryptedData);
    }

    @Override
    public String decrypt(String encryptedData) {
        byte[] encryptedDataAndIvBytes = Base64.getDecoder().decode(encryptedData);

        var iv = new byte[IV_LENGTH];
        System.arraycopy(encryptedDataAndIvBytes, 0, iv, 0, IV_LENGTH);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        int encryptedDataLength = encryptedDataAndIvBytes.length - IV_LENGTH;
        byte[] encryptedDataBytes = new byte[encryptedDataLength];
        System.arraycopy(encryptedDataAndIvBytes, IV_LENGTH, encryptedDataBytes, 0, encryptedDataLength);

        var keySpec = new SecretKeySpec(key, "AES");
        byte[] decryptedData = transform(Cipher.DECRYPT_MODE, encryptedDataBytes, keySpec, ivParameterSpec);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    private byte[] transform(int mode, byte[] data, SecretKeySpec keySpec, IvParameterSpec iv) {
        try {
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, keySpec, iv);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("Could not perform transformation mode:{}", mode, e);
            throw new EnciphermentException("Could not perform transformation", e);
        }
    }

    private byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        var secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }
}
