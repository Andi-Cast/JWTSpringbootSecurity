package Springboot.JWT.filter;

import Springboot.JWT.Service.JWTService;
import Springboot.JWT.Service.MemberDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MemberDetailsServiceImp memberDetailsServiceImp;

    public JWTAuthenticationFilter(JWTService jwtService, MemberDetailsServiceImp memberDetailsServiceImp) {
        this.jwtService = jwtService;
        this.memberDetailsServiceImp = memberDetailsServiceImp;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        //extracts authorization from the request
        String authHeader = request.getHeader("Authorization");

        //checks if authorization is present and starts with "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        //extract the JWT token from the authorization header
        String token = authHeader.substring(7);
        //extract username from the JWT token
        String username = jwtService.extractUsername(token);

        //checks if username is not null and there is no existing authentication in the SecurityContextHolder
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //loads UserDetails for the given username
            UserDetails userDetails = memberDetailsServiceImp.loadUserByUsername(username);

            //if token is valid, Authentication token is created
            if(jwtService.isValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                //set additional details for the authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //sets the authentication token in the SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

/*
This class is responsible for intercepting incoming HTTP requests, extracting JWT token from
authorization header, and validates them. If a valid token is found, an authentication is created
and set in the Spring Security context. This filter ensures only authenticated users can access
protected endpoints.
 */
