<h2>프로젝트를 제작하며 고민하고있고 생각하고있는것들...</h2>

<h3>1. paging 처리시 pageable 활용이 아닌 queryDSL를 사용한 이유</h3>
    -> 카테고리 검색등 더 조회 방식을 확장시키고 동적으로 처리하기 위해.

<h3>2. Presentation, Business Layer 별 DTO 사용 refactoring 예정.</h3>
    -> 각 Layer간 의존성 분리 하위 Layer가 상위 Layer를 의존하지 않도록 않도록. ex) Service가 Controller를 모르게끔<p>
    -> @Valid등 Service에서는 필요한 data만 이용하여 불필요한 데이터를 담지않도록 성능 최적화 <p>
    -> 단순 DTO뿐 아니라 Layer별로 의존하지않는 구조를 가지도록 틈틈히 refactoring 하자.

<h3>3. CQRS Command와 Query의 분리 </h3>
    -> 읽기 전용 테이블을 새로 생성할지 고민을 해봐야할듯...

<h3>4. Service 에서 Security 의존 코드 제거하기 (순수 Business Logic만 남겨두기)<h3>
    -> 멀티 모듈을 사용하거나 인증 전용 서버를 만들면 쉽게 해결할거같지만 대공사가 될거같다...

<h3>5. DB 동시성 문제를 해결할 때 JPA가 제공해주는 비관적,낙관적 Lock을 사용할지, Select for update Query를 사용할지</h3>
    -> 비관적 lock 사용으로 결정. 글 조회같이 쓰레드 경합이 빈번할때에는 낙관적락보다는 비관적락을 사용하자.

<h3>6. 이미지 업로드 방식 stream? or multiPart MediaType을 어느것을 사용할것인지 </h3>
    -> stream 으로 결정, 서버 메모리 디스크에 용량을 차지하지않다는점 (유지보수 편리), 구현의 간편함으로 인해

<h3>7. Testable Code를 위한 Refactoring을 진행해야함 </h3>
    -> 외부 요소로 인해 결정되는 값들은 매개변수로 옮기기, 관심사의 분리 등등....