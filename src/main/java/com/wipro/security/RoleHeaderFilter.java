package com.wipro.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RoleHeaderFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String rolesHeader = request.getHeader("role");

	    if (rolesHeader != null && !rolesHeader.isEmpty()) {
	        String rolesString = rolesHeader.replaceAll("[\"\\[\\] ]", "");

	        List<String> roles = Arrays.asList(rolesString.split(","));

	        Collection<GrantedAuthority> authorities = roles.stream()
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());

	        UsernamePasswordAuthenticationToken authenticationToken =
	                new UsernamePasswordAuthenticationToken("user", null, authorities);
	        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	    }

	    filterChain.doFilter(request, response);
	}
}
