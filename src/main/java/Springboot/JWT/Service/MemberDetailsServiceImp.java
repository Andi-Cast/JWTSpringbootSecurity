package Springboot.JWT.Service;

import Springboot.JWT.Repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//service layers uses the '@Service' annotation
@Service
public class MemberDetailsServiceImp implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsServiceImp(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Member not found."));
    }
}

/*
This class is used to ensure the authentication process has access to the necessary information
to perform authentication checks.
 */