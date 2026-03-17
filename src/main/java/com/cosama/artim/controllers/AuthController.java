package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.Fret;
import com.cosama.artim.models.Profil;
import com.cosama.artim.models.User;
import com.cosama.artim.services.AuthenticationService;
import com.cosama.artim.services.ParametrageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-In")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInDTO signInDTO) throws Exception {
        return ResponseEntity.ok(authenticationService.signin(signInDTO));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refresh_token(@RequestBody RefreshTokenDTO refreshTokenDTO) throws Exception {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenDTO));
    }

    //@PostMapping("/read")
    //public ResponseEntity<String> readRfid(@RequestBody Map<String, String> request) {
    //    String cardId = request.get("cardId");
    //    if (cardId != null && !cardId.isEmpty()) {
    //        System.out.println("Carte scannée : " + cardId);  // Log de l'ID de la carte
    //        return ResponseEntity.ok("ID de la carte reçu : " + cardId);
    //    } else {
    //        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de la carte manquant");
    //    }
    //}

    // Cette méthode sera appelée pour envoyer l'ID de la carte à tous les clients abonnés

}
