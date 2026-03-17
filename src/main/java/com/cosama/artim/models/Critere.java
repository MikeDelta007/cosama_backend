package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "crtId")
public class Critere {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long crtId;
    @Column(length = 45)
    private String crt_nom;

    @ManyToMany(mappedBy = "criteres")
    @JsonIgnore
    private List<Categorie> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "criteres")
    @JsonIgnore
    private List<GroupeCritere> groupeCriteres = new ArrayList<>();

    @ManyToMany(mappedBy = "criteres")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Billet> billets = new ArrayList<>();
}
