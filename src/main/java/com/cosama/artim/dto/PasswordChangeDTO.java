package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PasswordChangeDTO {

    private String oldPassword; // ancien mot de passe
    private String newPassword; // nouveau mot de passe
}