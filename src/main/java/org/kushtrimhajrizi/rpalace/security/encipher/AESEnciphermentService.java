package org.kushtrimhajrizi.rpalace.security.encipher;

/**
 * @author Kushtrim Hajrizi
 */
public interface AESEnciphermentService {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
