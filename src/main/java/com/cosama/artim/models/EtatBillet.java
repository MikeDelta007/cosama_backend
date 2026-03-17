package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ebId")
public class EtatBillet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ebId;
    @Column(length = 50)
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bilTime;
    @Column(length = 100)
    private String icon;
    @Column(length = 50)
    private String color;

    @ManyToOne
    @JoinColumn(name="bilId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    private Billet billet;
}
