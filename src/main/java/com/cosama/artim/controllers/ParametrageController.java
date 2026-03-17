package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.UserRepository;
import com.cosama.artim.security.SecurityConfig;
import com.cosama.artim.services.*;
import com.cosama.artim.services.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/parametrage")
@RequiredArgsConstructor
public class ParametrageController {

    @Autowired
    private final ParametrageService parametrage;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final VenteBilletService venteBilletService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final AuthenticationServiceImpl authS;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value="/createVilles")
    @ResponseStatus(HttpStatus.CREATED)
    public Ville createVille(@RequestBody VilleDTO villeDTO) {
        return parametrage.createVille(villeDTO);
    }

    @GetMapping("/villes")
    @ResponseStatus(HttpStatus.OK)
    public List<VilleDTO> getVilles()
    {
        return parametrage.getAllVille();
    }

    @PostMapping("/createAgences")
    @ResponseStatus(HttpStatus.CREATED)
    public Agence createAgence(@RequestBody AgenceDTO agenceDTO) {
        return parametrage.createAgence(agenceDTO);
    }

    @PutMapping("/updateAgences/{idAgence}")
    @ResponseStatus(HttpStatus.CREATED)
    public Agence updateAgence(@PathVariable long idAgence, @RequestBody AgenceDTO agenceDTO) {
        return parametrage.updateAgence(idAgence, agenceDTO);
    }

    @GetMapping("/agences")
    @ResponseStatus(HttpStatus.OK)
    public List<AgenceDTO> getAllAgence()
    {
        return parametrage.getAllAgence();
    }

    @DeleteMapping("/deleteAgence/{idAgence}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgence(@PathVariable long idAgence) {
        parametrage.deleteAgence(idAgence);
    }

    @GetMapping("/bateau/{idBat}")
    @ResponseStatus(HttpStatus.OK)
    public Bateau getBateauById(@PathVariable Long idBat)
    {
        return parametrage.getBateauById(idBat);
    }

    @PostMapping("/createBateau")
    @ResponseStatus(HttpStatus.CREATED)
    public Bateau createBateau(@RequestBody BateauDTO bateauDTO)
    {
        return parametrage.createBateau(bateauDTO);
    }

    /**
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getBateauPhoto(@PathVariable Long id) {
        byte[] photoData = parametrage.getBateauPhoto(id);
        String photoName = parametrage.getBateauPhotoName(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);  // Vous pouvez adapter le type de media selon votre format d'image
        headers.setContentDispositionFormData(photoName, photoName);

        return new ResponseEntity<>(photoData, headers, HttpStatus.OK);
    }**/

    @PutMapping("/updateBateau/{idBateau}")
    @ResponseStatus(HttpStatus.CREATED)
    public Bateau updateBateau(@PathVariable long idBateau, @RequestBody BateauDTO bateauDTO) {
        // Convertir le JSON reçu en objet BateauDTO
        // Gérer le fichier reçu (par exemple, le sauvegarder sur le serveur)
        // Appeler la logique métier pour mettre à jour le bateau
        return parametrage.updateBateau(idBateau, bateauDTO);
    }

    @GetMapping("/bateau")
    @ResponseStatus(HttpStatus.OK)
    public List<BateausDTO> getAllBateau()
    {
        return parametrage.getAllBateau();
    }

    @PutMapping("/updateEtatBateau/{idBateau}")
    @ResponseStatus(HttpStatus.CREATED)
    public Bateau updateEtatBateau(@PathVariable long idBateau, @RequestParam boolean state) {
        return parametrage.updateEtatBateau(idBateau, state);
    }

    @DeleteMapping("/deleteBateau/{idBateau}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBateau(@PathVariable long idBateau) {
        parametrage.deleteBateau(idBateau);
    }

    @PostMapping("/createTypePiece")
    @ResponseStatus(HttpStatus.CREATED)
    public TypePiece createTypePiece(@RequestBody TypePieceDTO typePieceDTO) {
        return parametrage.createTypePiece(typePieceDTO);
    }

    @GetMapping("/groupesCriteres")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupeCritereDTO> getAllGroupeCriteres() {
        return tarification.getAllGroupeCriteresParam();
    }

    @PutMapping("/updateTypePiece/{idTypePiece}")
    @ResponseStatus(HttpStatus.CREATED)
    public TypePiece updateTypePiece(@PathVariable long idTypePiece, @RequestBody TypePieceDTO typePieceDTO)
    {
        return parametrage.updateTypePiece(idTypePiece, typePieceDTO);
    }

    @PutMapping("/updateVille/{idVille}")
    @ResponseStatus(HttpStatus.CREATED)
    public Ville updateVille(@PathVariable long idVille, @RequestBody VilleDTO villeDTO)
    {
        return parametrage.updateVille(idVille, villeDTO);
    }

    @GetMapping("/typePieces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePieceDTO> getAllTypePiece()
    {
        return parametrage.getAllTypePiece();
    }

    @DeleteMapping("/deleteTypePiece/{idTypePiece}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTypePiece(@PathVariable long idTypePiece) {
        parametrage.deleteTypePiece(idTypePiece);
    }

    @PostMapping("/createUnite")
    @ResponseStatus(HttpStatus.CREATED)
    public Unite createUnite(@RequestBody UniteDTO uniteDTO) {
        return parametrage.createUnite(uniteDTO);
    }

    @PutMapping("/updateUnite/{idUnite}")
    @ResponseStatus(HttpStatus.CREATED)
    public Unite updateUnite(@PathVariable long idUnite, @RequestBody UniteDTO uniteDTO)
    {
        return parametrage.updateUnite(idUnite, uniteDTO);
    }

    @GetMapping("/unites")
    @ResponseStatus(HttpStatus.OK)
    public List<UniteDTO> getAllUnites()
    {
        return parametrage.getAllUnite();
    }

    @GetMapping("/volumes")
    @ResponseStatus(HttpStatus.OK)
    public List<Volume> getAllVolume()
    {
        return parametrage.getAllVolume();
    }

    @DeleteMapping("/deleteUnite/{idUnite}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUnite(@PathVariable long idUnite) {
        parametrage.deleteUnite(idUnite);
    }

    @PostMapping("/createTypeBagage")
    @ResponseStatus(HttpStatus.CREATED)
    public TypeBagage createTypeBagage(@RequestBody TypeBagageDTO typeBagageDTO) {
        return parametrage.createTypeBagage(typeBagageDTO);
    }

    @PutMapping("/updateTypeBagage/{idtBg}")
    @ResponseStatus(HttpStatus.CREATED)
    public TypeBagage updateTypeBagage(@PathVariable long idtBg, @RequestBody TypeBagageDTO typeBagageDTO)
    {
        return parametrage.updateTypeBagage(idtBg, typeBagageDTO);
    }

    //@GetMapping("/typeBagages")
    //@ResponseStatus(HttpStatus.OK)
    //public List<TypeBagage> getAllTypeBagage()
    //{
    //    return parametrage.getAllTypeBagage();
    //}

    @GetMapping("/typeBagages")
    @ResponseStatus(HttpStatus.OK)
    public List<TypeBagageDTO> getAllTypeBagages()
    {
        return parametrage.getAllTypeBagages();
    }

    @DeleteMapping("/deleteTypeBagage/{idtBg}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTypeBagage(@PathVariable long idtBg) {
        parametrage.deleteTypeBagage(idtBg);
    }

    @PostMapping("/createVoyage")
    @ResponseStatus(HttpStatus.CREATED)
    public Voyage createVoyage(@RequestBody VoyageDTO voyageDTO) {
        return parametrage.createVoyage(voyageDTO);
    }

    @PutMapping("/updateVoyage/{idVoy}")
    @ResponseStatus(HttpStatus.CREATED)
    public Voyage updateVoyage(@PathVariable long idVoy, @RequestBody VoyageDTO2 voyageDTO)
    {
        return parametrage.updateVoyage(idVoy,voyageDTO);
    }

    @GetMapping("/voyages")
    @ResponseStatus(HttpStatus.OK)
    public List<VoyageDTO> getAllVoyages()
    {
        return parametrage.getAllVoyage();
    }

    @GetMapping("/planVoyage")
    @ResponseStatus(HttpStatus.OK)
    public List<PlanVoyageDTO> getPlan()
    {
        return parametrage.getPlanning();
    }

    @GetMapping("/planVoyage2")
    @ResponseStatus(HttpStatus.OK)
    public List<PlanVoyageDTO> getPlan2()
    {
        return parametrage.getPlanning2();
    }

    @GetMapping("/getStateVP")
    @ResponseStatus(HttpStatus.OK)
    public int getEtatVP(@RequestParam Long voyId, @RequestParam Long placeId)
    {
        return parametrage.getvpletat(voyId, placeId);
    }

    @GetMapping("/stateVP")
    @ResponseStatus(HttpStatus.OK)
    public VoyagePlace getEtatVP_(@RequestParam Long voyId, @RequestParam Long placeId)
    {
        return parametrage.vpletat(voyId, placeId);
    }

    @DeleteMapping("/deleteVoyage/{idVoy}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVoyage(@PathVariable long idVoy) {
        parametrage.deleteVoyage(idVoy);
    }

    @PostMapping("/createPlace")
    @ResponseStatus(HttpStatus.CREATED)
    public Place createPlace(@RequestBody PlaceDTO placeDTO) {
        return parametrage.createPlace(placeDTO);
    }

    @PutMapping("/updatePlace/{idPlc}")
    @ResponseStatus(HttpStatus.CREATED)
    public Place updatePlace(@PathVariable long idPlc, @RequestBody PlaceEditDTO placeDTO)
    {
        return parametrage.updatePlace(idPlc, placeDTO);
    }

    @GetMapping("/places")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceEditDTO> getAllPlaces()
    {
        return parametrage.getAllPlace();
    }

    @GetMapping("/niveaux/{batId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Niveau> niveauWithPlaces(@PathVariable long batId)
    {
        return parametrage.getNiveauPlacesBateauId(batId);
    }

    @GetMapping("/niveaux")
    @ResponseStatus(HttpStatus.OK)
    public List<NiveauDTO> niveaux()
    {
        return parametrage.getNiveaux();
    }

    @GetMapping("/isPlaceInVoyagePlace")
    public List<Boolean> checkPlacesInVoyage(@RequestParam List<Long> placeIds,
                                             @RequestParam Long voyId,
                                             @RequestParam Long typePlace) {
        // Appel du service avec le filtre typePlace
        return parametrage.isPlacesInVoyagePlace(placeIds, voyId, typePlace);
    }

    @GetMapping("/isPlaceInVoyagePlace2")
    public List<Boolean> checkPlacesInVoyage(@RequestParam List<Long> placeIds,
                                             @RequestParam Long voyId) {
        // Appel du service avec le filtre typePlace
        return parametrage.isPlacesInVoyagePlace2(placeIds, voyId);
    }

    @GetMapping("/placesBateau/{batId}")
    public List<Place> getPlacesByBateauId(@PathVariable long batId) {
        return parametrage.getPlacesByBateauId(batId);
    }

    @DeleteMapping("/deletePlace/{idPlc}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlace(@PathVariable long idPlc) {
        parametrage.deletePlace(idPlc);
    }

    @PutMapping("/updateEtatPlace/{idPlc}")
    @ResponseStatus(HttpStatus.CREATED)
    public Place updateEtatPlace(@PathVariable long idPlc, @RequestBody PlaceDTO placeDTO)
    {
        return parametrage.updateEtatPlace(idPlc, placeDTO);
    }

    @GetMapping("/places/{idBat}")
    @ResponseStatus(HttpStatus.OK)
    public List<PlacesBoatDTO> getAllPlaces(@PathVariable long idBat)
    {
        return parametrage.getAllPlaceOfBateau(idBat);
    }

    @PostMapping("/createCritere")
    @ResponseStatus(HttpStatus.CREATED)
    public Critere createCritere(@RequestBody CritereDTO critereDTO) {
        return tarification.createCritere(critereDTO);
    }

    @PutMapping("/updateCriteres/{idCritere}")
    @ResponseStatus(HttpStatus.CREATED)
    public Critere updateCritere(@PathVariable long idCritere, @RequestBody CritereDTO critereDTO) {
        return tarification.updateCritere(idCritere, critereDTO);
    }

    @DeleteMapping("/deleteCritere/{idCritere}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCritere(@PathVariable long idCritere) {
        tarification.deleteCritere(idCritere);
    }

    @GetMapping("/critere/{idCritere}")
    @ResponseStatus(HttpStatus.OK)
    public Critere getCritere(@PathVariable long idCritere) {
        return tarification.getCritereyId(idCritere);
    }

    @PostMapping("/createCategoriePlace")
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie createCategoriePlace(@RequestBody CategorieDTO categorieDTO) {
        return tarification.createCategoriePlace(categorieDTO);
    }

    @PutMapping("/updateCategoriePlace/{idCatPlace}")
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie updateCatPlace(@PathVariable long idCatPlace, @RequestBody CategorieDTO categorieDTO) {
        return tarification.updateCategoriePlace(idCatPlace, categorieDTO);
    }

    @PutMapping("/updateCategorieBagage/{idCatBagage}")
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie updateCatBagage(@PathVariable long idCatBagage, @RequestBody CategorieDTO categorieDTO) {
        return tarification.updateCategorieBagage(idCatBagage, categorieDTO);
    }

    @PostMapping("/createCategorieBagage")
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie createCategorieBagage(@RequestBody CategorieDTO categorieDTO) {
        return tarification.createCategorieBagage(categorieDTO);
    }

    @PostMapping("/createNiveau")
    @ResponseStatus(HttpStatus.CREATED)
    public Niveau createNiveau(@RequestBody NiveauDTO niveauDTO) {
        return parametrage.createNiveau(niveauDTO);
    }


    @PutMapping("/updateNiveau/{idNiveau}")
    @ResponseStatus(HttpStatus.CREATED)
    public Niveau updateNiveau(@PathVariable long idNiveau, @RequestBody NiveauDTO niveauDTO) {
        return parametrage.updateNiveau(idNiveau, niveauDTO);
    }

    @PostMapping("/createTypePlace")
    @ResponseStatus(HttpStatus.CREATED)
    public TypePlace createTypePlace(@RequestBody TypePlaceDTO typePlaceDTO) {
        return parametrage.createTypePlace(typePlaceDTO);
    }

    @GetMapping("/TypePlaceName/{idTplc}")
    @ResponseStatus(HttpStatus.OK)
    public String getTypePlaceName(@PathVariable long idTplc)
    {
        return parametrage.getPlaceName(idTplc);
    }


    @GetMapping("/voyageDpt")
    @ResponseStatus(HttpStatus.OK)
    public Voyage getVoyageDpt(@RequestParam String voyDate, @RequestParam int voyDpt, @RequestParam long batId)
    {
        return parametrage.getVoyByDept(voyDate, voyDpt, batId);
    }

    @GetMapping("/voyageDate")
    @ResponseStatus(HttpStatus.OK)
    public List<VoyageDTO> getVoyageDate(@RequestParam long batId)
    {
        return parametrage.getVoyByDat(batId);
    }

    /*
    @PostMapping("/excel")
    public ResponseEntity<String> importExcelFile(@RequestParam("file") MultipartFile file)
    {
        try
        {
            parametrage.importExcel(file);
            return new ResponseEntity<>("Les données ont été importées avec succès.", HttpStatus.OK);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
     */

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<Nationalite> getAllCountry()
    {
        return parametrage.getAllCountries();
    }

    @GetMapping("/typePlaces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePlacesDTO> getAllTypePlace()
    {
        return parametrage.getAllTypePlace();
    }

    @GetMapping("/criteres")
    @ResponseStatus(HttpStatus.OK)
    public List<Critere> getAllCritere()
    {
        return parametrage.getAllCritere();
    }

    @PostMapping("/prixPlace")
    @ResponseStatus(HttpStatus.OK)
    public List<Number> getPrixPlace(@RequestBody TarifRequest tarifRequest) {
        return tarification.tarifPlaceTab(tarifRequest.getTplId(), tarifRequest.getCriteres());
    }

    @PostMapping("/createGroupeCritere")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupeCritere createGrpCrt(@RequestBody GroupeCritereDTO groupeCritereDTO) {
        return tarification.createGroupeCritere(groupeCritereDTO);
    }

    @PutMapping("/updateGroupeCritere/{idGrpCrt}")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupeCritere updateGroupeCrt(@PathVariable long idGrpCrt, @RequestBody GroupeCritereDTO groupeCritereDTO) {
        return tarification.updateGroupeCritere(idGrpCrt, groupeCritereDTO);
    }

    @GetMapping("/tarifs")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriesDTO> getAllCat() {
        return tarification.getAllCategorie();
    }

    @GetMapping("/availablePlace")
    public ResponseEntity<List<PlaceDTO>> getAvailablePlaces(
            @RequestParam Long tplcId,
            @RequestParam Long voyId,
            @RequestParam Long batId) {
        List<PlaceDTO> availablePlaces = venteBilletService.getAvailablePlaces(tplcId, voyId, batId);
        if (availablePlaces.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune place n'est disponible
        }
        return ResponseEntity.ok(availablePlaces); // 200 OK avec les places disponibles
    }

    @GetMapping("/reservedPlace")
    public ResponseEntity<List<PlaceDTO>> getreservedPlaces(
            @RequestParam Long tplcId,
            @RequestParam Long voyId,
            @RequestParam Long batId) {
        List<PlaceDTO> availablePlaces = venteBilletService.getReservedPlaces(tplcId, voyId, batId);
        if (availablePlaces.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune place n'est disponible
        }
        return ResponseEntity.ok(availablePlaces); // 200 OK avec les places disponibles
    }

    @DeleteMapping("/libererPlace")
    @ResponseStatus(HttpStatus.OK)
    public void libererPlace(@RequestParam Long plcId, @RequestParam Long voyId, @RequestParam Long batId)
    {
        venteBilletService.liberationPlace(plcId, voyId, batId);
    }

    @DeleteMapping("/shotPlace")
    @ResponseStatus(HttpStatus.OK)
    public void killPlace(@RequestParam Long plcId, @RequestParam Long voyId, @RequestParam Long batId)
    {
        venteBilletService.shotPlace(plcId, voyId, batId);
    }


    @PostMapping(value="/doReservation")
    @ResponseStatus(HttpStatus.CREATED)
    public VoyagePlace doReservation(@RequestParam Long plcId,
                                     @RequestParam Long voyId,
                                     @RequestParam Long batId) {
        return venteBilletService.blocagePlace(plcId, voyId, batId);
    }

    @PostMapping("/createEtatBillet")
    @ResponseStatus(HttpStatus.CREATED)
    public EtatBillet createEB(@RequestBody EtatBilletDTO etatBilletDTO) {
        return venteBilletService.addEtatBillet(etatBilletDTO);
    }

    @PostMapping("/createProfil")
    @ResponseStatus(HttpStatus.CREATED)
    public Profil createProfil(@RequestBody ProfilDTO profilDTO)
    {
        return parametrage.createProfil(profilDTO);
    }

    @PutMapping("/updateProfil")
    @ResponseStatus(HttpStatus.CREATED)
    public Profil updateProfil(@RequestParam long pfrId, @RequestBody ProfilDTO profilDTO)
    {
        return parametrage.updateChoiseOfProfil(pfrId, profilDTO);
    }

    @GetMapping("/profils")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfilDTO> getProfil()
    {
        return parametrage.getAllProfil();
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UtilisateurDTO> getUsers()
    {
        return parametrage.getAllUtilisateur();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpDTO signUpDTO)
    {
        return ResponseEntity.ok(authenticationService.signup(signUpDTO));
    }

    @PutMapping("/update-user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> updateUser(@RequestParam long usrId, @RequestBody SignUpDTO userDTO)
    {
        return ResponseEntity.ok(authenticationService.updateUser(usrId, userDTO));
    }

    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> updatePassword(@RequestParam long usrId, @RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        return ResponseEntity.ok(authenticationService.updatePassword(usrId, resetPasswordDTO));
    }

    @PutMapping("/updateEtat-user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> updateEtatUsr(@RequestParam long usrId, @RequestBody UtilisateurDTO userDTO)
    {
        return ResponseEntity.ok(parametrage.updateEtatUser(usrId, userDTO));
    }

    @PutMapping("/updateEtat-profil")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Profil> updateEtatPrfl(@RequestParam long usrId, @RequestBody ProfilDTO profilDTO)
    {
        return ResponseEntity.ok(parametrage.updateProfilEtat(usrId, profilDTO));
    }

    @PutMapping("/annulerVoyage/{id}")
    public ResponseEntity<String> annulerVoyage(@PathVariable("id") Long id, @RequestParam String motif)
    {
        try
        {
            venteBilletService.annulerVoyage(id, motif);
            return ResponseEntity.ok("Voyage annulé avec succès.");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'annulation du voyage.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDTO> getMyProfile(long id)
    {
        return ResponseEntity.ok(authS.getCurrentUser(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UtilisateurDTO> updateMyProfile(long id, @RequestBody UtilisateurDTO dto)
    {
        return ResponseEntity.ok(authS.updateCurrentUser(id, dto));
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestParam long id, @RequestBody PasswordChangeDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Utilisateur introuvable"));
        }

        User user = optionalUser.get();

        // Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ancien mot de passe incorrect"));
        }

        // Encodage et sauvegarde du nouveau mot de passe
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour"));
    }







}
