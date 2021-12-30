package com.thoughtworks.darkhorse.reservationsystem.domainservice;

import com.thoughtworks.darkhorse.reservationsystem.domainmodel.ErrorCode;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.PaymentGateway;
import com.thoughtworks.darkhorse.reservationsystem.domainservice.externalservice.UnsupportedDepositPaymentTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private ContractService contractService;

    @Test
    void should_throw_exception_when_create_payment_request_with_unsupported_payment_type() {
        UnsupportedDepositPaymentTypeException exception = assertThrows(UnsupportedDepositPaymentTypeException.class,
                () -> contractService.createDepositPaymentTransaction("Alipay"));
        assertEquals(ErrorCode.UNSUPPORTED_DEPOSIT_PAYMENT_TYPE, exception.getErrorCode());
        verify(paymentGateway, times(0)).createUnionPayTransaction(any());
        verify(paymentGateway, times(0)).createBankTransferTransaction(any());
    }
}