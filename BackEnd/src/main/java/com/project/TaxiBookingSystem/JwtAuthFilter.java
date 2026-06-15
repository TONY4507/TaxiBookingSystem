package com.project.TaxiBookingSystem;

import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.enums.Role;
import com.project.TaxiBookingSystem.repository.CustomerRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Claims claims = jwtUtil.extractClaims(token);
                    String roleName = claims.get("role", String.class);
                    Role role = Role.valueOf(roleName);

                    boolean approvedUser = true;
                    if (role == Role.ROLE_CUSTOMER) {
                        Optional<Customer> customerOpt = customerRepository.findByEmailIgnoreCase(username);
                        approvedUser = customerOpt.isPresent() && customerOpt.get().getApprovalStatus() == ApprovalStatus.APPROVED;
                    } else if (role == Role.ROLE_DRIVER) {
                        Optional<Driver> driverOpt = driverRepository.findByEmailIgnoreCase(username);
                        approvedUser = driverOpt.isPresent() && driverOpt.get().getApprovalStatus() == ApprovalStatus.APPROVED;
                    }

                    if (!jwtUtil.isTokenExpired(token) && approvedUser) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception ignored) {
                // If token is invalid or expired, do not authenticate the request.
            }
        }

        filterChain.doFilter(request, response);
    }
}
