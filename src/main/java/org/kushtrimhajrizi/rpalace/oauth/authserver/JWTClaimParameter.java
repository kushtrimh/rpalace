package org.kushtrimhajrizi.rpalace.oauth.authserver;

public enum JWTClaimParameter {
    AUTHORITIES("authorities"),
    EMAIL("email");

    String parameterName;

    JWTClaimParameter(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
