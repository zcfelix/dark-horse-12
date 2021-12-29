package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contracts")
public class ContractEntity {

    @Id
    @GenericGenerator(name = "id_generator", strategy = "uuid")
    @GeneratedValue(generator = "id_generator")
    private String id;

    @Column(name = "deposit_payed")
    private Boolean depositPayed;

    @Column(name = "service_charge_rate")
    private Integer serviceChargeRate;

    @Column(name = "created_at", updatable = false)
    @Generated(GenerationTime.ALWAYS)
    private Instant cratedAt;

    @Column(name = "created_at")
    @Generated(GenerationTime.ALWAYS)
    private Instant updatedAt;

    public static ContractEntity from(Contract contract) {
        return ContractEntity.builder()
                .id(contract.getId())
                .depositPayed(contract.getDepositPayed())
                .serviceChargeRate(contract.getServiceChargeRate())
                .cratedAt(contract.getCratedAt())
                .cratedAt(contract.getUpdatedAt())
                .build();
    }

    public static Contract toDomainObject(ContractEntity entity) {
        return Contract.builder()
                .id(entity.getId())
                .depositPayed(entity.getDepositPayed())
                .serviceChargeRate(entity.getServiceChargeRate())
                .cratedAt(entity.getCratedAt())
                .cratedAt(entity.getUpdatedAt())
                .build();
    }
}