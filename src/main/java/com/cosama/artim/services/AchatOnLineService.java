package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.AchatOnLineRepository;
import com.cosama.artim.repositories.BilletRepository;
import com.cosama.artim.repositories.VoyageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchatOnLineService {
    @Autowired
    private final VoyageRepository voyageRepository;
    @Autowired
    private final BilletRepository billetRepository;
    @Autowired
    private final AchatOnLineRepository achatOnLineRepository;
    @Autowired
    private final VenteBilletService venteBillet;

    private final JdbcTemplate jdbcTemplate;

    //Appel la fonction qui permet de generer le numero d'ordre pour concocter le code_d'achat
    public String callRemoteFunction() {
        String sql = "SELECT get_next_order_number_titre()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String callRemoteFunction2() {
        String sql = "SELECT get_next_order_number_billet()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeAchat_generator(String OrderNumber, boolean allerSimple, boolean allerRetour)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String code = "";
        log.info("formattedInput est {} :" + OrderNumber);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);

        if (allerSimple)
            code = "OW";

        if (allerRetour)
            code = "GB";


        //String c = "T-" + code + OrderNumber + mois + an;
        String c = "T-" + OrderNumber + mois + an;
        return c;
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


    public AchatOnLine achat_billet_online(AchatOnLineDTO achatOnLineDTO)
    {
        String ordreString = callRemoteFunction();
        Voyage voy1;
        Passager pas;
        Double prixBillets = 0.0;
        List<Passager> passagersList = new ArrayList<>();
        AchatOnLine achatOnLine = new AchatOnLine();
        achatOnLine.setAllerSimple(achatOnLineDTO.isAllerSimple());
        achatOnLine.setAllerRetour(achatOnLineDTO.isAllerRetour());
        achatOnLine.setVoyDateDpt(achatOnLineDTO.getVoyDateDpt());
        achatOnLine.setVoyDateRet(achatOnLineDTO.getVoyDateRet());
        achatOnLine.setVoyDepart(achatOnLineDTO.getVoyDepart());
        achatOnLine.setVoyRetour(achatOnLineDTO.getVoyRetour());
        achatOnLine.setBatId(achatOnLineDTO.getBatId());

        achatOnLine.setVoyDestination1(achatOnLineDTO.getVoyDestination1());
        achatOnLine.setVoyDestination2(achatOnLineDTO.getVoyDestination2());

        achatOnLine.setCoutAller(achatOnLineDTO.getCoutAller());
        achatOnLine.setCoutRetour(achatOnLineDTO.getCoutRetour());

        achatOnLine.setNumberPassagers(achatOnLineDTO.getNumberPassagers());

        String code_achat = codeAchat_generator(ordreString, achatOnLineDTO.isAllerSimple(), achatOnLineDTO.isAllerRetour());

        achatOnLine.setCodeAchat(code_achat);

        int nbP = achatOnLine.getNumberPassagers();
        voy1 = voyageRepository.findByDateAndDepart(achatOnLineDTO.getVoyDateDpt(), achatOnLineDTO.getVoyDepart(), achatOnLineDTO.getBatId());
        if (achatOnLineDTO.isAllerSimple() && voy1 != null)
        {
            System.out.print("Cas aller simple");
            for (PassagerWithBilletDTO passagerWithBilletDTO : achatOnLineDTO.getPassagerWithBilletDTOS())
            {
                String ordreString2 = callRemoteFunction2();
                String code_billet = codeBillet_generator(ordreString2);
                System.out.print("Accés à la boucle");
                venteBillet.createPassagerWithBillets(passagerWithBilletDTO, voy1, code_achat, code_billet, voy1.getVoyDatedpt().atStartOfDay());
                //passagersList.add(pas);
            }
            //achatOnLine.setPassagers(passagersList);
        }

        if (achatOnLine.isAllerRetour())
        {
            if (achatOnLine.getVoyDepart() == 1)
            {
                achatOnLine.setVoyDestination1(2);
            }
            if (achatOnLine.getVoyDepart() == 2)
            {
                achatOnLine.setVoyDestination1(1);
            }

            if (achatOnLine.getVoyRetour() == 1)
            {
                achatOnLine.setVoyDestination2(2);
            }
            if (achatOnLine.getVoyRetour() == 2)
            {
                achatOnLine.setVoyDestination2(1);
            }
            // Supposons que passagerWithBilletDTO est une liste ou un tableau
            List<PassagerWithBilletDTO> passagers = achatOnLineDTO.getPassagerWithBilletDTOS();
            Voyage voy2 = voyageRepository.findByDateAndDepart(achatOnLine.getVoyDateRet(), achatOnLine.getVoyRetour(), achatOnLineDTO.getBatId());
            // Calcul de l'index de la moitié
            int halfIndex = (nbP % 2 == 0) ? (nbP / 2) : ((nbP / 2) + 1);

            // Première boucle pour la première moitié des passagers
            for (int i = 0; i < halfIndex && i < passagers.size(); i++) {
                String ordreString2 = callRemoteFunction2();
                String code_billet = codeBillet_generator(ordreString2);
                PassagerWithBilletDTO passagerWithBilletDTO = passagers.get(i);
                venteBillet.createPassagerWithBillets(passagerWithBilletDTO, voy1, code_achat, code_billet, voy2.getVoyDatedpt().atStartOfDay());
                //passagersList.add(pas);
            }
            // Deuxième boucle pour la seconde moitié des passagers
            for (int i = halfIndex; i < nbP && i < passagers.size(); i++) {
                String ordreString2 = callRemoteFunction2();
                String code_billet = codeBillet_generator(ordreString2);
                PassagerWithBilletDTO passagerWithBilletDTO = passagers.get(i);
                venteBillet.createPassagerWithBillets(passagerWithBilletDTO, voy2, code_achat, code_billet, voy2.getVoyDatedpt().atStartOfDay());
                //passagersList.add(pas);
            }
        }
        achatOnLineRepository.save(achatOnLine);
        return achatOnLine;
    }

    // Méthode du service pour charger les billets des passagers
    public AchatOnLineWithBilletsDTO getAchatWithBillets(String codeAchat)
    {
        // Cherche les billets selon le code d'achat
        List<Billet> billets = billetRepository.findByCodeAchat(codeAchat);

        if (billets == null || billets.isEmpty())
        {
            billets = billetRepository.findByCodeBillet(codeAchat);  // Réutilisation de la variable `billets`
        }
        // Convertir la liste de billets en une liste de BilletDTO
        List<BilletsDTO> billetDTOs = billets.stream()
                .map(this::convertToDTO1)  // Utilise la méthode de conversion pour chaque billet
                .collect(Collectors.toList());

        return new AchatOnLineWithBilletsDTO(null, billetDTOs);
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

}
