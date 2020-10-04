package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ClientRegistrationTokenDTO {
    private String token;
    private Instant createdAt;
    @NotEmpty(message = "{validation.return_url_required}")
    private String returnUrl;

    public ClientRegistrationTokenDTO() {
    }

    public ClientRegistrationTokenDTO(String token, Instant createdAt, String returnUrl) {
        this.token = token;
        this.createdAt = createdAt;
        this.returnUrl = returnUrl;
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRegistrationTokenDTO that = (ClientRegistrationTokenDTO) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(returnUrl, that.returnUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, createdAt, returnUrl);
    }

    @Override
    public String toString() {
        return "ClientRegistrationTokenDTO{" +
                "token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
