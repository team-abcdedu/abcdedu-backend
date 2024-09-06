package com.abcdedu_backend.contact.entity;

import com.abcdedu_backend.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contacts")
public class Contact extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String userName;
    private String phoneNumber;
    private String email;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

}
