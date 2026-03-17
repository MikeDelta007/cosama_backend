package com.cosama.artim.dto;

import com.cosama.artim.models.Agence;
import com.cosama.artim.models.Profil;
import com.cosama.artim.models.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private long id;
    private String firstname;
    private String lastname;
    private String login;
    private boolean etat;

    @ManyToOne
    @JoinColumn(name="agcId")
    private Agence agence;

    @ManyToOne
    @JoinColumn(name="prflId")
    private ProfilDTO profil;
}
