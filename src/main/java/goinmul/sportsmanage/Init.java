package goinmul.sportsmanage;


import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.service.GroundService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    private final InitService initService;
    private final Environment environment;

    @PostConstruct
    public void init() throws InterruptedException {
        String[] profiles = environment.getActiveProfiles();
        List<String> profileList = Arrays.asList(profiles);
        if (profileList.contains("local")) {
            initService.initAll();
        }

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final GroundService groundService;

        public void initAll() {
            initIGround();
        }

        public void initIGround(){
            Ground ground = Ground.createGround("가양레포츠센터축구장", "서울 강서구 가양동 1493", 10000);
            Ground ground2 = Ground.createGround("강남세곡체육공원", "서울 강남구 헌릉로 679", 20000);
            groundService.saveGround(ground);
            groundService.saveGround(ground2);
        }

    }
}