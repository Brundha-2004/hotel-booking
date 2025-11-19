package com.example.hotel_booking_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hotel_booking_system.entities.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Find hotels by city (case-insensitive)
    List<Hotel> findByCityContainingIgnoreCase(String city);

    // Find hotels by name (case-insensitive)
    List<Hotel> findByNameContainingIgnoreCase(String name);

    // Find hotels by rating range
    List<Hotel> findByRatingBetween(Double minRating, Double maxRating);
    
    // Find hotels with rating greater than or equal to
    List<Hotel> findByRatingGreaterThanEqual(Double minRating);
    
    // Find hotels with rating less than or equal to
    List<Hotel> findByRatingLessThanEqual(Double maxRating);

    // Check if hotel exists by name and address
    boolean existsByNameAndAddress(String name, String address);

    // Custom query to find hotels with available rooms
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.rooms r WHERE r.available = true")
    List<Hotel> findHotelsWithAvailableRooms();

    // Get distinct cities
    @Query("SELECT DISTINCT h.city FROM Hotel h ORDER BY h.city")
    List<String> findDistinctCities();

    // Find hotel by name exact match
    Optional<Hotel> findByName(String name);

    // ADD THESE MISSING METHODS:
    
    // Combined search for city and name
    List<Hotel> findByCityContainingIgnoreCaseAndNameContainingIgnoreCase(String city, String name);
    
    // Find by city (exact match)
    List<Hotel> findByCity(String city);
    
    // Find by city containing (for backward compatibility)
    List<Hotel> findByCityContaining(String city);
    
    // Find by name containing (for backward compatibility)
    List<Hotel> findByNameContaining(String name);
    
    // Custom search query
    @Query("SELECT h FROM Hotel h WHERE " +
           "(:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Hotel> searchHotels(@Param("city") String city, @Param("name") String name);
}