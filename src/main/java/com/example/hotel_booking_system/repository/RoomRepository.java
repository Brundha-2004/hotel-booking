package com.example.hotel_booking_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hotel_booking_system.entities.Hotel;
import com.example.hotel_booking_system.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    Optional<Room> findByRoomNumberAndHotel(String roomNumber, Hotel hotel);
    
    @Query("SELECT r FROM Room r WHERE r.hotel.city = :city AND r.available = true " +
           "AND r.id NOT IN (" +
           "SELECT b.room.id FROM Booking b WHERE " +
           "b.status != 'CANCELLED' AND " +
           "((b.checkInDate BETWEEN :checkIn AND :checkOut) OR " +
           "(b.checkOutDate BETWEEN :checkIn AND :checkOut) OR " +
           "(b.checkInDate <= :checkIn AND b.checkOutDate >= :checkOut)))")
    List<Room> findAvailableRoomsByCityAndDates(
            @Param("city") String city,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);
}