package com.learning.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learning.service.UserServiceImpl;
import com.learning.utils.JwtTokenUtil;

@Component
public class JwtTokenFilter extends OncePerRequestFilter{
	
	
	@Autowired 
	UserServiceImpl userServiceImpl;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String autherizationHeader = request.getHeader("Authorization");
		String userName= null;
		String jwt= null;
		
		if(autherizationHeader != null && autherizationHeader.startsWith("Bearer ")) {
			jwt = autherizationHeader.substring(7);
			userName= jwtTokenUtil.getUsernameFromToken(jwt);
		}
		if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=userServiceImpl.loadUserByUsername(userName);
			if(jwtTokenUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			
		}
		filterChain.doFilter(request, response);
		System.err.println("JwtTokenFilter ");
	}

}
