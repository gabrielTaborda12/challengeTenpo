package com.tenpo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "histories" , schema = "public")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    @Column(name = "id")
    private Long id;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "https")
    private String https;

    @Column(name = "message")
    private String message;

    @Column(name = "consumer")
    private String consumer;

    @Column(name = "dateconsumer")
    private Date dateConsumer;
}
