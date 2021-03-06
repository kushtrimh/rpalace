package org.kushtrimhajrizi.rpalace.security.user;

import org.hibernate.annotations.GenericGenerator;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersion;
import org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken.RefreshToken;
import org.kushtrimhajrizi.rpalace.oauth.client.OAuth2AuthorizedClientEntity;
import org.kushtrimhajrizi.rpalace.security.authority.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class User implements UserDetails {

    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private boolean enabled;
    @OneToOne(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private AccessTokenVersion accessTokenVersion;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Authority> authorities = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<RefreshToken> refreshTokens = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<OAuth2AuthorizedClientEntity> oAuth2AuthorizedClientEntities = new LinkedHashSet<>();

    public static User fromEmailAndPassword(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(true);
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public AccessTokenVersion getAccessTokenVersion() {
        return accessTokenVersion;
    }

    public void setAccessTokenVersion(AccessTokenVersion accessTokenVersion) {
        this.accessTokenVersion = accessTokenVersion;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null || authorities.isEmpty()) {
            return Collections.emptyList();
        }
        String[] permissionsArray = authorities.stream().map(Authority::getAuthority)
                .collect(Collectors.toList()).toArray(new String[]{});
        return AuthorityUtils.createAuthorityList(permissionsArray);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Set<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(Set<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    @Transient
    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.setUser(this);
    }

    @Transient
    public void addAccessTokenVersion(AccessTokenVersion accessTokenVersion) {
        this.accessTokenVersion = accessTokenVersion;
        accessTokenVersion.setUser(this);
    }

    public Set<OAuth2AuthorizedClientEntity> getoAuth2AuthorizedClientEntities() {
        return oAuth2AuthorizedClientEntities;
    }

    public void setoAuth2AuthorizedClientEntities(Set<OAuth2AuthorizedClientEntity> oAuth2AuthorizedClientEntities) {
        this.oAuth2AuthorizedClientEntities = oAuth2AuthorizedClientEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", accessTokenVersion=" + accessTokenVersion +
                ", authorities=" + authorities +
                '}';
    }
}
