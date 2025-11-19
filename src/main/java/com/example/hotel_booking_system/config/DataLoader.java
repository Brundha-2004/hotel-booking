package com.example.hotel_booking_system.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.hotel_booking_system.entities.Hotel;
import com.example.hotel_booking_system.entities.Room;
import com.example.hotel_booking_system.entities.User;
import com.example.hotel_booking_system.repository.HotelRepository;
import com.example.hotel_booking_system.repository.RoomRepository;
import com.example.hotel_booking_system.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HotelRepository hotelRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        User admin = new User();
        admin.setEmail("admin@hotel.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole("ADMIN");
        userRepository.save(admin);
        
        // Create regular user
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole("USER");
        userRepository.save(user);
        
        // Create sample hotels
        Hotel hotel1 = new Hotel();
        hotel1.setName("Grand Plaza Hotel");
        hotel1.setCity("New York");
        hotel1.setAddress("123 Broadway, New York, NY");
        hotel1.setDescription("Luxury hotel in the heart of Manhattan");
        hotel1.setRating(4.5);
        hotel1.setImageUrl("/images/hotel1.jpg");
        hotelRepository.save(hotel1);
        
        Hotel hotel2 = new Hotel();
        hotel2.setName("Seaside Resort");
        hotel2.setCity("Miami");
        hotel2.setAddress("456 Ocean Drive, Miami, FL");
        hotel2.setDescription("Beautiful beachfront resort with stunning views");
        hotel2.setRating(4.2);
        hotel2.setImageUrl("/images/hotel2.jpg");
        hotelRepository.save(hotel2);
        
        // Create rooms for hotel1
        Room room1 = new Room();
        room1.setRoomNumber("101");
        room1.setType("SINGLE");
        room1.setPricePerNight(new BigDecimal("99.99"));
        room1.setMaxOccupancy(1);
        room1.setDescription("Cozy single room with city view");
        room1.setAmenities("WiFi, TV, AC");
        room1.setHotel(hotel1);
        roomRepository.save(room1);
        
        Room room2 = new Room();
        room2.setRoomNumber("201");
        room2.setType("DOUBLE");
        room2.setPricePerNight(new BigDecimal("149.99"));
        room2.setMaxOccupancy(2);
        room2.setDescription("Spacious double room with king bed");
        room2.setAmenities("WiFi, TV, AC, Mini Bar");
        room2.setHotel(hotel1);
        roomRepository.save(room2);
        
        Room room3 = new Room();
        room3.setRoomNumber("301");
        room3.setType("SUITE");
        room3.setPricePerNight(new BigDecimal("249.99"));
        room3.setMaxOccupancy(3);
        room3.setDescription("Luxury suite with living area and balcony");
        room3.setAmenities("WiFi, TV, AC, Mini Bar, Jacuzzi");
        room3.setHotel(hotel1);
        roomRepository.save(room3);
        
        // Create rooms for hotel2
        Room room4 = new Room();
        room4.setRoomNumber("101");
        room4.setType("DOUBLE");
        room4.setPricePerNight(new BigDecimal("129.99"));
        room4.setMaxOccupancy(2);
        room4.setDescription("Beach view double room");
        room4.setAmenities("WiFi, TV, AC, Ocean View");
        room4.setHotel(hotel2);
        roomRepository.save(room4);
        
        System.out.println("Sample data loaded successfully!");
        System.out.println("Admin: admin@hotel.com / admin123");
        System.out.println("User: user@example.com / user123");
    }
}