package org.la.ecom.msql.jwt;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


//this class not used
public class JwtTokenVerifier{// extends OncePerRequestFilter{

	public JwtTokenVerifier() {}
	
	//@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response, 
									FilterChain filterChain)
										throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		
		String SECRET_KEY = "secret";
		
		if(Strings.isNullOrEmpty(authorizationHeader) && !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.replace("Bearer ", "");
		
		try {
			
			//Jws<Claims> claimsJws = Jwts.parser()
			//								.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
			//								.parseClaimsJws(token);
			
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			
			Claims body = claimsJws.getBody();
			
			String username = body.getSubject();
			
			Set<SimpleGrantedAuthority> ssga = new HashSet<>();
			
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_ADMIN");
			
			ssga.add(sga);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch(JwtException e) {
			
			throw new IllegalStateException(String.format("token %s cannot be trusted", token));
		}
		
	}

}
