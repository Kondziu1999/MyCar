package com.KOndziu.ApiGateway.srvices;

import com.KOndziu.ApiGateway.models.JwtUserDetails;
import com.KOndziu.ApiGateway.models.MyUser;
import com.KOndziu.ApiGateway.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@Qualifier(value = "JwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    //TODO add db connection to retrieve users
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<MyUser> user=userRepository.findByUsername(s);
        JwtUserDetails userDetails=JwtUserDetails.create(user.get());

        return userDetails;


//        return new User("foo","pass",
//                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
