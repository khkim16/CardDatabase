package com.packt.cardatabase.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

// 서비스에 리포지터리에 대한 쿼리를 포함하기 위한
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;

/*
 ---------------------------------------------------------------------------------------------------------------------
 @RepositoryRestResource annotation
 -- 스프링 데이타 REST는 각각 리포지터리에 대한 서비스의 기본적인 CRUD 엔드포인트를 자동으로 생성/
 -- 각 리포지터리의 엔티티의 복수형 소문자 형태로 생성됨.
    예) Car가 Entity인 CarRepository는 엔드포인트가 /basePath/cars
 -- 위와 다른 경로의 이름을 이용할려면 아래 @RepositoryRestResource(path=newPath)를 이용 (application.properties 참고)
    spring.data.rest.basePath와 중복되지 않고, /basePath/newPath 이런 형태로 생성됨
    예) @RepositoryRestResource(path="vehicles")
    ===> localhost:8080/api/vehicles (instead of /api/cars)
 -- CRUD 메서드들에서 (@Param=whichQuery)을 함께 사용시, /search 라는 새로운 엔드포인트 생성, 해당 쿼리 매개변수(whichQuery)로 검색을
    가능하게 해중 (아래 설명 참고)
 ---------------------------------------------------------------------------------------------------------------------
*/
@RepositoryRestResource

/* ***CRUD Repository***
 -----------------------------------------------------------------------------------------------------
 - CRUD repository interface, provided by Spring Data JPA: 엔티티 클래스에 CRUD 기능을 제공
 - import statement
   ==> import org.springframework.data.repository.CrudRepository;
 - Entity가 되는 Car 클래스와 ID의 타입인 Long
   ==> public interface nameRepository extends CrudRepository<Entity_Class, id_type> {}

 - CRUD 메서드들
 long count()                              엔티티의 수를 반환
 Iterable<T> findAll()                     지정한 타입의 모든 항목을 반환
 Optional<T> findById(ID id)               지정한 ID 한 항목을 반환
 void delete(T entity)                     엔티티 삭제
 void deleteAll()                          리포지터리의 모든 엔티티 삭제
 <S extends T> save(S entity)              엔티티를 저장
 List<S> saveAll(Iterable<S> entities)     여러 엔티티를 저장
 
 **Optional<T>: 값을 포함하거나 포함하지 않는 단일 값 컨테이너.
                값이 있으면, isPresent()가 true, 없으면 false 반환
                If isPresent() == true, then get()로 값을 구할수 있다.
                ** NullPointerException 방지!!!
 -----------------------------------------------------------------------------------------------------
*/
public interface CarRepository extends CrudRepository<Car,Long> {
    /* PagingAndSortingRepository
    ---------------------------------------------------------------------------
     public interface CarRepository **extends PagingAndSortingRepository** {
     CrudRepository로 확장된 PagingAndSortingRepository from Spring data JPA
     대규모 결과 집합에서 모든 데이터를 반환할 필요가 없기 때문에 대규모 데이터 처리하기에 적합
     
     - PagingAndSortingRepository 메서드들
     Iterable<T> findAll(Sort sort)     : 지정된 옵션으로 정력된 모든 엔티티 반환
     Page<T> findAll(Pageable pageable) : 지정된 페이징 옵션으로 모든 엔티티 반환

     ==> import org.springframework.data.repository.PagingAndSortingRepository;
    ---------------------------------------------------------------------------
    */

    // CRUD 메서드 general format
    // findBy + @Entity_Class_Field + And/Or/OrderBy/more keywords + @Entity_Class_Field2

    /* @Param("쿼리매개변수")
    ---------------------------------------------------------------------------
     @Param("쿼리매개변수"): /search라는 새로운 엔드포인트 생성. ===> /api/cars/search
     아래는, /api/cars/search/findByBrand(?brand)
     브랜드 별로 자동차를 검색할수 있게 해줌
    ---------------------------------------------------------------------------
    */
    // 브랜드로 자동차를 검색
    List<Car> findByBrand(@Param("brand") String brand);
    
    /*
    ---------------------------------------------------------------------------
     아래는, /api/cars/search/findByColor(?color)
     색깔별로 자동차를 검색할수 있게 해줌
    ---------------------------------------------------------------------------
    */
    // 색상으로 자동차를 검색
    List<Car> findByColor(@Param("color") String color);

    // 연도로 자동차를 검색
    List<Car> findByModelYear(int modelYear);

    // By키워드 다음에 And 및 Or 키워드를 붙여 여러 필드를 지정
    // 브랜드와 모델로 자동차를 검색
    List<Car> findByBrandAndModel(String brand, String model);

    // 브랜드 또는 색상별로 자동차 검색
    List<Car> findByBrandOrColor(String brand, String color);

    // 쿼리를 정렬하려면 퀴리 메서드에서 OrderBy 키워드를 이용
    // z브랜드로 자동차 검색, 그리고 연도로 정렬
    List<Car> findByBrandOrderByModelYearAsc(String brand);

    /* @Query annotation
    -- @Query annotation을 이용하면 SQL 문으로 직접 쿼리를 만들수 있다.
       --> 단점: 다른 DB로의 migration이 힘들어짐. (특정한 SQL query로 만들어지기 때문)
       --> import org.springframework.data.jpa.repository.Query;

    예제>>
    SQL 쿼리문을 이용해서 브랜드로 자동차 검색
    @Query("select c from Car c where c.brand=?1")
    List<Car> findByBrand(String brand);

    예제>>
    SQL "like" 쿼리문을 이용해서 브랜드로 자동차 검색
    @Query("select c from Car c where c.brand like %?1")
    List<Car> findByBrandLike(String brand);

    */

    /* 자바17 이상
    1) Text block, """, 는  여러줄의 텍스트를 훨씬 편리하게 작성케 해줌
    예제>>
    String str = """
        SEELCT * FROM "items"
        WHERE "status" = "ON_SALE"
        ORDER BY "price";
        """;

    2) formatted 메서드 제공: 값을 parsing하기 위함
    예제>>
    String textBlock17 = """
    {
        "id": %d
        "name": %s,
    }
    """.formatted(2, "juice");
    */
}
