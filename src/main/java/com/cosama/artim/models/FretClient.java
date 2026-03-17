package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fretCltId")
public class FretClient
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long fretCltId;
    private String raisonSociale;
    private boolean expEqDest;
    private String firstname;
    private String lastname;
    private String numeroPiece;
    private String telephone;
    private String email;

    @ManyToOne
    @JoinColumn(name="tpieceId")
    @JsonIgnore
    private TypePiece typePiece;

    @OneToMany(mappedBy = "fretClient", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Fret> frets;
}
