package Springboot.JWT.Service;

import Springboot.JWT.Model.AuthenticationResponse;
import Springboot.JWT.Model.Member;
import Springboot.JWT.Model.Token;
import Springboot.JWT.Repository.MemberRepository;
import Springboot.JWT.Repository.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(Member request) {
        Member member = new Member();
        member.setFirstname(request.getFirstname());
        member.setLastname(request.getLastname());
        member.setUsername(request.getUsername());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRole(request.getRole());

        member = memberRepository.save(member);

        String jwt = jwtService.generateToken(member);

        saveMemberToken(jwt, member);


        return  new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authentication(Member request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(member);

        revokeAllTokensByMember(member);

        saveMemberToken(token, member);

        return new AuthenticationResponse(token);
    }

    private void revokeAllTokensByMember(Member member) {
        List<Token> validTokenListByMember = tokenRepository.findAllTokensByMember(member.getId());

        if(!validTokenListByMember.isEmpty()) {
            validTokenListByMember.forEach(t->
                    t.setLoggedOut(true)
            );
        }
    }

    private void saveMemberToken(String jwt, Member member) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setMember(member);
        tokenRepository.save(token);
    }
}
