package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken;

import java.time.Instant;
import java.util.Objects;

public class AccessTokenDTO {
    private String accessToken;
    private Instant expirationTime;

    public AccessTokenDTO(String accessToken, Instant expirationTime) {
        this.accessToken = accessToken;
        this.expirationTime = expirationTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessTokenDTO that = (AccessTokenDTO) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(expirationTime, that.expirationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, expirationTime);
    }

    @Override
    public String toString() {
        return "AccessTokenDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
