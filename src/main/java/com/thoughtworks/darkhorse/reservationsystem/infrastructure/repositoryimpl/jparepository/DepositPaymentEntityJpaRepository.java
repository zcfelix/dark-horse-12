package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.jparepository;

import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity.DepositPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositPaymentEntityJpaRepository extends JpaRepository<DepositPaymentEntity, String> {
}
