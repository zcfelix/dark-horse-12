package com.thoughtworks.darkhorse.reservationsystem.infrastructure.resource;

import com.thoughtworks.darkhorse.reservationsystem.appservice.ContractAppService;
import com.thoughtworks.darkhorse.reservationsystem.appservice.command.CreateDepositPaymentCommand;
import com.thoughtworks.darkhorse.reservationsystem.appservice.representation.DepositPaymentRequestRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contracts")
public class ContractResource {

    private final ContractAppService contractAppService;

    @Autowired
    public ContractResource(ContractAppService contractAppService) {
        this.contractAppService = contractAppService;
    }

    @PostMapping(path = "/{id}/deposit-payments")
    public ResponseEntity<DepositPaymentRequestRepresentation> createDepositPaymentRequest(@PathVariable("id") String contractId,
                                                                                           @Valid @RequestBody CreateDepositPaymentCommand command) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contractAppService.createDepositPaymentRequest(contractId, command.getPaymentType()));
    }
}
