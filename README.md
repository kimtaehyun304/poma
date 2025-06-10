
## 소개
POMA는 다양한 스포츠를 즐길 수 있는 플랫폼입니다. 구장 예약, 경기 매칭을 제공합니다.  

본인 역할 (팀 8명)
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

## 학습한 내용
<ul>
  <li>
    1:N 연관 관계는 N을 페치 조인하면 N은 where절 불가. (영속성 컨텍스트 충돌)  
  </li>
  <li>
    저장 로직은 중복 검사해도 동시에 요청오면 예외 발생 (db unique 공통 예외 처리 필요)  
  </li>
  <li>
   조건에 따라 ResponseEntity 또는 ModelAndView를 반환하려면 메서드 반환형을 Object로 지정
  </li>
    <li>
   조건에 따라 ResponseEntity 또는 ModelAndView를 반환하려면 메서드 반환형을 Object로 지정
  </li>
</ul>


@RequestBody는 fetch api에서 text/plain로 보내면 자동으로 형변환을 안해준다. applicaion/json 쓰자.  
*applicaion/json은 key없이 value만 보내도 JSON.stringfiy를 쓸 수 있다.  


## 개발 후기
실무처럼 해보고자 백엔드 방어 코드를 신경 썼습니다.  
스프링 시큐리티 CSP를 적용하니 카카오 지도 API, 포트원 결제 API가 원활하지 않게 동작해서 적용을 못했습니다.  

## 디자인 미리보기

### 회원가입
<p align="center">
<img src="https://github.com/user-attachments/assets/1a564d2c-1bab-4639-a212-b46c70318ff6" width="50%" height="50%"/>
</p>

### 로그인
<p align="center">
<img src="https://github.com/user-attachments/assets/15c42d39-7d74-4850-9e07-80bb4aa8ba6b" width="50%" height="50%"/>
</p>

### 분실 계정 찾기
<p align="center">
<img src="https://github.com/user-attachments/assets/be053b30-18b2-4011-b7a5-80eddbe9bd03" width="50%" height="50%"/>
</p>

### 마이페이지
<p align="center">
<img src="https://github.com/user-attachments/assets/d5489aed-a78e-4e59-8055-c99bb383c533" width="50%" height="50%"/>
</p>

### 관리자 페이지
<p align="center">
<img src="https://github.com/user-attachments/assets/904d4c24-7c77-45e0-a30b-d5e78ad2f120" width="50%" height="50%"/>
</p>

<p align="center">
<img src="https://github.com/user-attachments/assets/dd6c9852-b007-46a5-b6a8-a1d6f8e71e3c" width="50%" height="50%"/>
</p>

### 메인 페이지
<p align="center">
<img src="https://github.com/user-attachments/assets/0888f4c6-6062-4016-9dda-0f2f571e83b2" width="50%" height="50%"/>
</p>

### 매치
<p align="center">
<img src="https://github.com/user-attachments/assets/c8e7dd30-e881-4868-b07d-129649a2ee76" width="50%" height="50%"/>
</p>

<p align="center">
<img src="https://github.com/user-attachments/assets/043ce53f-9a90-4586-8822-2c6e6f0678ea" width="50%" height="50%"/>
</p>

### 구장 예약
<p align="center">
<img src="https://github.com/user-attachments/assets/2c17759a-da31-4834-a7c0-3da393ad58f4" width="50%" height="50%"/>
</p>

### 고객 센터
<p align="center">
<img src="https://github.com/user-attachments/assets/771149c7-c421-4b11-aa97-5d54cf8e1e9d" width="50%" height="50%"/>
</p>

### 멘토 멘티
<p align="center">
<img src="https://github.com/user-attachments/assets/31d13549-d7c0-451d-a0e5-5c202cad85de" width="50%" height="50%"/>
</p>





