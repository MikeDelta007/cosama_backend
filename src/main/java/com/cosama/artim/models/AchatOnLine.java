package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aolId")
public class AchatOnLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long aolId;
    private long batId;
    private boolean allerSimple;
    private boolean allerRetour;
    private String codeAchat;
    private int voyDepart;
    private int voyDestination1;
    private int voyRetour;
    private int voyDestination2;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDateDpt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate voyDateRet;

    private int numberPassagers;
    private boolean ifDataDepEqDataRet;
    private Double coutAller;
    private Double coutRetour;

    //@OneToMany(mappedBy = "achatonline", cascade = CascadeType.ALL)
    //@BatchSize(size = 10)
    //@JsonProperty(access = JsonProperty.Access.READ_WRITE)
    //private List<Passager> passagers;
}
