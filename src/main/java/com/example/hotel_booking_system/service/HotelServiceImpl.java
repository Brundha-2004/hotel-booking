package com.example.hotel_booking_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel_booking_system.entities.Hotel;
import com.example.hotel_booking_system.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public List<Hotel> searchHotels(String city, String name) {
        return hotelRepository.searchHotels(city, name);
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepository.findByCity(city);
    }

    @Override
    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        if (hotelDetails.getName() != null) {
            existingHotel.setName(hotelDetails.getName());
        }
        if (hotelDetails.getCity() != null) {
            existingHotel.setCity(hotelDetails.getCity());
        }
        if (hotelDetails.getAddress() != null) {
            existingHotel.setAddress(hotelDetails.getAddress());
        }
        if (hotelDetails.getDescription() != null) {
            existingHotel.setDescription(hotelDetails.getDescription());
        }
        if (hotelDetails.getRating() != null) {
            existingHotel.setRating(hotelDetails.getRating());
        }
        if (hotelDetails.getImageUrl() != null) {
            existingHotel.setImageUrl(hotelDetails.getImageUrl());
        }
        if (hotelDetails.getPricePerNight() != null) {
            existingHotel.setPricePerNight(hotelDetails.getPricePerNight());
        }
        if (hotelDetails.getAmenities() != null) {
            existingHotel.setAmenities(hotelDetails.getAmenities());
        }
        
        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel( Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new RuntimeException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }
}