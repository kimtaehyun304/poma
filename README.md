<h1>구장 예약·경기 인원 모집 / 2024-03 ~ 09</h1>

### 담당 역할
<ul>
  <li>html·css → thymeLeaf·반응형 변경 </li>
  <li>백엔드 모든 기능 개발 (마감 후 리팩토링)</li>
</ul>

### 기술
* spring (boot 3, security 6), hibernate 6, mysql 8
* thymeleaf 3, vanlia js, bootStrap 4

### 구조
* 시큐리티 필터체인 → 컨트롤러 → 뷰 
* 뷰에서 API 호출 → ajax 방식으로 뷰 반영
* csrf 방어를 위해 csrf 토큰 사용

### 구장 예약






### 프로젝트로 얻은 경험

보안
<ul>
  <li>csrf 토큰, csp (Content-Security-Policy) 적용</li>
  <li>
    <a href="https://github.com/kimtaehyun304/poma/blob/e3c4a97d4deb1eb61b1e4075d94dff6c39c7e2a5/src/main/java/goinmul/sportsmanage/config/SecurityConfig.java#L47">
      csp 이슈) 카카오 지도 css 누락 → 예외로 unsafe-inline</li>
    </a>
  <li>J세션 쿠키에 httpOnly, secure 적용</li>
  <li>api 응답 xss(Cross-Site-Scripting) 이스케이프 처리</li>
</ul>

UI/UX (좋은 사용자 경험 제공)
<ul>
    <li>로그인 성공 → reffer url로 redirect</li>
    <li>비 로그인 상태 응답 받음 → confirm 알림으로 로그인 페이지 이동 제안</li>
    <li>뒤로가기를 고려하여, 로그인 페이지에서는 브라우저 캐시를 비활성화</li>
    <li>ex) 로그인 상태임에도 뒤로가기로 로그인 페이지에 접근하면, UI상 비로그인 상태로 보일 수 있음</li>
</ul>

기타
<ul>
    <li>jpa 페치 조인 컬렉션 필드는 where절 자제 (이전 1차 캐시 결과와 다를 수 있기 때문)</li>
    <li>저장 전에, 중복 검사해도 동시에 요청 오면 저장 될 수 있음 → unique 제약 조건 필요</li>
    <li>java LocalDate 타입 직렬화 에러 → objectMapper 커스텀</li>
    <li>대댓글 ajax 구현</li>
    <li>클린 코드에 대한 지식이 적을 때 진행한 프로젝트라 개선 필요</li>
    <li>ex) ResponseEntity 제너럴 타입, 오류 메시지를 문자열 → 예외 변경 등</li>
</ul>

### 페이지

사용자 페이지
<ul>
  <li>홈 (경기 종목 선택)</li>
  <li>경기 조회·신청·수정·취소</li>
  <li>구장 조회·예약·결제</li>
  <li>고객 게시판 글 조회·등록 (비밀 글 가능)</li>
  <li>고객 게시판 댓글 조회·등록</li>
  <li>멘토 리뷰 게시판 (댓글·별점 조회·등록)</li>
  <li>팀 조회·생성</li>
  <li>로그인·회원가입·계정 찾기</li>
</ul>

마이 페이지
<ul>
  <li>회원 정보 조회·수정</li>
  <li>가입한 팀 수정</li>
</ul>

관리자 페이지
<ul>
  <li>유저 계정 권환 조회·수정</li>
  <li>구장 등록·수정</li>
</ul>

### erd
<p align="center">
<img src="https://github.com/user-attachments/assets/9011057d-cb83-41bc-8784-0d4d352f92ed"/>
</p>

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
