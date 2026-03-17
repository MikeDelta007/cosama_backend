package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/clientEnCompte")
@RequiredArgsConstructor
public class ClientEnCompteController
{
    @Autowired
    private final CltEnCompteService clientEnCompte;

    @Autowired
    private final FacturationService facturation;

    @GetMapping("/cltEnCompte/{cltCmptId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientEnCompte cltEnCmpt(@PathVariable long cltCmptId)
    {
        return clientEnCompte.getCltEnCompte(cltCmptId);
    }

    @GetMapping("/cltEnComptes")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ClientEnCompteDTO> cltEnCmpt()
    {
        return clientEnCompte.getAllCltEnCompte();
    }

    @GetMapping("/cltModeReglement")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CltModeReglement> cltModeRgltm()
    {
        return clientEnCompte.getAllModeReglment();
    }

    @PostMapping("/createCltCpt")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientEnCompte createCltCmp(@RequestBody ClientEnCompteDTO clientEnCompteDTO)
    {
        return clientEnCompte.createClientEnCompte(clientEnCompteDTO);
    }

    @PutMapping("/updateCltEnCmpt/{idCltCmpt}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientEnCompte updateAgence(@PathVariable long idCltCmpt, @RequestBody ClientEnCompteDTO clientEnCompteDTO) {
        return clientEnCompte.updateClientEnCompte(idCltCmpt, clientEnCompteDTO);
    }

    @PostMapping("/createFacture")
    @ResponseStatus(HttpStatus.CREATED)
    public CltFacture createFacture(@RequestBody CltFactureDTO cltFactureDTO, @RequestParam String usrFact)
    {
        return facturation.createFacture(cltFactureDTO, usrFact);
    }

    @PatchMapping("/updateFacture/{idFact}")
    @ResponseStatus(HttpStatus.CREATED)
    public CltFacture createFacture(@PathVariable long idFact, @RequestParam boolean isEmis, @RequestBody CltFactureDTO cltFactureDTO)
    {
        return facturation.updateFacture(idFact, isEmis, cltFactureDTO);
    }

    @PatchMapping("/updatePayment/{idFact}")
    @ResponseStatus(HttpStatus.CREATED)
    public CltFacture updatePayment(@PathVariable long idFact, @RequestBody CltFactureDTO cltFactureDTO, @RequestParam String usrPaie)
    {
        return facturation.updatePayment(idFact, cltFactureDTO, usrPaie);
    }

    @GetMapping("/getFretsByCltCompte")
    @ResponseStatus(HttpStatus.OK)
    public List<FretDTOs> getFretByCltCompte(@RequestParam Long idCltCompte, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {
        return facturation.getFretByCltCompte(idCltCompte, startDate, endDate);
    }

    @GetMapping("/groupFactureByCltCompte")
    @ResponseStatus(HttpStatus.OK)
    public List<FactureGroupDTO> getFactureByCltCompte()
    {
        return facturation.getFacturesGroupees();
    }

    @GetMapping("/getFacturesByEtatPayment")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<CltFactureDTO>> getFacturesParEtat()
    {
        return facturation.getFacturesParEtat();
    }

    @GetMapping("/getAllTypeReglement")
    @ResponseStatus(HttpStatus.OK)
    public List<TypeReglementDTO> getTypeReglement()
    {
        return clientEnCompte.getAllReglement();
    }

    @GetMapping("/getSituationClient")
    @ResponseStatus(HttpStatus.OK)
    public List<SituationClientDTO> getSituClient(@RequestParam long cltcmpt, @RequestParam int annee)
    {
        return clientEnCompte.situationClient(cltcmpt, annee);
    }

    @GetMapping("/getToutesLesFactures")
    @ResponseStatus(HttpStatus.OK)
    public List<CltFactureDTO> getToutesLesFactures(@RequestParam Long idCltCompte, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {
        return facturation.getToutesLesFactures(idCltCompte, startDate, endDate);
    }
}
