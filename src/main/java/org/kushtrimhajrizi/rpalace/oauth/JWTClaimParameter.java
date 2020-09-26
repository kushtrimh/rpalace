package org.kushtrimhajrizi.rpalace.oauth;

public enum JWTClaimParameter {
    AUTHORITIES("authorities"),
    EMAIL("email"),
    VERSION("version");

    String parameterName;

    JWTClaimParameter(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
