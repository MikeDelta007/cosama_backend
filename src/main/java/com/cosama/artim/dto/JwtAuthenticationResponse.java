package com.cosama.artim.dto;
import com.cosama.artim.models.Agence;
import com.cosama.artim.models.Profil;
import com.cosama.artim.models.User;
import lombok.Data;
@Data
public class JwtAuthenticationResponse
{
    private String token;
    private String refreshToken;
    private UserDTO user;
}
