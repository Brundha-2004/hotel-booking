package com.example.hotel_booking_system.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.hotel_booking_system.entities.Booking;
import com.example.hotel_booking_system.entities.Room;
import com.example.hotel_booking_system.entities.User;
import com.example.hotel_booking_system.repository.BookingRepository;
import com.example.hotel_booking_system.repository.RoomRepository;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private UserService userService;
    
    public Booking createBooking(Booking booking, @NonNull  Long userId,@NonNull  Long roomId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Check if room is available for the dates
        if (!isRoomAvailable(roomId, booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RuntimeException("Room not available for selected dates");
        }
        
        // Calculate total amount
        long numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal totalAmount = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        
        booking.setUser(user);
        booking.setRoom(room);
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");
        
        return bookingRepository.save(booking);
    }
    
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflictingBookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getRoom().getId().equals(roomId))
                .filter(booking -> !booking.getStatus().equals("CANCELLED"))
                .filter(booking -> datesOverlap(booking.getCheckInDate(), booking.getCheckOutDate(), checkIn, checkOut))
                .toList();
        
        return conflictingBookings.isEmpty();
    }
    
    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
    
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Optional<Booking> getBookingById(@NonNull Long id) {
        return bookingRepository.findById(id);
    }
    
    public Optional<Booking> getBookingByNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber);
    }
    
    public Booking cancelBooking(@NonNull Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
    
    public Booking updateBookingStatus(@NonNull Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}