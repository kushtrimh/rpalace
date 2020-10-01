package org.kushtrimhajrizi.rpalace.security.encipher;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Kushtrim Hajrizi
 */
@Converter
public class EnciphermentConverter implements AttributeConverter<String, String> {

    private AESEnciphermentService aesEnciphermentService;

    public EnciphermentConverter(AESEnciphermentService aesEnciphermentService) {
        this.aesEnciphermentService = aesEnciphermentService;
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
