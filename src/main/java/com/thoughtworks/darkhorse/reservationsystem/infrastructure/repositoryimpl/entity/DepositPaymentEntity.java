package com.thoughtworks.darkhorse.reservationsystem.infrastructure.repositoryimpl.entity;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.DepositPayment;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "deposit_payments")
public class DepositPaymentEntity {
    @Id
    @GenericGenerator(name = "id_generator", strategy = "uuid")
    @GeneratedValue(generator = "id_generator")
    private String id;

    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "expired_at", updatable = false)
    private Instant expiredAt;

    @Column(name = "paid")
    private Boolean paid;

    public static DepositPaymentEntity from(DepositPayment depositPayment) {
        return DepositPaymentEntity.builder()
                .id(depositPayment.getId())
                .contractId(depositPayment.getContractId())
                .transactionId(depositPayment.getTransactionId())
                .amount(depositPayment.getAmount())
                .createdAt(depositPayment.getCreatedAt())
                .expiredAt(depositPayment.getExpiredAt())
                .paid(depositPayment.getPaid())
                .build();
    }

    public DepositPayment toDomainObject() {
        return DepositPayment.builder()
                .id(this.getId())
                .contractId(this.getContractId())
                .transactionId(this.getTransactionId())
                .amount(this.getAmount())
                .createdAt(this.getCreatedAt())
                .expiredAt(this.getExpiredAt())
                .paid(this.getPaid())
                .build();
    }
}
