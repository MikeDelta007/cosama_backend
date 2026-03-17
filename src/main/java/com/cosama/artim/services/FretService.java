package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.logging.Logger.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FretService {
    @Autowired
    private final TypeBagageRepository typeBagageRepository;
    @Autowired
    private final VoyageRepository voyageRepository;
    @Autowired
    private final FretRepository fretRepository;
    @Autowired
    private final LigneFretRepository ligneFretRepository;
    @Autowired
    private final FretCltRepository fretCltRepository;
    @Autowired
    private final ClientEnCompteRepository clientEnCompteRepository;
    @Autowired
    private final BilletRepository billetRepository;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final CategorieRepository categorieRepository;

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FretService.class);

    public List<FretDTOs> getFretByCltCompte(Long idCltCmpt)
    {
        List<Fret> frets = fretRepository.findFretByCltCmpt(idCltCmpt);
        return frets.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }

    public List<FretDTOs> getAllFret()
    {
        List<Fret> frets = fretRepository.findAll();
        return frets.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }

    public Map<String, List<FretDTOs>> getAllFret_() {
        // Récupérer tous les frets
        List<Fret> fretsSorted = fretRepository.findAll().stream()
                .sorted(Comparator.comparing(Fret::getFretId).reversed())
                .collect(Collectors.toList());

        return fretsSorted.stream()
                .collect(Collectors.groupingBy(f -> Boolean.TRUE.equals(!f.isApplyPayment()) ? "Frêt pas encore payé" : "Frêt payé",
                        LinkedHashMap::new,
                        Collectors.mapping(f -> {
                            FretDTOs dto = new FretDTOs();
                            // Conversion des champs simples
                            dto.setFretCode(f.getFretCode());
                            dto.setFretId(f.getFretId());
                            dto.setExpEqDest(f.isExpEqDest());
                            dto.setRaisonSocialeDest(f.getRaisonSocialeDest());
                            dto.setFirstnameDest(f.getFirstnameDest());
                            dto.setLastnameDest(f.getLastnameDest());
                            dto.setTelephoneDest(f.getTelephoneDest());
                            dto.setEmailDest(f.getEmailDest());
                            dto.setFretAcompte(f.getFretAcompte());
                            dto.setFretMontant(f.getFretMontant());
                            dto.setFretTva(f.getFretTva());
                            dto.setFretRemiseTaux(f.getFretRemiseTaux());
                            dto.setFretRemise(f.getFretRemise());
                            dto.setFretMontant_ht(f.getFretMontant_ht());
                            dto.setApplyTVA(f.isApplyTVA());
                            dto.setApplyPayment(f.isApplyPayment());
                            dto.setBillet(f.getBillet());

                            // Conversion des dates avec format JSON
                            dto.setFretDate(f.getFretDate());
                            dto.setFretPayDate(f.getFretPayDate());
                            dto.setDateMagasinage(f.getDateMagasinage());
                            dto.setDateEncaissPayable(f.getDateEncaissPayable());

                            // Conversion des autres champs
                            dto.setFretDesc(f.getFretDesc());
                            dto.setUsrLogin(f.getUsrLogin());
                            dto.setFretPayUsr(f.getFretPayUsr());
                            dto.setFretEtat(f.getFretEtat());
                            dto.setCoutMagasinage(f.getCoutMagasinage());
                            dto.setCoutMagasinageRemise(f.getCoutMagasinageRemise());
                            dto.setUsrMagasinage(f.getUsrMagasinage());
                            dto.setUsrLoginPayable(f.getUsrLoginPayable());
                            dto.setCarabane(f.getCarabane());
                            dto.setMotif(f.getMotif());
                            dto.setPaymentMethod(f.getPaymentMethod());

                            // Conversion des IDs de relation
                            if (f.getClientEnCompte() != null)
                            {
                                dto.setCltcmpt_id(f.getClientEnCompte().getCltcmptId());
                            }
                            if (f.getVoyage() != null)
                            {
                                dto.setVoy_id(f.getVoyage().getVoyId());
                            }

                            // Conversion de la liste des LigneFret
                            List<LigneFretDTO> ligneFretDTOList = new ArrayList<>();
                            for (LigneFret ligneFret : f.getLigneFrets())
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
                            dto.setFretClt_id(f.getFretClient().getFretCltId());
                            FretCltDTO dtos = new FretCltDTO();
                            FretClient frtClt = fretCltRepository.findById(dto.getFretClt_id()).orElse(null);

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
                        }, Collectors.toList()))
                );
    }

    public Map<String, List<FretDTOs>> getAllFret_bis() {
        // Récupérer tous les frets
        List<Fret> fretsSorted = fretRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Fret::getFretId).reversed())
                .toList();

        return fretsSorted.stream()
                .collect(Collectors.groupingBy(
                        f -> !f.isApplyPayment() ? "Frêt pas encore réglé" : "Frêt réglé",
                        LinkedHashMap::new,
                        Collectors.mapping(f -> {

                            FretDTOs dto = new FretDTOs();
                            // Conversion des champs simples
                            dto.setFretCode(f.getFretCode());
                            dto.setFretId(f.getFretId());
                            dto.setExpEqDest(f.isExpEqDest());
                            dto.setRaisonSocialeDest(f.getRaisonSocialeDest());
                            dto.setFirstnameDest(f.getFirstnameDest());
                            dto.setLastnameDest(f.getLastnameDest());
                            dto.setTelephoneDest(f.getTelephoneDest());
                            dto.setEmailDest(f.getEmailDest());
                            dto.setFretAcompte(f.getFretAcompte());
                            dto.setFretMontant(f.getFretMontant());
                            dto.setFretTva(f.getFretTva());
                            dto.setFretRemiseTaux(f.getFretRemiseTaux());
                            dto.setFretRemise(f.getFretRemise());
                            dto.setFretMontant_ht(f.getFretMontant_ht());
                            dto.setApplyTVA(f.isApplyTVA());
                            dto.setApplyPayment(f.isApplyPayment());
                            dto.setBillet(f.getBillet());

                            // Conversion des dates avec format JSON
                            dto.setFretDate(f.getFretDate());
                            dto.setFretPayDate(f.getFretPayDate());
                            dto.setDateMagasinage(f.getDateMagasinage());
                            dto.setDateEncaissPayable(f.getDateEncaissPayable());

                            // Conversion des autres champs
                            dto.setFretDesc(f.getFretDesc());
                            dto.setUsrLogin(f.getUsrLogin());
                            dto.setFretPayUsr(f.getFretPayUsr());
                            dto.setFretEtat(f.getFretEtat());
                            dto.setCoutMagasinage(f.getCoutMagasinage());
                            dto.setCoutMagasinageRemise(f.getCoutMagasinageRemise());
                            dto.setUsrMagasinage(f.getUsrMagasinage());
                            dto.setUsrLoginPayable(f.getUsrLoginPayable());
                            dto.setCarabane(f.getCarabane());
                            dto.setMotif(f.getMotif());
                            dto.setPaymentMethod(f.getPaymentMethod());

                            // Conversion des IDs de relation
                            if (f.getClientEnCompte() != null)
                            {
                                dto.setCltcmpt_id(f.getClientEnCompte().getCltcmptId());
                            }
                            if (f.getVoyage() != null)
                            {
                                dto.setVoy_id(f.getVoyage().getVoyId());
                            }

                            // Conversion de la liste des LigneFret
                            List<LigneFretDTO> ligneFretDTOList = new ArrayList<>();
                            for (LigneFret ligneFret : f.getLigneFrets())
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
                            dto.setFretClt_id(f.getFretClient().getFretCltId());
                            FretCltDTO dtos = new FretCltDTO();
                            FretClient frtClt = fretCltRepository.findById(dto.getFretClt_id()).orElse(null);

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
                        }, Collectors.toList()))
                );
    }

    public FretDTOs convertToDTO1(Fret fret) {
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
        dto.setMotif(fret.getMotif());
        dto.setPaymentMethod(fret.getPaymentMethod());

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
        FretClient frtClt = fretCltRepository.findById(dto.getFretClt_id()).orElse(null);

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

    public Fret addFretOps(boolean appPaye, FretDTO fretDTO, long cltCmptId, long voyId, boolean isExp)
    {
        ClientEnCompte cltEnCpt = clientEnCompteRepository.findById(cltCmptId).orElse(null);
        Voyage vg = voyageRepository.findById(voyId).orElse(null);
        Fret fret = new Fret();

        String ordre_fret = callRemoteFunction();
        String code_fret = codeFret_generator(ordre_fret);

        if (appPaye)
        {
            fret.setFretPayDate(LocalDateTime.now());
            fret.setFretCode("OL-"+code_fret);
            fret.setApplyPayment(true);
            fret.setApplyTVA(true);
        }
        else
        {
            fret.setFretCode(fretDTO.getFretCode()+"-"+code_fret);
        }

        fret.setRaisonSocialeDest(fretDTO.getRaisonSocialeDest());
        fret.setFirstnameDest(fretDTO.getFirstnameDest());
        fret.setLastnameDest(fretDTO.getLastnameDest());
        fret.setTelephoneDest(fretDTO.getTelephoneDest());
        fret.setEmailDest(fretDTO.getEmailDest());
        fret.setExpEqDest(isExp);
        fret.setFretMontant(fretDTO.getFretMontant());
        fret.setFretMontant_ht(fretDTO.getFretMontant_ht());
        fret.setFretTva(fretDTO.getFretTva());
        fret.setFretDate(LocalDateTime.now());
        fret.setFretEtat(fretDTO.getFretEtat());
        fret.setVoyage(vg);
        fret.setBillet(fretDTO.getBillet());
        fret.setUsrLogin(fretDTO.getUsrLogin());
        fret.setCarabane(fretDTO.getCarabane());

        fret.setClientEnCompte(cltEnCpt);
        return fretRepository.save(fret);
    }

    public LigneFret addLigneFret(LigneFretDTO ligneFretDTO, TypeBagage tbg, Categorie cat)
    {
        LigneFret lFret = new LigneFret();
        lFret.setTypeBagage(tbg);
        lFret.setCategorie(cat);
        lFret.setDetails(ligneFretDTO.getDetails());
        lFret.setEtat(true);
        if (lFret.getTypeBagage().getUnite().getVolume().getVolId() == 1)
        {
            lFret.setQuantity(ligneFretDTO.getQuantity());
            lFret.setWeight(ligneFretDTO.getWeight());
            lFret.setVolume(0);
        }
        if (lFret.getTypeBagage().getUnite().getVolume().getVolId() == 2)
        {
            lFret.setQuantity(0);
            lFret.setWeight(ligneFretDTO.getWeight());
            lFret.setVolume(0);
        }
        if (lFret.getTypeBagage().getUnite().getVolume().getVolId() == 3)
        {
            lFret.setQuantity(0);
            lFret.setWeight(ligneFretDTO.getWeight());
            lFret.setVolume(ligneFretDTO.getVolume());
        }

        return lFret;
    }

    public LigneFret saveLigneFret(LigneFret ligneFret)
    {
        return ligneFretRepository.save(ligneFret);
    }

    public FretClientResponseDTO add_Fret(FretCltDTO fretCltDTO, long cltCmptId, long voyId, long bilId, boolean isExtEqDest)
    {
        FretClient cltFret = new FretClient();
        ClientEnCompte cltEnCpt = clientEnCompteRepository.findById(cltCmptId).orElse(null);
        Billet billet = billetRepository.findById(bilId).orElse(null);

        fretCltDTO.setExpEqDest(isExtEqDest);
        FretDTO fretDTO = fretCltDTO.getFretDTOS();

        FretClient savedCltfret = null;
        Fret fret = null;
        List<LigneFret> ligneFretList = new ArrayList<>();

        logger.info("On entre dans le fret ");

        if (cltEnCpt == null)
        {
            if (billet != null)
            {
                cltFret.setRaisonSociale("PARTICULIER");
                cltFret.setFirstname(billet.getFirstname());
                cltFret.setLastname(billet.getLastname());
                cltFret.setNumeroPiece(billet.getNumeropiece());
                cltFret.setTelephone(billet.getPassager().getPhone());
                cltFret.setEmail(billet.getPassager().getMail());

            }
            else
            {
                cltFret.setRaisonSociale("PARTICULIER");
                cltFret.setFirstname(fretCltDTO.getFirstname());
                cltFret.setLastname(fretCltDTO.getLastname());
                cltFret.setNumeroPiece(fretCltDTO.getNumeroPiece());
                cltFret.setTelephone(fretCltDTO.getTelephone());
                cltFret.setEmail(fretCltDTO.getEmail());
            }

            cltFret.setExpEqDest(isExtEqDest);
            savedCltfret = fretCltRepository.save(cltFret);

            if (isExtEqDest)
            {
                fretDTO.setRaisonSocialeDest("PARTICULIER");
                fretDTO.setFirstnameDest(savedCltfret.getFirstname());
                fretDTO.setLastnameDest(savedCltfret.getLastname());
                fretDTO.setTelephoneDest(savedCltfret.getTelephone());
                fretDTO.setEmailDest(savedCltfret.getEmail());
            }
            else
            {
                fretDTO.setRaisonSocialeDest("PARTICULIER");
                fretDTO.setFirstnameDest(fretCltDTO.getFretDTOS().getFirstnameDest() );
                fretDTO.setLastnameDest(fretCltDTO.getFretDTOS().getLastnameDest());
                fretDTO.setTelephoneDest(fretCltDTO.getFretDTOS().getTelephoneDest());
                fretDTO.setEmailDest(fretCltDTO.getFretDTOS().getEmailDest());
            }

            float tht = 0;
            float montant = 0;
            float montant_ht = 0;
            float tva;
            for (LigneFretDTO ligneFretDTO : fretDTO.getLigneFretDTOList())
            {
                logger.info("BAGAGE : " + ligneFretDTO);
                List<Number> tarif = tarification.tarifBagoTab(ligneFretDTO.getCat_id(), null);
                logger.info("Contenu de la liste tarif : {}", tarif);
                TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                float prixBago = (float) tarif.get(4);
                logger.info(String.valueOf(prixBago));
                assert tbg != null;


                if (tbg.getUnite().getVolume().getVolId() == 1)
                {
                    tht = (prixBago * ligneFretDTO.getQuantity());
                    logger.info("Q: " + prixBago + " , " + ligneFretDTO.getQuantity());
                }

                if (tbg.getUnite().getVolume().getVolId() == 2)
                {
                    tht = (prixBago * ligneFretDTO.getWeight());
                    logger.info("P: " + prixBago + " , " + ligneFretDTO.getWeight());
                }

                if (tbg.getUnite().getVolume().getVolId() == 3)
                {
                    tht = (prixBago * ligneFretDTO.getVolume());
                    logger.info("V: " + prixBago + " , " + ligneFretDTO.getVolume());
                }

                montant_ht += (tht);
                LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                ligneFretList.add(ligneFret);
            }
            logger.info("Montant Hors Taxe 1 : " + montant_ht);

            tva = (float) (montant_ht * 0.18);
            montant = montant_ht + tva;

            fretDTO.setFretMontant_ht(montant_ht);
            fretDTO.setFretTva(tva);
            fretDTO.setFretMontant(montant);
            fretDTO.setFretEtat(true);
            if (billet != null)
            {
                logger.info("AUTRE" + billet.getBilCode());
                fretDTO.setBillet(billet.getBilCode());
                logger.info("ID BILLET :" + fretDTO.getBillet());
            }
            fret = addFretOps(false, fretDTO, cltCmptId, voyId, isExtEqDest);
            fret.setFretClient(savedCltfret);

            for (LigneFret ligneFret : ligneFretList)
            {
                logger.info("ID FRET : " + fret.getFretId());
                ligneFret.setFret(fret);
                saveLigneFret(ligneFret);
            }

        }
        else
        {
            if (cltEnCpt.getSoldeCompte()>0)
            {
                if (billet != null)
                {
                    cltFret.setRaisonSociale(cltEnCpt.getRaisonSocial().toUpperCase());
                    cltFret.setFirstname(billet.getFirstname());
                    cltFret.setLastname(billet.getLastname());
                    cltFret.setNumeroPiece(billet.getNumeropiece());
                    cltFret.setTelephone(billet.getPassager().getPhone());
                    cltFret.setEmail(billet.getPassager().getMail());
                }
                else
                {
                    cltFret.setRaisonSociale(cltEnCpt.getRaisonSocial().toUpperCase());
                    cltFret.setFirstname(fretCltDTO.getFirstname());
                    cltFret.setLastname(fretCltDTO.getLastname());
                    cltFret.setNumeroPiece(fretCltDTO.getNumeroPiece());
                    cltFret.setTelephone(fretCltDTO.getTelephone());
                    cltFret.setEmail(fretCltDTO.getEmail());
                }

                cltFret.setExpEqDest(isExtEqDest);
                savedCltfret = fretCltRepository.save(cltFret);

                if (isExtEqDest)
                {
                    fretDTO.setRaisonSocialeDest(cltEnCpt.getRaisonSocial().toUpperCase());
                    fretDTO.setFirstnameDest(savedCltfret.getFirstname());
                    fretDTO.setLastnameDest(savedCltfret.getLastname());
                    fretDTO.setTelephoneDest(savedCltfret.getTelephone());
                    fretDTO.setEmailDest(savedCltfret.getEmail());
                }
                else
                {
                    fretDTO.setRaisonSocialeDest(fretCltDTO.getRaisonSociale());
                    fretDTO.setFirstnameDest(fretCltDTO.getFretDTOS().getFirstnameDest());
                    fretDTO.setLastnameDest(fretCltDTO.getFretDTOS().getLastnameDest());
                    fretDTO.setTelephoneDest(fretCltDTO.getFretDTOS().getTelephoneDest());
                    fretDTO.setEmailDest(fretCltDTO.getFretDTOS().getEmailDest());
                }

                float tht = 0;
                float montant = 0;
                float montant_ht = 0;
                float tva;
                for (LigneFretDTO ligneFretDTO : fretDTO.getLigneFretDTOList())
                {
                    List<Number> tarif = tarification.tarifBagoTab(ligneFretDTO.getCat_id(), null);
                    TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                    Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                    float prixBago = (float) tarif.get(4);
                    System.out.print(prixBago);
                    assert tbg != null;


                    if (tbg.getUnite().getVolume().getVolId() == 1)
                    {
                        tht = (prixBago * ligneFretDTO.getQuantity());
                        logger.info("Q: " + prixBago + " , " + ligneFretDTO.getQuantity());
                    }

                    if (tbg.getUnite().getVolume().getVolId() == 2)
                    {
                        tht = (prixBago * ligneFretDTO.getWeight());
                        logger.info("P: " + prixBago + " , " + ligneFretDTO.getWeight());
                    }

                    if (tbg.getUnite().getVolume().getVolId() == 3)
                    {
                        tht = (prixBago * ligneFretDTO.getVolume());
                        logger.info("V: " + prixBago + " , " + ligneFretDTO.getVolume());
                    }

                    montant_ht += (tht);
                    LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                    ligneFretList.add(ligneFret);
                }

                tva = (float) (montant_ht * 0.18);
                montant = montant_ht + tva;

                if (montant <= cltEnCpt.getSoldeCompte())
                {
                    cltEnCpt.setSoldeCompte(cltEnCpt.getSoldeCompte() - montant);
                }
                else
                {
                    return null;
                }

                logger.info("Montant Hors Taxe 2 : " + montant_ht);
                fretDTO.setFretMontant_ht(montant_ht);
                fretDTO.setFretTva(tva);
                fretDTO.setFretMontant(montant);
                fretDTO.setFretEtat(true);
                if (billet != null)
                {
                    logger.info("AUTRE" + billet.getBilCode());
                    fretDTO.setBillet(billet.getBilCode());
                }
                fret = addFretOps(false, fretDTO, cltCmptId, voyId, isExtEqDest);
                fret.setFretClient(savedCltfret);

                for (LigneFret ligneFret : ligneFretList)
                {
                    logger.info("ID FRET : " + fret.getFretId());
                    ligneFret.setFret(fret);
                    saveLigneFret(ligneFret);
                }

            }
            else
            {
                System.out.println("Solde insuffisant");
                return null;
            }

        }

        fret.setLigneFrets(ligneFretList);

        return new FretClientResponseDTO(savedCltfret, fret);

    }

    public FretClient add_FretOnLine(boolean applyPaye, FretCltDTO fretCltDTO, long cltCmptId, long voyId, long bilId, boolean isExtEqDest)
    {
        FretClient cltFret = new FretClient();
        ClientEnCompte cltEnCpt = clientEnCompteRepository.findById(cltCmptId).orElse(null);
        Billet billet = billetRepository.findById(bilId).orElse(null);

        fretCltDTO.setExpEqDest(isExtEqDest);
        FretDTO fretDTO = fretCltDTO.getFretDTOS();

        logger.info("On entre dans le fret ");

        if (cltEnCpt == null)
        {
            if (billet != null)
            {
                cltFret.setRaisonSociale("PARTICULIER");
                cltFret.setFirstname(billet.getFirstname());
                cltFret.setLastname(billet.getLastname());
                cltFret.setNumeroPiece(billet.getNumeropiece());
                cltFret.setTelephone(billet.getPassager().getPhone());
                cltFret.setEmail(billet.getPassager().getMail());

            }
            else
            {
                cltFret.setRaisonSociale("PARTICULIER");
                cltFret.setFirstname(fretCltDTO.getFirstname());
                cltFret.setLastname(fretCltDTO.getLastname());
                cltFret.setNumeroPiece(fretCltDTO.getNumeroPiece());
                cltFret.setTelephone(fretCltDTO.getTelephone());
                cltFret.setEmail(fretCltDTO.getEmail());
            }

            List<LigneFret> ligneFretList = new ArrayList<>();
            cltFret.setExpEqDest(isExtEqDest);
            FretClient savedCltfret = fretCltRepository.save(cltFret);

            if (isExtEqDest)
            {
                fretDTO.setRaisonSocialeDest("PARTICULIER");
                fretDTO.setFirstnameDest(savedCltfret.getFirstname());
                fretDTO.setLastnameDest(savedCltfret.getLastname());
                fretDTO.setTelephoneDest(savedCltfret.getTelephone());
                fretDTO.setEmailDest(savedCltfret.getEmail());
            }
            else
            {
                fretDTO.setRaisonSocialeDest("PARTICULIER");
                fretDTO.setFirstnameDest(fretCltDTO.getFretDTOS().getFirstnameDest() );
                fretDTO.setLastnameDest(fretCltDTO.getFretDTOS().getLastnameDest());
                fretDTO.setTelephoneDest(fretCltDTO.getFretDTOS().getTelephoneDest());
                fretDTO.setEmailDest(fretCltDTO.getFretDTOS().getEmailDest());
            }

            float tht = 0;
            float montant = 0;
            float montant_ht = 0;
            float tva;
            for (LigneFretDTO ligneFretDTO : fretDTO.getLigneFretDTOList())
            {
                logger.info("BAGAGE : " + ligneFretDTO);
                List<Number> tarif = tarification.tarifBagoTab(ligneFretDTO.getCat_id(), null);
                logger.info("Contenu de la liste tarif : {}", tarif);
                TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                float prixBago = (float) tarif.get(4);
                logger.info(String.valueOf(prixBago));
                assert tbg != null;
                if (tbg.getUnite().getVolume().getVolId() == 1)
                {
                    tht = (prixBago * ligneFretDTO.getQuantity());
                    logger.info("Q: " + prixBago + " , " + ligneFretDTO.getQuantity());
                }
                if (tbg.getUnite().getVolume().getVolId() == 2)
                {
                    tht = (prixBago * ligneFretDTO.getWeight());
                    logger.info("P: " + prixBago + " , " + ligneFretDTO.getWeight());
                }
                if (tbg.getUnite().getVolume().getVolId() == 3)
                {
                    tht = (prixBago * ligneFretDTO.getVolume());
                    logger.info("V: " + prixBago + " , " + ligneFretDTO.getVolume());
                }

                montant_ht += (tht);
                LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                ligneFretList.add(ligneFret);
            }
            logger.info("Montant Hors Taxe 1 : " + montant_ht);

            tva = (float) (montant_ht * 0.18);
            montant = montant_ht + tva;

            fretDTO.setFretMontant_ht(montant_ht);
            fretDTO.setFretTva(tva);
            fretDTO.setFretMontant(montant);
            fretDTO.setFretEtat(true);
            if (billet != null)
            {
                logger.info("AUTRE" + billet.getBilCode());
                fretDTO.setBillet(billet.getBilCode());
                logger.info("ID BILLET :" + fretDTO.getBillet());
            }
            Fret fret = addFretOps(applyPaye, fretDTO, cltCmptId, voyId, isExtEqDest);
            fret.setFretClient(savedCltfret);

            for (LigneFret ligneFret : ligneFretList)
            {
                logger.info("ID FRET : " + fret.getFretId());
                ligneFret.setFret(fret);
                saveLigneFret(ligneFret);
            }

        }
        else
        {
            if (cltEnCpt.getSoldeCompte()>0)
            {
                if (billet != null)
                {
                    cltFret.setRaisonSociale(cltEnCpt.getRaisonSocial().toUpperCase());
                    cltFret.setFirstname(billet.getFirstname());
                    cltFret.setLastname(billet.getLastname());
                    cltFret.setNumeroPiece(billet.getNumeropiece());
                    cltFret.setTelephone(billet.getPassager().getPhone());
                    cltFret.setEmail(billet.getPassager().getMail());
                }
                else
                {
                    cltFret.setRaisonSociale(cltEnCpt.getRaisonSocial().toUpperCase());
                    cltFret.setFirstname(fretCltDTO.getFirstname());
                    cltFret.setLastname(fretCltDTO.getLastname());
                    cltFret.setNumeroPiece(fretCltDTO.getNumeroPiece());
                    cltFret.setTelephone(fretCltDTO.getTelephone());
                    cltFret.setEmail(fretCltDTO.getEmail());
                }

                List<LigneFret> ligneFretList = new ArrayList<>();
                cltFret.setExpEqDest(isExtEqDest);
                FretClient savedCltfret = fretCltRepository.save(cltFret);

                if (isExtEqDest)
                {
                    fretDTO.setRaisonSocialeDest(cltEnCpt.getRaisonSocial().toUpperCase());
                    fretDTO.setFirstnameDest(savedCltfret.getFirstname());
                    fretDTO.setLastnameDest(savedCltfret.getLastname());
                    fretDTO.setTelephoneDest(savedCltfret.getTelephone());
                    fretDTO.setEmailDest(savedCltfret.getEmail());
                }
                else
                {
                    fretDTO.setRaisonSocialeDest(fretCltDTO.getRaisonSociale());
                    fretDTO.setFirstnameDest(fretCltDTO.getFretDTOS().getFirstnameDest());
                    fretDTO.setLastnameDest(fretCltDTO.getFretDTOS().getLastnameDest());
                    fretDTO.setTelephoneDest(fretCltDTO.getFretDTOS().getTelephoneDest());
                    fretDTO.setEmailDest(fretCltDTO.getFretDTOS().getEmailDest());
                }

                float tht = 0;
                float montant = 0;
                float montant_ht = 0;
                float tva;
                for (LigneFretDTO ligneFretDTO : fretDTO.getLigneFretDTOList())
                {
                    List<Number> tarif = tarification.tarifBagoTab(ligneFretDTO.getCat_id(), null);
                    TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                    Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                    float prixBago = (float) tarif.get(4);
                    System.out.print(prixBago);
                    assert tbg != null;
                    if (tbg.getUnite().getVolume().getVolId() == 1)
                    {
                        tht = (prixBago * ligneFretDTO.getQuantity());
                        logger.info("Q: " + prixBago + " , " + ligneFretDTO.getQuantity());
                    }
                    if (tbg.getUnite().getVolume().getVolId() == 2)
                    {
                        tht = (prixBago * ligneFretDTO.getWeight());
                        logger.info("P: " + prixBago + " , " + ligneFretDTO.getWeight());
                    }
                    if (tbg.getUnite().getVolume().getVolId() == 3)
                    {
                        tht = (prixBago * ligneFretDTO.getVolume());
                        logger.info("V: " + prixBago + " , " + ligneFretDTO.getVolume());
                    }

                    montant_ht += (tht);
                    LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                    ligneFretList.add(ligneFret);
                }

                tva = (float) (montant_ht * 0.18);
                montant = montant_ht + tva;

                if (montant <= cltEnCpt.getSoldeCompte())
                {
                    cltEnCpt.setSoldeCompte((int) (cltEnCpt.getSoldeCompte() - montant));
                }
                else
                {
                    return null;
                }

                logger.info("Montant Hors Taxe 2 : " + montant_ht);
                fretDTO.setFretMontant_ht(montant_ht);
                fretDTO.setFretTva(tva);
                fretDTO.setFretMontant(montant);
                fretDTO.setFretEtat(true);
                if (billet != null)
                {
                    logger.info("AUTRE" + billet.getBilCode());
                    fretDTO.setBillet(billet.getBilCode());
                }
                Fret fret = addFretOps(applyPaye, fretDTO, cltCmptId, voyId, isExtEqDest);
                fret.setFretClient(savedCltfret);

                for (LigneFret ligneFret : ligneFretList)
                {
                    logger.info("ID FRET : " + fret.getFretId());
                    ligneFret.setFret(fret);
                    saveLigneFret(ligneFret);
                }

            }
            else
            {
                System.out.println("Solde insuffisant");
                return null;
            }

        }

        return cltFret;

    }


    public Fret update_payment(long fretId, Long cltCmptId, PaymentDTO paymentDTO)
    {
        log.info("PAIEMENT METHODE" + paymentDTO.getPaymentMethod());
        Fret getFret = this.fretRepository.findById(fretId).orElse(null);
        ClientEnCompte cltCmpt = this.clientEnCompteRepository.findById(cltCmptId).orElse(null);

        log.info("B");
        getFret.setFretMontant_ht(paymentDTO.getFretMontant_ht());
        getFret.setFretMontant(paymentDTO.getFretMontant());
        getFret.setFretRemise(paymentDTO.getFretRemise());
        getFret.setFretRemiseTaux(paymentDTO.getFretRemiseTaux());
        getFret.setApplyPayment(paymentDTO.isApplyPayment());
        getFret.setApplyTVA(paymentDTO.isApplyTVA());
        getFret.setFretTva(paymentDTO.getFretTva());
        getFret.setFretPayDate(LocalDateTime.now());
        getFret.setFretPayUsr(paymentDTO.getFretPayUsr());
        getFret.setPaymentMethod(paymentDTO.getPaymentMethod());

        log.info("C");
        if(null != cltCmpt)
        {
            float soldeC = cltCmpt.getSoldeCompte();
            if (soldeC >= paymentDTO.getFretMontant())
            {
                cltCmpt.setSoldeCompte(soldeC - paymentDTO.getFretMontant());
            }
        }
        log.info("D");

        Fret fretReturn = fretRepository.save(getFret);

        return fretReturn;
    }

    public FretDTOs update_magas(long fretId, float cout_magasinage, String usr_magasinage)
    {
        log.info("A");
        Fret getFret = this.fretRepository.findById(fretId).orElse(null);
        log.info("B");
        getFret.setCoutMagasinage(cout_magasinage);
        getFret.setUsrMagasinage(usr_magasinage);
        getFret.setDateMagasinage(LocalDateTime.now());
        log.info("C");
        Fret fretReturn = fretRepository.save(getFret);

        return this.convertToDTO1(fretReturn);
    }


    public float update_Fret(long fretId, List<LigneFretDTO> ligneFretDTOList, long cltCmptId, long voyId, long bilId, boolean isExtEqDest)
    {
        log.info(String.valueOf(ligneFretDTOList));

        float montant = calculateAndSaveLignesFret(ligneFretDTOList, fretId);

        //Fret fret = addFretOps(fretDTO, cltCmptId, voyId, isExtEqDest);
        //fret.setFretClient(updatedCltFret);
        //saveLignesFretWithFret(fretId, ligneFretDTOList);

        return montant;
    }

    private float calculateAndSaveLignesFret(List<LigneFretDTO> getLignesFret, long fretId)
    {
        Fret fret = fretRepository.findById(fretId).orElse(null);

        float montant_ht = fret.getFretMontant_ht();

        for (LigneFretDTO ligneFretDTO : getLignesFret)
        {
            LigneFret lfret = ligneFretRepository.findById(ligneFretDTO.getLigneFret_id()).orElse(null);
            if (lfret == null)
            {
                List<Number> tarif = tarification.tarifBagoTab(ligneFretDTO.getCat_id(), null);
                TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                float prixBago = (float) tarif.get(4);

                assert tbg != null;
                float tht = switch ((int) tbg.getUnite().getVolume().getVolId()) {
                    case 1 -> prixBago * ligneFretDTO.getQuantity();
                    case 2 -> prixBago * ligneFretDTO.getWeight();
                    case 3 -> prixBago * ligneFretDTO.getVolume();
                    default -> 0;
                };

                montant_ht += tht;
                LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                ligneFret.setFret(this.fretRepository.findById(fretId).orElse(null));
                saveLigneFret(ligneFret);
            }
        }

        float tva = montant_ht * 0.18f;
        float montant = montant_ht + tva;

        Fret frt = this.fretRepository.findById(fretId).orElse(null);

        frt.setFretMontant_ht(montant_ht);
        frt.setFretTva(tva);
        frt.setFretMontant(montant);

        fretRepository.save(frt);

        return montant;
    }

    public float calculateAndDeleteLignesFret(LigneFretDTO ligneFret, long fretId)
    {
        Fret fret = fretRepository.findById(fretId).orElse(null);

        float montant_ht = fret.getFretMontant_ht();

        List<Number> tarif = tarification.tarifBagoTab(ligneFret.getCat_id(), null);
        TypeBagage tbg = typeBagageRepository.findById(ligneFret.getTbg_id()).orElse(null);
        Categorie cat = categorieRepository.findById(ligneFret.getCat_id()).orElse(null);
        float prixBago = (float) tarif.get(4);

        assert tbg != null;
        float tht = switch ((int) tbg.getUnite().getVolume().getVolId()) {
            case 1 -> prixBago * ligneFret.getQuantity();
            case 2 -> prixBago * ligneFret.getWeight();
            case 3 -> prixBago * ligneFret.getVolume();
            default -> 0;
        };

        float tva = (montant_ht - tht) * 0.18f;
        float montant = (montant_ht - tht) + tva;
        montant_ht = montant_ht - tht;

        Fret frt = this.fretRepository.findById(fretId).orElse(null);

        frt.setFretMontant_ht(montant_ht);
        frt.setFretTva(tva);
        frt.setFretMontant(montant);
        fretRepository.save(frt);
        ligneFretRepository.deleteById(ligneFret.getLigneFret_id());

        return montant;
    }

    // Fonction pour assigner les lignes de fret au fret et les sauvegarder
    private void saveLignesFretWithFret(Long fretId, List<LigneFretDTO> ligneFretDTOList) {
        for (LigneFretDTO ligneFretDTO : ligneFretDTOList)
        {
            LigneFret lfret = ligneFretRepository.findById(ligneFretDTO.getLigneFret_id()).orElse(null);
            if (lfret == null)
            {
                TypeBagage tbg = typeBagageRepository.findById(ligneFretDTO.getTbg_id()).orElse(null);
                Categorie cat = categorieRepository.findById(ligneFretDTO.getCat_id()).orElse(null);
                LigneFret ligneFret = addLigneFret(ligneFretDTO, tbg, cat);
                ligneFret.setFret(this.fretRepository.findById(fretId).orElse(null));
                saveLigneFret(ligneFret);
            }
        }
    }

    public Categorie getPrixByTbgId(Long catId) {
        return categorieRepository.findById(catId).orElse(null);
    }

    public String callRemoteFunction() {
        String sql = "SELECT get_next_order_number_fret()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeFret_generator(String OrderNumber2)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        log.info("formattedInput est {} :" + OrderNumber2);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);
        String c =  "F" + OrderNumber2 + mois + an;
        return c;
    }

    public List<LigneFretDTOS> getLigneFretByFretId(long fretId) {
        List<Object[]> results = ligneFretRepository.getLigneFretByFretId(fretId);

        return results.stream().map(obj -> new LigneFretDTOS(
                ((Number) obj[0]).longValue(),  // ligne_fret_id
                ((Number) obj[1]).intValue(),   // quantity
                ((Number) obj[2]).floatValue(), // weight
                ((Number) obj[3]).floatValue(), // volume
                (String) obj[4],                // details
                (Boolean) obj[5],                // details
                ((Number) obj[6]).longValue(),  // fret_id
                (String) obj[7],                // fret_code
                (String) obj[8],                // raison_sociale_dest
                (String) obj[9],                // firstname_dest
                (String) obj[10],                // lastname_dest
                (String) obj[11],               // telephone_dest
                (String) obj[12],               // email_dest
                (Boolean) obj[13],              // exp_eq_dest
                ((Number) obj[14]).floatValue(), // fret_acompte
                ((Number) obj[15]).floatValue(), // fret_montant
                ((Number) obj[16]).floatValue(), // fret_tva
                ((Number) obj[17]).floatValue(), // fret_remise_taux
                ((Number) obj[18]).floatValue(), // fret_remise
                ((Number) obj[19]).floatValue(), // fret_montant_ht
                (Boolean) obj[20],               // applyTVA
                (Boolean) obj[21],               // applyPayment
                (String) obj[22],                // billet
                obj[23] != null ? ((java.sql.Timestamp) obj[23]).toLocalDateTime() : null, // fret_date
                (String) obj[24],                // fret_desc
                (String) obj[25],                // usr_login
                obj[26] != null ? ((java.sql.Timestamp) obj[26]).toLocalDateTime() : null, // fret_pay_date
                (String) obj[27],                // fret_pay_usr
                (Boolean) obj[28],               // fret_etat
                ((Number) obj[29]).floatValue(), // cout_magasinage
                ((Number) obj[30]).floatValue(), // cout_magasinage_remise
                (String) obj[31],                // usr_magasinage
                obj[32] != null ? ((java.sql.Timestamp) obj[32]).toLocalDateTime() : null, // date_magasinage
                (String) obj[33],                // usr_login_payable
                obj[34] != null ? ((java.sql.Timestamp) obj[34]).toLocalDateTime() : null, // date_encaiss_payable
                ((Number) obj[35]).longValue(),  // voy_id (correspondance respectée)
                ((Number) obj[36]).longValue(),  // fret_clt_id
                (String) obj[37],                // raison_sociale
                (Boolean) obj[38],               // client_exp_eq_dest
                (String) obj[39],                // client_firstname
                (String) obj[40],                // client_lastname
                (String) obj[41],                // numero_piece
                (String) obj[42],                // client_telephone
                (String) obj[43]                 // client_email
        )).collect(Collectors.toList());
    }

    public FretDTOs getFretByCode(String code)
    {
        Fret fret = fretRepository.getFretByCode(code);
        if (fret != null)
        {
            return this.convertToDTOX(fret);  // Retourne une liste contenant un seul DTO
        }
        else
        {
            return null;
        }

    }

    public FretDTOs getFretByCode_(String code)
    {
        Fret fret = fretRepository.getFretByCode_(code);
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
        FretClient frtClt = fretCltRepository.findById(dto.getFretClt_id()).orElse(null);

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

    public Fret cancelFret(AnnulationDTO annulationDTO)
    {
        log.info("A");
        Fret getFret = this.fretRepository.findById(annulationDTO.getFretId()).orElse(null);
        //ClientEnCompte cltCmpt = this.clientEnCompteRepository.findById(cltCmptId).orElse(null);

        log.info("B");
        getFret.setFretEtat(false);
        getFret.setApplyPayment(false);
        getFret.setMotif(annulationDTO.getMotif());

        log.info("C");

        Fret fretReturn = fretRepository.save(getFret);

        for (LigneFret lf : fretReturn.getLigneFrets())
        {
            lf.setEtat(false);
            ligneFretRepository.save(lf);
        }

        return fretReturn;
    }



}
