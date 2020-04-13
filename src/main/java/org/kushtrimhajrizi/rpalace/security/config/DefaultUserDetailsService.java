package org.kushtrimhajrizi.rpalace.security.config;

import org.kushtrimhajrizi.rpalace.entity.DefaultUser;
import org.kushtrimhajrizi.rpalace.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public DefaultUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        DefaultUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }
}
