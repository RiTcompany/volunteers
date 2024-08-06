package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "parent")
@Getter
@Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private Date birthday;

    @Column(name = "register_place")
    private String registerPlace;

    @Column(name = "child_full_name")
    private String childFullName;

    @Column(name = "child_birthday")
    private Date childBirthday;

    @Column(name = "child_register_place")
    private String childRegisterPlace;

    @Column(name = "chat_id")
    private Long chatId;
}
