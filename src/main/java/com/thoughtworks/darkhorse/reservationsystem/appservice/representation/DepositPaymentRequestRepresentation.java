package com.thoughtworks.darkhorse.reservationsystem.appservice.representation;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.DepositPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DepositPaymentRequestRepresentation {
    private String uri;

    private Double amount;

    private Instant createdAt;

    private Instant expiredAt;

    public static DepositPaymentRequestRepresentation from(DepositPayment saved) {
        return DepositPaymentRequestRepresentation.builder()
                .uri("/contracts/" + saved.getContractId() + "/deposit-payments/" + saved.getId())
                .amount(saved.getAmount().doubleValue())
                .createdAt(saved.getCreatedAt())
                .expiredAt(saved.getExpiredAt())
                .build();
    }
}
