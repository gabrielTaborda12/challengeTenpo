package com.tenpo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users" , schema = "public")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    private String name;

    @Column(name = "lastname")
    private String lastName;

    private String email;

    @Column(name = "pw")
    private String password;

    @Column(name = "status")
    private String status;

}
