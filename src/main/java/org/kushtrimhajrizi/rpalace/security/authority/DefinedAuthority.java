package org.kushtrimhajrizi.rpalace.security.authority;

public enum DefinedAuthority {
    ADMIN("admin"),
    USER("user"),
    REDDIT_CLIENT("reddit_client");

    String authorityName;

    DefinedAuthority(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }
}
