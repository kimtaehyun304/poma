package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public boolean saveUser(User user) {
        boolean result = false;
        user.changeRegdate(LocalDateTime.now());
        User findUser = userRepository.findWithTeamByLoginIdAndNameAndEmail(user.getLoginId(), user.getName(), user.getEmail());
        if(findUser == null){
            userRepository.save(user);
            result = true;
        }
        return result;
    }

    @Transactional
    public void changeTeam(User user, Team team) {
        userRepository.updateTeam(user, team);
    }

    @Transactional
    public void chargeMoney(User user, Integer money) {
        userRepository.plusMoney(user, money);
    }

    @Transactional
    public boolean useMoney(User user, Integer money) {
        User findUser = userRepository.findOne(user.getId());
        if(findUser.getMoney() < money) return false;
        userRepository.minusMoney(user, money);
        return true;
    }

    @Transactional
    public boolean changePasswordAndSendMail(String email) {
        boolean result = false;
        User user = userRepository.findByEmail(email);
        if(user != null){
            String newPassword = userRepository.createNewPassword();
            userRepository.updatePassword(email, newPassword);
            userRepository.sendNewPasswordByMail(user.getEmail(), user.getLoginId(), newPassword);
            result = true;
        }
        return result;
    }



}
