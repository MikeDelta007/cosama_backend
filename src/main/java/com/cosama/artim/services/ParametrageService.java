package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParametrageService {

    @Autowired
    private final BateauRepository bateauRepository;
    @Autowired
    private final AgenceRepository agenceRepository;
    @Autowired
    private final VilleRepository villeRepository;
    @Autowired
    private final CritereRepository critereRepository;
    @Autowired
    private final TypePieceRepository typePieceRepository;
    @Autowired
    private final UniteRepository uniteRepository;
    @Autowired
    private final VolumeRepository volumeRepository;
    @Autowired
    private final TypeBagageRepository typeBagageRepository;
    @Autowired
    private final VoyageRepository voyageRepository;
    @Autowired
    private final TypePlaceRepository typePlaceRepository;
    @Autowired
    private final PlaceRepository placeRepository;
    @Autowired
    private final NiveauRepository niveauRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProfilRepository profilRepository;
    @Autowired
    private final NationaliteRepository nationalityRepository;
    @Autowired
    private final VoyagePlaceRepository voyagePlaceRepository;
    @Autowired
    private final CategorieRepository categorieRepository;

    private final JdbcTemplate jdbcTemplate;

    public Ville createVille(VilleDTO villeDTO)
    {
        Ville ville = Ville.builder()
                .vilCode(villeDTO.getVil_code())
                .vilNom(villeDTO.getVil_nom())
                .build();

        return villeRepository.save(ville);
    }

    public List<VilleDTO> getAllVille()
    {
        List<Ville> villes = villeRepository.findAll();
        return villes.stream()
                .map(this::convertToDTO5)
                .collect(Collectors.toList());
    }

    public Ville updateVille(long id, VilleDTO villeDTO){
        Ville ville = this.villeRepository.findById(id).orElse(null);
        log.info("ICI", villeDTO.getVil_code());
        log.info("ICI", villeDTO.getVil_nom());
        if (ville != null) {
            ville.setVilNom(villeDTO.getVil_nom());
            ville.setVilCode(villeDTO.getVil_code());

            return villeRepository.save(ville);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    private VilleDTO convertToDTO5(Ville ville) {
        VilleDTO dto = new VilleDTO();
        dto.setVil_id(ville.getVilId());
        dto.setVil_code(ville.getVilCode());
        dto.setVil_nom(ville.getVilNom());
        return dto;
    }

    public List<TypeBagage> getAllBagage()
    {
        return typeBagageRepository.findAll();
    }

    public Agence createAgence(AgenceDTO agenceDTO)
    {
        Ville ville = villeRepository.findVilleByvilId(agenceDTO.getVil_id());
        Agence agence = Agence.builder()
                .agcNom(agenceDTO.getAgc_nom())
                .agcTel(agenceDTO.getAgc_tel())
                .agcFax(agenceDTO.getAgc_fax())
                .agcMail(agenceDTO.getAgc_mail())
                .agcResp(agenceDTO.getAgc_resp())
                .sigle(agenceDTO.getSigle())
                .codeAgc(agenceDTO.getCode_agc())
                .ville(ville)
                .build();

        return agenceRepository.save(agence);
    }

    public List<AgenceDTO> getAllAgence()
    {
        List<Agence> agences = agenceRepository.findAll();
        return agences.stream()
                .map(this::convertToDTO3)
                .collect(Collectors.toList());
    }
    private AgenceDTO convertToDTO3(Agence agence) {
        AgenceDTO dto = new AgenceDTO();
        dto.setAgc_id(agence.getAgcId());
        dto.setAgc_nom(agence.getAgcNom());
        dto.setAgc_tel(agence.getAgcTel());
        dto.setAgc_resp(agence.getAgcResp());
        dto.setAgc_fax(agence.getAgcFax());
        dto.setAgc_mail(agence.getAgcMail());
        dto.setSigle(agence.getSigle());
        dto.setVil_id(agence.getVille().getVilId());

        return dto;
    }



    public Agence updateAgence(long id, AgenceDTO agenceDTO){
        Agence agence = this.agenceRepository.findById(id).orElse(null);
        if (agence != null) {
            agence.setAgcResp(agenceDTO.getAgc_resp());
            agence.setAgcFax(agenceDTO.getAgc_fax());
            agence.setAgcTel(agenceDTO.getAgc_tel());
            agence.setAgcMail(agenceDTO.getAgc_mail());

            return agenceRepository.save(agence);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteAgence(long id){
        if(agenceRepository.existsById(id)){
            agenceRepository.deleteById(id);
        }else {
            throw new NotFoundException("Agence with ID "+id+" is not found");
        }
    }

    public Bateau createBateau(BateauDTO bateauDTO) {
        Agence agence = this.agenceRepository.findById(bateauDTO.getAgc_id()).orElse(null);
        Bateau bateau = Bateau.builder()
                .batRef(bateauDTO.getBat_ref())
                .batNom(bateauDTO.getBat_nom())
                .batDesc(bateauDTO.getBat_desc())
                .batNbplace(bateauDTO.getBat_nbplace())
                .batMarkeur(bateauDTO.getBat_markeur())
                .agence(agence)
                .build();

        return bateauRepository.save(bateau);
    }

    public Bateau updateBateau(long id, BateauDTO bateauDTO) {
        Bateau bateau = this.bateauRepository.findById(id).orElse(null);
        Agence agence = this.agenceRepository.findById(bateauDTO.getAgc_id()).orElse(null);
        if (bateau != null) {
            bateau.setBatRef(bateauDTO.getBat_ref());
            bateau.setBatNom(bateauDTO.getBat_nom());
            bateau.setBatDesc(bateauDTO.getBat_desc());
            bateau.setBatNbplace(bateauDTO.getBat_nbplace());
            bateau.setBatMarkeur(bateauDTO.getBat_markeur());
            bateau.setAgence(agence);
            return bateauRepository.save(bateau);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public Bateau updateEtatBateau(long id, boolean state){
        Bateau bateau = this.bateauRepository.findById(id).orElse(null);
        if (bateau != null) {
            bateau.setBatEtat(state);
            return bateauRepository.save(bateau);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteBateau(long id){
        if(bateauRepository.existsById(id)){
            bateauRepository.deleteById(id);
        }else {
            throw new NotFoundException("Bateau with ID "+id+" is not found");
        }
    }

    //public List<Bateau> getAllBateau()
    //{
    //    return bateauRepository.findAllBateauxWithAgence();
    //}

    public List<BateausDTO> getAllBateau() {
        List<Bateau> bateaux = bateauRepository.findAllBateauxWithAgence();
        return bateaux.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }
    private BateausDTO convertToDTO1(Bateau bateau) {
        BateausDTO dto = new BateausDTO();
        dto.setBat_id(bateau.getBatId());
        dto.setBat_ref(bateau.getBatRef());
        dto.setBat_nom(bateau.getBatNom());
        dto.setBat_etat(bateau.isBatEtat());
        dto.setBat_desc(bateau.getBatDesc());
        dto.setBat_nbplace(bateau.getBatNbplace());
        dto.setBat_markeur(bateau.getBatMarkeur());
        dto.setAgc_id(bateau.getAgence().getAgcId());

        // Logique conditionnelle pour Volume
        AgenceDTO agenceDTO = new AgenceDTO();
        agenceDTO.setAgc_id(bateau.getAgence().getAgcId());
        agenceDTO.setAgc_nom(bateau.getAgence().getAgcNom());
        agenceDTO.setAgc_resp(bateau.getAgence().getAgcResp());
        agenceDTO.setAgc_tel(bateau.getAgence().getAgcTel());
        dto.setAgence(agenceDTO);

        return dto;
    }

    public Bateau getBateauById(Long id)
    {
        Bateau bateau = this.bateauRepository.findById(id).orElse(null);

        if(bateau!= null)
        {
            return bateau;
        }
        else
        {
            return null;
        }
    }


    public TypePiece createTypePiece(TypePieceDTO typePieceDTO)
    {
        TypePiece typePiece = TypePiece.builder()
                .tpieceNom(typePieceDTO.getTpiece_nom())
                .dispo(true)
                .build();

        return typePieceRepository.save(typePiece);
    }

    public TypePiece updateTypePiece(long id, TypePieceDTO typePieceDTO){
        TypePiece typePiece = this.typePieceRepository.findById(id).orElse(null);
        if (typePiece != null) {
            typePiece.setTpieceNom(typePieceDTO.getTpiece_nom());
            typePiece.setDispo(typePiece.getDispo());

            return typePieceRepository.save(typePiece);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteTypePiece(long id){
        if(typePieceRepository.existsById(id)){
            typePieceRepository.deleteById(id);
        }else {
            throw new NotFoundException("TypePiece with ID "+id+" is not found");
        }
    }

    public List<TypePieceDTO> getAllTypePiece()
    {
        List<TypePiece> type_pieces = typePieceRepository.findAll();
        return type_pieces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TypePieceDTO convertToDTO(TypePiece typePiece) {
        TypePieceDTO dto = new TypePieceDTO();
        dto.setTpiece_id(typePiece.getTpieceId());
        dto.setTpiece_nom(typePiece.getTpieceNom());
        dto.setDispo(typePiece.getDispo());
        return dto;
    }

    //

    public Unite createUnite(UniteDTO uniteDTO)
    {
        Volume volume = this.volumeRepository.findById(uniteDTO.getVol_id()).orElse(null);
        Unite unite = Unite.builder()
                .uniteCode(uniteDTO.getUnite_code())
                .uniteNom(uniteDTO.getUnite_nom())
                .volume(volume)
                .build();

        return uniteRepository.save(unite);
    }

    public Unite updateUnite(long id, UniteDTO uniteDTO){
        Unite unite = this.uniteRepository.findById(id).orElse(null);
        Volume volume = this.volumeRepository.findById(uniteDTO.getVol_id()).orElse(null);
        if (unite != null) {
            unite.setUniteCode(uniteDTO.getUnite_code());
            unite.setUniteNom(uniteDTO.getUnite_nom());
            unite.setVolume(volume);
            return uniteRepository.save(unite);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteUnite(long id){
        if(uniteRepository.existsById(id)){
            uniteRepository.deleteById(id);
        }else {
            throw new NotFoundException("Unite with ID "+id+" is not found");
        }
    }

    public List<UniteDTO> getAllUnite()
    {
        List<Unite> unites = uniteRepository.findAll();
        return unites.stream()
                .map(this::convertToDTO4)
                .collect(Collectors.toList());
    }

    private UniteDTO convertToDTO4(Unite unite) {
        UniteDTO dto = new UniteDTO();
        dto.setUnite_id(unite.getUniteId());
        dto.setUnite_code(unite.getUniteCode());
        dto.setUnite_nom(unite.getUniteNom());
        dto.setVol_id(unite.getVolume().getVolId());
        return dto;
    }

    public TypeBagage createTypeBagage(TypeBagageDTO typeBagageDTO)
    {
        Unite unite = uniteRepository.findById(typeBagageDTO.getUnite_id()).orElse(null);
        TypeBagage typeBagage = TypeBagage.builder()
                .tbgNom(typeBagageDTO.getTbg_nom())
                .etat(true)
                .volId(unite.getVolume().getVolId())
                .unite(unite)
                .build();

        return typeBagageRepository.save(typeBagage);
    }

    public TypeBagage updateTypeBagage(long id, TypeBagageDTO typeBagageDTO){
        TypeBagage typeBagage = this.typeBagageRepository.findBagageBytbgId(id);
        Unite unite = uniteRepository.findById(typeBagageDTO.getUnite_id()).orElse(null);
        if (typeBagage != null) {
            typeBagage.setTbgNom(typeBagageDTO.getTbg_nom());
            typeBagage.setUnite(unite);

            return typeBagageRepository.save(typeBagage);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteTypeBagage(long id){
        if(typeBagageRepository.existsById(id)){
            typeBagageRepository.deleteById(id);
        }else {
            throw new NotFoundException("TypeBagage with ID "+id+" is not found");
        }
    }

    public List<TypeBagageDTO> getAllTypeBagages() {
        List<TypeBagage> typeBagages = typeBagageRepository.findAll();
        return typeBagages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private TypeBagageDTO convertToDTO(TypeBagage typeBagage) {
        TypeBagageDTO dto = new TypeBagageDTO();
        dto.setTbg_id(typeBagage.getTbgId());
        dto.setTbg_nom(typeBagage.getTbgNom());
        dto.setEtat(typeBagage.isEtat());
        dto.setUnite_id(typeBagage.getUnite().getUniteId());
        dto.setVol_id(typeBagage.getVolId());

        // Convertir Unite en UniteDTO
        UniteDTO uniteDTO = new UniteDTO();
        uniteDTO.setUnite_id(typeBagage.getUnite().getUniteId());
        uniteDTO.setUnite_nom(typeBagage.getUnite().getUniteNom());
        uniteDTO.setUnite_code(typeBagage.getUnite().getUniteCode());
        dto.setUnite(uniteDTO);

        // Logique conditionnelle pour Volume
        VolumeDTO volumeDTO = new VolumeDTO();
        volumeDTO.setVolId(typeBagage.getUnite().getVolume().getVolId());
        volumeDTO.setVolNom(typeBagage.getUnite().getVolume().getVolNom());
        dto.setVolume(volumeDTO);

        return dto;
    }

    public List<CategorieDTO> getAllVoitures()
    {
        List<Categorie> categories = categorieRepository.findByFixedCode();
        return categories.stream()
                .map(this::convertToDTO5)
                .collect(Collectors.toList());
    }

    private CategorieDTO convertToDTO5(Categorie categorie) {
        CategorieDTO dto = new CategorieDTO();

        // Champs de base
        dto.setCat_id(categorie.getCatId());
        dto.setCat_nom(categorie.getCatNom());
        dto.setCat_prix(categorie.getCatPrix());
        dto.setCat_prix_ttc(categorie.getCatPrixTtc());
        dto.setPlace(categorie.isPlace());
        dto.setBagage(categorie.isBagage());
        dto.setCat_remise(categorie.getCatRemise());
        dto.setTaux_remise(categorie.getTauxRemise());
        dto.setCat_forfait(categorie.getCatForfait());
        dto.setCat_taxe(categorie.getCatTaxe());
        //dto.setFrais_msg(categorie.getFraisMag());
        dto.setCat_commission(categorie.getCatCommission());
        // Champs liés
        dto.setAge(categorie.getAge());
        dto.setOriginPax(categorie.getOriginPax());
        dto.setCode(categorie.getCode());

        dto.setTbg_id(categorie.getTypeBagage().getTbgId());

        return dto;
    }




    public String callRemoteFunction3() {
        String sql = "SELECT get_next_order_number_voyage()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeVoy_generator(String OrderNumber3)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        log.info("formattedInput est {} :" + OrderNumber3);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);
        String c =  "V-" + OrderNumber3 + mois + an;
        return c;
    }

    public Voyage createVoyage(VoyageDTO voyageDTO) {

        Bateau bateau = bateauRepository.findById(voyageDTO.getBat_id())
                .orElseThrow(() -> new RuntimeException("Bateau introuvable"));

        // 🔴 Vérifier si le voyage existe déjà (métier)
        Optional<Voyage> existing = voyageRepository
                .findByVoyDepartAndVoyDestinationAndVoyDatedptAndVoyDatearrivAndBateau(
                        (long) voyageDTO.getVoy_depart(),
                        (long) voyageDTO.getVoy_destination(),
                        voyageDTO.getVoy_datedpt(),
                        voyageDTO.getVoy_datearriv(),
                        bateau
                );

        if (existing.isPresent()) {
            throw new RuntimeException("Ce voyage existe déjà");
        }

        // ✅ Génération code seulement si OK
        String ordreString = callRemoteFunction3();
        String code_voyage = codeVoy_generator(ordreString);

        String color = bateau.getBatMarkeur();

        Voyage voyage = Voyage.builder()
                .voyDepart(voyageDTO.getVoy_depart())
                .voyDestination(voyageDTO.getVoy_destination())
                .voyDatedpt(voyageDTO.getVoy_datedpt())
                .voyDatearriv(voyageDTO.getVoy_datearriv())
                .voyEtat(voyageDTO.getVoy_etat())
                .codeVoyage(code_voyage)
                .bateau(bateau)
                .batMarkeur(color)
                .build();

        Voyage voy = voyageRepository.save(voyage);

        // ✅ Création des places
        List<Place> places = placeRepository.placeReserved(bateau.getBatId());

        for (Place p : places) {
            VoyagePlace voyagePlace = new VoyagePlace();
            voyagePlace.setVpl_etat(2);
            voyagePlace.setBateau(bateau);
            voyagePlace.setPlace(p);
            voyagePlace.setVoyage(voy);
            voyagePlaceRepository.save(voyagePlace);
        }

        return voy;
    }

    public Voyage updateVoyage(long id, VoyageDTO2 voyageDTO) {

        // 🔎 1. Récupérer le voyage
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable"));

        // 🔎 2. Récupérer le bateau
        Bateau bateau = bateauRepository.findById(voyageDTO.getBat_id())
                .orElseThrow(() -> new RuntimeException("Bateau introuvable"));

        // 🔴 3. Vérifier doublon métier (IMPORTANT)
        boolean exists = voyageRepository
                .existsByVoyDepartAndVoyDestinationAndVoyDatedptAndVoyDatearrivAndBateauAndVoyIdNot(
                        (long) voyageDTO.getVoy_depart(),
                        (long) voyageDTO.getVoy_destination(),
                        voyageDTO.getVoy_datedpt(),
                        voyageDTO.getVoy_datearriv(),
                        bateau,
                        id
                );

        if (exists) {
            throw new RuntimeException("Un voyage identique existe déjà");
        }

        // ✅ 4. Mise à jour
        voyage.setVoyDepart(voyageDTO.getVoy_depart());
        voyage.setVoyDestination(voyageDTO.getVoy_destination());
        voyage.setVoyDatedpt(voyageDTO.getVoy_datedpt());
        voyage.setVoyDatearriv(voyageDTO.getVoy_datearriv());
        voyage.setBateau(bateau);

        return voyageRepository.save(voyage);
    }

    public void deleteVoyage(long id){
        if(voyageRepository.existsById(id)){
            voyageRepository.deleteById(id);
        }else {
            throw new NotFoundException("Voyage with ID "+id+" is not found");
        }
    }

    public List<VoyageDTO> getAllVoyage() {
        return voyageRepository.findAll().stream()
                .sorted(Comparator.comparing(Voyage::getVoyId).reversed())
                .map(this::convertToDTO5)
                .collect(Collectors.toList());
    }

    private VoyageDTO convertToDTO5(Voyage voyage) {
        VoyageDTO dto = new VoyageDTO();
        dto.setVoy_id(voyage.getVoyId());
        dto.setVoy_etat(voyage.getVoyEtat());
        dto.setVoy_depart(voyage.getVoyDepart());
        dto.setVoy_destination(voyage.getVoyDestination());
        dto.setVoy_datedpt(voyage.getVoyDatedpt());
        dto.setVoy_datearriv(voyage.getVoyDatearriv());
        dto.setBat_id(voyage.getBateau().getBatId());
        dto.setCode_voyage(voyage.getCodeVoyage());

        return dto;
    }

    @Scheduled(cron = "0 50 0 * * *") // Tous les jours à minuit
    public void desactiverFactures() {
        List<Voyage> allVoyage = voyageRepository.findTheVoyagesBeforeToday();
        for (Voyage v : allVoyage) {
            v.setVoyEtat(0);
            voyageRepository.save(v);
            log.info("Voyage : " + v.getCodeVoyage() + "désactivé avec succés.");
        }
    }


    public VoyagePlace vpletat(long idVoy, long idPlc)
    {
        return voyagePlaceRepository.checkPlace(idVoy, idPlc);
    }

    public int getvpletat(long idVoy, long idPlc)
    {
        VoyagePlace vpstate = voyagePlaceRepository.checkPlace(idVoy, idPlc);
        int getState = 0;
        if (vpstate != null)
        {
            getState = vpstate.getVpl_etat();
        }
        return getState;
    }

    public List<PlanVoyageDTO> getPlanning() {
        List<Voyage> voyages = voyageRepository.findAll();
        return voyages.stream()
                .map(this::convertToDTO6)
                .collect(Collectors.toList());
    }

    public List<PlanVoyageDTO> getPlanning2() {
        List<Voyage> voyages = voyageRepository.getVoyageActif();
        return voyages.stream()
                .map(this::convertToDTO6)
                .collect(Collectors.toList());
    }

    private PlanVoyageDTO convertToDTO6(Voyage voyage) {
        PlanVoyageDTO dto = new PlanVoyageDTO();
        dto.setId(voyage.getVoyId());
        dto.setStart(voyage.getVoyDatedpt().toString());
        dto.setEnd(voyage.getVoyDatearriv().toString());
        dto.setTitle(voyage.getCodeVoyage());
        dto.setBackgroundColor(voyage.getBatMarkeur());
        dto.setBatId(voyage.getBateau().getBatId());
        dto.setDepart(voyage.getVoyDepart());
        dto.setArrive(voyage.getVoyDestination());
        dto.setCodeV(voyage.getCodeVoyage());

        return dto;
    }

    public Voyage updateEtatVoyage(long id, VoyageDTO voyageDTO){
        Voyage voyage = this.voyageRepository.findBateauByvoyId(id);
        if (voyage != null) {
            voyage.setVoyEtat(voyageDTO.getVoy_etat());
            return voyageRepository.save(voyage);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }


    public Place createPlace(PlaceDTO placeDTO)
    {
        TypePlace typePlace = this.typePlaceRepository.findBateauBytplcId(placeDTO.getTplc_id());
        Bateau bateau = this.bateauRepository.findById(placeDTO.getBat_id()).orElse(null);
        Niveau niveau = this.niveauRepository.findBateauBynivId(placeDTO.getNiv_id());
        Place place = Place.builder()
                .plcCode(placeDTO.getPlc_code())
                .sexe(placeDTO.getSexe())
                .bateau(bateau)
                .typePlace(typePlace)
                .niveau(niveau)
                .plcEtat(placeDTO.isPlc_etat())
                .build();
        return placeRepository.save(place);
    }

    public Place updatePlace(long id, PlaceEditDTO placeDTO){
        Place place = this.placeRepository.findBateauByplcId(id);
        TypePlace typePlace = this.typePlaceRepository.findBateauBytplcId(placeDTO.getTplc_id());
        Bateau bateau = this.bateauRepository.findById(placeDTO.getBat_id()).orElse(null);
        Niveau niveau = this.niveauRepository.findBateauBynivId(placeDTO.getNiv_id());
        if (place != null) {
            place.setPlcCode(placeDTO.getPlc_code());
            place.setSexe(placeDTO.getSexe());
            place.setBateau(bateau);
            place.setTypePlace(typePlace);
            place.setPlcEtat(placeDTO.getPlc_etat());
            place.setNiveau(niveau);
            place.setIsCarabane(placeDTO.getIs_carabane());
            place.setSituation(placeDTO.getSituation());
            return placeRepository.save(place);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public Place updateEtatPlace(long id, PlaceDTO placeDTO){
        Place place = this.placeRepository.findBateauByplcId(id);
        if (place != null) {
            place.setPlcEtat(placeDTO.isPlc_etat());
            return placeRepository.save(place);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deletePlace(long id){
        if(placeRepository.existsById(id)){
            placeRepository.deleteById(id);
        }else {
            throw new NotFoundException("Place with ID "+id+" is not found");
        }
    }

    public List<PlaceEditDTO> getAllPlace()
    {
        List<Place> places = placeRepository.findAll();
        return places.stream()
                .map(this::convertToDTO3)
                .collect(Collectors.toList());
    }

    private PlaceEditDTO convertToDTO3(Place place) {
        PlaceEditDTO dto = new PlaceEditDTO();
        dto.setPlc_id(place.getPlcId());
        dto.setPlc_code(place.getPlcCode());
        dto.setPlc_etat(place.isPlcEtat());
        dto.setSituation(place.getSituation());
        dto.setIs_carabane(place.getIsCarabane());
        dto.setTplc_id(place.getTypePlace().getTplcId());
        dto.setBat_id(place.getBateau().getBatId());
        dto.setNiv_id(place.getNiveau().getNivId());


        // Convertir Unite en UniteDTO
        TypePlacesDTO typePlaceDTO = new TypePlacesDTO();
        typePlaceDTO.setTplc_id(place.getTypePlace().getTplcId());
        typePlaceDTO.setTplc_nom(place.getTypePlace().getTplcNom());
        dto.setTypePlace(typePlaceDTO);

        // Logique conditionnelle pour Volume
        NiveauDTO niveauDTO = new NiveauDTO();
        niveauDTO.setNiv_id(place.getNiveau().getNivId());
        niveauDTO.setNiv_nom(place.getNiveau().getNivNom());
        dto.setNiveau(niveauDTO);

        BateausDTO bateauDTO = new BateausDTO();
        bateauDTO.setBat_id(place.getBateau().getBatId());
        bateauDTO.setBat_nom(place.getBateau().getBatNom());
        dto.setBateau(bateauDTO);

        return dto;
    }

    public List<Place> getPlacesByBateauId(long batId) {
        return placeRepository.findPlacesByBateauId(batId);
    }

    public List<Niveau> getNiveauPlacesBateauId(long batId)
    {
        return niveauRepository.findNiveauWithPlacesByBateauId(batId);
    }

    public List<NiveauDTO> getNiveaux()
    {
        List<Niveau> niveaux = niveauRepository.findAll();
        return niveaux.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }

    private NiveauDTO convertToDTO1(Niveau niveau) {
        NiveauDTO dto = new NiveauDTO();
        dto.setNiv_id(niveau.getNivId());
        dto.setNiv_nom(niveau.getNivNom());
        dto.setNiv_affiche(niveau.getNivAffiche());
        return dto;
    }

    public List<Boolean> isPlacesInVoyagePlace(List<Long> placeIds, Long voyId, Long typePlace) {
        // Appel de la requête avec le typePlace pour obtenir les résultats filtrés
        List<Object[]> results = voyagePlaceRepository.checkPlacesInVoyage(placeIds, voyId, typePlace);

        // Conversion des résultats en Map
        Map<Long, Boolean> resultMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> (Boolean) result[1]
                ));

        // Retourne une liste de booléens dans l'ordre des placeIds d'origine
        return placeIds.stream()
                .map(placeId -> resultMap.getOrDefault(placeId, false))
                .collect(Collectors.toList());
    }

    public List<Boolean> isPlacesInVoyagePlace2(List<Long> placeIds, Long voyId) {
        // Appel de la requête avec le typePlace pour obtenir les résultats filtrés
        List<Object[]> results = voyagePlaceRepository.checkPlacesInVoyage2(placeIds, voyId);

        // Conversion des résultats en Map
        Map<Long, Boolean> resultMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> (Boolean) result[1]
                ));

        // Retourne une liste de booléens dans l'ordre des placeIds d'origine
        return placeIds.stream()
                .map(placeId -> resultMap.getOrDefault(placeId, false))
                .collect(Collectors.toList());
    }


    public List<PlacesBoatDTO> getAllPlaceOfBateau(Long idBat)
    {
        List<Place> places = placeRepository.findAllPlaceOfTheBoat(idBat);
        return places.stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }

    private PlacesBoatDTO convertToDTO2(Place place) {
        PlacesBoatDTO dto = new PlacesBoatDTO();
        dto.setPlc_id(place.getPlcId());
        dto.setTplc_id(place.getTypePlaceId());
        dto.setBat_id(place.getBateau().getBatId());
        return dto;
    }

    public void deleteUtilisateur(long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }else {
            throw new NotFoundException("Utilisateur with ID "+id+" is not found");
        }
    }

    public List<UtilisateurDTO> getAllUtilisateur()
    {
        List<User> frets = userRepository.findAll();
        return frets.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }


    public UtilisateurDTO convertToDTO1(User user) {
        UtilisateurDTO dto = new UtilisateurDTO();

        dto.setUsr_id(user.getId());
        dto.setUsr_firstname(user.getFirstname());
        dto.setUsr_lastname(user.getLastname());
        dto.setUsr_desc(user.getUsrDesc());
        dto.setUsr_login(user.getLogin());

        // ✅ Sécurisation agence
        if (user.getAgence() != null) {
            dto.setAgc_id(user.getAgence().getAgcId());
        }

        // ✅ Sécurisation profil
        if (user.getProfil() != null) {
            dto.setPrfl_id(user.getProfil().getPrflId());
        }

        dto.setUsr_password(user.getPassword());
        dto.setUsr_etat(user.isEtat());

        return dto;
    }



    public User updateEtatUser(long usrId, UtilisateurDTO utilisateurDTO) {
        // Retrieve the existing Utilisateur from the repository
        User existingUser = userRepository.findById(usrId).orElse(null);

        if (existingUser != null) {
            // Update the fields of the existing Utilisateur
            existingUser.setEtat(utilisateurDTO.isUsr_etat());
            existingUser.setUsrActiv(utilisateurDTO.getUsr_activ());
            existingUser.setUsrActivDate(LocalDateTime.now());
            // Save and return the updated Utilisateur entity
            return userRepository.save(existingUser);
        }
        else
        {
            // Handle the case where the user is not found
            throw new NotFoundException("Utilisateur with ID " + usrId + " is not found");
        }
    }

    public Profil updateProfilEtat(long prflId, ProfilDTO profilDTO) {
        // Retrieve the existing Utilisateur from the repository
        Profil existingPrfl = profilRepository.findById(prflId).orElse(null);

        if (existingPrfl != null) {
            // Update the fields of the existing Utilisateur
            existingPrfl.setActif(profilDTO.isActif());
            // Save and return the updated Utilisateur entity
            return profilRepository.save(existingPrfl);
        }
        else
        {
            // Handle the case where the user is not found
            throw new NotFoundException("Utilisateur with ID " + prflId + " is not found");
        }
    }

    public Profil createProfil(ProfilDTO profilDTO)
    {
        Profil profil = Profil.builder()
                .prflLibelle(profilDTO.getPrfl_libelle())
                .build();
        return profilRepository.save(profil);
    }

    public Profil updateChoiseOfProfil(long pfrId, ProfilDTO profilDTO) {
        // Retrieve the existing Profil from the repository
        Profil existingProfil = profilRepository.findById(pfrId).orElse(null);

        if (existingProfil == null)
        {
            throw new NotFoundException("Profil with ID " + pfrId + " not found");
        }

        existingProfil.setPrflLibelle(profilDTO.getPrfl_libelle());

        // Mise à jour des droits du profil
        existingProfil.setValide(profilDTO.isValide());

        // Billet
        existingProfil.setAddBillet(profilDTO.isAdd_billet());
        existingProfil.setEditBillet(profilDTO.isEdit_billet());
        existingProfil.setCancelBillet(profilDTO.isCancel_billet());
        existingProfil.setAddCheckBillet(profilDTO.isAdd_check_billet());

        // Embarquement
        existingProfil.setAddEmbarqment(profilDTO.isAdd_embarqment());

        // Fret
        existingProfil.setAddFret(profilDTO.isAdd_fret());
        existingProfil.setEditFret(profilDTO.isEdit_fret());
        existingProfil.setCancelFret(profilDTO.isCancel_fret());
        existingProfil.setPayeFret(profilDTO.isPaye_fret());
        existingProfil.setDelFretDetails(profilDTO.isDel_fret_details());
        existingProfil.setCheckFret(profilDTO.isCheck_fret());

        // Paramétrage
        existingProfil.setAddVoyage(profilDTO.isAdd_voyage());
        existingProfil.setEditVoyage(profilDTO.isEdit_voyage());
        existingProfil.setPlanVoyage(profilDTO.isPlan_voyage());
        existingProfil.setViewVoyage(profilDTO.isView_voyage());
        existingProfil.setDoRemboursement(profilDTO.isDo_remboursement());
        existingProfil.setDoRepSurclassment(profilDTO.isDo_rep_surclassment());
        existingProfil.setEditParam(profilDTO.isEdit_param());
        existingProfil.setViewStat(profilDTO.isView_stat());
        existingProfil.setRechercher(profilDTO.isRechercher());
        existingProfil.setEdition(profilDTO.isEdition());
        existingProfil.setCancelVoyage(profilDTO.isCancel_voyage());
        existingProfil.setCheckFret(profilDTO.isCheck_fret());
        existingProfil.setViewEtat(profilDTO.isView_etat());

        // Blocage de places
        existingProfil.setBloqPlaces(profilDTO.isBloq_places());

        // Clients en compte
        existingProfil.setAddCltCompte(profilDTO.isAdd_clt_compte());
        existingProfil.setEditCltCompte(profilDTO.isEdit_clt_compte());
        existingProfil.setDelCltCompte(profilDTO.isDel_clt_compte());

        // Facturation
        existingProfil.setAddFacture(profilDTO.isAdd_facture());
        existingProfil.setEditFacture(profilDTO.isEdit_facture());
        existingProfil.setDelFacture(profilDTO.isDel_facture());

        // Règlement
        existingProfil.setAddReglement(profilDTO.isAdd_reglement());
        existingProfil.setEditReglement(profilDTO.isEdit_reglement());
        existingProfil.setDelReglement(profilDTO.isDel_reglement());

        // Passager
        existingProfil.setAddPassager(profilDTO.isAdd_passager());
        existingProfil.setEditPassager(profilDTO.isEdit_passager());
        existingProfil.setDelPassager(profilDTO.isDel_passager());

        // Statut actif
        existingProfil.setActif(profilDTO.isActif());

        // DEC/Reclamation
        existingProfil.setCampagne(profilDTO.isCampagne());
        existingProfil.setReclamation(profilDTO.isReclamation());

        // Sauvegarde et retour
        return profilRepository.save(existingProfil);
    }

    public List<ProfilDTO> getAllProfil()
    {
        List<Profil> prf = profilRepository.findAll();
        return prf.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
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


    public List<User> getAllUserByProfil(long profilId)
    {
        return userRepository.findUsersByProfilId(profilId);
    }

    public Niveau createNiveau(NiveauDTO niveauDTO)
    {
        Niveau niveau = Niveau.builder()
                .nivNom(niveauDTO.getNiv_nom())
                .nivAffiche(niveauDTO.getNiv_affiche())
                .build();

        return niveauRepository.save(niveau);
    }

    public Niveau updateNiveau(long id, NiveauDTO niveauDTO){
        Niveau niveau = this.niveauRepository.findById(id).orElse(null);
        if (niveau != null) {
            niveau.setNivNom(niveauDTO.getNiv_nom());
            return niveauRepository.save(niveau);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public TypePlace createTypePlace(TypePlaceDTO typePlaceDTO)
    {
        TypePlace tPlace = TypePlace.builder()
                .tplcCode(typePlaceDTO.getTplcCode())
                .tplcNom(typePlaceDTO.getTplcNom())
                .tplcPrix(typePlaceDTO.getTplcPrix())
                .build();

        return typePlaceRepository.save(tPlace);
    }

    public String getPlaceName(long idTplc)
    {
        return typePlaceRepository.findNameTypePlace(idTplc);
    }

    public Voyage getVoyByDept(String voyDate, int voyDep, long batId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = null;
        try
        {
            localDate = LocalDate.parse(voyDate, formatter);
            System.out.println("Date convertie en LocalDate : " + localDate);
        }
        catch (DateTimeParseException e)
        {
            System.err.println("Erreur lors de la conversion de la date : " + e.getMessage());
        }
        Voyage voy1 = voyageRepository.findByDateAndDepart(localDate, voyDep, batId);
        return voy1;
    }

    public List<VoyageDTO> getVoyByDat(long batId)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = null;
        try
        {
            localDate = LocalDate.parse(LocalDate.now().toString(), formatter);
            System.out.println("Date convertie en LocalDate : " + localDate);
        }
        catch (DateTimeParseException e)
        {
            System.err.println("Erreur lors de la conversion de la date : " + e.getMessage());
        }
        List<Voyage> voys = voyageRepository.findByDate(localDate, batId);
        return voys.stream()
                .map(this::convertToDTO7)
                .collect(Collectors.toList());
    }

    public List<VoyageDTO> getAllVoySinceNow()
    {
        List<Voyage> voyages = voyageRepository.findAllFromDate(LocalDate.now());
        return voyages.stream()
                .map(this::convertToDTO7)
                .collect(Collectors.toList());
    }

    private VoyageDTO convertToDTO7(Voyage voyage) {
        VoyageDTO dto = new VoyageDTO();
        dto.setVoy_id(voyage.getVoyId());
        dto.setVoy_depart(voyage.getVoyDepart());
        dto.setVoy_destination(voyage.getVoyDestination());
        dto.setCode_voyage(voyage.getCodeVoyage());
        dto.setBat_id(voyage.getBateau().getBatId());
        dto.setVoy_datedpt(voyage.getVoyDatedpt());
        dto.setCode_voyage(voyage.getCodeVoyage());
        return dto;
    }

    private Niveau getNiveau(long niveauValue) {
        // Logic to retrieve Niveau from the database or any other source
        // This is a placeholder implementation
        return niveauRepository.findById(niveauValue).orElse(null);
    }

    // Méthode pour obtenir une instance de TypePlace à partir d'un identifiant ou d'un nom
    private TypePlace getTypePlace(long typePlaceValue) {
        // Logic to retrieve TypePlace from the database or any other source
        // This is a placeholder implementation
        return typePlaceRepository.findById(typePlaceValue).orElse(null);
    }

    private Unite getTypeUnite(long typeUniteValue) {
        // Logic to retrieve TypePlace from the database or any other source
        // This is a placeholder implementation
        return uniteRepository.findById(typeUniteValue).orElse(null);
    }

    private Volume getTypeVolume(long volumeValue) {
        // Logic to retrieve TypePlace from the database or any other source
        // This is a placeholder implementation
        return volumeRepository.findById(volumeValue).orElse(null);
    }

    // Méthode pour obtenir une instance de Bateau à partir d'un identifiant ou d'un nom
    private Bateau getBateau(long bateauValue) {
        // Logic to retrieve Bateau from the database or any other source
        // This is a placeholder implementation
        return bateauRepository.findById(bateauValue).orElse(null);
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return "";  // ou une valeur par défaut selon votre logique
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Utilisez le format de date approprié si c'est une date
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    // Gestion des valeurs numériques (peut nécessiter un formattage approprié)
                    return String.valueOf((int)cell.getNumericCellValue());
                }
            default:
                return "";  // ou une valeur par défaut selon votre logique
        }
    }

    /*
    public void importExcel(MultipartFile file) throws IOException {
        List<TypeBagage> typeBagages = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header row
                    continue;
                }
                TypeBagage typeBagage = new TypeBagage();
                typeBagage.setTbgNom(getStringValue(row.getCell(0)));

                String uniteValueStr = getStringValue(row.getCell(1));
                Long uniteValue = Long.parseLong(uniteValueStr);
                Unite unite = getTypeUnite(uniteValue);
                typeBagage.setUnite(unite);

                typeBagage.setEtat(Boolean.parseBoolean(row.getCell(2).getStringCellValue()));
                typeBagage.setCode(row.getCell(3).getStringCellValue());

                String volValueStr = getStringValue(row.getCell(4));
                Long volValue = Long.parseLong(volValueStr);
                Volume vol = getTypeVolume(volValue);
                typeBagage.setVolume(vol);

                //String niveauValueStr =  getStringValue(row.getCell(2));
                //Long niveauValue = Long.parseLong(niveauValueStr);
                //Niveau niveau = getNiveau(niveauValue);
                //place.setNiveau(niveau);

                //String bateauValueStr =  getStringValue(row.getCell(3));
                //Long bateauValue = Long.parseLong(bateauValueStr);
                //Bateau bateau = getBateau(bateauValue);
                //place.setBateau(bateau);

                //place.setPlcEtat(Boolean.parseBoolean(getStringValue(row.getCell(4))));
                //place.setSexe( getStringValue(row.getCell(5)));
                typeBagages.add(typeBagage);
            }
        }

        typeBagageRepository.saveAll(typeBagages);
    }
     */

    public List<Nationalite> getAllCountries()
    {
        return nationalityRepository.findAll();
    }

    public List<TypePlacesDTO> getAllTypePlace()
    {
        List<TypePlace> typePlaces = typePlaceRepository.findAll();
        return typePlaces.stream()
                .map(this::convertToDTO4)
                .collect(Collectors.toList());
    }

    private TypePlacesDTO convertToDTO4(TypePlace typePlace) {
        // Convertir Unite en UniteDTO
        TypePlacesDTO dto = new TypePlacesDTO();
        dto.setTplc_id(typePlace.getTplcId());
        dto.setTplc_nom(typePlace.getTplcNom());

        return dto;
    }

    public List<Critere> getAllCritere()
    {
        return critereRepository.findAll();
    }

    public List<Volume> getAllVolume()
    {
        return volumeRepository.findAll();
    }

}
