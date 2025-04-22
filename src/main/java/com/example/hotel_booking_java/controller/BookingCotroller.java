package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.BookingServices;
import com.example.hotel_booking_java.services.UserServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingCotroller {
    @Autowired
    private BookingServices bookingServices;

    @Autowired
    private UserServices userServices;

    @Autowired
    private JwtHelper jwtHelper;

    private Integer userIdFromHeader(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        Map<String, Object> c = jwtHelper.decodeToken(auth.substring(7));
        return c == null ? null : (Integer) c.get("id");
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createBooking(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody BookingRequest req) {

        Integer userId = userIdFromHeader(auth);
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new BaseResponse() {{
                        setCode(401);
                        setMessage("Invalid token");
                    }});
        }

        BookingDTO dto = bookingServices.createBooking(req, userId);
        return ResponseEntity.ok(new BaseResponse() {{
            setCode(200);
            setMessage("Booking created");
            setData(dto);
        }});
    }

    @GetMapping
    public ResponseEntity<BaseResponse> list(
            @RequestParam int pageNumber, @RequestParam int pageSize) {

        List<BookingDTO> list = bookingServices.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(new BaseResponse() {{
            setCode(200);
            setMessage("success");
            setData(list);
        }});
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getOne(@PathVariable int id) {
        BookingDTO dto = bookingServices.getOne(id);
        return ResponseEntity.ok(new BaseResponse() {{
            setCode(200);
            setMessage("success");
            setData(dto);
        }});
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable int id,
            @RequestBody BookingRequest req) {

        Integer userId = userIdFromHeader(auth);
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new BaseResponse() {{
                        setCode(401);
                        setMessage("Invalid token");
                    }});
        }
        BookingDTO dto = bookingServices.updateBooking(id, req, userId);
        return ResponseEntity.ok(new BaseResponse() {{
            setCode(200);
            setMessage("Booking updated");
            setData(dto);
        }});
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(
            @RequestHeader("Authorization") String auth,
            @PathVariable int id) {

        Integer userId = userIdFromHeader(auth);
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new BaseResponse() {{
                        setCode(401);
                        setMessage("Invalid token");
                    }});
        }
        bookingServices.deleteBooking(id);
        return ResponseEntity.ok(new BaseResponse() {{
            setCode(200);
            setMessage("Booking deleted");
        }});
    }
}
