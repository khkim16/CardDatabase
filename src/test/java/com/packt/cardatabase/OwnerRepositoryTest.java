package com.packt.cardatabase;

import com.packt.cardatabase.domain.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.packt.cardatabase.domain.Owner;
import com.packt.cardatabase.domain.OwnerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository repository;

    // 통합 테스트: repository가 database와의 상호작용 확인
    @Test
    void saveOwner() {
        repository.save(new Owner("Lucy", "Smith"));
        assertThat(
                repository.findByFirstName("Lucy").isPresent()
        ).isTrue();
    }

    @Test
    void deleteOwner() {
        repository.save(new Owner("Lisa", "Morrison"));
        repository.deleteAll();
        assertThat(repository.count()).isEqualTo(0);
    }

}
