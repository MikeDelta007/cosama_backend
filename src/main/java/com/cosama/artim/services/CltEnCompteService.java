package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CltEnCompteService
{
    @Autowired
    private final ClientEnCompteRepository clientEnCompteRepository;
    @Autowired
    private final CltModeReglementRepository cltModeReglementRepository;
    @Autowired
    private final CltTypeReglementRepository cltTypeReglementRepository;
    @Autowired
    private final FactureRepository factureRepository;

    public ClientEnCompte getCltEnCompte(Long cltC)
    {
        return clientEnCompteRepository.findCltEnCompteById(cltC);
    }

    public List<ClientEnCompteDTO> getAllCltEnCompte()
    {
        List<ClientEnCompte> cltcmpts = clientEnCompteRepository.findAll();
        return cltcmpts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ClientEnCompteDTO convertToDTO(ClientEnCompte clientEnCompte) {
        ClientEnCompteDTO dto = new ClientEnCompteDTO();
        dto.setCltcmptId(clientEnCompte.getCltcmptId());
        dto.setRaisonSocial(clientEnCompte.getRaisonSocial());
        dto.setFirstnameContact(clientEnCompte.getFirstnameContact());
        dto.setLastnameContact(clientEnCompte.getLastnameContact());
        dto.setContact(clientEnCompte.getContact());
        dto.setMail(clientEnCompte.getMail());
        dto.setPlafond(clientEnCompte.getPlafond());
        dto.setSoldeCompte(clientEnCompte.getSoldeCompte());
        dto.setCptgen_compta(clientEnCompte.getCptgen_compta());
        dto.setCpttiers_compta(clientEnCompte.getCpttiers_compta());

        if (clientEnCompte.getCltModeReglement() != null) {
            dto.setModergltId(clientEnCompte.getCltModeReglement().getModergltId());
        } else {
            dto.setModergltId(null); // ou laisse vide si DTO permet les valeurs null
        }

        return dto;
    }

    public List<CltModeReglement> getAllModeReglment()
    {
        return cltModeReglementRepository.findAll();
    }

    public ClientEnCompte createClientEnCompte(ClientEnCompteDTO clientEnCompteDTO)
    {
        CltModeReglement cltmdrt = cltModeReglementRepository.findById(clientEnCompteDTO.getModergltId()).orElse(null);
        ClientEnCompte client = ClientEnCompte.builder()
                .raisonSocial(clientEnCompteDTO.getRaisonSocial())
                .firstnameContact(clientEnCompteDTO.getFirstnameContact())
                .lastnameContact(clientEnCompteDTO.getLastnameContact())
                .contact(clientEnCompteDTO.getContact())
                .mail(clientEnCompteDTO.getMail())
                .plafond(clientEnCompteDTO.getPlafond())
                .soldeCompte(clientEnCompteDTO.getPlafond())
                .cptgen_compta(clientEnCompteDTO.getCptgen_compta())
                .cpttiers_compta(clientEnCompteDTO.getCpttiers_compta())
                .cltModeReglement(cltmdrt)
                .build();
        return clientEnCompteRepository.save(client);
    }

    public ClientEnCompte updateClientEnCompte(long id, ClientEnCompteDTO clientEnCompteDTO){
        ClientEnCompte cltEnCmpt = this.clientEnCompteRepository.findById(id).orElse(null);
        CltModeReglement cltmdrt = cltModeReglementRepository.findById(clientEnCompteDTO.getModergltId()).orElse(null);
        if (cltEnCmpt != null) {
            cltEnCmpt.setRaisonSocial(clientEnCompteDTO.getRaisonSocial());
            cltEnCmpt.setFirstnameContact(clientEnCompteDTO.getFirstnameContact());
            cltEnCmpt.setLastnameContact(clientEnCompteDTO.getLastnameContact());
            cltEnCmpt.setContact(clientEnCompteDTO.getContact());
            cltEnCmpt.setMail(clientEnCompteDTO.getMail());
            cltEnCmpt.setPlafond(clientEnCompteDTO.getPlafond());
            cltEnCmpt.setCltModeReglement(cltmdrt);
            return clientEnCompteRepository.save(cltEnCmpt);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public List<TypeReglementDTO> getAllReglement() {
        List<CltTypeReglement> typeReglements = cltTypeReglementRepository.findAll();

        return typeReglements.stream()
                .map(this::convertToDTOZ)
                .collect(Collectors.toList());
    }

    // Exemple de méthode de mapping
    private TypeReglementDTO convertToDTOZ(CltTypeReglement entity) {
        TypeReglementDTO dto = new TypeReglementDTO();
        dto.setTyperglt_id(entity.getTypergltId());
        dto.setLibelle_typeReglm(entity.getLibelleTypeReglm());
        // ajoute les autres champs...
        return dto;
    }


    public List<SituationClientDTO> situationClient(long cltcmpt, int annee) {
        List<Object[]> results = factureRepository.getSituationClient(cltcmpt, annee);

        return results.stream().map(obj -> new SituationClientDTO(
                ((Number) obj[0]).longValue(),
                (String) obj[1],
                ((Number) obj[2]).intValue(),
                (String) obj[3],
                ((Number) obj[4]).intValue(),
                ((Number) obj[5]).floatValue(),
                ((Number) obj[6]).floatValue(),
                ((Number) obj[7]).floatValue(),
                ((Number) obj[8]).floatValue(),
                ((Number) obj[9]).floatValue()
        )).collect(Collectors.toList());
    }




}
