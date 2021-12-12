package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
}
