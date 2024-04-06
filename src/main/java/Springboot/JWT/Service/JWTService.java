package Springboot.JWT.Service;

import Springboot.JWT.Model.Member;
import Springboot.JWT.Repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    private final String SECRETE_KEY = "cc2500b65301f9301bd497af74a23760b09cc5138d043c541f7ca54a91727580";

    private final TokenRepository tokenRepository;

    public JWTService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(Member member) {
        return Jwts
                .builder()
                .subject(member.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) //24 hours to expire
                .signWith(getSigninKey())
                .compact();
    }

    public String extractUsername(String token) {
        //username of Member is stored in the subject claim
        return extractClaim(token, Claims::getSubject);
    }


    public boolean isValid(String token, UserDetails member) {
        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByToken(token)
                .map(t->!t.isLoggedOut()).orElse(false);

        return username.equals(member.getUsername()) && !isTokenExpired(token) && isValidToken;
    }

    /*
    * extractClaims(String, Claims) method takes in the token and applies a
    * resolver function to extract specific information
     */
    public <T> T extractClaim(String token, Function<Claims,T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRETE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    * extractAllClaims(String) takes in JWT token, verifies signature with secret key, parses claims,
    * and return claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}

/*
This service class is used for token generation, validation, and manipulation for authentication
and authorization purposes.
 */
