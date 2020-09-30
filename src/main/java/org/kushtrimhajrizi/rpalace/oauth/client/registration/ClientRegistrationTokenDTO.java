package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import java.time.Instant;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ClientRegistrationTokenDTO {
    private String token;
    private Instant createdAt;

    public ClientRegistrationTokenDTO(String token, Instant createdAt) {
        this.token = token;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRegistrationTokenDTO that = (ClientRegistrationTokenDTO) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, createdAt);
    }

    @Override
    public String toString() {
        return "ClientRegistrationTokenDTO{" +
                "token='" + token + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
