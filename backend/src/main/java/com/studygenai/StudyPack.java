package com.studygenai;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="study_packs")
public class StudyPack {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private String topic; private String type; private String provider;
  @Column(columnDefinition="TEXT") private String content; private Instant createdAt;
  protected StudyPack() {}
  public StudyPack(String topic,String type,String provider,String content){this.topic=topic;this.type=type;this.provider=provider;this.content=content;this.createdAt=Instant.now();}
}
