<h2>프로젝트를 제작하며 고민하고있고 생각하고있는것들...</h2>
<h3>1. paging 처리시 pageable 활용이 아닌 queryDSL를 사용한 이유</h3>
    -> 카테고리 검색등 더 조회 방식을 확장시키고 동적으로 처리하기 위해.
<h3>2. Presentation, Business Layer 별 DTO 사용 refactoring 예정.</h3>
    -> 각 Layer간 의존성 분리 하위 Layer가 상위 Layer를 의존하지 않도록 않도록. ex) Service가 Controller를 모르게끔<p>
    -> @Valid등 Service에서는 필요한 data만 이용하여 불필요한 데이터를 담지않도록 성능 최적화 <p>
    -> 단순 DTO뿐 아니라 Layer별로 의존하지않는 구조를 가지도록 틈틈히 refactoring 하자.
<h3>3. CQRS Command와 Query의 분리 </h3>

<h3>4. Service 에서 Security 의존 코드 제거하기 (순수 Business Logic만 남겨두기)<h3>

<h3>5. DB 동시성 문제를 해결할 때 JPA가 제공해주는 비관적,낙관적 Lock을 사용할지, Select for update Query를 사용할지</h3>
    -> 아직 DB와 ORM에 부족한 지식이 많아 둘의 장단점 비교후 적용하자.