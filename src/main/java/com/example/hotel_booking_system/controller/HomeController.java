package com.example.hotel_booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index.html"; // Serve hotel.html as the main page
    }
    
    @GetMapping("/hotel")
    public String hotel() {
        return "hotel.html";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }
    
    @GetMapping("/api/status")
    @org.springframework.web.bind.annotation.ResponseBody
    public String status() {
        return "Hotel Booking System API is working!";
    }
    
    @GetMapping("/api/info")
    @org.springframework.web.bind.annotation.ResponseBody
    public String info() {
        return "Hotel Booking System v1.0 - Ready for bookings!";
    }
}