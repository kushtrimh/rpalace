package org.kushtrimhajrizi.rpalace.security.authority;

import org.kushtrimhajrizi.rpalace.security.user.User;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Authority implements GrantedAuthority {
    @Id
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
        return definedAuthority == authority.definedAuthority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(definedAuthority);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "definedAuthority=" + definedAuthority +
                '}';
    }

    @Override
    public String getAuthority() {
        return definedAuthority.getAuthorityName();
    }
}
