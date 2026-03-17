package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "paxId")
public class Passager
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long paxId;
    private String numeropiece;
    private String civilite;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateNaiss;
    private String phone;
    private String mail;
    private String adresse;

    //@ManyToOne
    //@JoinColumn(name="aolId")
    //private AchatOnLine achatonline;

    @OneToMany(mappedBy = "passager", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<Billet> billets;

    @ManyToOne
    @JoinColumn(name="natId")
    private Nationalite nationalite;

    @ManyToOne
    @JoinColumn(name="tpieceId")
    private TypePiece typePiece;


}
