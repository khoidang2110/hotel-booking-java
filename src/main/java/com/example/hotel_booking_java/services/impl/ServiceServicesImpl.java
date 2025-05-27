package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.dto.service.CreateServiceDto;
import com.example.hotel_booking_java.dto.service.UpdateServiceDto;
import com.example.hotel_booking_java.dto.user.UserPermissionDto;
import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Payments;
import com.example.hotel_booking_java.entity.Services;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.repository.BookingRepo;
import com.example.hotel_booking_java.repository.PaymentRepository;
import com.example.hotel_booking_java.repository.ServiceRepo;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.ServiceServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServicesImpl implements ServiceServices {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Services getServiceById(String authHeader, int id) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        return serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service with ID " + id + " not found"));
    }

    @Override
    public List<Services> getAllServices(String authHeader, int page, int size) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Fetch payments with pagination
        return serviceRepo.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public void createService(String authHeader, CreateServiceDto request) {





        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);

        if (permission.isHasPermission()) {
            // Người dùng có quyền, xử lý tiếp theo

            try {
                // Tạo dịch vụ mới với tên và giá tiền từ request
                Services service = new Services();
                service.setName(request.getName());
                service.setPrice(request.getPrice());

                // Lưu dịch vụ vào cơ sở dữ liệu
                serviceRepo.save(service);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid service name or price", e);
            }

        } else {
            throw new RuntimeException("Permission denied");
        }


    }


    @Override
    @Transactional
    public void updateService(String authHeader, UpdateServiceDto request) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Tìm service theo ID
        Services service = serviceRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + request.getId()));

        // Cập nhật name nếu có
        if (request.getName() != null && !request.getName().isEmpty()) {
            service.setName(request.getName());
        }

        // Cập nhật price nếu có
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }

        // Lưu lại vào DB
        serviceRepo.save(service);
    }


    @Override
    public void deleteService(String authHeader, int id) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        if (!serviceRepo.existsById(id)) {
            throw new RuntimeException("service not found with id " + id);
        }
        serviceRepo.deleteById(id);
    }


}
