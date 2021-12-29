package com.thoughtworks.darkhorse.reservationsystem.domainservice.repository;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;

import java.util.Optional;

public interface ContractRepository {
    Contract save(Contract contract);

    Optional<Contract> findById(String id);

    void  deleteAll();
}