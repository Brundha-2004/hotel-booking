package com.example.hotel_booking_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel_booking_system.entities.Room;
import com.example.hotel_booking_system.repository.RoomRepository;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    public Optional<Room> getRoomById(Long id) {  // Removed @NonNull
        return roomRepository.findById(id);
    }
    
    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
    
    public List<Room> findAvailableRoomsByCityAndDates(String city, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRoomsByCityAndDates(city, checkIn, checkOut);
    }
    
    public Room updateRoom(Long id, Room roomDetails) {  // Removed @NonNull
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setType(roomDetails.getType());
        room.setPricePerNight(roomDetails.getPricePerNight());
        room.setMaxOccupancy(roomDetails.getMaxOccupancy());
        room.setDescription(roomDetails.getDescription());
        room.setAmenities(roomDetails.getAmenities());
        room.setAvailable(roomDetails.getAvailable());
        
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long id) {  // Removed @NonNull
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }
        roomRepository.deleteById(id);
    }
    
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {  // Removed @NonNull
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return room.getAvailable();
    }
}