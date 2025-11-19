package com.example.hotel_booking_system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel_booking_system.entities.Payment;
import com.example.hotel_booking_system.repository.PaymentRepository;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public Optional<Payment> getPaymentById(Long id) {  // Removed @NonNull
        return paymentRepository.findById(id);
    }
    
    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
    
    public Payment updatePaymentStatus(Long paymentId, String status, String transactionId) {  // Removed @NonNull
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        payment.setStatus(status);
        if (transactionId != null) {
            payment.setTransactionId(transactionId);
        }
        if ("SUCCESS".equals(status)) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment processPayment(Long paymentId) {  // Removed @NonNull
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        payment.setStatus("SUCCESS");
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now());
        
        return paymentRepository.save(payment);
    }
}