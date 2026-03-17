package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.BateauRepository;
import com.cosama.artim.repositories.FretRepository;
import com.cosama.artim.repositories.MotifRepository;
import com.cosama.artim.repositories.ReclamationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReclamationService
{
    @Autowired
    private final ReclamationRepository reclamationRepository;
    @Autowired
    private final FretRepository fretRepository;
    @Autowired
    private final MotifRepository motifRepository;

    private final JdbcTemplate jdbcTemplate;

    public String callRemoteFunction() {
        String sql = "SELECT get_next_order_number_reclamation()"; // fonction SQL depuis la Base de donnee ARTIM
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String codeReclamation_generator(String OrderNumber2)
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        log.info("formattedInput est {} :" + OrderNumber2);
        String mois = String.format("%02d", month + 1);
        String an = String.valueOf(year).substring(2);
        String c =  "R" + OrderNumber2 + mois + an;
        return c;
    }

    public Reclamation createRec(ReclamationDTO reclamationDTO)
    {
        String ordre_rec = callRemoteFunction();
        String code_rec = codeReclamation_generator(ordre_rec);
        Fret frt = this.fretRepository.findById(reclamationDTO.getFret_id()).orElse(null);
        Reclamation rec = Reclamation.builder()
                .dossierReclamation(code_rec)
                .dateReclamation(LocalDateTime.now())
                .motifReclamation(reclamationDTO.getMotif_reclamation())
                .decision(0)
                .fret(frt)
                .build();
        return reclamationRepository.save(rec);
    }

    public Reclamation updateRec(long id, ReclamationDTO reclamationDTO){
        Reclamation reclamation = this.reclamationRepository.findById(id).orElse(null);
        if (reclamation != null) {
            reclamation.setDecision(reclamationDTO.getDecision());
            reclamation.setDateTraitement(LocalDateTime.now());
            return reclamationRepository.save(reclamation);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }
    public List<Reclamation> getAllRec()
    {
        List<Reclamation> reclamations = reclamationRepository.findAll();
        return reclamations;
    }

    public List<Motif> getMotifs()
    {
        List<Motif> motifs = motifRepository.findAll();
        return motifs;
    }

    public Map<String, List<ReclamationDTO>> getAllRec_()
    {
        List<Reclamation> recs = reclamationRepository.findAll();

        return recs.stream()
                .collect(Collectors.groupingBy(
                        f -> {
                            switch (f.getDecision()) {
                                case 0: return "Reçu";
                                case 1: return "Rejeté";
                                case 2: return "Accepté";
                                default: return "Inconnu";
                            }
                        },
                        Collectors.mapping(f -> {
                            ReclamationDTO dto = new ReclamationDTO();
                            dto.setDate_reclamation(f.getDateReclamation());
                            dto.setDossier_reclamation(f.getDossierReclamation());
                            dto.setRecl_id(f.getReclId());
                            dto.setFret_id(f.getFret().getFretId());
                            dto.setDate_traitement(f.getDateTraitement());
                            dto.setDecision(f.getDecision());
                            dto.setMotif_reclamation(f.getMotifReclamation());

                            return dto;
                        }, Collectors.toList())
                ));
    }


}
