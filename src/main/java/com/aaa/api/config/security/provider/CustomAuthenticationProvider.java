package com.aaa.api.config.security.provider;

import com.aaa.api.exception.InvalidSignInInfomation;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
       try {
            return super.authenticate(authentication);
       }catch (BadCredentialsException | InternalAuthenticationServiceException e){
           throw new InvalidSignInInfomation();
       }
    }
}
