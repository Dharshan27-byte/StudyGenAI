package com.studygenai;
import jakarta.persistence.*;
@Entity @Table(name="app_users")
public class AppUser {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true) private String email;
  @Column(nullable=false) private String name;
  @Column(nullable=false) private String passwordHash;
  protected AppUser() {}
  public AppUser(String name,String email,String passwordHash){this.name=name;this.email=email;this.passwordHash=passwordHash;}
  public String getName(){return name;} public String getPasswordHash(){return passwordHash;}
}
