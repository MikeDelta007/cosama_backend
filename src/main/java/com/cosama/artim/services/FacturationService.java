package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacturationService
{
    @Autowired
    private final ClientEnCompteRepository clientEnCompteRepository;
    @Autowired
    private final CltModeReglementRepository cltModeReglementRepository;
    @Autowired
    private final CltTypeReglementRepository cltTypeReglementRepository;
    @Autowired
    private final FactureRepository factureRepository;
    @Autowired
    private final LineFactureRepository lineFactureRepository;
    @Autowired
    private final FretRepository fretRepository;
    @Autowired
    private final FretCltRepository fretCltRepository;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FretService.class);

    public String callRemoteFunction() {
        String sql = "SELECT get_next_order_number_facture()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeFact_generator(String OrderNumber)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        log.info("formattedInput est {} :" + OrderNumber);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);
        String c =  "FCT-" + OrderNumber + mois + an;
        return c;
    }


    public CltFacture createFacture(CltFactureDTO cltFactureDTO, String usrFact)
    {
        logger.info(String.valueOf(cltFactureDTO.getId_cltcmpt()));
        ClientEnCompte cltCmpt = clientEnCompteRepository.findById(cltFactureDTO.getId_cltcmpt()).orElse(null);
        CltFacture fct_ = null;
        if (cltCmpt != null)
        {
            CltModeReglement cltMdr = cltModeReglementRepository.findById(cltCmpt.getCltModeReglement().getModergltId()).orElse(null);
            String ordreString = this.callRemoteFunction();
            String code_fact = this.codeFact_generator(ordreString);
            CltFacture fct = CltFacture.builder()
                    .factCode(code_fact)
                    .libelle(cltFactureDTO.getLibelle())
                    .firstname(cltFactureDTO.getFirstname())
                    .lastname(cltFactureDTO.getLastname())
                    .adresse(cltFactureDTO.getAdresse())
                    .telephone(cltFactureDTO.getTelephone())
                    .dateFacturation(LocalDateTime.now())
                    .dateEcheance(LocalDateTime.now().plusDays(cltMdr.getDureeJr()))
                    .clientEnCompte(cltCmpt)
                    .observation(cltFactureDTO.getObservation())
                    .montantFacture(cltFactureDTO.getMontant_facture())
                    .tvaFacture(cltFactureDTO.getTva_facture())
                    .etatFacturation(false)
                    .estEmis(false)
                    .montantVerse(0)
                    .reliquat(0)
                    .usrFacture(usrFact)
                    .build();
            fct_ = factureRepository.save(fct);
        }

        if (cltCmpt == null)
        {
            String ordreString = this.callRemoteFunction();
            String code_fact = this.codeFact_generator(ordreString);
            CltFacture fct = CltFacture.builder()
                    .factCode(code_fact)
                    .libelle(cltFactureDTO.getLibelle())
                    .firstname(cltFactureDTO.getFirstname())
                    .lastname(cltFactureDTO.getLastname())
                    .adresse(cltFactureDTO.getAdresse())
                    .telephone(cltFactureDTO.getTelephone())
                    .dateFacturation(LocalDateTime.now())
                    .dateEcheance(LocalDateTime.now())
                    .clientEnCompte(cltCmpt)
                    .observation(cltFactureDTO.getObservation())
                    .montantFacture(cltFactureDTO.getMontant_facture())
                    .tvaFacture(cltFactureDTO.getTva_facture())
                    .etatFacturation(false)
                    .estEmis(false)
                    .montantVerse(0)
                    .reliquat(0)
                    .usrFacture(usrFact)
                    .build();
            fct_ = factureRepository.save(fct);
        }

        for (CltDetailsFactureDTO cdf : cltFactureDTO.getCltDetailsFactures())
        {
            CltDetailsFacture cdf_ = new CltDetailsFacture();
            cdf_.setCltFacture(fct_);
            cdf_.setEtat_ligne(cdf.isEtat_ligne());
            cdf_.setDescription_ligne(cdf.getDescription_ligne());
            cdf_.setMontant_ligne(cdf.getMontant_ligne());
            cdf_.setMontant_ligne_tva(cdf.getMontant_ligne_tva());
            cdf_.setFretId(cdf.getFret_id());
            lineFactureRepository.save(cdf_);
            logger.info("Ok");
        }

        return fct_;
    }

    public CltFacture updateFacture(long id, boolean isEmis, CltFactureDTO cltFactureDTO)
    {
        CltFacture cfact = this.factureRepository.findById(id).orElse(null);
        if (cfact != null)
        {
            logger.info(String.valueOf(cltFactureDTO.getId_cltcmpt()));

            cfact.setLibelle(cltFactureDTO.getLibelle());
            cfact.setObservation(cltFactureDTO.getObservation());
            cfact.setMontantFacture(cltFactureDTO.getMontant_facture());
            cfact.setTvaFacture(cltFactureDTO.getTva_facture());
            cfact.setEtatFacturation(false);
            cfact.setEstEmis(isEmis);
            CltFacture fct_ = factureRepository.save(cfact);

            for (CltDetailsFactureDTO cdf : cltFactureDTO.getCltDetailsFactures())
            {
                CltDetailsFacture cdf0 = this.lineFactureRepository.findById(cdf.getLignefct_id()).orElse(null);
                if (cdf0 != null)
                {
                    cdf0.setCltFacture(fct_);
                    cdf0.setEtat_ligne(cdf.isEtat_ligne());
                    cdf0.setDescription_ligne(cdf.getDescription_ligne());
                    cdf0.setMontant_ligne(cdf.getMontant_ligne());
                    cdf0.setMontant_ligne_tva(cdf.getMontant_ligne_tva());
                    cdf0.setFretId(cdf.getFret_id());
                    lineFactureRepository.save(cdf0);
                    logger.info("Old line fret");
                }
                else
                {
                    CltDetailsFacture cdf_ = new CltDetailsFacture();
                    cdf_.setCltFacture(fct_);
                    cdf_.setEtat_ligne(cdf.isEtat_ligne());
                    cdf_.setDescription_ligne(cdf.getDescription_ligne());
                    cdf_.setMontant_ligne(cdf.getMontant_ligne());
                    cdf_.setMontant_ligne_tva(cdf.getMontant_ligne_tva());
                    cdf_.setFretId(cdf.getFret_id());
                    lineFactureRepository.save(cdf_);
                    logger.info("New Line Fret");
                }

            }
            return fct_;
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public CltFacture updatePayment(long id, CltFactureDTO cltFactureDTO, String usr_paie)
    {
        CltFacture cfact = this.factureRepository.findById(id).orElse(null);
        ClientEnCompte cltcmpt = this.clientEnCompteRepository.findById(cfact.getClientEnCompte().getCltcmptId()).orElse(null);
        CltTypeReglement ctr = this.cltTypeReglementRepository.findById(cltFactureDTO.getClt_tr()).orElse(null);
        if (cfact != null)
        {
            float r = (cfact.getMontantFacture() + cfact.getTvaFacture()) - cltFactureDTO.getMontant_verse();
            cfact.setMontantVerse(cltFactureDTO.getMontant_verse());
            cfact.setCltTypeReglement(ctr);
            cfact.setEtatFacturation(true);
            cfact.setUsrPaieFacture(usr_paie);
            cfact.setRefPaieFacture(cltFactureDTO.getRef_paieFacture());
            cfact.setReliquat(r);
            CltFacture fct_ = this.factureRepository.save(cfact);

            if (cltcmpt.getSoldeCompte() < cltcmpt.getPlafond())
            {
                float ajout;
                ajout = cltcmpt.getSoldeCompte() + fct_.getMontantVerse();
                cltcmpt.setSoldeCompte(ajout);
                this.clientEnCompteRepository.save(cltcmpt);
            }

            return fct_;
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public List<FretDTOs> getFretByCltCompte(Long idCltCmpt, LocalDate startDate, LocalDate endDate)
    {
        List<Fret> frets = fretRepository.getFretByInfos(idCltCmpt, startDate, endDate);
        return frets.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
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

    public List<FactureGroupDTO> getFacturesGroupees() {
        List<CltFacture> allFactures = factureRepository.findAll();

        // Client fictif pour les cas null
        ClientEnCompte clientAutres = new ClientEnCompte();
        clientAutres.setCltcmptId(0L);
        clientAutres.setRaisonSocial("AUTRES FACTURES");

        return allFactures.stream()
                .collect(Collectors.groupingBy(f -> {
                    return f.getClientEnCompte() != null ? f.getClientEnCompte() : clientAutres;
                }))
                .entrySet().stream()
                .map(entry -> {
                    FactureGroupDTO dto = new FactureGroupDTO();
                    dto.setCltcmpt_id(entry.getKey().getCltcmptId());
                    dto.setRaison_social(entry.getKey().getRaisonSocial());

                    List<CltFactureDTO> factures = entry.getValue().stream().map(f -> {
                        CltFactureDTO fDto = new CltFactureDTO();
                        fDto.setCltfct_id(f.getCltfctId());
                        fDto.setFact_code(f.getFactCode());
                        fDto.setMontant_facture(f.getMontantFacture());
                        fDto.setDate_facturation(f.getDateFacturation());
                        fDto.setFirstname(f.getFirstname());
                        fDto.setLastname(f.getLastname());
                        fDto.setTelephone(f.getTelephone());
                        fDto.setId_cltcmpt(f.getClientEnCompte() != null ? f.getClientEnCompte().getCltcmptId() : 0L);
                        fDto.setLibelle(f.getLibelle());
                        fDto.setObservation(f.getObservation());
                        fDto.setEst_emis(f.getEstEmis());
                        fDto.setMontant_verse(f.getMontantVerse());
                        fDto.setReliquat(f.getReliquat());
                        fDto.setEtat_facturation(f.getEtatFacturation());

                        List<CltDetailsFactureDTO> detailsDTO = f.getCltDetailsFactures().stream()
                                .map(detail -> {
                                    CltDetailsFactureDTO dDto = new CltDetailsFactureDTO();
                                    dDto.setLignefct_id(detail.getLignefctId());
                                    dDto.setDescription_ligne(detail.getDescription_ligne());
                                    dDto.setMontant_ligne(detail.getMontant_ligne());
                                    dDto.setMontant_ligne_tva(detail.getMontant_ligne_tva());
                                    dDto.setFret_id(detail.getFretId());
                                    return dDto;
                                })
                                .collect(Collectors.toList());

                        fDto.setCltDetailsFactures(detailsDTO);
                        return fDto;
                    }).collect(Collectors.toList());

                    dto.setFactures(factures);
                    return dto;
                }).collect(Collectors.toList());
    }


    public Map<String, List<CltFactureDTO>> getFacturesParEtat() {
        List<CltFacture> allFactures = factureRepository.findAll();

        return allFactures.stream()
                .collect(Collectors.groupingBy(f -> Boolean.TRUE.equals(f.getEtatFacturation()) ? "PAYES" : "IMPAYES",
                        Collectors.mapping(f -> {
                            CltFactureDTO fDto = new CltFactureDTO();
                            fDto.setCltfct_id(f.getCltfctId());
                            fDto.setFact_code(f.getFactCode());
                            fDto.setMontant_facture(f.getMontantFacture());
                            fDto.setDate_facturation(f.getDateFacturation());
                            fDto.setDate_echeance(f.getDateEcheance());
                            fDto.setFirstname(f.getFirstname());
                            fDto.setLastname(f.getLastname());
                            fDto.setTelephone(f.getTelephone());
                            fDto.setId_cltcmpt(
                                    f.getClientEnCompte() != null ? f.getClientEnCompte().getCltcmptId() : 0L
                            );
                            fDto.setLibelle(f.getLibelle());
                            fDto.setObservation(f.getObservation());
                            fDto.setEst_emis(f.getEstEmis());
                            fDto.setMontant_verse(f.getMontantVerse());
                            fDto.setReliquat(f.getReliquat());

                            List<CltDetailsFactureDTO> detailsDTO = f.getCltDetailsFactures().stream()
                                    .map(detail -> {
                                        CltDetailsFactureDTO dDto = new CltDetailsFactureDTO();
                                        dDto.setLignefct_id(detail.getLignefctId());
                                        dDto.setDescription_ligne(detail.getDescription_ligne());
                                        dDto.setMontant_ligne(detail.getMontant_ligne());
                                        dDto.setMontant_ligne_tva(detail.getMontant_ligne_tva());
                                        dDto.setFret_id(detail.getFretId());
                                        return dDto;
                                    })
                                    .collect(Collectors.toList());

                            fDto.setCltDetailsFactures(detailsDTO);
                            return fDto;
                        }, Collectors.toList()))
                );
    }

    public List<CltFactureDTO> getToutesLesFactures(Long cltCmptId, LocalDate startDate, LocalDate endDate) {
        return factureRepository.getFactureBeetweenDate(cltCmptId, startDate, endDate).stream()
                .map(f -> {
                    CltFactureDTO fDto = new CltFactureDTO();
                    fDto.setCltfct_id(f.getCltfctId());
                    fDto.setFact_code(f.getFactCode());
                    fDto.setMontant_facture(f.getMontantFacture());
                    fDto.setTva_facture(f.getTvaFacture());
                    fDto.setDate_facturation(f.getDateFacturation());
                    fDto.setDate_echeance(f.getDateEcheance());
                    fDto.setFirstname(f.getFirstname());
                    fDto.setLastname(f.getLastname());
                    fDto.setTelephone(f.getTelephone());
                    fDto.setId_cltcmpt(f.getClientEnCompte().getCltcmptId());
                    fDto.setLibelle(f.getLibelle());
                    fDto.setObservation(f.getObservation());
                    fDto.setEst_emis(f.getEstEmis());
                    fDto.setMontant_verse(f.getMontantVerse());
                    fDto.setReliquat(f.getReliquat());
                    fDto.setEtat_facturation(f.getEtatFacturation());
                    return fDto;
                }).collect(Collectors.toList());
    }







}
