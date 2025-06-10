
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


### 학습 내용
<ul>
  <li>
    로그인 성공하면 reffer url로 redirect 하기
  </li>
  <li>
    1:N 연관 관계는 N을 페치 조인하면 N은 where절 불가 (영속성 컨텍스트 충돌)  
  </li>
  <li>
    저장 로직은 중복 검사해도 동시에 요청 오면 예외 발생 (db unique 공통 예외 처리 필요)  
  </li>
</ul>

### 개선할 점
<ul>
  <li>
    스프링 시큐리티 CSP를 적용하니 외부 API(카카오 지도, 포트원) CSS 못 불러옴<br>
    → 주소를 CSP 화이트 리스트에 추가하려했는데, 난독화 돼있어서 주소 확인 실패
  </li>
  <li>
    <s>조건에 따라 ResponseEntity 또는 String(ModelAndView)를 반환하려면 메서드 반환형을 Object로 지정</s><br>
    → 지금 보니 공통 예외처리하면 String 반환형 가능
  </li>
  <li>
    <s>@RequestBody는 text/plain로 오면 자동 형변환 안해줌. text/plain 대신 applicaion/json 쓰자. </s><br>
    <s>applicaion/json은 value만 있어도 JSON.stringfiy 가능</s><br>
     → 지금 보니 text 인데 json으로 보내는 건 표준에 안맞는 것 같습니다
  </li>  
</ul>

### 기타
<ul>
  <li>@EnableWebSecurity와 csrf 토큰 적용</li>
  <li>restful url 고민하다 new 키워드 써도되는거 알게 됨</li>
  <li>spring, jpa, thymeLeaf 학습 이후 처음 적용한 프로젝트라 코드가 클린하지 않음<br>
    → tama api 프로젝트는 클린합니다</li>
</ul>

### UI/UX
<h4 align="center">메인 페이지</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/0888f4c6-6062-4016-9dda-0f2f571e83b2" width="50%" height="50%"/>
</p>

<h4 align="center">경기 매칭</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/c8e7dd30-e881-4868-b07d-129649a2ee76" width="50%" height="50%"/>
</p>

<h4 align="center">경기 매칭 디테일</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/043ce53f-9a90-4586-8822-2c6e6f0678ea" width="50%" height="50%"/>
</p>

<h4 align="center">구장 예약</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/2c17759a-da31-4834-a7c0-3da393ad58f4" width="50%" height="50%"/>
</p>

<h4 align="center">고객 센터</h4>
<p align="center">
<img src="https://github.com/user-attachments/assets/771149c7-c421-4b11-aa97-5d54cf8e1e9d" width="50%" height="50%"/>
</p>

분실 계정 찾기, 마이페이지, 멘토 멘티, 관리자 페이지, 분실 계정 찾기 등..

