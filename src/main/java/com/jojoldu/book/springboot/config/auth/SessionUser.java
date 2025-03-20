package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.User;
import lombok.Getter;

@Getter
public class SessionUser {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.picture = user.getPicture();
        this.email = user.getEmail();
    }
}
