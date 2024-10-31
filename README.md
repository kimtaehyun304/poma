# POMA - 1학기 캡스톤 디자인

https://dlpoma.store  

## 소개
POMA는 다양한 종목의 스포츠를 편리하게 즐길 수 있는 플랫폼입니다.  
구장 예약, 매칭 기능을 통해 사용자들이 편리하게 스포츠를 즐길 수 있습니다.

## 설계
화면은 스프링 MVC와 타임리프로 설계했습니다. SSR이지만 POST 메서드는 UI/UX를 위해 fetch api를 적용했습니다.  
디자인은 부트스트랩을 사용해 반응형 웹으로 만들었습니다.    
DB는 MySql-rds, JPA로 설계했습니다.  

## 로직
회원가입, 로그인: 비밀번호를 Bcrypt로 해싱하여 db에 저장합니다.  
구장 예약: 결제하고 예약 데이터를 db에 저장합니다.   
*카카오 지도 API를 통해 예약할 구장 위치를 확인할 수 있습니다.  
*포트원 결제 API를 사용해 캐시를 충전하고, 예약할 때 캐시를 사용합니다. 





  








## 기능 설명

### 메인 페이지
종목을 선택하는 페이지입니다. 선택하면 해당 종목에 개설된 매치를 볼 수 있습니다.
<p align="center">
<img src="https://github.com/user-attachments/assets/0888f4c6-6062-4016-9dda-0f2f571e83b2" width="50%" height="50%"/>
</p>

### 매치
매치는 소셜 매치, 팀 매치, 용병 매치 3개가 존재합니다.   
<p align="center">
<img src="https://github.com/user-attachments/assets/c8e7dd30-e881-4868-b07d-129649a2ee76" width="50%" height="50%"/>
</p>

소셜 매치: 개인 인원을 모집하는 매치입니다.  
팀 매치: 팀별 인원을 모집하는 매치입니다.  
용병 매치: 기존의 팀 매치에서 용병 시스템이 추가된 매치입니다.  

### 구장 예약
어느 구장을 몇 시에 예약할 지 정하는 기능입니다. 아임포트 결제 API, 카카오 지도 API를 사용합니다.
<p align="center">
<img src="https://github.com/user-attachments/assets/2c17759a-da31-4834-a7c0-3da393ad58f4" width="50%" height="50%"/>
</p>

### 고객 센터
이용자들이 의견을 남길수 있는 게시판입니다. 게시글과 댓글을 남길 수 있습니다.
<p align="center">
<img src="https://github.com/user-attachments/assets/771149c7-c421-4b11-aa97-5d54cf8e1e9d" width="50%" height="50%"/>
</p>

### 멘토 멘티
멘토 지도 후기를 남길 수 있는 게시판입니다. 별점과 간단한 코멘트를 남길 수 있습니다.
<p align="center">
<img src="https://github.com/user-attachments/assets/31d13549-d7c0-451d-a0e5-5c202cad85de" width="50%" height="50%"/>
</p>

## 프로젝트 사용 기술
SpringBoot, SpringSecurity, ThymeLeaf, Jpa, Gradle, BootStrap, JavaScript, Aws




