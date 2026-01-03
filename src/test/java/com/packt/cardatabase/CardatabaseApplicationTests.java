package com.packt.cardatabase;

// 단위 테스트: assertJ library에서 assertThat 함수를 이용해서 해당 함수의 예상출력과 실제 출력을 비교
import static org.assertj.core.api.Assertions.assertThat;

// @DisplayName("Name_for_the_test") : 해당 테스트의 이름을 정의, JUnit test runner에 표시
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.packt.cardatabase.web.CarController;

@SpringBootTest
class CardatabaseApplicationTests {

    @Autowired
    private CarController controller;
-
    @Test
    @DisplayName("First example test case")
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
