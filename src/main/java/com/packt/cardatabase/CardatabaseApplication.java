package com.packt.cardatabase;

// 스프링부트 스타타 패키지 built-in 로깅: Logback (spring-boot-starter => spring-boot-starter-logging > Logback)
// Logback uses SLF4J as a basic interface
import com.packt.cardatabase.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CommandLineRunner는
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Arrays;

@SpringBootApplication
@EnableMethodSecurity
public class CardatabaseApplication implements CommandLineRunner {
    private static final Logger logger =
            LoggerFactory.getLogger(
                    CardatabaseApplication.class
            );

    private final CarRepository repository;
    private final OwnerRepository orepository;
    private final AppUserRepository urepository;

    // 생성자를 이용해서 리포지터리 객체 주입
    public CardatabaseApplication(CarRepository repository,
                                  OwnerRepository orepository, AppUserRepository urepository) {
        this.repository = repository;
        this.orepository = orepository;
        this.urepository = urepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CardatabaseApplication.class, args);
        // logger.info("message-to-be-printed-on-console");
        logger.info("Application started");
    }

    // CommandLineRunner를 이용하여 application이 완전히 시작하기 전에 아래 코드를 실행하여 예제 DB를 입력 for testing purpose
    // SpringApplication.run을 override
    @Override
    public void run(String... args) throws Exception {
        // 소유자 객체 추가하고 DB에 저장
        Owner owner1 =  new Owner("John", "Johnson");
        Owner owner2 =  new Owner("Mary", "Robinson");
        // saveAll 메서드는 Iterable of entities(ex: List)를 매개변수로 받음, save()를 매번 calling하는 대신.
        orepository.saveAll(Arrays.asList(owner1, owner2));

        // 자동차 객체를 추가하고 DB에 저장
        // 소유자를 자동차 객체에 포함해서 두 entity 연결
        repository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000, owner1));
        repository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000, owner2));
        repository.save(new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000, owner2));

        // 모든 자동차를 검색하고 Console에 출력
        for(Car car: repository.findAll())
        {
            logger.info("brand: {}, model: {}", car.getBrand(), car.getModel());
        }

        // 사용자명: user, 비밀번호: user
        urepository.save(new AppUser("user",
                "$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue","USER"));

        // 사용자명: admin, 비밀번호: admin
        urepository.save(new AppUser("admin",
                "$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW", "ADMIN"));
    }

}
