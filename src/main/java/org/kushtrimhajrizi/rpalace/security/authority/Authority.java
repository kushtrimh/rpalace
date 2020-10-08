package org.kushtrimhajrizi.rpalace.security.authority;

import org.hibernate.annotations.GenericGenerator;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Authority implements GrantedAuthority {
    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
    @Column
    @Enumerated(EnumType.STRING)
    private DefinedAuthority definedAuthority;
    @JoinColumn(name = "user_id")
    @ManyToOne
    public User user;

    public Authority() {
    }

    public Authority(DefinedAuthority definedAuthority) {
        this.definedAuthority = definedAuthority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DefinedAuthority getDefinedAuthority() {
        return definedAuthority;
    }

    public void setDefinedAuthority(DefinedAuthority definedAuthority) {
        this.definedAuthority = definedAuthority;
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
        Authority authority = (Authority) o;
        return Objects.equals(id, authority.id) &&
                definedAuthority == authority.definedAuthority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, definedAuthority);
    }

    @Override
    public String getAuthority() {
        return definedAuthority.getAuthorityName();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id='" + id + '\'' +
                ", definedAuthority=" + definedAuthority +
                '}';
    }
}
