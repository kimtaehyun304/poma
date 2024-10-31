# POMA - 1학기 캡스톤 디자인

https://dlpoma.store  

## 소개
POMA는 다양한 종목의 스포츠를 편리하게 즐길 수 있는 플랫폼입니다.  
구장 예약, 매칭 기능을 통해 사용자들이 편리하게 스포츠를 즐길 수 있습니다.

## 설계
[화면]  
스프링 MVC와 타임리프로 설계했습니다. SSR이지만 POST 메서드는 UI/UX를 위해 fetch api를 적용했습니다.  

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

[매치]  
DB테이블은 매치 정보(1)-매치 팀원(N)를 1:N 관계로 설계했습니다.  
매치 리스트를 보고 참가할 수 있습니다. 검색 기능을 제공합니다.  
*매치 구분: 소셜 매치(개인끼리 모인 매치), 팀 매치, 용병 매치(팀 매치에서 용병을 허용)  
*매치 종목: 8개  

[팀 생성, 가입]  
팀 매치에 사용합니다.  
DB테이블은 팀(1)-유저(N)를 1:N 관계로 설정했습니다. 팀에 가입하면 유저 데이터를 업데이트합니다.  

[마이페이지]  
유저 정보, 매치 참가 이력, 구장 예약 이력을 볼 수 있습니다.  
*유저 정보: 팀(1)-유저(N)  
*매치 참가 이력: 구장(1)-예약(N)-매치(1)  
*구장 예약 이력: 구장(1)-예약(N)  

[멘토 멘티]  
멘티가 멘토에 대한 리뷰를 작성할 수 있습니다. DB테이블은 리뷰(N)-유저(1) N:1 관계로 설정했습니다.  
group by sql을 통해 멘토의 평균 평점을 계산했습니다.  
페에ㅣㅈㅇ...

















  








## 디자인 미리보기

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




