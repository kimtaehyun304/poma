
## 소개
POMA는 다양한 스포츠를 즐길 수 있는 플랫폼입니다. 구장 예약, 경기 매칭을 제공합니다. (배포 중단)  

본인 역할 (팀원 8명)
<ul>
  <li>
    퍼블리싱 결과물을 ThymeLeaf, 반응형으로 변경
  </li>
  <li>
    백엔드 모든 기능 개발 (팀원도 했는데 에러 나서 제가 고쳤습니다)
  </li>
</ul>
 
개발 기간  
2024-03-19 ~ 2024-09-10 

프로젝트 스택  
spring (boot3, security6) / jpa / thymeleaf / vanlia js / bootStrap  


### 어필
[스프링 시큐리티](https://github.com/kimtaehyun304/poma/blob/e3c4a97d4deb1eb61b1e4075d94dff6c39c7e2a5/src/main/java/goinmul/sportsmanage/config/SecurityConfig.java#L47)
<ul>
    <li>@EnableWebSecurity와 csrf 토큰 적용</li>
    <li>
        CSP 적용 (카카오 지도 페이지만 unsafe-inline하기 위해 별도 필터체인 등록)
    </li>
</ul>
XSS 방어
<ul>
    <li>js 이벤트 onClick 대신 eventListener 사용</li>
    <li>세션 쿠키 httpOnly, secure 적용</li>
</ul>
기타
<ul>
    <li>
      1:N 연관 관계에서 N을 페치 조인한 경우, where절 N 사용 자제<br>
      → 영속성 컨텍스트 충돌하기 때문
    </li>
    <li>
      저장 로직은 중복 검사해도 동시에 요청 오면 예외 발생<br>
       → 공통 예외 핸들러에 db unique 예외 추가
    </li>
    <li>
      LocalDate 직렬화하면 에러나서 customObjectMapper 사용
    </li>
  <li>대댓글 구현</li>
</ul>
좋은 사용자 경험 제공
<ul>
    <li>로그인 성공하면 reffer url로 redirect</li>
    <li>401응답 받으면 confirm으로 로그인 페이지 이동 제안</li>
    <li>뒤로가기를 고려해 로그인 페이지는 브라우저 캐시 비활성화</li>
</ul>

    


### 개선
개발 경험이 적을때라 코드가 클린하지 않아서 개선 중
<ul>
  <li>
    <s>조건에 따라 ResponseEntity 또는 view를 반환하려면 메서드 반환형을 Object로 지정</s><br>
    → 공통 예외처리하면 반환형 String(view)가능
  </li>
  <li>
    <s>@RequestBody는 text/plain로 오면 자동 형변환 안해주니까 applicaion/json 쓰자. </s><br>
    <s>applicaion/json은 value만 있어도 JSON.stringfiy 가능</s><br>
     → text 인데 json으로 보내는 건 부적절, 귀찮아도 key-value로 보내는 게 적절
  </li>  
    <li>
      개발, 배포 프로젝트 따로 사용하면 동기화할 때 힘들어서 합침<br>
      → db 환경은 application.yml 과 application-prod.yml로 구분 <br>
      → fetch api url은 window.location.origin 쓰면 알아서 구분 됨
    </li>
  <li>뷰 컨트롤러에 api 있는거 api 컨트롤러로 옮겨야 함</li>
</ul>

### UI/UX
<h4 align="center">메인 페이지</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/0888f4c6-6062-4016-9dda-0f2f571e83b2" />
</p>

<h4 align="center">경기 매칭</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/c8e7dd30-e881-4868-b07d-129649a2ee76" />
</p>

<h4 align="center">경기 매칭 디테일</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/043ce53f-9a90-4586-8822-2c6e6f0678ea" />
</p>

<h4 align="center">구장 예약</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/2c17759a-da31-4834-a7c0-3da393ad58f4" />
</p>

<h4 align="center">고객 센터</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/771149c7-c421-4b11-aa97-5d54cf8e1e9d" />
</p>

분실 계정 찾기, 멘토 멘티, 마이페이지, 관리자 페이지 등..

