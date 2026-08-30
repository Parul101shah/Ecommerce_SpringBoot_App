package com.ecommerce.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async // runs in background thread
    public void sendOrderConfirmation(String to, Long orderId, Double totalAmount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Order Confirmed - #" + orderId);
            message.setText(
                    "Hi,\n\n"
                            + "Your order has been placed successfully!\n\n"
                            + "Order ID: #" + orderId + "\n"
                            + "Total Amount: ₹" + String.format("%.2f", totalAmount) + "\n\n"
                            + "We will notify you when your order is shipped.\n\n"
                            + "Thank you for shopping with us!\n"
                            + "- EcomStore"
            );
            mailSender.send(message);
            logger.info("Order confirmation email sent to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email to {}: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendOrderStatusUpdate(String to, Long orderId, String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Order #" + orderId + " - " + status);
            message.setText(
                    "Hi,\n\n"
                            + "Your order #" + orderId + " has been updated.\n\n"
                            + "New Status: " + status + "\n\n"
                            + statusMessage(status) + "\n\n"
                            + "Thank you!\n"
                            + "- EcomStore"
            );
            mailSender.send(message);
            logger.info("Order status email sent to {} for order #{}", to, orderId);
        } catch (Exception e) {
            logger.error("Failed to send status email to {}: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to EcomStore!");
            message.setText(
                    "Hi " + username + ",\n\n"
                            + "Welcome to EcomStore! Your account has been created successfully.\n\n"
                            + "Start shopping now!\n\n"
                            + "- EcomStore"
            );
            mailSender.send(message);
            logger.info("Welcome email sent to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", to, e.getMessage());
        }
    }

    private String statusMessage(String status) {
        return switch (status) {
            case "ACCEPTED" -> "Your payment has been confirmed.";
            case "PROCESSING" -> "Your order is being prepared.";
            case "SHIPPED" -> "Your order is on the way!";
            case "DELIVERED" -> "Your order has been delivered. Enjoy!";
            case "CANCELLED" -> "Your order has been cancelled. Refund will be processed shortly.";
            case "REFUNDED" -> "Your refund has been processed.";
            default -> "Your order status has been updated.";
        };
    }
}
