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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "uniteId")
public class Unite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long uniteId;
    @Column(length = 100)
    private String uniteNom;
    @Column(length = 50)
    private String uniteCode;

    @OneToMany(mappedBy = "unite", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonIgnore
    private List<TypeBagage> typeBagages;

    @ManyToOne
    @JoinColumn(name="volId")
    private Volume volume;

}
