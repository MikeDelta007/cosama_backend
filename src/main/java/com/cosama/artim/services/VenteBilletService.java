package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.models.ClientEnCompte;
import com.cosama.artim.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.cosama.artim.models.SingleMessageDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VenteBilletService {

    private static final Logger logger = LoggerFactory.getLogger(VenteBilletService.class);

    @Autowired
    private final VoyageRepository voyageRepository;
    @Autowired
    private final PlaceRepository placeRepository;
    @Autowired
    private final TypePlaceRepository typePlaceRepository;
    @Autowired
    private final VoyagePlaceRepository voyagePlaceRepository;
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private final PassagerRepository passagerRepository;
    @Autowired
    private final ClientEnCompteRepository clientEnCompteRepository;
    @Autowired
    private final TypePieceRepository typePieceRepository;
    @Autowired
    private final BilletRepository billetRepository;
    @Autowired
    private final BateauRepository bateauRepository;
    @Autowired
    private final CritereRepository critereRepository;
    @Autowired
    private final NationaliteRepository nationaliteRepository;
    @Autowired
    private final EnfantRepository enfantRepository;
    @Autowired
    private final EtatBilletRepository etatBilletRepository;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final FretRepository fretRepo;
    @Autowired
    private final FretCltRepository fretClientRepo;
    @Autowired
    private final MessageService smsService;


    @PersistenceContext
    private EntityManager entityManager;


    public VoyagePlace getVPByVoyAndBat(Long voyId, Long batId, Long plcId)
    {
        // Récupérer les billets associés
        VoyagePlace vp = voyagePlaceRepository.findPlaceByVoyAndBat(voyId, batId, plcId);
        // Convertir la liste de billets en une liste de BilletDTO
        if (vp == null)
        {
            return null;
        }
        else
        {
            return vp;
        }
    }

    //recherche tarif selon type_place et criteres

    public Long getVoy(Long bateauId) {
        List<Long> result = voyageRepository.findVoyIdByBateauAndEtat(bateauId);

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            result = voyageRepository.findVoyIdByBateau(bateauId);
            return result.isEmpty() ? null : result.get(0);
        }
    }

    public List<PlaceDTO> getAvailablePlaces(Long tplcId, Long voyId, Long batId) {
        List<Place> places = placeRepository.findAvailablePlaces(tplcId, voyId, batId);
        return places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    private PlaceDTO convertToDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setPlc_id(place.getPlcId());
        dto.setPlc_code(place.getPlcCode());
        dto.setSexe(place.getSexe());
        dto.setSituation(place.getSituation());
        dto.setTplc_id(place.getTypePlaceId());
        dto.setBat_id(place.getBateau().getBatId());
        return dto;
    }

    public List<PlaceDTO> getPlaceReserved(Long batId) {
        List<Place> places = placeRepository.placeReserved(batId);
        return places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    public List<PlaceDTO> getReservedPlaces(Long tplcId, Long voyId, Long batId) {
        List<Place> places = placeRepository.findReservedPlaces(tplcId, voyId, batId);
        return places.stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());

    }

    private PlaceDTO convertToDTO2(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setPlc_id(place.getPlcId());
        dto.setPlc_code(place.getPlcCode());
        dto.setSexe(place.getSexe());
        dto.setSituation(place.getSituation());
        dto.setTplc_id(place.getTypePlaceId());
        dto.setBat_id(place.getBateau().getBatId());
        boolean a = dto.getTplc_id() == 1;
        boolean b = dto.getTplc_id() == 2;
        boolean c = dto.getTplc_id() == 3;
        boolean d = dto.getTplc_id() == 4;
        if (a){dto.setColor("success");}
        if (b){dto.setColor("warning");}
        if (c){dto.setColor("danger");}
        if (d){dto.setColor("info");}
        return dto;
    }

    public Long getFirstTypePlaceId() {
        List<TypePlace> typePlaces = typePlaceRepository.findAll();
        if (typePlaces.isEmpty()) {
            return 0L;
        }
        return typePlaces.get(0).getTplcId();
    }

    //occupe_place
    @Transactional
    public int occupePlace(Long plcId, Long voyId, Long batId, String codeBillet) {
        try {
            // Récupérer les entités Bateau, Place et Voyage correspondantes
            Bateau bateau = bateauRepository.findById(batId).orElse(null);
            Place place = placeRepository.findById(plcId).orElse(null);
            Voyage voyage = voyageRepository.findById(voyId).orElse(null);

            if (bateau == null || place == null || voyage == null) {
                // Si une des entités n'est pas trouvée, retourner échec
                return 0;
            }

            // Créer une nouvelle instance de VoyagePlace et définir ses relations
            VoyagePlace voyagePlace = new VoyagePlace();
            voyagePlace.setVpl_etat(1); // Assumant que 1 signifie "occupé"
            voyagePlace.setBateau(bateau);
            voyagePlace.setPlace(place);
            voyagePlace.setVoyage(voyage);
            voyagePlace.setCode_billet(codeBillet);

            // Sauvegarder VoyagePlace
            voyagePlaceRepository.save(voyagePlace);

            return 1; // Succès
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Échec
        }
    }

    //liberer_place (à verifier avec disponible_place)
    @Transactional
    public String disponiblePlace(Long plcId, Long voyId, Long batId)
    {
        try
        {
            // Chercher l'entrée correspondante dans la table VoyagePlace
            VoyagePlace voyagePlace = voyagePlaceRepository.findByPlacePlcIdAndVoyageVoyIdAndBateauBatId(plcId, voyId, batId);
            if (voyagePlace != null)
            {
                // Supprimer l'entrée de la table VoyagePlace
                voyagePlaceRepository.delete(voyagePlace);
            }
            return "Succés"; // Succès
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Echec"; // Échec
        }
    }

    public void liberationPlace(Long plcId, Long voyId, Long batId)
    {
        try
        {
            // Chercher l'entrée correspondante dans la table VoyagePlace
            VoyagePlace voyagePlace = voyagePlaceRepository.findByPlacePlcIdAndVoyageVoyIdAndBateauBatId(plcId, voyId, batId);
            if (voyagePlace != null && voyagePlace.getVpl_etat() == 2)
            {
                System.out.println(voyagePlace.getPlace().getPlcId());
                // Supprimer l'entrée de la table VoyagePlace
                voyagePlaceRepository.delete(voyagePlace);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void shotPlace(Long plcId, Long voyId, Long batId)
    {
        try
        {
            // Chercher l'entrée correspondante dans la table VoyagePlace
            VoyagePlace voyagePlace = voyagePlaceRepository.findByPlacePlcIdAndVoyageVoyIdAndBateauBatId(plcId, voyId, batId);
            //System.out.println(voyagePlace.getPlace().getPlcId());
            // Supprimer l'entrée de la table VoyagePlace
            voyagePlaceRepository.delete(voyagePlace);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void annulerVoyage(Long voyId, String motif)
    {
        try
        {
            Voyage theVoy = voyageRepository.findById(voyId).orElse(null);
            assert theVoy != null;
            theVoy.setVoyEtat(0);
            theVoy.setMotif(motif);
            voyageRepository.save(theVoy);

            List<Billet> billets = billetRepository.findByVoyId(voyId);
            if (billets == null)
            {
                log.info("Aucun billet n'a été retrouvé");
            }
            else
            {
                List<SmsRecipient> billetsTextos = new ArrayList<>();

                for (Billet b : billets) {
                    // Met à jour l'état du billet
                    b.setBilEtat(false);
                    Billet sortie = billetRepository.save(b);

                    // Récupère le passager associé
                    Passager pax = passagerRepository.findById(sortie.getPassager().getPaxId()).orElse(null);
                    if (pax == null) {
                        throw new RuntimeException("Passager introuvable pour le billet ID : " + sortie.getBilId());
                    }

                    // Création du destinataire du SMS
                    SmsRecipient texto = new SmsRecipient();
                    texto.setId((int) sortie.getBilId());
                    texto.setValue(pax.getPhone());
                    billetsTextos.add(texto);

                    // Suppression de la réservation de la place
                    VoyagePlace voyagePlace = voyagePlaceRepository.findByVoyageVoyId(voyId, sortie.getBilCode());
                    if (voyagePlace != null)
                    {
                        voyagePlaceRepository.delete(voyagePlace);
                    }
                    log.info("Annulation effectuée avec succès pour le billet {}", sortie.getBilCode());
                }

                // Récupération du voyage
                Voyage v = voyageRepository.findById(theVoy.getVoyId()).orElse(null);
                if (v == null) {
                    throw new RuntimeException("Voyage introuvable pour l'ID : " + theVoy.getVoyId());
                }

                // Préparation du message SMS
                SingleMessageDetails sms = new SingleMessageDetails();
                sms.setSignature("COSAMA");
                sms.setSubject("Annulation de voyage");
                sms.setContent("Votre voyage du : " + v.getVoyDatedpt() + " a été annulé");
                sms.setRecipients(billetsTextos);

                // Envoi du SMS
                smsService.sendSms("808f77dd3df865774d33996cce2f4782", sms);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public VoyagePlace blocagePlace(Long plcId, Long voyId, Long batId) {
        try {
            // Récupérer les entités Bateau, Place et Voyage correspondantes
            Bateau bateau = bateauRepository.findById(batId).orElse(null);
            Place place = placeRepository.findById(plcId).orElse(null);
            Voyage voyage = voyageRepository.findById(voyId).orElse(null);

            if (bateau == null || place == null || voyage == null) {
                // Si une des entités n'est pas trouvée, retourner échec
                return null;
            }

            // Créer une nouvelle instance de VoyagePlace et définir ses relations
            VoyagePlace voyagePlace = new VoyagePlace();
            voyagePlace.setVpl_etat(2); // Assumant que 1 signifie "occupé"
            voyagePlace.setBateau(bateau);
            voyagePlace.setPlace(place);
            voyagePlace.setVoyage(voyage);

            // Sauvegarder VoyagePlace
            return voyagePlaceRepository.save(voyagePlace);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //est_dispo
    public boolean isPlaceAvailable(Long voyId, Long plcId) {
        Voyage BatVoy = voyageRepository.findBateauByvoyId(voyId);
        int voyagePlaceCount = voyagePlaceRepository.countByVoyageAndPlaceAndBateau(voyId, plcId, BatVoy.getBateau().getBatId());
        int validBilletCount = voyagePlaceRepository.countValidBilletsByVoyageAndPlace(voyId, plcId);
        System.out.println("IS PLACE AVAILABLE :" + voyagePlaceCount + " " + validBilletCount);
        return voyagePlaceCount == 0 && validBilletCount == 0;
    }

    @Transactional
    public void deleteCriteresBillet(Long bilId) {
        String selectSql = "SELECT COUNT(*) FROM billet_critere WHERE bil_id = ?1";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter(1, bilId);
        Long count = (Long) selectQuery.getSingleResult();

        if (count > 0) {
            // Il y a des entrées pour ce billet_id, nous pouvons procéder à la suppression
            String deleteSql = "DELETE FROM billet_critere WHERE bil_id = ?1";
            Query deleteQuery = entityManager.createNativeQuery(deleteSql);
            deleteQuery.setParameter(1, bilId);
            int rowsAffected = deleteQuery.executeUpdate();
            System.out.println(rowsAffected + " lignes supprimées.");
        }
    }

    @Transactional
    public void addCriteresBillet(List<Critere> criteres, Long bilId) {
        logger.debug(criteres.toString());
        if (criteres == null || criteres.isEmpty()) {
            // logger.debug("No criteria provided to add to billet with ID: " + bilId);
            return;
        }

        // logger.debug("Adding criteria to billet with ID: " + bilId);

        for (Critere critere : criteres) {
            //logger.debug("Adding critere with ID: " + critere + " to billet with ID: " + bilId);
            String sql = "INSERT INTO billet_critere (bil_id, crt_id) VALUES (?1, ?2)";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, bilId);
            query.setParameter(2, critere);
            query.executeUpdate();

        }
    }


    //place_dispo
    public Place placeDispo(Long tplId, Long voyId, Long batId) {
        String sql = "SELECT * FROM place " +
                "WHERE tpl_id = ? " +
                "AND plc_id NOT IN (SELECT plc_id FROM voyage_place WHERE vpl_etat <> 0 AND voy_id = ? AND bat_id = ?) " +
                "AND bat_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{tplId, voyId, batId, batId}, (rs, rowNum) -> {
            Place place = new Place();
            place.setPlcId(rs.getLong("plc_id"));
            // Ajouter d'autres attributs de place si nécessaire
            return place;
        });
    }

    public PassagerWithBilletDTO findPassenger(String cinorpass)
    {
        Passager passager = passagerRepository.findByCinOrPassport(cinorpass).orElse(null);
        PassagerWithBilletDTO pwb = convertToDTOX(passager);
        return pwb;
    }

    private PassagerWithBilletDTO convertToDTOX(Passager passager) {
        PassagerWithBilletDTO dto = new PassagerWithBilletDTO();

        // Conversion des champs simples
        if (passager != null) {
            dto.setCivilite(passager.getCivilite());
            dto.setLastName(passager.getLastName());
            dto.setFirstName(passager.getFirstName());
            dto.setNumeropiece(passager.getNumeropiece());

            if (passager.getNationalite() != null) {
                dto.setNatId(passager.getNationalite().getNatId());
            }

            if (passager.getTypePiece() != null) {
                dto.setTpiece_id(passager.getTypePiece().getTpieceId());
            }

            dto.setPhone(passager.getPhone());
        }
        else
        {
            // Logger une erreur ou gérer proprement
            System.err.println("Le passager est null. Impossible de construire le DTO.");
        }
        return dto;
    }


    public String callRemoteFunction2() {
        String sql = "SELECT get_next_order_number_billet()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeBillet_generator(String OrderNumber2)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        log.info("formattedInput est {} :" + OrderNumber2);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);
        String c =  "B-" + OrderNumber2 + mois + an;
        return c;
    }

    public BilletsDTO createDirectBillet(PassagerWithBilletDTO passagerWithBilletDTO, Long voyId)
    {
        Voyage voy1 = voyageRepository.findById(voyId).orElse(null);
        String ordreString2 = this.callRemoteFunction2();
        String code_billet_ = this.codeBillet_generator(ordreString2);
        Billet billet = this.createPassagerWithBillets2(passagerWithBilletDTO, voy1, "G", code_billet_, voy1.getVoyDatedpt().atStartOfDay());
        BilletsDTO billetDTO = convertToDTOX(billet);
        return billetDTO;

    }

    private BilletsDTO convertToDTOX(Billet billet) {
        BilletsDTO dto = new BilletsDTO();

        // Conversion des champs simples
        dto.setBilId(billet.getBilId());
        dto.setBilCode(billet.getBilCode());
        dto.setCode_achat(billet.getCode_achat());
        dto.setIpVente(billet.getIpVente());
        dto.setFirstname(billet.getFirstname());
        dto.setLastname(billet.getLastname());
        dto.setNumeropiece(billet.getNumeropiece());
        dto.setCivilite(billet.getCivilite());
        dto.setBilPht(billet.getBilPht());
        dto.setBilPtt(billet.getBilPtt());
        dto.setBilTaxe(billet.getBilTaxe());
        dto.setBilRemise(billet.getBilRemise());

        // Conversion des dates avec format JSON
        dto.setBilDateEmission(billet.getBilDateEmission());
        dto.setBilDateValidite(billet.getBilDateValidite());

        dto.setBilEtat(billet.getBilEtat());
        dto.setBilPenalite(billet.getBilPenalite());
        dto.setBilReporter(billet.getBilReporter());
        dto.setNoShow(billet.getNoShow());
        dto.setDateNoShow(billet.getDateNoShow());

        dto.setUserModif(billet.getUserModif());
        dto.setDateModif(billet.getDateModif());

        dto.setUserAnnule(billet.getUserAnnule());
        dto.setDateAnnule(billet.getDateAnnule());

        dto.setUserEmbarq(billet.getUserEmbarq());
        dto.setDateEmbarq(billet.getDateEmbarq());

        dto.setUserDebarque(billet.getUserDebarque());
        dto.setDateDebarque(billet.getDateDebarque());

        dto.setUserRembours(billet.getUserRembours());
        dto.setDateRembours(billet.getDateRembours());
        dto.setMtnRembours(billet.getMtnRembours());
        dto.setMotifRembours(billet.getMotifRembours());

        dto.setEdit(billet.getEdit());
        dto.setRemb(billet.getRemb());
        dto.setRep_sur(billet.getRep_sur());
        dto.setCancel(billet.getCancel());

        // Conversion des IDs de relation
        //dto.setTypePieceId(billet.getTypePiece().getTpieceId());
        dto.setTypePlaceId(billet.getTypePlace().getTplcId());

        //dto.setClientEnCompteId(billet.getClientEnCompte().getCltcmptId());
        dto.setPassagerId(billet.getPassager().getPaxId());
        //dto.setVoyageId(billet.getVoyage().getVoyId());

        //dto.setVoyageId(billet.getVoyage().getVoyId());
        dto.setBatId(billet.getBatId());
        dto.setPlcId(billet.getPlcId());
        dto.setNatId(billet.getNationalite());
        dto.setTypePieceId(billet.getTypePiece().getTpieceId());

        VoyageDTO vdto = new VoyageDTO();
        vdto.setVoy_id(billet.getVoyage().getVoyId());
        vdto.setBat_id(billet.getVoyage().getBateau().getBatId());
        vdto.setVoy_datedpt(billet.getVoyage().getVoyDatedpt());
        vdto.setVoy_depart(billet.getVoyage().getVoyDepart());
        vdto.setVoy_destination(billet.getVoyage().getVoyDestination());
        vdto.setCode_voyage(billet.getVoyage().getCodeVoyage());

        EnfantDTO edto = new EnfantDTO();

        if (billet.getEnfants() != null && !billet.getEnfants().isEmpty()) {
            for (Enfant enfant : billet.getEnfants()) {
                // Traitez chaque enfant ici
                edto.setEnfNomComplet(enfant.getEnfNomComplet());
                edto.setEnfAge(enfant.getEnfAge());
                edto.setUniteTemps(enfant.getUniteTemps());
            }
        }

        // Supposons que billet contient une liste d'enfants
        List<CritereDTO> ctdto = new ArrayList<>();

        // Parcourir la liste des enfants dans billet
        for (Critere crtDto : billet.getCriteres()) {
            // Créer un nouveau EnfantDTO pour chaque enfant
            CritereDTO cdto = new CritereDTO();

            // Remplir les informations de l'enfant à partir de billet
            cdto.setCrtId(crtDto.getCrtId());
            cdto.setCrt_nom(crtDto.getCrt_nom());

            // Ajouter l'EnfantDTO à la liste
            ctdto.add(cdto);
        }

        dto.setEnfantDTOS(edto);
        dto.setCritereIds(ctdto);
        dto.setVoyageDTO(vdto);

        return dto;
    }

    //Creer un passager avec son billet
    public Billet createPassagerWithBillets2(PassagerWithBilletDTO passagerWithBilletDTO, Voyage voy, String code_achat, String code_billet, LocalDateTime dateEmis)
    {
        //long cinOrPassPort = Long.parseLong(cinorpass);
        String cinorpass = passagerWithBilletDTO.getNumeropiece();
        Passager passager = passagerRepository.findByCinOrPassport(cinorpass).orElse(null);
        Nationalite nationalite = nationaliteRepository.findById(passagerWithBilletDTO.getNatId()).orElse(null);
        TypePiece type_piece = typePieceRepository.findById(passagerWithBilletDTO.getTpiece_id()).orElse(null);
        System.out.println(passagerWithBilletDTO.getNatId());

        // Créer le passager s'il n'existe pas
        if (passager == null || passagerRepository.count() == 0) {
            passager = new Passager();
            // Initialiser les champs du passager à partir du DTO
            passager.setNumeropiece(cinorpass);
            passager.setCivilite(passagerWithBilletDTO.getCivilite());
            passager.setFirstName(passagerWithBilletDTO.getFirstName());
            passager.setLastName(passagerWithBilletDTO.getLastName());
            passager.setDateNaiss(passagerWithBilletDTO.getDateNaiss());
            passager.setPhone(passagerWithBilletDTO.getPhone());
            passager.setNationalite(nationalite);
            passager.setTypePiece(type_piece);
            // Initialiser d'autres champs du passager
            passager = passagerRepository.save(passager);
        }

        // Créer et associer les billets au passager
        BilletDTO billetDTO = passagerWithBilletDTO.getBilletsDTOS();
        Billet billet = new Billet();
        EnfantDTO enfantDTO = billetDTO.getEnfantDTOS();
        Enfant enf = new Enfant();
        Billet bilSaved = new Billet();

        // Créez une liste de Critere à partir des IDs fournis
        List<Critere> criteres = billetDTO.getCritereIds().stream()
                .filter(Objects::nonNull) // 🔹 Supprime les valeurs nulles
                .map(id -> critereRepository.findById(id.getCrtId())
                        .orElseThrow(() -> new RuntimeException("Critere not found: " + id)))
                .collect(Collectors.toList());

        TypePiece typePiece = typePieceRepository.findById(billetDTO.getTypePieceId()).orElse(null);
        List<Number> tarif = tarification.tarifPlaceTab(billetDTO.getTypePlaceId(), criteres);
        ClientEnCompte cltEnCpt = clientEnCompteRepository.findById(billetDTO.getClientEnCompteId()).orElse(null);
        TypePlace typePlace = typePlaceRepository.findById(billetDTO.getTypePlaceId()).orElse(null);
        Nationalite nat = nationaliteRepository.findById(billetDTO.getNatId()).orElse(null);
        //Voyage voy = voyageRepository.findById(billetDTO.getVoyageId()).orElse(null);

        System.out.print("Check and fill place");
        if(this.isPlaceAvailable(voy.getVoyId(), billetDTO.getPlcId()))
        {
            System.out.println("Place pas encore occupée");
            if (billetDTO.getClientEnCompteId() != 0) {
                if (cltEnCpt != null) {
                    billet.setClientEnCompte(cltEnCpt);
                } else {
                    // Log a warning or throw an exception depending on your logic
                    throw new IllegalArgumentException("ClientEnCompte not found for the given ID.");
                }
            } else {
                billet.setClientEnCompte(null);  // Ensure this is valid according to your schema
            }
            billet.setFirstname(passager.getFirstName());
            billet.setLastname(passager.getLastName());
            billet.setCivilite(passager.getCivilite());
            billet.setNumeropiece(passager.getNumeropiece());
            billet.setNationalite(passager.getNationalite().getNatId());
            //billet.setTypePiece(passager.getTypePiece());
            billet.setBilPht((Float) tarif.get(0));
            billet.setBilRemise((Float) tarif.get(1));
            billet.setBilTaxe((Float) tarif.get(2));
            billet.setBilPtt((Float) tarif.get(4));

            billet.setBilDateEmission(LocalDateTime.now());
            System.out.println(billet.getBilDateEmission());
            billet.setBilDateValidite(dateEmis.plusDays(25));
            billet.setBilEtat(true);
            billet.setTypePiece(typePiece);
            billet.setTypePlace(typePlace);
            billet.setNationalite(nat.getNatId());
            billet.setVoyage(voy);
            assert voy != null;
            billet.setBatId(voy.getBateau().getBatId());
            billet.setPlcId(billetDTO.getPlcId());
            billet.setCriteres(criteres);
            billet.setCode_achat(code_achat);
            billet.setBilCode(code_billet);

            //Le paiement marchand interviendra à ce niveau

            int oc_place = occupePlace(billet.getPlcId(), billet.getVoyage().getVoyId(), billet.getBatId(), billet.getBilCode());
            if (oc_place==1)
            {
                System.out.println("Place occupée avec succés");
                EtatBillet eb = new EtatBillet();
                billet.setPassager(passager);
                bilSaved = billetRepository.save(billet);
                enf.setEnfNomComplet(enfantDTO.getEnfNomComplet());
                enf.setEnfAge(enfantDTO.getEnfAge());
                enf.setUniteTemps(enfantDTO.getUniteTemps());
                enf.setBillet(bilSaved);
                bilSaved.setEnfants(List.of(enf));
                enfantRepository.save(enf);
                eb.setBilTime(LocalDateTime.now());
                eb.setColor("blue");
                eb.setStatus("Payé");
                eb.setIcon("PrimeIcons.SHOPPING_CART");
                eb.setBillet(bilSaved);
                etatBilletRepository.save(eb);

            }
        }
        else
        {
            System.out.println("Place déjà occupée");
            throw new IllegalArgumentException("Place déjà occupée");
        }

        //deleteCriteresBillet(billet.getBilId());
        //addCriteresBillet(billet.getCriteres(), billet.getBilId());

        return bilSaved;
    }


    //Creer un passager avec son billet
    public Passager createPassagerWithBillets(PassagerWithBilletDTO passagerWithBilletDTO, Voyage voy, String code_achat, String code_billet, LocalDateTime dateEmis)
    {
        //long cinOrPassPort = Long.parseLong(cinorpass);
        String cinorpass = passagerWithBilletDTO.getNumeropiece();
        Passager passager = passagerRepository.findByCinOrPassport(cinorpass).orElse(null);
        Nationalite nationalite = nationaliteRepository.findById(passagerWithBilletDTO.getNatId()).orElse(null);
        TypePiece type_piece = typePieceRepository.findById(passagerWithBilletDTO.getTpiece_id()).orElse(null);
        System.out.println(passagerWithBilletDTO.getNatId());

        // Créer le passager s'il n'existe pas
        if (passager == null || passagerRepository.count() == 0) {
            passager = new Passager();
            // Initialiser les champs du passager à partir du DTO
            passager.setNumeropiece(cinorpass);
            passager.setCivilite(passagerWithBilletDTO.getCivilite());
            passager.setFirstName(passagerWithBilletDTO.getFirstName());
            passager.setLastName(passagerWithBilletDTO.getLastName());
            passager.setDateNaiss(passagerWithBilletDTO.getDateNaiss());
            passager.setPhone(passagerWithBilletDTO.getPhone());
            passager.setNationalite(nationalite);
            passager.setTypePiece(type_piece);
            // Initialiser d'autres champs du passager
            passager = passagerRepository.save(passager);
        }

        // Créer et associer les billets au passager
        BilletDTO billetDTO = passagerWithBilletDTO.getBilletsDTOS();
        Billet billet = new Billet();
        EnfantDTO enfantDTO = billetDTO.getEnfantDTOS();
        Enfant enf = new Enfant();
        Billet bilSaved = new Billet();

            // Créez une liste de Critere à partir des IDs fournis
            List<Critere> criteres = billetDTO.getCritereIds().stream()
                    .filter(Objects::nonNull) // 🔹 Supprime les valeurs nulles
                    .map(id -> critereRepository.findById(id.getCrtId())
                            .orElseThrow(() -> new RuntimeException("Critere not found: " + id)))
                    .collect(Collectors.toList());

        TypePiece typePiece = typePieceRepository.findById(billetDTO.getTypePieceId()).orElse(null);
            List<Number> tarif = tarification.tarifPlaceTab(billetDTO.getTypePlaceId(), criteres);
            ClientEnCompte cltEnCpt = clientEnCompteRepository.findById(billetDTO.getClientEnCompteId()).orElse(null);
            TypePlace typePlace = typePlaceRepository.findById(billetDTO.getTypePlaceId()).orElse(null);
            Nationalite nat = nationaliteRepository.findById(billetDTO.getNatId()).orElse(null);
            //Voyage voy = voyageRepository.findById(billetDTO.getVoyageId()).orElse(null);

            System.out.print("Check and fill place");
            if(this.isPlaceAvailable(voy.getVoyId(), billetDTO.getPlcId()))
            {
                System.out.println("Place pas encore occupée");
                if (billetDTO.getClientEnCompteId() != 0) {
                    if (cltEnCpt != null) {
                        billet.setClientEnCompte(cltEnCpt);
                    } else {
                        // Log a warning or throw an exception depending on your logic
                        throw new IllegalArgumentException("ClientEnCompte not found for the given ID.");
                    }
                } else {
                    billet.setClientEnCompte(null);  // Ensure this is valid according to your schema
                }
                billet.setFirstname(passager.getFirstName());
                billet.setLastname(passager.getLastName());
                billet.setCivilite(passager.getCivilite());
                billet.setNumeropiece(passager.getNumeropiece());
                billet.setNationalite(passager.getNationalite().getNatId());
                billet.setBilPht((Float) tarif.get(0));
                billet.setBilRemise((Float) tarif.get(1));
                billet.setBilTaxe((Float) tarif.get(2));
                billet.setBilPtt((Float) tarif.get(4));

                billet.setBilDateEmission(LocalDateTime.now());
                System.out.println(billet.getBilDateEmission());
                billet.setBilDateValidite(dateEmis.plusDays(25));
                billet.setBilEtat(true);
                billet.setTypePiece(typePiece);
                billet.setTypePlace(typePlace);
                billet.setNationalite(nat.getNatId());
                billet.setVoyage(voy);
                assert voy != null;
                billet.setBatId(voy.getBateau().getBatId());
                billet.setPlcId(billetDTO.getPlcId());
                billet.setCriteres(criteres);
                billet.setCode_achat(code_achat);
                billet.setBilCode(code_billet);

                //Le paiement marchand interviendra à ce niveau

                int oc_place = occupePlace(billet.getPlcId(), billet.getVoyage().getVoyId(), billet.getBatId(), billet.getBilCode());
                if (oc_place==1)
                {
                    System.out.println("Place occupée avec succés");
                    EtatBillet eb = new EtatBillet();
                    billet.setPassager(passager);
                    bilSaved = billetRepository.save(billet);
                    enf.setEnfNomComplet(enfantDTO.getEnfNomComplet());
                    enf.setEnfAge(enfantDTO.getEnfAge());
                    enf.setUniteTemps(enfantDTO.getUniteTemps());
                    enf.setBillet(bilSaved);
                    enfantRepository.save(enf);

                    eb.setBilTime(LocalDateTime.now());
                    eb.setColor("blue");
                    eb.setStatus("Payé");
                    eb.setIcon("PrimeIcons.SHOPPING_CART");
                    eb.setBillet(bilSaved);
                    etatBilletRepository.save(eb);

                }
            }
            else
            {
                System.out.println("Place déjà occupée");
                throw new IllegalArgumentException("Place déjà occupée");
            }

            //deleteCriteresBillet(billet.getBilId());
            //addCriteresBillet(billet.getCriteres(), billet.getBilId());

        return passager;
    }

    public Billet getBilletWithCriteres(long bilId)
    {
        return billetRepository.findById(bilId)
                .orElseThrow(() -> new NotFoundException("Billet not found with id " + bilId));
    }

    public List<EtatBilletDTO> getEtatBillet(String bilCode)
    {
        if (bilCode == "")
        {
            return null;
        }
        else
        {
            Billet billet = billetRepository.findByCodeBil(bilCode);
            long bilId = billet.getBilId();
            List<EtatBillet> etatBillets = etatBilletRepository.findByCodeBil(bilId);
            return etatBillets.stream()
                    .map(this::convertToDTO1)
                    .collect(Collectors.toList());
        }
    }

    private EtatBilletDTO convertToDTO1(EtatBillet etatBillet) {
        EtatBilletDTO dto = new EtatBilletDTO();
        dto.setEbId(etatBillet.getEbId());
        dto.setColor(etatBillet.getColor());
        dto.setIcon(etatBillet.getIcon());
        dto.setStatus(etatBillet.getStatus());
        dto.setBilTime(etatBillet.getBilTime());
        dto.setBilletId(etatBillet.getBillet().getBilId());
        return dto;
    }


    public EtatBillet addEtatBillet(EtatBilletDTO etatBilletDTO)
    {
        Billet billet = this.billetRepository.findById(etatBilletDTO.getBilletId()).orElse(null);
        EtatBillet etatBillet = EtatBillet.builder()
                .ebId(etatBilletDTO.getEbId())
                .status(etatBilletDTO.getStatus())
                .icon(etatBilletDTO.getIcon())
                .color(etatBilletDTO.getColor())
                .bilTime(LocalDateTime.now())
                .billet(billet)
                .build();

        return etatBilletRepository.save(etatBillet);
    }

    public BilletsDTO getBilletDetails(String billCode)
    {
        // Récupérer les billets associés
        Billet billet = billetRepository.findByCodeBil(billCode);
        // Convertir la liste de billets en une liste de BilletDTO
        if (billet == null)
        {
            return null;
        }
        else
        {
            BilletsDTO billetDTO = convertToDTO1(billet);
            return billetDTO;
        }
    }

    private BilletsDTO convertToDTO1(Billet billet) {
        BilletsDTO dto = new BilletsDTO();

        // Conversion des champs simples
        dto.setBilId(billet.getBilId());
        dto.setBilCode(billet.getBilCode());
        dto.setCode_achat(billet.getCode_achat());
        dto.setIpVente(billet.getIpVente());
        dto.setFirstname(billet.getFirstname());
        dto.setLastname(billet.getLastname());
        dto.setNumeropiece(billet.getNumeropiece());
        dto.setCivilite(billet.getCivilite());
        dto.setBilPht(billet.getBilPht());
        dto.setBilPtt(billet.getBilPtt());
        dto.setBilTaxe(billet.getBilTaxe());
        dto.setBilRemise(billet.getBilRemise());
        dto.setCancel(billet.getCancel());
        dto.setRemb(billet.getRemb());
        dto.setEdit(billet.getEdit());

        // Conversion des dates avec format JSON
        dto.setBilDateEmission(billet.getBilDateEmission());
        dto.setBilDateValidite(billet.getBilDateValidite());

        dto.setBilEtat(billet.getBilEtat());
        dto.setBilPrint(billet.getBilPrint());
        dto.setBilCheck(billet.getBilCheck());
        dto.setBilPenalite(billet.getBilPenalite());
        dto.setBilReporter(billet.getBilReporter());
        dto.setNoShow(billet.getNoShow());
        dto.setDateNoShow(billet.getDateNoShow());

        dto.setUserModif(billet.getUserModif());
        dto.setDateModif(billet.getDateModif());

        dto.setUserAnnule(billet.getUserAnnule());
        dto.setDateAnnule(billet.getDateAnnule());

        dto.setUserEmbarq(billet.getUserEmbarq());
        dto.setDateEmbarq(billet.getDateEmbarq());

        dto.setUserDebarque(billet.getUserDebarque());
        dto.setDateDebarque(billet.getDateDebarque());

        dto.setUserRembours(billet.getUserRembours());
        dto.setDateRembours(billet.getDateRembours());
        dto.setMtnRembours(billet.getMtnRembours());
        dto.setMotifRembours(billet.getMotifRembours());

        // Conversion des IDs de relation
        //dto.setTypePieceId(billet.getTypePiece().getTpieceId());
        dto.setTypePlaceId(billet.getTypePlace().getTplcId());

        //dto.setClientEnCompteId(billet.getClientEnCompte().getCltcmptId());
        dto.setPassagerId(billet.getPassager().getPaxId());
        //dto.setVoyageId(billet.getVoyage().getVoyId());

        //dto.setVoyageId(billet.getVoyage().getVoyId());
        dto.setBatId(billet.getBatId());
        dto.setPlcId(billet.getPlcId());
        dto.setNatId(billet.getNationalite());
        dto.setTypePieceId(billet.getTypePiece().getTpieceId());

        VoyageDTO vdto = new VoyageDTO();
        vdto.setVoy_id(billet.getVoyage().getVoyId());
        vdto.setBat_id(billet.getVoyage().getBateau().getBatId());
        vdto.setVoy_datedpt(billet.getVoyage().getVoyDatedpt());
        vdto.setVoy_depart(billet.getVoyage().getVoyDepart());
        vdto.setVoy_destination(billet.getVoyage().getVoyDestination());
        vdto.setCode_voyage(billet.getVoyage().getCodeVoyage());

        EnfantDTO edto = new EnfantDTO();
        //edto.setEnfNomComplet(billet.getEnfants().get(0).getEnfNomComplet());
        //edto.setEnfAge(billet.getEnfants().get(0).getEnfAge());
        //edto.setUniteTemps(billet.getEnfants().get(0).getUniteTemps());

        if (billet.getEnfants() != null && !billet.getEnfants().isEmpty()) {
            for (Enfant enfant : billet.getEnfants()) {
                // Traitez chaque enfant ici
                edto.setEnfNomComplet(enfant.getEnfNomComplet());
                edto.setEnfAge(enfant.getEnfAge());
                edto.setUniteTemps(enfant.getUniteTemps());
            }
        }

        // Supposons que billet contient une liste d'enfants
        List<CritereDTO> ctdto = new ArrayList<>();

        // Parcourir la liste des enfants dans billet
        for (Critere crtDto : billet.getCriteres()) {
            // Créer un nouveau EnfantDTO pour chaque enfant
            CritereDTO cdto = new CritereDTO();

            // Remplir les informations de l'enfant à partir de billet
            cdto.setCrtId(crtDto.getCrtId());
            cdto.setCrt_nom(crtDto.getCrt_nom());

            // Ajouter l'EnfantDTO à la liste
            ctdto.add(cdto);
        }

        dto.setEnfantDTOS(edto);
        dto.setCritereIds(ctdto);
        dto.setVoyageDTO(vdto);

        return dto;
    }

    public Billet patchBilCheckIn(Long bilId, Boolean checkIn)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId)
                .orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        logger.info("A ce niveau : " + checkIn);
        billet.setBilCheck(checkIn);
        logger.info("A ce niveau : " + billet.getVoyage().getVoyId());
        VoyagePlace vp = voyagePlaceRepository.checkPlace(billet.getVoyage().getVoyId(), billet.getPlcId());
        logger.info("A ce niveau 2 : " + billet.getBilEtat() + " " + vp.getVpl_etat());
        if (billet.getBilEtat() && vp.getVpl_etat() == 1)
        {
                logger.info("A ce niveau 2 : " + billet.getBilEtat() + " " + vp.getVpl_etat());
                vp.setVpl_etat(3);
                // Sauvegarder les modifications
                billetRepository.save(billet);
                voyagePlaceRepository.save(vp);
                EtatBillet eb = new EtatBillet();
                eb.setBilTime(LocalDateTime.now());
                eb.setColor("crimson");
                eb.setStatus("Check-In");
                eb.setIcon("PrimeIcons.QRCODE");
                eb.setBillet(billet);
                etatBilletRepository.save(eb);
                return billet;
        }
        else
        {
            return null;
        }
    }

    public Billet patchBilInboard(Long bilId)
    {
        Billet billet = billetRepository.findById(bilId)
                .orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        VoyagePlace vp = voyagePlaceRepository.checkPlace(billet.getVoyage().getVoyId(), billet.getPlcId());
        if (billet.getBilEtat() == true && vp.getVpl_etat() == 3)
        {
            vp.setVpl_etat(4);
            // Sauvegarder les modifications
            voyagePlaceRepository.save(vp);
            EtatBillet eb = new EtatBillet();
            eb.setBilTime(LocalDateTime.now());
            eb.setColor("green");
            eb.setStatus("Embarqué");
            eb.setIcon("PrimeIcons.CHECK_SQUARE");
            eb.setBillet(billet);
            etatBilletRepository.save(eb);
            return billet;
        }
        else
        {
            return null;
        }
    }

    public Billet patchBilPrint(Long bilId, PrintBilletDTO printBilletDTO)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId)
                .orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));

        billet.setBilPrint(printBilletDTO.getBilPrint());
        // Sauvegarder les modifications
        billetRepository.save(billet);
        return billet;
    }

    public Billet cancelBil(Long bilId)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        billet.setBilEtat(false);
        billet.setBilCheck(false);
        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        billetRepository.save(billet);
        EtatBillet eb = new EtatBillet();
        eb.setBilTime(LocalDateTime.now());
        eb.setColor("red");
        eb.setStatus("Annulé");
        eb.setIcon("PrimeIcons.TRASH");
        eb.setBillet(billet);
        etatBilletRepository.save(eb);
        return billet;
    }

    public Billet rembourser(long bilId, String motif)
    {
        Billet billet = this.billetRepository.findById(bilId).orElse(null);
        EtatBillet eb = new EtatBillet();
        eb.setBilTime(LocalDateTime.now());
        eb.setColor("purple");
        eb.setStatus("Remboursé");
        eb.setIcon("PrimeIcons.WALLET");
        eb.setBillet(billet);
        etatBilletRepository.save(eb);
        return billet;
    }

    public BilletsDTO rembourserBil(Long bilId, String motif, Integer penality, String usrRemb)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));

        double pena = billet.getBilPtt() * ((billet.getBilPenalite() + penality) / 100.0);
        // Arrondi à 2 décimales
        BigDecimal penaBD = new BigDecimal(pena).setScale(2, RoundingMode.HALF_UP);
        double penaRounded = penaBD.doubleValue();
        double toRem = billet.getBilPtt() - penaRounded;
        BigDecimal toRemBD = new BigDecimal(toRem).setScale(2, RoundingMode.HALF_UP);
        double toRemRounded = toRemBD.doubleValue();

        logger.info("PENA TAUX " + billet.getBilPenalite());
        logger.info("PENA INPUT " + penality);
        logger.info("REMBOURS2 " + toRemRounded);
        logger.info("PENA " + pena);
        logger.info("REMBOURS " + toRem);
        billet.setBilEtat(false);
        billet.setBilPenalite(billet.getBilPenalite() + penality);
        billet.setMotifRembours(motif);
        billet.setDateRembours(LocalDateTime.now());
        billet.setMtnRembours(toRemRounded);
        billet.setUserRembours(usrRemb);

        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        Billet v = billetRepository.save(billet);
        BilletsDTO billetDTO = convertToDTO1(v);  // Conversion du billet unique

        return billetDTO;
    }

    public Billet autEditBil(Long bilId)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        billet.setEdit(true);
        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        billetRepository.save(billet);
        return billet;
    }

    public Billet autRembBil(Long bilId)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        billet.setRemb(true);
        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        billetRepository.save(billet);
        return billet;
    }

    public Billet autRepBil(Long bilId)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        billet.setRep_sur(true);
        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        billetRepository.save(billet);
        return billet;
    }

    public Billet autCancBil(Long bilId)
    {
        // Trouver le billet par ID
        Billet billet = billetRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        billet.setBilEtat(false);
        //VoyagePlace vp = voyagePlaceRepository.findById(bilId).orElseThrow(() -> new RuntimeException("Billet not found with id " + bilId));
        // Sauvegarder les modifications
        billetRepository.save(billet);
        return billet;
    }


    public FretDTOs getFretByBillet(String billet)
    {
        Fret fret = fretRepo.getFretByBillet(billet);
        if (fret != null)
        {
            return this.convertToDTOX(fret);  // Retourne une liste contenant un seul DTO
        }
        else
        {
            return null;
        }

    }


    public FretDTOs convertToDTOX(Fret fret) {
        FretDTOs dto = new FretDTOs();
        // Conversion des champs simples
        dto.setFretCode(fret.getFretCode());
        dto.setFretId(fret.getFretId());
        dto.setExpEqDest(fret.isExpEqDest());
        dto.setRaisonSocialeDest(fret.getRaisonSocialeDest());
        dto.setFirstnameDest(fret.getFirstnameDest());
        dto.setLastnameDest(fret.getLastnameDest());
        dto.setTelephoneDest(fret.getTelephoneDest());
        dto.setEmailDest(fret.getEmailDest());
        dto.setFretAcompte(fret.getFretAcompte());
        dto.setFretMontant(fret.getFretMontant());
        dto.setFretTva(fret.getFretTva());
        dto.setFretRemiseTaux(fret.getFretRemiseTaux());
        dto.setFretRemise(fret.getFretRemise());
        dto.setFretMontant_ht(fret.getFretMontant_ht());
        dto.setApplyTVA(fret.isApplyTVA());
        dto.setApplyPayment(fret.isApplyPayment());
        dto.setBillet(fret.getBillet());

        // Conversion des dates avec format JSON
        dto.setFretDate(fret.getFretDate());
        dto.setFretPayDate(fret.getFretPayDate());
        dto.setDateMagasinage(fret.getDateMagasinage());
        dto.setDateEncaissPayable(fret.getDateEncaissPayable());

        // Conversion des autres champs
        dto.setFretDesc(fret.getFretDesc());
        dto.setUsrLogin(fret.getUsrLogin());
        dto.setFretPayUsr(fret.getFretPayUsr());
        dto.setFretEtat(fret.getFretEtat());
        dto.setCoutMagasinage(fret.getCoutMagasinage());
        dto.setCoutMagasinageRemise(fret.getCoutMagasinageRemise());
        dto.setUsrMagasinage(fret.getUsrMagasinage());
        dto.setUsrLoginPayable(fret.getUsrLoginPayable());
        dto.setCarabane(fret.getCarabane());

        // Conversion des IDs de relation
        if (fret.getClientEnCompte() != null)
        {
            dto.setCltcmpt_id(fret.getClientEnCompte().getCltcmptId());
        }
        if (fret.getVoyage() != null)
        {
            dto.setVoy_id(fret.getVoyage().getVoyId());
        }

        // Conversion de la liste des LigneFret
        List<LigneFretDTO> ligneFretDTOList = new ArrayList<>();
        for (LigneFret ligneFret : fret.getLigneFrets())
        {
            LigneFretDTO lfDto = new LigneFretDTO();

            // Exemple de conversion des champs de LigneFret
            lfDto.setLigneFret_id(ligneFret.getLigneFretId());
            lfDto.setFret_id(ligneFret.getFret().getFretId());
            lfDto.setDetails(ligneFret.getDetails());
            lfDto.setQuantity(ligneFret.getQuantity());
            lfDto.setWeight(ligneFret.getWeight());
            lfDto.setVolume(ligneFret.getVolume());

            Long tbgId = ligneFret.getTypeBagage().getTbgId();

            if (tbgId == null)
            {
                lfDto.setTbg_id(0L); // Valeur par défaut
            }
            else
            {
                lfDto.setTbg_id(tbgId);
            }

            Categorie categorie = ligneFret.getCategorie();

            if (categorie == null)
            {
                lfDto.setCat_id(0L); // Valeur par défaut si la catégorie est null
            }
            else
            {
                Long catId = categorie.getCatId();
                lfDto.setCat_id(catId != null ? catId : 0L); // Valeur par défaut si catId est null
            }

            // Ajouter chaque LigneFretDTO à la liste
            ligneFretDTOList.add(lfDto);
        }

        dto.setLigneFretDTOList(ligneFretDTOList);
        dto.setFretClt_id(fret.getFretClient().getFretCltId());
        FretCltDTO dtos = new FretCltDTO();
        FretClient frtClt = fretClientRepo.findById(dto.getFretClt_id()).orElse(null);

        if (frtClt != null)
        {
            dtos.setFirstname(frtClt.getFirstname());
            dtos.setLastname(frtClt.getLastname());
            dtos.setTelephone(frtClt.getTelephone());
            dtos.setEmail(frtClt.getEmail());
            dtos.setNumeroPiece(frtClt.getNumeroPiece());
        };

        dto.setFretCltDTO(dtos);

        return dto;
    }







}
