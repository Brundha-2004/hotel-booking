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
        // Create admin user if not exists
        if (userRepository.findByEmail("admin@hotel.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@hotel.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Admin user created successfully");
        } else {
            System.out.println("ℹ️ Admin user already exists, skipping creation");
        }
        
        // Create regular user if not exists
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole("USER");
            userRepository.save(user);
            System.out.println("✅ Regular user created successfully");
        } else {
            System.out.println("ℹ️ Regular user already exists, skipping creation");
        }
        
        // Create sample hotels if not exists
        if (hotelRepository.findByName("Grand Plaza Hotel").isEmpty()) {
            Hotel hotel1 = new Hotel();
            hotel1.setName("Grand Plaza Hotel");
            hotel1.setCity("New York");
            hotel1.setAddress("123 Broadway, New York, NY");
            hotel1.setDescription("Luxury hotel in the heart of Manhattan");
            hotel1.setRating(4.5);
            hotel1.setImageUrl("/images/hotel1.jpg");
            hotelRepository.save(hotel1);
            System.out.println("✅ Grand Plaza Hotel created");
        } else {
            System.out.println("ℹ️ Grand Plaza Hotel already exists");
        }
        
        if (hotelRepository.findByName("Seaside Resort").isEmpty()) {
            Hotel hotel2 = new Hotel();
            hotel2.setName("Seaside Resort");
            hotel2.setCity("Miami");
            hotel2.setAddress("456 Ocean Drive, Miami, FL");
            hotel2.setDescription("Beautiful beachfront resort with stunning views");
            hotel2.setRating(4.2);
            hotel2.setImageUrl("/images/hotel2.jpg");
            hotelRepository.save(hotel2);
            System.out.println("✅ Seaside Resort created");
        } else {
            System.out.println("ℹ️ Seaside Resort already exists");
        }
        
        // Get hotels for room creation
        Hotel grandPlaza = hotelRepository.findByName("Grand Plaza Hotel")
            .orElseThrow(() -> new RuntimeException("Grand Plaza Hotel not found"));
        Hotel seasideResort = hotelRepository.findByName("Seaside Resort")
            .orElseThrow(() -> new RuntimeException("Seaside Resort not found"));
        
        // Create rooms for hotel1 if not exists
        if (roomRepository.findByRoomNumberAndHotel("101", grandPlaza).isEmpty()) {
            Room room1 = new Room();
            room1.setRoomNumber("101");
            room1.setType("SINGLE");
            room1.setPricePerNight(new BigDecimal("99.99"));
            room1.setMaxOccupancy(1);
            room1.setDescription("Cozy single room with city view");
            room1.setAmenities("WiFi, TV, AC");
            room1.setHotel(grandPlaza);
            roomRepository.save(room1);
            System.out.println("✅ Room 101 created for Grand Plaza Hotel");
        }
        
        if (roomRepository.findByRoomNumberAndHotel("201", grandPlaza).isEmpty()) {
            Room room2 = new Room();
            room2.setRoomNumber("201");
            room2.setType("DOUBLE");
            room2.setPricePerNight(new BigDecimal("149.99"));
            room2.setMaxOccupancy(2);
            room2.setDescription("Spacious double room with king bed");
            room2.setAmenities("WiFi, TV, AC, Mini Bar");
            room2.setHotel(grandPlaza);
            roomRepository.save(room2);
            System.out.println("✅ Room 201 created for Grand Plaza Hotel");
        }
        
        if (roomRepository.findByRoomNumberAndHotel("301", grandPlaza).isEmpty()) {
            Room room3 = new Room();
            room3.setRoomNumber("301");
            room3.setType("SUITE");
            room3.setPricePerNight(new BigDecimal("249.99"));
            room3.setMaxOccupancy(3);
            room3.setDescription("Luxury suite with living area and balcony");
            room3.setAmenities("WiFi, TV, AC, Mini Bar, Jacuzzi");
            room3.setHotel(grandPlaza);
            roomRepository.save(room3);
            System.out.println("✅ Room 301 created for Grand Plaza Hotel");
        }
        
        // Create rooms for hotel2 if not exists
        if (roomRepository.findByRoomNumberAndHotel("101", seasideResort).isEmpty()) {
            Room room4 = new Room();
            room4.setRoomNumber("101");
            room4.setType("DOUBLE");
            room4.setPricePerNight(new BigDecimal("129.99"));
            room4.setMaxOccupancy(2);
            room4.setDescription("Beach view double room");
            room4.setAmenities("WiFi, TV, AC, Ocean View");
            room4.setHotel(seasideResort);
            roomRepository.save(room4);
            System.out.println("✅ Room 101 created for Seaside Resort");
        }
        
        System.out.println("🎉 Sample data loading completed!");
        System.out.println("Admin credentials: admin@hotel.com / admin123");
        System.out.println("User credentials: user@example.com / user123");
    }
}