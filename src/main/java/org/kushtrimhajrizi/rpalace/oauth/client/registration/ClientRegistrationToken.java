package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.hibernate.annotations.GenericGenerator;
import org.kushtrimhajrizi.rpalace.security.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Entity
@Table(name = "client_registration", indexes = {
        @Index(columnList = "token", name = "client_registration_registration_token_idx")
})
public class ClientRegistrationToken {

    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
    @Column
    private String token;
    @Column
    private Instant createdAt;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
    @Column
    private Boolean active;
    @Column
    private String returnUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        ClientRegistrationToken that = (ClientRegistrationToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(token, that.token) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, active);
    }

    @Override
    public String toString() {
        return "ClientRegistrationToken{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", active=" + active +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
