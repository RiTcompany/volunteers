package org.example.pojo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.EGender;

import java.util.Date;

@Entity
@Table(name = "volonteer")
@Getter @Setter
@NoArgsConstructor
public class Volonteer {
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

    @Column(name = "tg_link")
    private String tgLink;

    @Column(name = "education_institution")
    private String educationInstitution;

    @Column(name = "educational_specialty")
    private String educationalSpecialty;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "complete_step")
    private Integer completeStep;

    public Volonteer(Long chatId) {
        this.chatId = chatId;
    }
}
