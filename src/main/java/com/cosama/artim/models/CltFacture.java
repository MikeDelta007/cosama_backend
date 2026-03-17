package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cltfctId")
public class CltFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cltfctId;
    private String libelle;
    private String firstname;
    private String lastname;
    private String telephone;
    private String adresse;
    private String factCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateFacturation;
    private Boolean etatFacturation = false;
    private Boolean estEmis = false;
    private String usrFacture;
    private String observation;
    private float acompteFacture;
    private float montantFacture;
    private float tvaFacture;
    @Column(name = "montant_verse")
    private float montantVerse;
    @Column(name = "reliquat")
    private float reliquat;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateEcheance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime datePaieFacture;
    private String usrPaieFacture;
    private String refPaieFacture;

    @ManyToOne
    @JoinColumn(name="typergltId")
    private CltTypeReglement cltTypeReglement;

    @ManyToOne
    @JoinColumn(name="cltcmptId")
    private ClientEnCompte clientEnCompte;

    @OneToMany(mappedBy = "cltFacture", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private List<CltDetailsFacture> cltDetailsFactures;

}
