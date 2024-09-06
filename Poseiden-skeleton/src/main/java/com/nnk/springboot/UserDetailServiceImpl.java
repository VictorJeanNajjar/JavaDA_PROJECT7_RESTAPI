package com.nnk.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        com.nnk.springboot.domain.User person;

        try {
            person = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(com.nnk.springboot.domain.User.class), username);
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.withUsername(person.getUsername())
                .password(person.getPassword())
                .authorities(person.getRole())
                .build();

    }

}
