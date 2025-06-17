# AI 활용 지능형 수강신청 시스템

- 사용자 경험(UX) 중심의 시스템 재설계 및 개발

## 1. 프로젝트 소개

본 프로젝트는 기존 수강신청 시스템의 여러 불편함(시간표 확인, 졸업요건 계산 등)을 해결하고, AI 추천 기능 등을 도입하여 사용자 경험을 개선한 JavaFX 기반의 데스크톱 수강신청 애플리케이션입니다.

실행 불가능한 레거시 콘솔 오픈 소스를 분석하는 것에서 출발하여, 3-Tier 아키텍처와 MVC, DAO 디자인 패턴을 적용해 전체 시스템을 독자적으로 재설계 및 개발했습니다.

## 2. 주요 기능

- 통합 대시보드: 수강신청 내역, 실시간 시간표, 졸업요건 현황을 한눈에 제공
- 실시간 시간표 시각화: 수강 신청/취소 시 시간표가 즉시 업데이트
- 실시간 졸업요건 트래커: 학점 변경 시 졸업까지 필요한 학점을 자동 계산
- 지능형 신청 로직: 시간표 충돌 시 원클릭 과목 교체, 정원 초과 시 자동 대기열 등록 및 관리
- AI 기반 과목 추천: 학생의 전공 및 이수 내역을 분석하여 개인화된 과목 추천

## 3. 기술 스택

- Language: Java (JDK 21)
- Framework: JavaFX (Version 21)
- Database: MySQL
- Build Tool: Maven
- IDE: IntelliJ IDEA

## 4. 실행 화면

| 로그인 화면 | 학생 대시보드 |
| :---: | :---: |
| ![Login Screen]([login.png]) | ![Student Dashboard]([student_dash.png]) |

## 5. 실행 방법

#### 사전 준비사항
1.  JDK 21 이상 설치
2.  MySQL 설치 및 서버 실행
3.  Maven 및 IntelliJ IDEA 설치

#### 단계별 실행 순서

1.  데이터베이스 설정
    - MySQL에 접속하여 `db_setup.sql` 파일에 포함된 스크립트 전체를 실행합니다.
    - `university_course_db` 데이터베이스와 필요한 모든 테이블, 초기 데이터가 생성됩니다.
    ```sql
    -- 예시: mysql -u root -p < db_setup.sql
    ```

2.  프로젝트 설정
    - IntelliJ IDEA에서 `pom.xml` 파일을 `Open as a Project`로 열어 Maven 프로젝트를 로드합니다.
    - `src/main/java/com/example/universitycoursesystemui/util/DatabaseConnection.java` 파일을 엽니다.
    - `USER`와 `PASSWORD` 변수 값을 자신의 MySQL 계정 정보에 맞게 수정합니다.

3.  애플리케이션 실행
    - `src/main/java/com/example/universitycoursesystemui/Launcher.java` 파일을 찾아 실행(Run)합니다.
    - 로그인 창이 나타나면 정상적으로 실행된 것입니다.

    - 시연용 초기 계정 정보:
      - **ID:** `2022111053`
      - **PW:** `password`
