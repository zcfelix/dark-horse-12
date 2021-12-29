package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.repository.ContractRepository;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity.ContractEntity;
import com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.jparepository.ContractEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContractRepositoryImpl implements ContractRepository {

    private final ContractEntityJpaRepository entityRepository;

    @Autowired
    public ContractRepositoryImpl(ContractEntityJpaRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Contract save(Contract contract) {
        return ContractEntity.toDomainObject(entityRepository.save(ContractEntity.from(contract)));
    }

    @Override
    public Optional<Contract> findById(String id) {
        return entityRepository.findById(id).map(ContractEntity::toDomainObject);
    }

    @Override
    public void deleteAll() {
        entityRepository.deleteAll();
    }
}
