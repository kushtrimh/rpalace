package org.kushtrimhajrizi.rpalace.oauth.client;

import org.hibernate.annotations.GenericGenerator;
import org.kushtrimhajrizi.rpalace.security.encipher.EnciphermentConverter;
import org.kushtrimhajrizi.rpalace.security.user.User;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Entity
@Table(name = "oauth2_authorized_client")
public class OAuth2AuthorizedClientEntity {

    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
    @Column
    private String clientId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private String accessTokenType;
    @Column
    @Convert(converter = EnciphermentConverter.class)
    private String accessToken;
    @Column
    private Instant accessTokenIssuedAt;
    @Column
    private Instant accessTokenExpiresAt;
    @Column
    private String accessTokenScopes;
    @Column
    @Convert(converter = EnciphermentConverter.class)
    private String refreshToken;
    @Column
    private Instant refreshTokenIssuedAt;
    @Column
    private Instant createdAt;

    public OAuth2AuthorizedClientEntity() {
    }

    public OAuth2AuthorizedClientEntity(Builder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.user = builder.user;
        this.accessTokenType = builder.accessTokenType;
        this.accessToken = builder.accessToken;
        this.accessTokenIssuedAt = builder.accessTokenIssuedAt;
        this.accessTokenExpiresAt = builder.accessTokenExpiresAt;
        this.accessTokenScopes = builder.accessTokenScopes;
        this.refreshToken = builder.refreshToken;
        this.refreshTokenIssuedAt = builder.refreshTokenIssuedAt;
        this.createdAt = builder.createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getAccessTokenIssuedAt() {
        return accessTokenIssuedAt;
    }

    public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }

    public Instant getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public String getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(String accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
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
        OAuth2AuthorizedClientEntity that = (OAuth2AuthorizedClientEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(user, that.user) &&
                Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(refreshToken, that.refreshToken) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, user, accessToken, refreshToken, createdAt);
    }

    @Override
    public String toString() {
        return "OAuth2AuthorizedClientEntity{" +
                "id='" + id + '\'' +
                ", clientId='" + clientId + '\'' +
                ", user=" + user +
                ", accessTokenType='" + accessTokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", accessTokenIssuedAt=" + accessTokenIssuedAt +
                ", accessTokenExpiresAt=" + accessTokenExpiresAt +
                ", accessTokenScopes='" + accessTokenScopes + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenIssuedAt=" + refreshTokenIssuedAt +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class Builder {
        private String id;
        private String clientId;
        private User user;
        private String accessTokenType;
        private String accessToken;
        private Instant accessTokenIssuedAt;
        private Instant accessTokenExpiresAt;
        private String accessTokenScopes;
        private String refreshToken;
        private Instant refreshTokenIssuedAt;
        private Instant createdAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder accessTokenType(String accessTokenType) {
            this.accessTokenType = accessTokenType;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder accessTokenIssuedAt(Instant accessTokenIssuedAt) {
            this.accessTokenIssuedAt = accessTokenIssuedAt;
            return this;
        }

        public Builder accessTokenExpiresAt(Instant accessTokenExpiresAt) {
            this.accessTokenExpiresAt = accessTokenExpiresAt;
            return this;
        }

        public Builder accessTokenScopes(String accessTokenScopes) {
            this.accessTokenScopes = accessTokenScopes;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder refreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
            this.refreshTokenIssuedAt = refreshTokenIssuedAt;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OAuth2AuthorizedClientEntity build() {
            return new OAuth2AuthorizedClientEntity(this);
        }
    }
}
