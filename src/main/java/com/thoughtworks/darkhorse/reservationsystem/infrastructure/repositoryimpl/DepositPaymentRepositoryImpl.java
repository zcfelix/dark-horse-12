package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.DepositPayment;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.DepositPaymentRepository;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity.DepositPaymentEntity;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.jparepository.DepositPaymentEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DepositPaymentRepositoryImpl implements DepositPaymentRepository {

    private final DepositPaymentEntityJpaRepository entityRepository;

    @Autowired
    public DepositPaymentRepositoryImpl(DepositPaymentEntityJpaRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public DepositPayment save(DepositPayment depositPayment) {
        return entityRepository.save(DepositPaymentEntity.from(depositPayment)).toDomainObject();
    }
}
