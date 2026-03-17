package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tplcId")
public class TypePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tplcId;
    private String tplcCode;
    private String tplcNom;
    private float tplcPrix;
    private String batTplcName;
    @Lob
    private byte[] tplcPhoto;

    @OneToMany(mappedBy = "typePlace", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Place> places;

    @OneToMany(mappedBy = "typePlace", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    //@JsonIgnore
    @JsonBackReference
    private List<Categorie> categories;

    @OneToMany(mappedBy = "typePlace", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Billet> billets;


}
