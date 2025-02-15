package com.sayaha.vani.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String location;
    private String description;
    private String mediaUrl; // Can store URL if media is uploaded elsewhere
    private String status; // e.g., "Submitted," "In Progress," "Resolved"
    private String whatsappNumber;
}
