package com.packt.cardatabase.web;

import com.packt.cardatabase.domain.CarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.packt.cardatabase.domain.Car;

@RestController
public class CarController {

    private final CarRepository repository;

    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    // endpoint 정의: /cars
    // /cars endpoint로 Get http request가 들어오면 getCars() 메서드가 실행
    @GetMapping("/cars") // PostMapping, DeleteMapping etc..
    public Iterable<Car> getCars() {
        // 자동차 검색 및 반환
        return repository.findAll();
    }
}
