package com.omm.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {
    private String paymentKey;
    private String orderId;
    private String amount;
}
