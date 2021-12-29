package com.thoughtworks.darkhorse.reservationsystem.domainmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    private String id;

    private Boolean depositPayed;

    private Integer serviceChargeRate;

    private Instant cratedAt;

    private Instant updatedAt;
}
