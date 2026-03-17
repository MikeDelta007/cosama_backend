package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "agcId")
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long agcId;
    private String agcNom;
    @Column(length = 10)
    private String sigle;
    @Column(length = 10)
    private String codeAgc;
    @Column(length = 20)
    private String agcTel;
    @Column(length = 20)
    private String agcFax;
    @Column(length = 150)
    private String agcResp;
    @Column(length = 150)
    private String agcMail;
    private int numFret;
    private int numFretCli;
    private int gmkLastBilid;
    private int gmkLastClient;
    private int gmkLastFretid;
    private int gmkLastLignefret;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateImport;

    @ManyToOne
    @JoinColumn(name="vilId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Ville ville;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<Bateau> bateaus;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<User> users;

    public Agence(Long l, String agcNom, String sigle, String codeAgc, String agcTel, String agcFax, String aggcResp, String agcMail, int i, int i1, int i2, int i3, int i4, int i5, Date dImp, Long i6) {
    }
}
