# 👥 AutoMeet

> **4인 팀 프로젝트**

참여자들의 일정을 기반으로 최적의 회의 시간을 추천하고, 손쉽게 회의 일정을 조율할 수 있는 온라인 협업 플랫폼입니다. 회의 중에는 관련 문서를 실시간으로 공유하며 함께 검토할 수 있어 효율적인 의사결정을 지원합니다.

---

## 🛠️ 기술 스택

### 📌 Backend
- **Spring Boot** `3.4.3`
- **Spring Security** `6.2.2`
- **MyBatis**
- **Spring Data Redis**
- **MySQL** (Docker)

### 💻 Frontend
- **React**

---

## 🎨 주요 화면

| 메인화면 | 회의생성 | 전체회의 |
|----------|--------|-----------|
| ![메인화면](https://github.com/user-attachments/assets/68b2e796-b0f3-4887-b5ec-495d33c7bc23) | ![회의생성](https://github.com/user-attachments/assets/6df3a48b-1a8e-4276-b610-d0e2a74e5147) | ![전체회의](https://github.com/user-attachments/assets/27764ff6-faee-4008-8adc-ed6e7887e0d3) |

| 시간표등록 | 시간확정 | 회의확정 |
|--------------|--------------|--------|
| ![시간표등록](https://github.com/user-attachments/assets/9fa4f16f-7654-41aa-8093-7e4e3b1fa44f) | ![시간확정](https://github.com/user-attachments/assets/8853d28b-8670-473c-8ed8-bbeeb98499ac) | ![회의확정](https://github.com/user-attachments/assets/a1accabb-ce88-45f3-9526-cc031f650917) |

---

## 📦 ERD
![ERD](https://github.com/user-attachments/assets/ff5644e1-1855-4ac1-a6af-f043c29cd5c4)

---

## 🎯 주요 기능

### 🔐 인증 및 보안
- 소셜 로그인: Google, Kakao OAuth2 연동
- JWT + Redis: Refresh Token Rotation 및 Whitelist 기반 인증 관리

### 시간표 등록
- 초대: 회의 생성 후 회의 채팅방에 초대 가능
- 시간표 확인: 등록한 시간표와 상대방이 확정한 시간표 확인 가능
- 시간 탐색: 참여자들의 시간표 교집합을 계산하여 가능한 회의 시간을 추천

---

## ⚙️ 핵심 구현

### 1. JWT 기반 인증 및 Refresh Token Rotation
- **Problem**: Stateless한 JWT는 서버가 토큰 상태를 직접 관리하지 않아 로그아웃 후에도 토큰이 유효하거나, 탈취된 Refresh Token이 재사용될 수 있음
- **Action**: Access Token과 Refresh Token을 분리하여 인증 구조를 설계하고, Refresh Token Rotation을 적용하여 재발급 시 기존 Refresh Token을 폐기 후 새로운 Refresh Token을 발급하도록 구현. 또한 서버에 Refresh Token Whitelist를 저장하여 유효한 토큰만 재발급을 허용
- **Result**: Refresh Token 재사용 공격(Replay Attack)을 방지하고, 로그아웃 시 즉시 토큰을 무효화할 수 있도록 서버 주도의 인증 관리 구조를 구축

### 2. 웹 보안: CSRF 공격 방어
- **Problem**: Cookie 기반 인증 환경에서는 브라우저가 인증 정보를 자동 전송하여 CSRF(Cross-Site Request Forgery) 공격에 노출될 수 있음
- **Action**: Access Token을 Local Storage에 저장하고 Authorization Header 방식으로 전송하도록 구현. Refresh Token은 HttpOnly, Secure, SameSite 속성을 적용한 Cookie로 관리하고 Access Token 만료 시에만 사용하도록 설계
- **Result**: 브라우저의 자동 인증 정보 전송을 최소화하여 CSRF 공격 위험을 감소시키고, Refresh Token 노출 빈도를 줄여 인증 토큰의 안전성을 향상

### 3. 아키텍처 설계: Port & Adapter 패턴을 통한 기술 스택 유연성 확보
- **Problem**: 팀 내 영속성 기술 선택 논의 중 MyBatis vs JPA 의견 대립 발생. 특정 기술에 종속된 구조로 개발 시 향후 기술 전환 시 비즈니스 로직까지 전면 수정 필요, DB 없이 순수 도메인 로직 테스트 불가능
- **Action**: Port 인터페이스를 통해 비즈니스 로직이 영속성 계층에 직접 의존하지 않도록 설계하고, MyBatis 구현체를 Adapter로 연결하여 인프라 기술을 외부로 분리
- **Result**: 비즈니스 로직과 영속성 기술의 결합도를 낮추어 향후 MyBatis ↔ JPA 전환 시 영향 범위를 최소화할 수 있는 구조를 확보

### 4. 회의 시간 추천 알고리즘 구현
- **Problem**: 참여자가 증가할수록 각자의 가능 시간을 수동으로 비교하여 공통 가능한 회의 시간을 찾는 과정이 비효율적
- **Action**: 참여자별 가능 시간을 시간 구간(Interval)으로 관리하고, 첫 번째 참여자의 시간 구간을 기준으로 나머지 참여자들의 시간 구간과 순차적으로 교집합을 계산하는 알고리즘을 구현
- **Result**: 모든 참여자가 참석 가능한 시간대를 자동으로 추출하여 회의 일정 조율 과정을 단순화