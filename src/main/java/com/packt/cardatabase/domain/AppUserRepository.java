package com.packt.cardatabase.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/* exported=false
 --------------------------------------------------------------------------------------------------------------------------
 * 해당 repository가 REST resource로 노출이 되지 않게 하는 annotation
 * postMon이나 Insomnia로 /api/appUsers로 GET request하면 returned json response에 users 정보 노출됨. 그걸 방지하는 exported=false
 * @RepositoryRestResource annotation은 CarRepository.java에서 자세한 설명이 되어있음
 --------------------------------------------------------------------------------------------------------------------------
 */
@RepositoryRestResource(exported=false)
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
