package com.swiftselect.infrastructure.security;

import com.swiftselect.domain.entities.Employer;
import com.swiftselect.domain.entities.JobSeeker;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.repositories.JobSeekerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final EmployerRepository employerRepository;
    private final JobSeekerRepository jobSeekerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (employerRepository.existsByEmail(email)) {
            Employer employer = employerRepository.findByEmail(email).get();

            Set<GrantedAuthority> authorities = employer.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                    .collect(Collectors.toSet());

            return new User(
                    employer.getEmail(),
                    employer.getPassword(),
                    authorities
            );
        } else if (jobSeekerRepository.existsByEmail(email)) {
            JobSeeker jobSeeker = jobSeekerRepository.findByEmail(email).get();

            Set<GrantedAuthority> authorities = jobSeeker.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                    .collect(Collectors.toSet());

            return new User(
                    jobSeeker.getEmail(),
                    jobSeeker.getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
