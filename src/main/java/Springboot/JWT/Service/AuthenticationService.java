package Springboot.JWT.Service;

import Springboot.JWT.Model.AuthenticationResponse;
import Springboot.JWT.Model.Member;
import Springboot.JWT.Repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(Member request) {
        Member member = new Member();
        member.setFirstname(request.getFirstname());
        member.setLastname(request.getLastname());
        member.setUsername(request.getUsername());
        member.setPassword(request.getPassword());
        member.setRole(request.getRole());

        member = memberRepository.save(member);

        String token = jwtService.generateToken(member);

        return  new AuthenticationResponse(token);
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

        return new AuthenticationResponse(token);
    }
}
