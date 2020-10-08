package org.kushtrimhajrizi.rpalace.api.reddit.response;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class UserInfoResponse {
    private String name;
    private String iconImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconImg() {
        return iconImg;
    }

    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoResponse that = (UserInfoResponse) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(iconImg, that.iconImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, iconImg);
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "name='" + name + '\'' +
                ", iconImg='" + iconImg + '\'' +
                '}';
    }
}
