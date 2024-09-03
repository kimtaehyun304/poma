package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.JoinedMatchesDto;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialUserRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;
    private final SocialUserRepository socialUserRepository;
    private final TeamUserRepository teamUserRepository;
    private final MercenaryMatchUserRepository mercenaryMatchUserRepository;
    private final JavaMailSender javaMailSender;

    //영속성 컨텍스면 변경감지로 처리하고, 영속성 아니면 업데이트 쿼리(조회 안하고 업데이트만 하고싶은 경우가 있음)
    public void updateTeam(User user, Team team) {
        if (em.contains(user))
            user.changeTeam(team);
        else {
            em.createQuery("update User u set u.team.id = :teamId where u.id = :userId")
                    .setParameter("teamId", team.getId())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
        }
    }

    //영속성 컨텍스트에 존재하는 상황이 없음
    public void plusMoney(User user, Integer money) {
        em.createQuery("update User u set u.money = u.money+:money where u.id = :userId")
                .setParameter("money", money)
                .setParameter("userId", user.getId())
                .executeUpdate();
    }

    public void minusMoney(User user, Integer money) {
        em.createQuery("update User u set u.money = u.money-:money where u.id = :userId")
                .setParameter("money", money)
                .setParameter("userId", user.getId())
                .executeUpdate();
    }

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public User findOneWithTeamById(Long id) {
        List<User> users = em.createQuery("select u from User u left join fetch u.team where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }


    //내부 조인은 user 팀 컬럼이 null이면 조회안되서 외부 조인으로 해야되요
    public User findWithTeamByLoginId(String loginId) {
        List<User> users = em.createQuery("select u from User u left join fetch u.team where u.loginId = :loginId", User.class)
                .setParameter("loginId", loginId)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    //로그인 인증, 세션 - 단축 평가와 삼항 연산자로 코드 줄이기
    public User login(String loginId, String password) {
        User user = findWithTeamByLoginId(loginId);
        User user1 = user != null && user.getPassword().equals(password) ? user : null;
        return user1;
    }


    public void updatePassword(String email, String password) {
        em.createQuery("update User u set u.password = :password where u.email = :email")
                .setParameter("password", password)
                .setParameter("email", email)
                .executeUpdate();
    }

    public User findByEmail(String email) {
        List<User> user = em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return user.isEmpty() ? null : user.get(0);

    }

    public List<User> findByName(String name) {
        List<User> users = em.createQuery("select u from User u where u.name like concat('%', :name ,'%')", User.class)
                .setParameter("name", name)
                .getResultList();

        return users;

    }

    public void sendNewPasswordByMail(String toMailAddr, String loginId, String newPassword) {

        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[POMA] 아이디와 새 비밀번호 안내입니다.");
                mimeMessageHelper.setText("아이디 : " +loginId + ", 새 비밀번호 : " + newPassword, true);
            }

        };
        javaMailSender.send(mimeMessagePreparator);

    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    //관리자 페이지
    public List<User> findExcludeSuperAdmin() {
        return em.createQuery("select u from User u where u.authority != : authority", User.class)
                .setParameter("authority", Authority.SUPER_ADMIN)
                .getResultList();
    }

    //구장 예약 성별 검사
    public List<User> findWithTeamByUserIdIn(List<Long> userIdList) {
        return em.createQuery("select u from User u join fetch u.team where u.id IN :userIdList", User.class)
                .setParameter("userIdList", userIdList)
                .getResultList();
    }

    public List<User> findByTeamIdAndGender(Long teamId, Gender gender) {
        List<User> users;
        if(gender.equals(Gender.BOTH)) {
            users = em.createQuery("select u from User u join fetch u.team where u.team.id = :teamId AND u.gender =:gender", User.class)
                    .setParameter("teamId", teamId)
                    .setParameter("gender", gender)
                    .getResultList();
        }else{
            users = em.createQuery("select u from User u join fetch u.team where u.team.id = :teamId", User.class)
                    .setParameter("teamId", teamId)
                    .getResultList();
        }
        return users;
    }

    public List<User> findByTeamIdAndGenderUserIdNotIn(Long teamId, Gender gender, List<Long> userIdList) {
        return em.createQuery("select u from User u join fetch u.team where u.team.id = :teamId AND u.gender =:gender and u.id not in :userIdList", User.class)
                .setParameter("teamId", teamId)
                .setParameter("gender", gender)
                .setParameter("userIdList", userIdList)
                .getResultList();
    }

    //용병 조회
    public List<User> findAllByUserIdIn(List<Long> userIdList) {
        List<User> users = em.createQuery("select u from User u where u.id in :userIdList", User.class)
                    .setParameter("userIdList", userIdList)
                    .getResultList();
        return users.isEmpty() ? null : users;
    }



    //매치 선발 멤버 선택 및 조회 (상대 팀에 용병으로 참가한 멤버는 제외함)
    public List<User> findAllByTeamIdAndGenderAndUserIdNotIn(Long teamId, Gender gender, List<Long> userIdList) {
        List<User> users;

        //BOTH면 남,여 데이터 모두 가져옵니다
        if(gender.equals(Gender.BOTH)) {
            if(userIdList.isEmpty()){
                users = em.createQuery("select u from User u where u.team.id = :teamId", User.class)
                        .setParameter("teamId", teamId)
                        .getResultList();
            }else{
                users = em.createQuery("select u from User u where u.team.id = :teamId and u.id not in :userIdList", User.class)
                        .setParameter("teamId", teamId)
                        .setParameter("userIdList", userIdList)
                        .getResultList();
            }
        }else {
            if(userIdList.isEmpty()){
                users = em.createQuery("select u from User u where u.team.id = :teamId AND u.gender =:gender", User.class)
                        .setParameter("teamId", teamId)
                        .setParameter("gender", gender)
                        .getResultList();
            }else{
                users = em.createQuery("select u from User u where u.team.id = :teamId AND u.gender =:gender and u.id not in :userIdList", User.class)
                        .setParameter("teamId", teamId)
                        .setParameter("gender", gender)
                        .setParameter("userIdList", userIdList)
                        .getResultList();
            }
        }

        return users;
    }


    //외부 조인으로 해야함
    public User findWithTeamByLoginIdAndNameAndEmail(String loginId, String name, String email) {
        List<User> users = em.createQuery("select u from User u left join fetch u.team " +
                        "where u.loginId = :loginId or u.name = :name or u.email = :email", User.class)
                .setParameter("loginId", loginId)
                .setParameter("name", name)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    //아이디,비밀번호 찾기할때 사용
    public String createNewPassword() {
        char[] chars = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'
        };

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int index = 0;
        int length = chars.length;
        for (int i = 0; i < 8; i++) {
            index = secureRandom.nextInt(length);

            if (index % 2 == 0)
                stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
            else
                stringBuffer.append(String.valueOf(chars[index]).toLowerCase());

        }

        return stringBuffer.toString();
    }

    public JoinedMatchesDto findJoinedMatchesDtoByUserId(Long userId) {
        List<SocialUser> socialUsers = socialUserRepository.findWithSocialMatchWithReservationWithGroundByUserId(userId);
        List<TeamUser> teamUsers = teamUserRepository.findWithTeamWithTeamMatchWithReservationWithGroundByUserId(userId);
        List<MercenaryMatchUser> mercenaryMatchUsers = mercenaryMatchUserRepository.findWithTeamWithMercenaryMatchWithReservationWithGroundByUserId(userId);
        return JoinedMatchesDto.createJoinedMatchesDto(socialUsers, teamUsers, mercenaryMatchUsers);
    }



}