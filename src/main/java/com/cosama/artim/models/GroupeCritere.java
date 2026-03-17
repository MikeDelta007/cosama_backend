package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "grpcrtId")
public class GroupeCritere {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long grpcrtId;
    @Column(length = 100)
    private String grpcrtNom;
    private int onLine;

    @ManyToMany
    @JoinTable(
            name = "groupecritere_critere",
            joinColumns = @JoinColumn(name = "grpcrtId"),
            inverseJoinColumns = @JoinColumn(name = "crtId")
    )
    private List<Critere> criteres = new ArrayList<>();

    public GroupeCritere(GroupeCritere groupeCritere)
    {

    }
}
