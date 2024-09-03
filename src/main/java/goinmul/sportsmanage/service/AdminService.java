package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.repository.AdminRepository;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean grantAuthority(User user, Authority authority) {
        boolean result = false;
        User findUser = userRepository.findOne(user.getId());
        if(findUser != null){
            adminRepository.updateAuthority(user, authority);
            result = true;
        }
        return result;
    }

}
