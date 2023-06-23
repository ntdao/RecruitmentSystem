package com.recruitmentsystem.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.recruitmentsystem.entity.User;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
//	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
	
	@Value("${app.jwt.secret}")
	private String SECRET_KEY;
	@Value("${app.jwt.expiration}")
	private long jwtExpiration;
	@Value("${app.jwt.refresh-token.expiration}")
	private long refreshExpiration;
	public String generateToken(User user) {
		return buildToken(new HashMap<>(), user, jwtExpiration);
	}

	public String generateRefreshToken(
			User user
	) {
		return buildToken(new HashMap<>(), user, refreshExpiration);
	}

	private String buildToken(
			Map<String, Object> extraClaims,
			User user,
			long expiration
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(user.getEmail())
				.claim("roles", user.getRole().toString())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS256, getSignInKey())
				.compact();
	}



	public String extractEmail(String token){
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	public Date extractExpiration(String token){
		return extractClaim(token, Claims::getExpiration);
	}
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
//	public boolean validateAccessToken(String token) {
//		try {
//			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//			return true;
//		} catch (ExpiredJwtException ex) {
//			LOGGER.error("JWT expired", ex.getMessage());
//		} catch (IllegalArgumentException ex) {
//			LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
//		} catch (MalformedJwtException ex) {
//			LOGGER.error("JWT is invalid", ex);
//		} catch (UnsupportedJwtException ex) {
//			LOGGER.error("JWT is not supported", ex);
//		} catch (SignatureException ex) {
//			LOGGER.error("Signature validation failed");
//		}
//
//		return false;
//	}

	
//	public Claims parseClaims(String token) {
//		return Jwts.parser()
//				.setSigningKey(getSignInKey())
//				.parseClaimsJws(token)
//				.getBody();
//	}
}
