package com.KOndziu.ApiGateway.controllers;

import com.KOndziu.ApiGateway.models.ApiResponse;
import com.KOndziu.ApiGateway.models.JwtRequest;
import com.KOndziu.ApiGateway.models.JwtResponse;
import com.KOndziu.ApiGateway.models.MyUser;
import com.KOndziu.ApiGateway.repos.UserRepository;
import com.KOndziu.ApiGateway.srvices.JwtUserDetailsService;
import com.KOndziu.ApiGateway.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
public class JwtAuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }



    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody MyUser user){
        Optional<MyUser> myUser=userRepository
                .findByUsername(user.getUsername());

        if(myUser.isPresent()){
            return new ResponseEntity(new ApiResponse("Username is already taken!",false),
                    HttpStatus.BAD_REQUEST);
        }
        else {
            Optional<MyUser> usr=userRepository.findByRegistrationEmail(user.getRegistrationEmail());
            //if there is user with given email throw error
            if(usr.isPresent()){
                return new ResponseEntity(new ApiResponse("Email is already taken!",false),
                        HttpStatus.BAD_REQUEST);
            }
            else {
                MyUser newUser=MyUser.builder()
                        .firstName(user.getFirstName())
                        .secondName(user.getSecondName())
                        .registrationEmail(user.getRegistrationEmail())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities("ROLE_USER")
                        .build();
                userRepository.save(newUser);
                return new ResponseEntity(new ApiResponse("Registration Successful",false),
                        HttpStatus.OK);
            }
        }


    }
}
