package com.cosama.artim.services.impl;

import com.cosama.artim.config.JwtAuthenticationFilter;
import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.AgenceRepository;
import com.cosama.artim.repositories.ProfilRepository;
import com.cosama.artim.repositories.UserRepository;
import com.cosama.artim.services.AuthenticationService;
import com.cosama.artim.services.JWTService;
import com.cosama.artim.services.VenteBilletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    private final AgenceRepository agenceRepository;
    @Autowired
    private final ProfilRepository profilRepository;

    private static final Logger logger = LoggerFactory.getLogger(VenteBilletService.class);

    public UtilisateurDTO getCurrentUser(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return mapToDto(user);
    }

    public UtilisateurDTO updateCurrentUser(long id, UtilisateurDTO dto)
    {
        System.out.println(id);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setLogin(dto.getUsr_login());
        user.setFirstname(dto.getUsr_firstname());
        user.setLastname(dto.getUsr_lastname());
        user.setUsrDesc(dto.getUsr_desc());

        // ⭐ si mot de passe fourni et non vide
        if (dto.getUsr_password() != null && !dto.getUsr_password().isBlank()) {

            // ⚠️ vérifier que ce n’est pas déjà un hash
            if (!dto.getUsr_password().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(dto.getUsr_password()));
            }
        }

        userRepository.save(user);

        return mapToDto(user);
    }


    public User signup(SignUpDTO signUpDTO)
    {
        logger.info("TEST : " + signUpDTO.getUsr_password());
        Agence agence = this.agenceRepository.findById(signUpDTO.getAgc_id()).orElse(null);
        Profil profil = this.profilRepository.findById(signUpDTO.getPrfl_id()).orElse(null);
        User user = new User();
        user.setFirstname(signUpDTO.getUsr_firstname());
        user.setLastname(signUpDTO.getUsr_lastname());
        user.setLogin(signUpDTO.getUsr_login());
        user.setUsrDesc(signUpDTO.getUsr_description());
        user.setPassword(passwordEncoder.encode(signUpDTO.getUsr_password()));
        user.setRole(Role.ADMIN);
        user.setAgence(agence);
        user.setProfil(profil);
        user.setEtat(true);

        return userRepository.save(user);

    }

    public User updateUser(long usrId, UtilisateurDTO userDTO) {
    {
        // Retrieve the existing Profil from the repository
        User existingUser = userRepository.findById(usrId).orElse(null);
        Agence agc = agenceRepository.findById(userDTO.getAgc_id()).orElse(null);
        Profil prf = profilRepository.findById(userDTO.getPrfl_id()).orElse(null);

        if (existingUser == null)
        {
            throw new NotFoundException("User with ID " + usrId + " not found");
        }

        existingUser.setLogin(userDTO.getUsr_login());
        existingUser.setFirstname(userDTO.getUsr_firstname());
        existingUser.setLastname(userDTO.getUsr_lastname());
        existingUser.setPassword(userDTO.getUsr_password());
        existingUser.setAgence(agc);
        existingUser.setProfil(prf);
        return userRepository.save(existingUser);
    }

    }

    public User updateUser(long usrId, SignUpDTO signUpDTO) {
        // Retrieve the existing Utilisateur from the repository
        User existingUser = userRepository.findById(usrId).orElse(null);

        if (existingUser != null) {
            // Retrieve the associated Agence and Profil
            Agence agence = this.agenceRepository.findById(signUpDTO.getAgc_id()).orElse(null);;
            Profil profil = this.profilRepository.findById(signUpDTO.getPrfl_id()).orElse(null);;

            // Update the fields of the existing Utilisateur
            existingUser.setLogin(signUpDTO.getUsr_login());
            existingUser.setPassword(signUpDTO.getUsr_password());
            existingUser.setFirstname(signUpDTO.getUsr_firstname());
            existingUser.setLastname(signUpDTO.getUsr_lastname());
            existingUser.setUsrDesc(signUpDTO.getUsr_description());
            //existingUser.setPassword(passwordEncoder.encode(signUpDTO.getUsr_password()));
            existingUser.setAgence(agence);
            existingUser.setProfil(profil);

            // Save and return the updated Utilisateur entity
            return userRepository.save(existingUser);
        } else {
            // Handle the case where the user is not found
            throw new NotFoundException("Utilisateur with ID " + usrId + " is not found");
        }
    }

    public User updatePassword(long usrId, ResetPasswordDTO resetPasswordDTO)
    {
        // Retrieve the existing Utilisateur from the repository
        User existingUser = userRepository.findById(usrId).orElse(null);

        if (existingUser != null)
        {
            // Update the fields of the existing Utilisateur
            existingUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getUsr_password()));
            // Save and return the updated Utilisateur entity
            return userRepository.save(existingUser);
        }
        else
        {
            // Handle the case where the user is not found
            throw new NotFoundException("Utilisateur with ID " + usrId + " is not found");
        }
    }

    public JwtAuthenticationResponse signin(SignInDTO signInDTO) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        try {
            logger.info("Start");
            logger.info("Login: " + signInDTO.getLogin());
            logger.info("Password: " + (signInDTO.getPassword() != null ? signInDTO.getPassword() : "null"));

            // Authentification de l'utilisateur
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getLogin(), signInDTO.getPassword()));
            logger.info("A");

            // Recherche de l'utilisateur dans la base de données
            var user = userRepository.findByLogin(signInDTO.getLogin()).orElseThrow(
                    () -> new IllegalArgumentException("Invalid login or password"));
            logger.info("B");

            UserDTO usergoToFront = new UserDTO();
            User users = userRepository.findById(user.getId()).orElse(null);

            assert users != null;
            usergoToFront.setId(users.getId());
            usergoToFront.setLogin(users.getLogin());
            usergoToFront.setFirstname(users.getFirstname());
            usergoToFront.setLastname(users.getLastname());
            usergoToFront.setEtat(users.isEtat());

            Profil profils = profilRepository.findById(users.getProfil().getPrflId()).orElse(null);
            Agence agences = agenceRepository.findById(users.getAgence().getAgcId()).orElse(null);

            ProfilDTO profil = convertToDTO1(profils);
            usergoToFront.setAgence(agences);
            usergoToFront.setProfil(profil);


            // Génération du token JWT
            var jwt = jwtService.generateToken(user);
            logger.info("JWT: " + jwt);

            // Génération du refresh token
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            logger.info("Refresh Token: " + refreshToken);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            jwtAuthenticationResponse.setUser(usergoToFront);

            logger.info("Response generated.");
            return jwtAuthenticationResponse;
        } catch (Exception e) {
            logger.error("Error during sign-in process: ", e);
            throw e;  // Rethrow or handle the exception as needed
        }
    }

    public ProfilDTO convertToDTO1(Profil profil) {
        ProfilDTO dto = new ProfilDTO();
        // Conversion des champs simples
        dto.setPrfl_id(profil.getPrflId());
        dto.setPrfl_libelle(profil.getPrflLibelle());
        dto.setActif(profil.isActif());
        dto.setAdd_billet(profil.isAddBillet());
        dto.setAdd_facture(profil.isAddFacture());
        dto.setAdd_fret(profil.isAddFret());
        dto.setAdd_clt_compte(profil.isAddCltCompte());
        dto.setAdd_check_billet(profil.isAddCheckBillet());
        dto.setEdit_billet(profil.isEditBillet());
        dto.setCancel_billet(profil.isCancelBillet());
        dto.setAdd_embarqment(profil.isAddEmbarqment());
        dto.setDo_remboursement(profil.isDoRemboursement());
        dto.setDo_rep_surclassment(profil.isDoRepSurclassment());
        dto.setEdit_fret(profil.isEditFret());
        dto.setCancel_fret(profil.isCancelFret());
        dto.setPaye_fret(profil.isPayeFret());
        dto.setView_stat(profil.isViewStat());
        dto.setView_etat(profil.isViewEtat());
        dto.setAdd_voyage(profil.isAddVoyage());
        dto.setView_voyage(profil.isViewVoyage());
        dto.setEdition(profil.isEdition());
        dto.setEdit_voyage(profil.isEditVoyage());
        dto.setPointer_voyage(profil.isPointerVoyage());
        dto.setPlan_voyage(profil.isPlanVoyage());
        dto.setEdit_param(profil.isEditParam());
        dto.setValide(profil.isValide());
        dto.setRechercher(profil.isRechercher());
        dto.setEdit_clt_compte(profil.isEditCltCompte());
        dto.setDel_clt_compte(profil.isDelCltCompte());
        dto.setEdit_facture(profil.isEditFacture());
        dto.setDel_facture(profil.isDelFacture());
        dto.setAdd_reglement(profil.isAddReglement());
        dto.setEdit_reglement(profil.isEditReglement());
        dto.setDel_reglement(profil.isDelReglement());
        dto.setBloq_places(profil.isBloqPlaces());
        dto.setAdd_passager(profil.isAddPassager());
        dto.setEdit_passager(profil.isEditPassager());
        dto.setDel_passager(profil.isDelPassager());
        dto.setAdd_nav_data(profil.isAddNavData());
        dto.setEdit_nav_data(profil.isEditNavData());
        dto.setCancel_voyage(profil.isCancelVoyage());
        dto.setDel_fret_details(profil.isDelFretDetails());
        dto.setCheck_fret(profil.isCheckFret());
        dto.setCampagne(profil.isCampagne());
        dto.setReclamation(profil.isReclamation());
        return dto;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {

        // Validation de l'entrée
        if (refreshTokenDTO == null) {
            logger.error("RefreshTokenDTO is null");
            throw new IllegalArgumentException("Refresh token request cannot be null");
        }

        String refreshToken = refreshTokenDTO.getToken();
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            logger.error("Refresh token is null or empty");
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }

        logger.info("=== Starting refresh token process ===");
        logger.debug("Refresh token received: {}", maskToken(refreshToken));

        try {
            // 1. Extraire le login du token
            String login;
            try {
                login = jwtService.extractUserName(refreshToken);
                logger.info("Username extracted from token: {}", login);
            } catch (Exception e) {
                logger.error("Failed to extract username from refresh token: {}", e.getMessage());
                throw new RuntimeException("Invalid refresh token format", e);
            }

            // 2. Récupérer l'utilisateur
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> {
                        logger.error("User not found for login: {}", login);
                        return new RuntimeException("User not found for token");
                    });

            logger.info("User found: {}", user.getLogin());

            // 3. Valider le token
            boolean isValid;
            try {
                isValid = jwtService.isTokenValid(refreshToken, user);
            } catch (Exception e) {
                logger.error("Error during token validation: {}", e.getMessage());
                throw new RuntimeException("Token validation failed", e);
            }

            if (!isValid) {
                logger.error("Refresh token is invalid or expired for user: {}", login);
                throw new RuntimeException("Invalid or expired refresh token");
            }

            // 4. Générer un nouveau token d'accès
            logger.info("Refresh token is valid, generating new access token for user: {}", login);

            String newAccessToken;
            try {
                newAccessToken = jwtService.generateToken(user);
            } catch (Exception e) {
                logger.error("Failed to generate new access token: {}", e.getMessage());
                throw new RuntimeException("Access token generation failed", e);
            }

            // 5. Construire la réponse
            JwtAuthenticationResponse response = new JwtAuthenticationResponse();
            response.setToken(newAccessToken);
            response.setRefreshToken(refreshToken); // Optionnel: vous pouvez aussi générer un nouveau refresh token

            logger.info("=== Refresh token process completed successfully for user: {} ===", login);
            logger.debug("New access token generated: {}", maskToken(newAccessToken));

            return response;

        } catch (RuntimeException e) {
            // Log déjà fait dans les cas spécifiques
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during refresh token process: ", e);
            throw new RuntimeException("An unexpected error occurred during token refresh", e);
        }
    }

    /**
     * Masque un token pour les logs (ne montre que les premiers et derniers caractères)
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 10) {
            return "****";
        }
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
    private UtilisateurDTO mapToDto(User user)
    {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setUsr_id(user.getId());
        dto.setUsr_login(user.getLogin());
        dto.setUsr_firstname(user.getFirstname());
        dto.setUsr_lastname(user.getLastname());
        dto.setUsr_password(user.getPassword());
        dto.setUsr_desc(user.getUsrDesc());
        dto.setUsr_etat(user.isEtat());
        dto.setAgc_id(user.getAgence().getAgcId());
        dto.setPrfl_id(user.getProfil().getPrflId());
        return dto;
    }

}
