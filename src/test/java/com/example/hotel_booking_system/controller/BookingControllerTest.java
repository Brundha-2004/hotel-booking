package com.example.hotel_booking_system.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.hotel_booking_system.entities.Booking;
import com.example.hotel_booking_system.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking testBooking;
    private Booking testBooking2;

    @BeforeEach
    void setUp() {
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setBookingNumber("BK001");
        testBooking.setCheckInDate(LocalDate.now().plusDays(1));
        testBooking.setCheckOutDate(LocalDate.now().plusDays(3));
        testBooking.setTotalAmount(new BigDecimal("299.99")); // Fixed: Use BigDecimal
        testBooking.setStatus("CONFIRMED");

        testBooking2 = new Booking();
        testBooking2.setId(2L);
        testBooking2.setBookingNumber("BK002");
        testBooking2.setCheckInDate(LocalDate.now().plusDays(5));
        testBooking2.setCheckOutDate(LocalDate.now().plusDays(7));
        testBooking2.setTotalAmount(new BigDecimal("399.99")); // Fixed: Use BigDecimal
        testBooking2.setStatus("PENDING");
    }

    @Test
    void createBooking_ValidRequest_ShouldReturnCreatedBooking() {
        // Arrange
        Long userId = 1L;
        Long roomId = 101L;
        when(bookingService.createBooking(any(Booking.class), eq(userId), eq(roomId)))
                .thenReturn(testBooking);

        // Act
        ResponseEntity<?> response = bookingController.createBooking(testBooking, userId, roomId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testBooking, response.getBody());
        verify(bookingService, times(1)).createBooking(testBooking, userId, roomId);
    }

    @Test
    void createBooking_ServiceThrowsException_ShouldReturnBadRequest() {
        // Arrange
        Long userId = 1L;
        Long roomId = 101L;
        String errorMessage = "Room not available";
        when(bookingService.createBooking(any(Booking.class), eq(userId), eq(roomId)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = bookingController.createBooking(testBooking, userId, roomId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(bookingService, times(1)).createBooking(testBooking, userId, roomId);
    }

    @Test
    void getUserBookings_WhenBookingsExist_ShouldReturnBookings() {
        // Arrange
        Long userId = 1L;
        List<Booking> expectedBookings = Arrays.asList(testBooking, testBooking2);
        when(bookingService.getUserBookings(userId)).thenReturn(expectedBookings);

        // Act
        ResponseEntity<List<Booking>> response = bookingController.getUserBookings(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBookings, response.getBody());
        verify(bookingService, times(1)).getUserBookings(userId);
    }

    @Test
    void getUserBookings_WhenNoBookings_ShouldReturnEmptyList() {
        // Arrange
        Long userId = 99L;
        when(bookingService.getUserBookings(userId)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Booking>> response = bookingController.getUserBookings(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(bookingService, times(1)).getUserBookings(userId);
    }

    @Test
    void getAllBookings_WhenBookingsExist_ShouldReturnAllBookings() {
        // Arrange
        List<Booking> expectedBookings = Arrays.asList(testBooking, testBooking2);
        when(bookingService.getAllBookings()).thenReturn(expectedBookings);

        // Act
        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBookings, response.getBody());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void getAllBookings_WhenNoBookings_ShouldReturnEmptyList() {
        // Arrange
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void getBookingById_WhenBookingExists_ShouldReturnBooking() {
        // Arrange
        Long bookingId = 1L;
        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(testBooking));

        // Act
        ResponseEntity<Booking> response = bookingController.getBookingById(bookingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testBooking.getId(), response.getBody().getId());
        verify(bookingService, times(1)).getBookingById(bookingId);
    }

    @Test
    void getBookingById_WhenBookingNotExists_ShouldReturnNotFound() {
        // Arrange
        Long bookingId = 99L;
        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Booking> response = bookingController.getBookingById(bookingId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).getBookingById(bookingId);
    }

    @Test
    void getBookingByNumber_WhenBookingExists_ShouldReturnBooking() {
        // Arrange
        String bookingNumber = "BK001";
        when(bookingService.getBookingByNumber(bookingNumber)).thenReturn(Optional.of(testBooking));

        // Act
        ResponseEntity<Booking> response = bookingController.getBookingByNumber(bookingNumber);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookingNumber, response.getBody().getBookingNumber());
        verify(bookingService, times(1)).getBookingByNumber(bookingNumber);
    }

    @Test
    void getBookingByNumber_WhenBookingNotExists_ShouldReturnNotFound() {
        // Arrange
        String bookingNumber = "INVALID";
        when(bookingService.getBookingByNumber(bookingNumber)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Booking> response = bookingController.getBookingByNumber(bookingNumber);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).getBookingByNumber(bookingNumber);
    }

    @Test
    void cancelBooking_ValidBooking_ShouldReturnCancelledBooking() {
        // Arrange
        Long bookingId = 1L;
        Booking cancelledBooking = new Booking();
        cancelledBooking.setId(bookingId);
        cancelledBooking.setStatus("CANCELLED");
        
        when(bookingService.cancelBooking(bookingId)).thenReturn(cancelledBooking);

        // Act
        ResponseEntity<Booking> response = bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CANCELLED", response.getBody().getStatus());
        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    @Test
    void cancelBooking_BookingNotFound_ShouldReturnNotFound() {
        // Arrange
        Long bookingId = 99L;
        when(bookingService.cancelBooking(bookingId)).thenThrow(new RuntimeException("Booking not found"));

        // Act
        ResponseEntity<Booking> response = bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    @Test
    void updateBookingStatus_ValidRequest_ShouldReturnUpdatedBooking() {
        // Arrange
        Long bookingId = 1L;
        String newStatus = "CONFIRMED";
        Booking updatedBooking = new Booking();
        updatedBooking.setId(bookingId);
        updatedBooking.setStatus(newStatus);
        
        when(bookingService.updateBookingStatus(bookingId, newStatus)).thenReturn(updatedBooking);

        // Act
        ResponseEntity<Booking> response = bookingController.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newStatus, response.getBody().getStatus());
        verify(bookingService, times(1)).updateBookingStatus(bookingId, newStatus);
    }

    @Test
    void updateBookingStatus_BookingNotFound_ShouldReturnNotFound() {
        // Arrange
        Long bookingId = 99L;
        String newStatus = "CONFIRMED";
        when(bookingService.updateBookingStatus(bookingId, newStatus))
                .thenThrow(new RuntimeException("Booking not found"));

        // Act
        ResponseEntity<Booking> response = bookingController.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).updateBookingStatus(bookingId, newStatus);
    }

    @Test
    void updateBookingStatus_InvalidStatus_ShouldReturnNotFound() {
        // Arrange
        Long bookingId = 1L;
        String invalidStatus = "INVALID_STATUS";
        when(bookingService.updateBookingStatus(bookingId, invalidStatus))
                .thenThrow(new RuntimeException("Invalid status"));

        // Act
        ResponseEntity<Booking> response = bookingController.updateBookingStatus(bookingId, invalidStatus);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).updateBookingStatus(bookingId, invalidStatus);
    }
}