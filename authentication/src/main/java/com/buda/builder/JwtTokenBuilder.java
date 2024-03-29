package com.buda.builder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.buda.entities.Staff;
import com.buda.entities.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenBuilder implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    // public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    //@Value("${jwt.secret}")
	private String secret = JwtConfig.secretKey;
    
    // retrieve username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    // get userID from jwttoken
    public Long getUserIDFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        Long userID = claims.get("userID", Long.class);
        return userID;
    }

    // get staffID from jwttoken
    public Long getStaffIDFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        Long staffID = claims.get("staffID", Long.class);
        return staffID;
    }
    // get staff uuid from jwttoken
    public String getStaffUUIDFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        String staffUUID = claims.get("staffID", String.class);
        return staffUUID;
    }

    // get tokentype from jwttoken
    public String getTokenTypeFromToken(String token)
    {
        Claims claims = getAllClaimsFromToken(token);
        String tokenType = claims.get("tokenType", String.class);
        return tokenType;
    }

    // get roles from jwttoken
    public Collection<?> getRolesFromToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        Collection<?> authorities = claims.get("roles", Collection.class);
        return authorities; 
    }


    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secretkey
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // check if the token has expired 
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("roles", user.getRoles());
        claims.put("tokenType", "Refresh");
        claims.put("userID", user.getUserID());
        return doGenerateToken(claims, user.getEmail(), JwtConfig.HoursRefreshToken);
    }

    public String generateStaffRefreshToken(Optional<Staff> staff) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("roles", staff.get().getRoles());
        claims.put("tokenType", "Refresh");
        claims.put("staffID", staff.get().getStaffID());
        claims.put("userID", staff.get().getUserID());
        return doGenerateToken(claims, staff.get().getEmail(), JwtConfig.HoursRefreshToken);
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("roles", user.getRoles());
        claims.put("tokenType", "Access");
        claims.put("userID", user.getUserID());
        return doGenerateToken(claims, user.getEmail(), JwtConfig.HoursAccessToken);
    }

    public String generateStaffAccessToken(Optional<Staff> staff) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("roles", staff.get().getRoles());
        claims.put("tokenType", "Access");
        claims.put("staffID", staff.get().getStaffID());
        claims.put("userID", staff.get().getUserID());
        return doGenerateToken(claims, staff.get().getEmail(), JwtConfig.HoursAccessToken);
    }
    // while creating the token
    // 1.Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT úing the HS512 algorithm and secret key
    // 3. According to JWS Compact Serialization 
    // compacton of the JWT to a URL-safe string 
    private String doGenerateToken(Map<String, Object> claims, String subject, int expiredTime){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiredTime * 60 * 60 * 1000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String doGenerateToken(Map<String, Object> claims)
    {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        try {
            Claims claims = getAllClaimsFromToken(token);
            String systemToken = doGenerateToken(claims);
            return (systemToken.equals(token) && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
        }
        return false;
        
    }
    public boolean isValid(String token)
    {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String systemToken = doGenerateToken(claims);
            return ((systemToken.equals(token)) && (!isTokenExpired(token)));
        } catch (JwtException | IllegalArgumentException e) {
        }
        return false;
    }
}