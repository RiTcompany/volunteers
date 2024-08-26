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
@Getter
@Setter
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sender;

    private String recipient;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "approval_control")
    private boolean approvalControl;

    @Column(name = "center_id")
    private Long centerId;

    @Column(name = "district_team_id")
    private Long districtTeamId;

    @Column(name = "file_path")
    private String filePath;
}
