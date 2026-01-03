package com.packt.cardatabase.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    
    /* Java17 이상
    -- 레코드: 데이터 전달 목적, 객체를 더 빠르고 간편하게 만들기 위한 기능
    -- 상속 불가능, record로 정의한 field는 private final 또한 getter 자동 생성
    
    예제>>
    record Item(String name, int price) {
        // 위 파라미터가 private final로 정의, getter도 자동 생성
    }
    Item juice = new Item("juice", 3000);
    juice.price(); // 3000
    
     */

    private Long id;

    private String brand, model, color, registrationNumber;

    private int modelYear, price;

    // 여러차가 한 소유주에게 있을수 있음
    // FetchType: DB에서 data를 검색하는 전략
    // LAZY: 지연검색 (반대: EAGER: 즉시 검색)
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "owner")
    private Owner owner;

    /* 다대다 관계, @ManyToMany
    -- 다대다 관계로 바꾸면 아래와 같음. 여기서는 한 소유자가 자동차 여러대 가능. 한 자동차가 여러 소유자 가능.
    -- 다대다 관계에서는 List대신 Set을 이용하는것이 좋다.
    @ManyToMany(mappedBy="cars")
    private Set<Owner> owners = new HashSet<Owner>();

    public Set<Owner> getOwners() {
        return owners;
    }

    public void SetOwners(Set<Owner> owners) {
        this.owners - owners;
    }
     */

    /* @Column
    ----------------------------------------------------------------
    - DB의 칼럼의 이름은 기본적으로 class의 field 명명 규칙에 따라 지정
    - DB의 칼럼의 명명 규칙을 customize하고 싶다면, 아래와 같이.
    - name: 칼럼 이름, nullable: 칼럼의 nullable 여부, length: 칼럼 길이
    
    예제>>
     @Column(name="explanation", nullable=false, length=512)
     private String description;
    ----------------------------------------------------------------
    */

    public Car() {

    }

    public Car(String brand, String model, String color,
               String registrationNumber, int modelYear, int price,
               Owner owner)
    {
        super();
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.price = price;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
