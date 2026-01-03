package com.packt.cardatabase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Owner {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName, lastName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Car> cars;

    /* 다대다 관계로 변환할 경우
    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable은 두 테이블간의 다대다 관계를 관리하기 위한 특별한 종류의 테이블
    @JoinTable(name="car_owner", joinColumns =
        {
        @JoinColumn(name="onwer_id" },
        inverseJoinColumns =
        {
        @JoinColumn(name="id") }
    )
    다대다 관계에서는 List 대신 Set을 이용하는것이 좋다
    private Set<Car> cars = new HashSet<Car>();
    public Set<Car> getCars() {
        return cars;
    }
    public void setCars(Set<Cars> cars) {
        this.cars = cars;
    }

     */

    public Owner() {

    }

    public Owner(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Car> getCars() {
        return cars;
    }
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}

