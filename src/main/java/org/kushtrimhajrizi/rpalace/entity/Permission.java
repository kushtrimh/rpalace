package org.kushtrimhajrizi.rpalace.entity;

import org.kushtrimhajrizi.rpalace.security.PermissionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Permission {

    @Id
    @Column
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    @Column
    private String description;
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<DefaultUser> users = new LinkedHashSet<>();

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DefaultUser> getUsers() {
        return users;
    }

    public void setUsers(Set<DefaultUser> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return permissionType == that.permissionType &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionType);
    }

    @Override
    public String toString() {
        return "UserPermission{" +
                "permissionType=" + permissionType +
                ", description='" + description + '\'' +
                '}';
    }
}
