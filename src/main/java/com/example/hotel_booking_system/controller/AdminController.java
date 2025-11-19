package com.example.hotel_booking_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel_booking_system.service.BookingService;
import com.example.hotel_booking_system.service.HotelService;
import com.example.hotel_booking_system.service.PaymentService;
import com.example.hotel_booking_system.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserService userService;
    private final HotelService hotelService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    // Remove @Autowired - constructor injection doesn't need it in Spring 4.3+
    public AdminController(UserService userService, HotelService hotelService, 
                         BookingService bookingService, PaymentService paymentService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        long totalUsers = userService.getAllUsers().size();
        long totalHotels = hotelService.getAllHotels().size();
        long totalBookings = bookingService.getAllBookings().size();
        long totalPayments = paymentService.getAllPayments().size();
        
        DashboardStats stats = new DashboardStats(totalUsers, totalHotels, totalBookings, totalPayments);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public static class DashboardStats {
        private final long totalUsers;
        private final long totalHotels;
        private final long totalBookings;
        private final long totalPayments;

        public DashboardStats(long totalUsers, long totalHotels, long totalBookings, long totalPayments) {
            this.totalUsers = totalUsers;
            this.totalHotels = totalHotels;
            this.totalBookings = totalBookings;
            this.totalPayments = totalPayments;
        }

        public long getTotalUsers() { return totalUsers; }
        public long getTotalHotels() { return totalHotels; }
        public long getTotalBookings() { return totalBookings; }
        public long getTotalPayments() { return totalPayments; }
    }
}