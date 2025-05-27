package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.entity.Payments;
import com.example.hotel_booking_java.payload.request.PagingRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.repository.PaymentRepository;
import com.example.hotel_booking_java.services.PaymentServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentServices paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody CreatePaymentDto request) {
        BaseResponse response = new BaseResponse();


        try {
            paymentService.createPayment(authHeader, request);
            response.setMessage("payment created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PostMapping("/list")
    public ResponseEntity<?> getAllPayments(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody PagingRequest pagingRequest) {
        BaseResponse response = new BaseResponse();

        int page = pagingRequest.getPage();
        int size = pagingRequest.getSize();

        try {
            List<PaymentDto> payments = paymentService.getAllPayments(authHeader, page, size);
            response.setMessage("payment get successfully");
            response.setData(payments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("get failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updatePayment(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody UpdatePaymentDto request) {
        BaseResponse response = new BaseResponse();

        try {
            // Truyền 'id' từ request body thay vì từ URL
            paymentService.updatePayment(authHeader,  request);
            response.setMessage("Payment updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Update failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deletePayment(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("id") int id) {
        BaseResponse response = new BaseResponse();

        try {

            paymentService.deletePayment(authHeader,id);
            response.setMessage("Payment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/get-by-id")
    public ResponseEntity<BaseResponse> getPaymentById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam("id") int id) {

        BaseResponse response = new BaseResponse();

        try {
            // Tiến hành lấy payment từ service
            PaymentDto payment = paymentService.getPaymentById(authHeader, id);

            if (payment != null) {
                System.out.println("Found payment: " + payment);
                response.setCode(0);
                response.setMessage("Payment retrieved successfully");
                response.setData(payment);
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Payment not found");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.setCode(1);
            response.setMessage("Failed to retrieve payment: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}


