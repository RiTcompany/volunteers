package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EClothingSize;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;

import java.util.Date;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private Date birthday;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String phone;

    @Column(name = "tg_link", nullable = false)
    private String tgLink;

    @Column(name = "education_status")
    @Enumerated(EnumType.STRING)
    private EEducationStatus educationStatus;

    @Column(name = "education_institution")
    private String educationInstitution;

    @Column(name = "educational_specialty")
    private String educationalSpecialty;

    private String vk;

    @Column(name = "clothing_size")
    @Enumerated(EnumType.STRING)
    private EClothingSize clothingSize;

    private String reason;

    private String experience;

    @Column(name = "photo_path")
    private String photoPath;

    private String email;

    @Column(name = "volunteer_id")
    private String volunteerId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;
}