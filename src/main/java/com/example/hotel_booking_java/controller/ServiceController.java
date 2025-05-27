package com.example.hotel_booking_java.controller;


import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.dto.service.CreateServiceDto;
import com.example.hotel_booking_java.dto.service.UpdateServiceDto;
import com.example.hotel_booking_java.entity.Payments;
import com.example.hotel_booking_java.entity.Services;
import com.example.hotel_booking_java.payload.request.PagingRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.PaymentServices;
import com.example.hotel_booking_java.services.ServiceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
@CrossOrigin
public class ServiceController {

    @Autowired
    private ServiceServices serviceService;


    @PostMapping("/list")
    public ResponseEntity<?> getAllServices(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody PagingRequest pagingRequest) {
        BaseResponse response = new BaseResponse();

        int page = pagingRequest.getPage();
        int size = pagingRequest.getSize();

        try {
            List<Services> services = serviceService.getAllServices(authHeader, page, size);
            response.setMessage("service get successfully");
            response.setData(services);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("get failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<BaseResponse> getServiceById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam("id") int id) {

        BaseResponse response = new BaseResponse();

        try {
            Services service = serviceService.getServiceById(authHeader, id);

            if (service != null) {
                System.out.println("Found service: " + service);
                response.setCode(0);
                response.setMessage("service retrieved successfully");
                response.setData(service);
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("service not found");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.setCode(1);
            response.setMessage("Failed to retrieve service: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createService(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody CreateServiceDto request) {
        BaseResponse response = new BaseResponse();


        try {
            serviceService.createService(authHeader, request);
            response.setMessage("service created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateService(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody UpdateServiceDto request) {
        BaseResponse response = new BaseResponse();

        try {
            // Truyền 'id' từ request body thay vì từ URL
            serviceService.updateService(authHeader,  request);
            response.setMessage("service updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Update failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteService(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("id") int id) {
        BaseResponse response = new BaseResponse();

        try {

            serviceService.deleteService(authHeader,id);
            response.setMessage("Service deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}
