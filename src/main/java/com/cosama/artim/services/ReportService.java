package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    @Autowired
    private final BilletRepository billetRepository;
    @Autowired
    private final VoyageRepository voyageRepository;
    @Autowired
    private final TypePlaceRepository typePlaceRepository;
    @Autowired
    private final EnfantRepository enfantRepository;
    @Autowired
    private final CritereRepository critereRepository;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final VenteBilletService venteBillet;
    @Autowired
    private final EtatBilletRepository etatBilletRepository;
    @Autowired
    private final AchatOnLineService achatOnLineService;
    private static final Logger logger = LoggerFactory.getLogger(FretService.class);

    public String checkSurclassement(long bilId, long tplcChoisi)
    {
        //String surclassement;
        Billet billet = this.billetRepository.findById(bilId).orElse(null);
        assert billet != null;
        long typePlaceExistant = billet.getTypePlace().getTplcId();
        String codeById = this.typePlaceRepository.findNameTypePlace(tplcChoisi);
        String surclassement = null;

        if (tplcChoisi != typePlaceExistant) {
            if (typePlaceExistant == 1)
            {
                surclassement = "P->"+codeById;
            }
            else if (typePlaceExistant == 4 && !codeById.equals("CH") && !codeById.equals("C8") && tplcChoisi != 0)
            {
                surclassement = "C8->"+codeById;
            }
            else if (typePlaceExistant == 3 && !codeById.equals("CH") && !codeById.equals("C8") && !codeById.equals("C4") && tplcChoisi != 0)
            {
                surclassement = "C4->"+codeById;
            }
            else if (typePlaceExistant == 2 && !codeById.equals("CH") && !codeById.equals("C8") && !codeById.equals("C4") && !codeById.equals("C2") && tplcChoisi != 0)
            {
                surclassement = "C2->"+codeById;
            }
        }
        return surclassement;
    }

    public ResultReport checkReport(long bilId) {
        // Récupérer le billet avec ses critères
        Billet billet = this.billetRepository.findById(bilId).orElse(null);
        boolean reportDejaEffectif = false;
        List<Critere> criteresBillet = new ArrayList<>();

        if (billet != null) {
            List<Critere> criteres = billet.getCriteres();
            // ID des critères à tester
            String[] criteresToCheck = {"P->C2","P->C4","P->C8","C4->C2","C8->C2","C8->C4","C2->VIP","C4->VIP","C8->VIP","Report"};

            // Parcourir les critères du billet
            for (Critere critere : criteres) {
                long critereId = critere.getCrtId();
                System.out.println("critere ancien billet : "+critereId);
                boolean found = false;
                System.out.println("Entree dans la boucle");
                // Vérifier si le critère est dans la liste des critères à tester
                for (String crtName : criteresToCheck)
                {
                    logger.info(crtName);
                    Long id = this.critereRepository.findIdCriteriaByName(crtName);
                    logger.info(String.valueOf(id));
                    if (id != null)
                    {
                        if (critereId == id)
                        {
                            found = true;
                            if (critereId == 8) {
                                reportDejaEffectif = true;
                            }
                            break;
                        }
                    }
                    else
                    {
                        logger.warn("Aucun critère trouvé pour : " + crtName);
                    }
                }

                if (!found)
                {
                    criteresBillet.add(critere);
                }
            }
        }

        return new ResultReport(reportDejaEffectif, criteresBillet);
    }

    public BilletsDTO reportBillet(long bilId, boolean updatePassager, long voyId, BilletDTO billetDTO, float penality)
    {
        ResultReport result = checkReport(bilId);
        boolean reportDejaEffectif = result.isReportDejaEffectif();
        List<Critere> criteresForNewBillet = result.getCriteresBillet();
        Billet ancienBillet = this.billetRepository.findById(bilId).orElse(null);
        TypePlace typePlace = this.typePlaceRepository.findById(billetDTO.getTypePlaceId()).orElse(null);
        Enfant ef = this.enfantRepository.findByBilId(bilId);
        String checkSU = checkSurclassement(bilId, billetDTO.getTypePlaceId());
        Voyage bat = this.voyageRepository.findBateauByvoyId(voyId);
        Voyage voy = this.voyageRepository.findById(voyId).orElse(null);
        Billet newBillet = new Billet();
        BilletsDTO ok;
        Billet getBillSave = new Billet();

        if(reportDejaEffectif)
        {
            logger.info("Report existant");
            BilletsDTO ok_ = convertToDTO1(ancienBillet);  // Conversion du billet unique
            return ok_;
        }
        else
        {
            Voyage voyAncienB = this.voyageRepository.findById(ancienBillet.getVoyage().getVoyId()).orElse(null);
            if (voyAncienB != voy)
            {
                System.out.println("Report inexistant");
                long crtRep = this.critereRepository.findIdCriteriaByName("Report");
                Critere crtR = this.critereRepository.findById(crtRep).orElse(null);
                criteresForNewBillet.add(crtR);
                assert ancienBillet != null;
                ancienBillet.setCriteres(criteresForNewBillet);
            }

            if (updatePassager)
            {
                logger.info("Mise à jour du passager effectif");
                newBillet.setFirstname(billetDTO.getFirstname());
                newBillet.setLastname(billetDTO.getLastname());
                newBillet.setTypePlace(typePlace);
                newBillet.setNumeropiece(billetDTO.getNumeropiece());
                newBillet.setCivilite(billetDTO.getCivilite());
            }

            if (checkSU != null)
            {
                logger.info("Surclassement : "+checkSU);
                long id_crt = critereRepository.findIdCriteriaByName(checkSU);
                logger.info(String.valueOf(id_crt));
                Critere crtSU = critereRepository.findById(id_crt).orElse(null);
                criteresForNewBillet.add(crtSU);
            }

            System.out.println(criteresForNewBillet.toArray().length);
            List<Number> tarif = tarification.tarifPlaceTab(billetDTO.getTypePlaceId(), criteresForNewBillet);
            float bil_pht = (float) tarif.get(0) + penality;
            float bil_remise = (float) tarif.get(1);
            float bil_taxe = (float) tarif.get(2);
            float bil_ptt = (float) tarif.get(4) + penality;
            double PHT = bil_pht; //- ancienBillet.getBilPht();
            double PTT = bil_ptt; //- ancienBillet.getBilPtt();
            logger.info(String.valueOf(ancienBillet.getBilPht()));
            boolean dispo_place = venteBillet.isPlaceAvailable(voyId, billetDTO.getPlcId());

            if (dispo_place)
            {
                logger.info("Place dispo creation du nouveau billet");
                int place_deja_liberee = ancienBillet.getNoShow();
                logger.info("Ancien No Show" + place_deja_liberee);

                newBillet.setFirstname(ancienBillet.getFirstname());
                newBillet.setLastname(ancienBillet.getLastname());
                newBillet.setTypePlace(ancienBillet.getTypePlace());
                newBillet.setNumeropiece(ancienBillet.getNumeropiece());
                newBillet.setCivilite(ancienBillet.getCivilite());
                newBillet.setBilCode(ancienBillet.getBilCode());
                newBillet.setNationalite(ancienBillet.getNationalite());
                newBillet.setPassager(ancienBillet.getPassager());
                newBillet.setTypePiece(ancienBillet.getTypePiece());
                newBillet.setCode_achat(ancienBillet.getCode_achat()+"SU/R");
                newBillet.setBilEtat(true);
                newBillet.setBilPht(PHT);
                newBillet.setBilRemise(bil_remise);
                newBillet.setBilTaxe(bil_taxe);
                newBillet.setBilPtt(PTT);
                newBillet.setBilDateEmission(LocalDateTime.now());
                newBillet.setBilDateValidite(bat.getVoyDatedpt().atStartOfDay());
                newBillet.setBatId(bat.getBateau().getBatId());
                newBillet.setVoyage(voy);
                newBillet.setBilPenalite((int) penality);

                if (billetDTO.getPlcId() == 0)
                {
                    newBillet.setPlcId(ancienBillet.getPlcId());
                    newBillet.setTypePlace(ancienBillet.getTypePlace());
                }
                else
                {
                    newBillet.setPlcId(billetDTO.getPlcId());
                    newBillet.setTypePlace(typePlace);
                }
                newBillet.setCriteres(criteresForNewBillet);

                String ordreString = this.achatOnLineService.callRemoteFunction2();
                String code_billet = this.achatOnLineService.codeBillet_generator(ordreString);
                newBillet.setBilCode(code_billet);
                getBillSave = billetRepository.save(newBillet);

                if (place_deja_liberee==0)
                {
                    venteBillet.disponiblePlace(ancienBillet.getPlcId(), ancienBillet.getVoyage().getVoyId(), ancienBillet.getVoyage().getBateau().getBatId());
                }

                int oc_place = venteBillet.occupePlace(getBillSave.getPlcId(), getBillSave.getVoyage().getVoyId(), getBillSave.getBatId(), getBillSave.getBilCode());
                if (oc_place == 1)
                {
                    System.out.println("Place occupé avec succés");
                    ancienBillet.setBilReporter(getBillSave.getBilId());
                    ancienBillet.setNoShow(1);
                    ancienBillet.setDateNoShow(LocalDateTime.now());
                    ancienBillet.setBilEtat(false);
                    ef.setBillet(getBillSave);
                    billetRepository.save(ancienBillet);
                    enfantRepository.save(ef);
                }
                EtatBillet eb = new EtatBillet();
                eb.setBilTime(LocalDateTime.now());
                eb.setColor("orange");
                eb.setStatus("Reporté/Surclassé");
                eb.setIcon("PrimeIcons.TAGS");
                eb.setBillet(ancienBillet);
                etatBilletRepository.save(eb);
            }
        }
        ok = convertToDTO1(getBillSave);  // Conversion du billet unique
        return ok;

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

        if (billet != null && billet.getEnfants() != null && !billet.getEnfants().isEmpty() && billet.getEnfants().get(0) != null) {
            edto.setEnfNomComplet(billet.getEnfants().get(0).getEnfNomComplet());
            edto.setEnfAge(billet.getEnfants().get(0).getEnfAge());
            edto.setUniteTemps(billet.getEnfants().get(0).getUniteTemps());
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