package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "logSoldeId")
public class LogsSoldes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long logSoldeId;
    private int depot;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateSolde;

    @ManyToOne
    @JoinColumn(name="cltcmptId")
    private ClientEnCompte clientEnCompte;
}
