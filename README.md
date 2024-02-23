## 1. TomorrowLand 1.0 [ 개인 프로젝트 ]

<p>
    <img src="https://github.com/kwon93/TomorrowLandAPI/assets/133971731/7bce1eac-c77a-43b0-a754-eb57123c658c](https://github.com/kwon93/TomorrowLandAPI/assets/133971731/b9918f64-4aeb-4fc1-913f-407d7dd58d6b">
</p>
**지식 정보 공유 커뮤니티 사이트**

**질문자:** 20 *Point*를 차감하고 답변을 채택

**답변자:** 답변이 채택될시 50 *Point* 보상 *( Point에 따른 사용자 Level 존재 )*

*답변글은 질문자 본인만 확인 가능한 시스템.*

### *제작 목적*

- Frontend ~ BackEnd , 배포 까지 혼자서 진행해 **Web Site의 전체적인 흐름과 구조**를 직접 개발하기.
- 지난 팀 프로젝트에서 구현하지않았던 **Spring Security** 와 Session 방식이 아닌 **JWT** 방식의 인증 및 인가 시스템 구축
- 프로젝트 완성 후 **지속적인 Refactoring** 및 **기능 확장**을 해보며 건강한 Web Application 개발 학습

### ***제작 기간***

- **[Ver 1.0] *23.12.12 ~ 24.02.14** { BackEnd 1Month, Front 1Month }*
- **[Ver 2.0] 24.02.20 ~ 현재 추가 개발 중 …**

### ***사용한 기술 스택***

<p>
    <img src="https://github.com/kwon93/TomorrowLandAPI/assets/133971731/7bce1eac-c77a-43b0-a754-eb57123c658c">
</p>

![Database.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2cfd76ad-af06-49a6-8535-c74feb79a8e6/bc8e2d21-f622-491b-98bb-2872d2f51d85/Database.png)

![FrontTechStack.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2cfd76ad-af06-49a6-8535-c74feb79a8e6/83c3f265-cdb4-4aad-ada1-eef54a206b65/FrontTechStack.png)

![DevOpsTechStack.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2cfd76ad-af06-49a6-8535-c74feb79a8e6/5065f3ec-0a16-4e6d-ba4b-8e992992459f/DevOpsTechStack.png)

### *기능 구현*

1. **JWT 를 통한 인증 및 인가 시스템 구축.** 
    - AccessToken → Web Storage 저장
    - RefreshToken → Cookie 저장

[JWT를 구현하며 … ( Blog Link 1 )](https://kdh931228.tistory.com/33)

[JWT Token, Cookie ? LocalStorage ? ( Blog Link 2 )](https://kdh931228.tistory.com/79)

1. **AWS Cloud Image Upload , Download 기능 구현**
    - Multi Part가 아닌 **Octet-Stream**을 활용해 간편하고 빠른 이미지 업로드 기능 구현

[Image Upload, MIME Type을 Octet-Stream으로 결정한 이유 ( Blog Link )](https://kdh931228.tistory.com/62)

1. **조회수 및 좋아요 기능의 동시성 문제 해결**
    - 낙관적 Lock 과 비관적 Lock 중 **비관적 Lock** 을 활용해 동시성 문제 해결
    - 성능 테스트를 위해 **Jmeter** 활용.

[조회수 동시성문제 비관적 Lock을 선택한이유. ( Blog Link )](https://kdh931228.tistory.com/46)

1. **Spring REST Docs 를 활용해 BackEnd API 문서화 진행**
    - @AuthenticationPrincipal **Error**로 인해
    
    MockMvc를 설정할 때 *standAloneSetup()* 가 아닌 **@AutoConfigureRestDocs** 활용해 해결.
    

[RestDocs @AuthenticationPrinciapl 인증 실패 Error ( Blog Link )](https://kdh931228.tistory.com/70)

1. **Lyerd Architecture 사용, Layer들이 상위 Layer를 의존하지 않도록 구현**
    - 상위 **Layer를 의존하지않게** 확장성 있는 Application 개발

[Layer간 의존성 줄이기 ( Blog Link )](https://kdh931228.tistory.com/43)

***그 외 프로젝트를 개발하며 고민했던 점들 …***

- Teat Code에 관한 고민
    - Testable Code를 만들기위해 AWS S3와 같은 외부 API 분리 ( **관심사의 분리 진행** )
    - 외부 API 혹은 외부 라이브러리의 API 또한 테스트를 반드시 진행해야하는지에대한 고민
    - 각 **계층별 테스트의 목적**에 관한 고민 ( Controller 계층에서 테스트해야하는 점들 학습 )
- BackEnd, FrontEnd 의 역할에 관한 고민
    - 권한별 동적 렌더링을 진행할 때 BackEnd Server에서 렌더링을 위한 BusinessLogic을 작성해야하는지 Or FrontEnd에서 권한별 동적 렌더링에 관한 Logic을 작성해야하는지

[TomorrowLand 1.0 배포](http://tomorrow-front.s3-website.ap-northeast-2.amazonaws.com/)

[TomorrowLand GitHub Repository](https://github.com/kwon93/TomorrowLandAPI)
