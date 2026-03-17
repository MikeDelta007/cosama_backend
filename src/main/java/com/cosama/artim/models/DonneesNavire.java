package com.cosama.artim.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dnId")
public class DonneesNavire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long dnId;


    @ManyToOne
    @JoinColumn(name="voyId")
    private Voyage voyage;

}
