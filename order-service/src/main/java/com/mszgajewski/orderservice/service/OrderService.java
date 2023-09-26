package com.mszgajewski.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mszgajewski.orderservice.common.Payment;
import com.mszgajewski.orderservice.common.TransactionRequest;
import com.mszgajewski.orderservice.common.TransactionResponse;
import com.mszgajewski.orderservice.entity.Order;
import com.mszgajewski.orderservice.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri")
    private String ENDPOINT_URL;

     Logger logger = LoggerFactory.getLogger(OrderService.class);

    public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
        String response= "";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        logger.info("OrderService request :" + new ObjectMapper().writeValueAsString(request));

        Payment paymentResponse = restTemplate.postForObject(ENDPOINT_URL,payment, Payment.class);
        response = paymentResponse.getPaymentStatus().equals("success")? "płatność zrealizowana":"błąd płatności";
        logger.info("PaymentService response from OrderService Rest call : "+ new ObjectMapper().writeValueAsString(response));

        orderRepository.save(order);
        return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
    }
}