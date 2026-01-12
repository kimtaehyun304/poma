<h1>구장 예약·경기 인원 모집 / 2024-03 ~ 09</h1>

### 담당 역할
<ul>
  <li>html·css → thymeLeaf·반응형 변경 </li>
  <li>백엔드 모든 기능 개발 (마감 후 리팩토링)</li>
</ul>

### 기술
* spring (boot 3, security 6), hibernate 6, mysql 8
* thymeleaf 3, vanlia js, bootStrap 4
* aws (ec2, rds)

### 구조
* 시큐리티 필터체인 → 컨트롤러 → 뷰 
* 뷰에서 API 호출 → ajax 방식으로 뷰 반영
* csrf 방어를 위해 csrf 토큰 사용
* j세션 쿠키에 httpOnly, secure 적용
* 반응형 웹 (pc, 모바일)

### 구장 예약 (경기 모집)
예약 검증: 이미 예약된 시간인지, 머니가 충분한지  
* 포트원 결제로 포마 머니 충전
* 결제한만큼 충전하면 결제 검증 생략 가능
* 머니로 구장 예약과 동시에 경기 모집
* 구장 위치는 카카오 지도로 안내

### 경기 참여 방법
참여 검증: 이미 신청했는지, 마감됐는지, 모집 성별과 맞는지  
* 3:3, 4:4 방식의 인원 모집
* 팀을 만들어서 경기 가능 (용병 포함 가능)
* 매치 공고 게시판에서 참여 신청 가능
* 날짜, 종목, 성별로 경기 검색 가능 (동적 조건)
  
### 고객센터
* 비밀글로 문의 가능
* 게시글, 댓글, 대댓글 방식으로 CS 응대

### 트러블 슈팅
innerHTML로 html을 바꾸면, 내용은 바뀌나 script 태그 미동작 
* 원인: html5에 내재된 xss를 예방하는 규칙
* 시도: document.write를 쓰면 script 태그 동작하지만, 뒤로가기하면 전전 페이지가 나와서 실패
* ㄴhistory.pushState & popstate로 뒤로가기 문제를 해결하려 했지만
* ㄴhistory state에는 문자열만 저장 가능 (html 저장 불가) → script 태그 미동작 
* 해결: 리다이렉트로 페이지 이동 (본인 인증 API 호출 1회, 리다이렉트 1회)
* ㄴ총 2회라 innerHTML 고안한 것

jwt + ssr 조합은 안 어울린다
* 상황: 세션은 로컬 메모리를 사용하기에, 스케일 아웃 시 문제가 된다
* redis를 사용하면 해결되지만, 비싸고 아키텍처가 복잡해져서 싫어서 이 방법을 고안
* ajax 방식은 로컬스토리지를 사용할 수 있지만, 뷰 렌더링 방식은 못 사용
* 쿠키는 csrf 위험 때문에 채택X (csrf 토큰을 사용하면 되지만 메모리 필요)

<a href="https://github.com/kimtaehyun304/poma/blob/e3c4a97d4deb1eb61b1e4075d94dff6c39c7e2a5/src/main/java/goinmul/sportsmanage/config/SecurityConfig.java#L47">
  컨텐츠 보안 정책 적용 → 카카오 지도 css 못 불러옴
</a>

* 카카오 지도 사용하는 페이지에서만 컨턴츠 보안 정책 해재
* 위험 요소가 없어서 해재해도 괜찮다고 판단

저장 로직은 중복 검사를 해도 동시 요청엔 문제 발생
* unique 제약 조건을 추가

UI, UX 개선
<ul>
    <li>로그인 성공 → 이전 url로 redirect시키기</li>
    <li>비 로그인이라 요청 실패 → confirm 알림으로 로그인 페이지 이동 제안</li>
    <li>로그인 페이지로 뒤로가면 로그인 상태여도 비로그인으로 보임 → 브라우저 캐시 비활성화</li>
</ul>

기타
* 경험이 적을때 개발한 프로젝트라 개선 필요
* ex) 오류는 예외로 보내고 공통 예외처리 하기
* jpa 페치 조인 컬렉션 필드는 where절 자제 - 이전 1차 캐시 결과와 다를 수 있기 때문
* @PathVariable은 model에 자동으로 데이터를 넣는다.

### 페이지
경기 관련 페이지
<ul>
  <li>홈 (경기 종목 선택)</li>
  <li>경기 조회·신청·수정·취소</li>
  <li>구장 조회·예약·결제</li>
</ul>

게시판
<ul>
  <li>고객 게시판 (글,댓글 조회·등록)</li>
  <li>멘토 리뷰 게시판 (댓글·별점 조회·등록)</li>
  <li>팀 조회·생성</li>
</ul>

로그인·회원가입
* 계정 비밀번호 찾기 제공 (이메일로 임시 비밀번호 전송)

마이 페이지
<ul>
  <li>회원 정보 조회·수정</li>
  <li>소속 팀 변경</li>
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
