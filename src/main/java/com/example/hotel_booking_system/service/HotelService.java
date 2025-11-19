package com.example.hotel_booking_system.service;

import java.util.List;
import java.util.Optional;

import com.example.hotel_booking_system.entities.Hotel;

public interface HotelService {
    Hotel createHotel(Hotel hotel);
    List<Hotel> getAllHotels();
    Optional<Hotel> getHotelById(Long id);
    List<Hotel> searchHotels(String city, String name);
    List<Hotel> getHotelsByCity(String city);
    Hotel updateHotel(Long id, Hotel hotel);
    void deleteHotel(Long id);
}