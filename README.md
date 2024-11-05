# POMA - 1학기 캡스톤 디자인

https://dlpoma.store  

## 소개
POMA는 다양한 종목의 스포츠를 편리하게 즐길 수 있는 플랫폼입니다.  
구장 예약, 매칭 기능을 통해 사용자들이 편리하게 스포츠를 즐길 수 있습니다.

# 팀원 
총 8명으로 이루어진 팀 입니다. 저는 백엔드와 전체적인 프로젝트 관리를 맡았습니다.  

# 개발 기간
2024-03-19 ~ 2024-09-10 

## 프로젝트 스택
SpringBoot3, SpringSecurity6, Jpa, Gradle, Aws(rds, route53), lets`encrypt  
ThymeLeaf, BootStrap, JavaScript

## 설계
[스프링 시큐리티]  
SecurityFilterChain이 제공하는 기본 보안 설정과 csrf 토큰을 사용했습니다.  

[화면]  
스프링 MVC와 타임리프로 설계했습니다. SSR이지만 POST 메서드는 UI/UX를 위해 fetch api를 적용했습니다.  
url을 restful하게 지었습니다.  

[디자인]  
부트스트랩을 사용해 반응형 웹으로 만들었습니다.    

[DB]   
MySql-rds, JPA로 설계했습니다.  

## 로직
[회원가입, 로그인]  
비밀번호를 Bcrypt로 해싱하여 db에 저장합니다.  
*분실 계정 찾기: google smtp api를 통해 메일로 새로운 비밀번호를 보냅니다.  

[구장 예약]  
DB테이블은 구장(1)-예약(N)-매치(1)를 1:N:1 관계로 설계했습니다.  
결제하고 예약 데이터를 db에 저장합니다. 매치 데이터도 같이 저장합니다.  
*카카오 지도 API를 통해 예약할 구장 위치를 확인할 수 있습니다.  
*포트원 결제 API를 사용해 캐시를 충전하고, 예약할 때 캐시를 사용합니다.  
*실제로 구장을 예약하는 건 아닙니다.

[마이페이지]  
유저 정보, 매치 참가 이력, 구장 예약 이력을 볼 수 있습니다.  
*유저 정보: 팀(1)-유저(N)  
*매치 참가 이력: 구장(1)-예약(N)-매치(1)  
*구장 예약 이력: 구장(1)-예약(N)  

[관리자 페이지]  
관리자를 임명할 수 있고, 구장을 추가하거나 수정할 수 있습니다.  
연관관계는 없습니다.  

[매치]  
DB테이블은 매치 정보(1)-매치 팀원(N)를 1:N 관계로 설계했습니다.  
매치 리스트를 보고 참가할 수 있습니다. (수정, 취소 가능)  
*검색 기능을 제공합니다.  
*매치 구분: 소셜 매치(개인끼리 모인 매치), 팀 매치, 용병 매치(팀 매치에서 용병을 허용)  
*매치 종목: 8개  

[팀 생성, 가입]  
DB테이블은 팀(1)-유저(N)를 1:N 관계로 설정했습니다.  
팀 매치에 사용합니다. 팀에 가입하면 유저 데이터를 업데이트합니다.  

[멘토 멘티]  
DB테이블은 리뷰(N)-유저(1) N:1 관계로 설정했습니다.  
멘티가 멘토에 대한 리뷰와 평점을 작성할 수 있습니다.  
*페이징과 검색 기능이 있습니다.  

[고객 센터]  
DB테이블은 게시판(1)-댓글(N)을 1:N 관계로 설정했습니다.  
부모 댓글(1)-자식 댓글(N)을 1:N 관계로 설정했습니다. (셀프 조인)  
비밀 글로 작성하면 작성자와 관리자만 볼 수 있습니다.  
*페이징과 검색 기능이 있습니다.  

## 이번 프로젝트로 알게 된 점
JPA 객체 그래프 사상으로 인한 한계 (일부만 조회할 수 없다)  
*JPA 1:N 관계에서, N이 페치 조인 대상이면 N을 where절에 쓸 수 없다.  

db unique 제약 조건 예외는 검증 로직만으로는 동시성 문제를 해결 할 수 없어서 공통 예외 처리가 필요하다.  

ResponseEntity와 ModelAndView를 동적으로 반환하려면 메서드 반환형을 Object로 하면 된다.  
*검증에 따라 에러메시지와 뷰를 동적으로 보내는 로직이 있었습니다.  

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





