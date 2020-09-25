package org.kushtrimhajrizi.rpalace.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class IdGenerator implements IdentifierGenerator {

    private static final Logger logger = LogManager.getLogger(IdGenerator.class);

    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
    private static final int MAX_ID_LENGTH = 32;

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        Random random = new Random();
        char[] allowedCharacters = ALLOWED_CHARACTERS.toCharArray();
        int allowedCharactersArrayLength = allowedCharacters.length;
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < MAX_ID_LENGTH; i++) {
            idBuilder.append(allowedCharacters[random.nextInt(allowedCharactersArrayLength)]);
        }
        String generatedId = idBuilder.toString();
        logger.debug("Generated id: {}", generatedId);
        return generatedId;
    }
}
