package com.example.mysqlfiles.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Builder
@Entity(name = "users")
public class User implements Serializable {

    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 1L;


    @JsonIgnore
    @Transient
    private String filePath=null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Column(name = "password", nullable = false)
    private String password;
}