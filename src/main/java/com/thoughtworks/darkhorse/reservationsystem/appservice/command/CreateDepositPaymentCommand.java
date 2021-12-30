package com.thoughtworks.darkhorse.reservationsystem.appservice.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepositPaymentCommand {
    private String paymentType;
}
