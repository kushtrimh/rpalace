package org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken;

import org.hibernate.annotations.GenericGenerator;
import org.kushtrimhajrizi.rpalace.security.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.Objects;

@Entity
public class RefreshToken {

    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
    @Column
    private String refreshTokenHash;
    @Column
    private Instant createdAt;
    @Column
    private Boolean active;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public static RefreshToken newActiveRefreshToken(String refreshTokenHash, User user) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setRefreshTokenHash(refreshTokenHash);
        rt.setActive(true);
        rt.setCreatedAt(Instant.now());
        return rt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public void setRefreshTokenHash(String refreshTokenHash) {
        this.refreshTokenHash = refreshTokenHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(refreshTokenHash, that.refreshTokenHash) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, refreshTokenHash, createdAt, active);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id='" + id + '\'' +
                ", refreshTokenHash='" + refreshTokenHash + '\'' +
                ", createAt=" + createdAt +
                ", active=" + active +
                '}';
    }
}
