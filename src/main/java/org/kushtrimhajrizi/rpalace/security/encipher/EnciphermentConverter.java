package org.kushtrimhajrizi.rpalace.security.encipher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Kushtrim Hajrizi
 */
@Component
@Converter
public class EnciphermentConverter implements AttributeConverter<String, String> {

    private static AESEnciphermentService aesEnciphermentService;

    @Autowired
    public void init(AESEnciphermentService aesEnciphermentService) {
        EnciphermentConverter.aesEnciphermentService = aesEnciphermentService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return aesEnciphermentService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return aesEnciphermentService.decrypt(dbData);
    }
}
