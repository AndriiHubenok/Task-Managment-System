package org.example.security;

import org.example.dto.AccountRepository;
import org.example.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findById(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .authorities(List.of())
                .build();
    }
}
