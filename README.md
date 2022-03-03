# Taglog
> 태그를 활용한 웹노트 API 서버

SpringBoot, Java, JPA project, Travis CI, AWS (EC2 & RDS & S3 & CodeDeploy)

작성한 콘텐츠를 검색 및 카테고리, 태그 sorting할 수 있는 서비스

[개발 일정](https://www.notion.so/6feb905083334719823ed46eabf1f199)

## 🔭 개발 프로세스

<aside>
💡 아이템 선정 → 기술 선정 → 기능 정의 → 페이지 기획 → 설계 → 개발 → 배포

</aside>

개발 순서

로그인관련 기능은 마지막

요구사항 리스트

- 콘텐츠 등록, 수정, 삭제, 배경화면 설정
    - 콘텐츠 타이틀
    - 콘텐츠 내용
    - 카테고리 선택
    - 태그 등록
    - 콘텐츠 북마크
- 메뉴
    - 카테고리 리스트
    - 카테고리 생성, 수정, 삭제
    - 카테고리 DragNDrop
- 검색
    - 콘텐츠 타이틀, 내용, 카테고리, 태그 전체 검색
    - 카테고리 검색
    - 태그 검색
- 회원가입 및 카카오톡 자동 로그인 연동, 로그인/아웃

## 🔭 기획부터 DB설계까지

### 서비스흐름도 및 와이어프레임 만들기

요구사항에 따라 필요한 화면 Whimsical을 이용하여 그리기

### Frontend(현서림)

적용 기술

|  |  |
| --- | --- |
| Language | JavaScript, TypeScript |
| Test |  |

개발 툴

| | |
| --- | --- |
| IDE  | Visual Studio Code |
| OS | Windows 10 |

### Backend(황명아)

적용 기술

|  |  |
| --- | --- |
| Framework | Spring Boot |
|  | Spring Data JPA |
|  | QueryDSL |
| Test | JUnit |
| Language | Java |
| DB | AWS RDS MariaDB |
| Server | AWS EC2 |
| VC | Git/Github |
| CI, CD | Travis, S3, CodeDeploy |
| API 문서화 | SWAGGER |

개발 툴

|  |  |
| --- | --- |
| IDE | IntelliJ |
| DB GUI | DBeaver |
| OS | Mac OS |

## **SWAGGER API DOCS**

[Swagger UI](http://ec2-54-180-22-149.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html)

## DB
1차 개발
![태그로그2](https://user-images.githubusercontent.com/79449735/151661138-23093a32-1f8b-4dc8-8a69-50970d40177a.png)

2차 개발
![seorim](https://user-images.githubusercontent.com/79449735/151661140-14dcb3e2-9d2e-42c7-b5bc-16b8b3f1d9f0.png)

 
## Notion 
https://www.notion.so/With-Meongtori-513d66dabcba4ae3b201e95ddd8e6d28

## 티스토리 
https://dvpdvp.tistory.com/

