package com.treviratech.clientsapibackend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 12, message = "The name must be greater than 2 letters and smaller than 12")
    @NotEmpty(message = "The name field must not be empty")
    private String name;
    @Column(name = "last_name")
    @NotEmpty(message = "The last name field must not be empty")
    @Size(min = 2, max = 12, message = "The last name must be greater than 2 and smaller than 12")
    private String lastName;
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "The email field must not be empty")
    @Email(message = "The email field must be a valid email")
    private String email;
    @NotNull(message = "The created at field must not be empty")
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    //With fetch.Lazy it is generated a proxy to the region and some additional attributes, thats why we want to exclude
    // them from the JSON response
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "The region field must not be empty")
    private Region region;
    public Client() {
    }

    public Client(String name, String lastName, String email, Date createdAt, String photo) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    private static final long serialVersionUID = 1L;
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
