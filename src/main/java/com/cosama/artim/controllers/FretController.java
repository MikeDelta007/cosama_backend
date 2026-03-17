package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.Categorie;
import com.cosama.artim.models.Fret;
import com.cosama.artim.models.FretClient;
import com.cosama.artim.services.FretService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/fret")
@RequiredArgsConstructor
public class FretController {
    @Autowired
    private final FretService fret;

    @PostMapping("/createFret")
    @ResponseStatus(HttpStatus.CREATED)

    public FretClientResponseDTO createFret(@RequestBody FretCltDTO fretCltDTO, @RequestParam long cltCmptId, @RequestParam long voyId, @RequestParam long bilId, @RequestParam boolean expEqDest)
    {
        return fret.add_Fret(fretCltDTO, cltCmptId, voyId, bilId, expEqDest);
    }

    @PutMapping("/updateFret")
    @ResponseStatus(HttpStatus.CREATED)
    public float updateFret(@RequestParam long idFret, @RequestBody List<LigneFretDTO> lignefret, @RequestParam long cltCmptId, @RequestParam long voyId, @RequestParam long bilId, @RequestParam boolean expEqDest)
    {
        return fret.update_Fret(idFret, lignefret, cltCmptId, voyId, bilId, expEqDest);
    }

    @PutMapping("/updateMagCout")
    @ResponseStatus(HttpStatus.CREATED)
    public FretDTOs updateMaga(@RequestParam long idFret, @RequestParam float cout_magasinage, @RequestParam String usr_magasinage)
    {
        return fret.update_magas(idFret, cout_magasinage, usr_magasinage);
    }

    @PutMapping("/doPayment")
    @ResponseStatus(HttpStatus.CREATED)
    public Fret payment(@RequestParam long idFret, @RequestParam Long cltCmptId, @RequestBody PaymentDTO paymentDTO)
    {
        return fret.update_payment(idFret, cltCmptId, paymentDTO);
    }

    @DeleteMapping("/deleteLigneFret")
    @ResponseStatus(HttpStatus.OK)
    public float delete_lFret(@RequestBody LigneFretDTO lignefret, @RequestParam long idFret)
    {
        return fret.calculateAndDeleteLignesFret(lignefret, idFret);
    }


    @GetMapping("/getFret")
    @ResponseStatus(HttpStatus.OK)
    public List<FretDTOs> getFret()
    {
        return fret.getAllFret();
    }

    @GetMapping("/getFretsByEtatPayment")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<FretDTOs>> getFretsParEtat()
    {
        return fret.getAllFret_();
    }


    @GetMapping("/getFretsByEtatPayment_")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<FretDTOs>> getFretsParEtat_()
    {
        return fret.getAllFret_bis();
    }

    @GetMapping("/byFret")
    @ResponseStatus(HttpStatus.OK)
    public List<LigneFretDTOS> getLigneFretByFretId(@RequestParam long fretId) {
        return fret.getLigneFretByFretId(fretId);
    }

    @GetMapping("/getFretByCltCompte")
    @ResponseStatus(HttpStatus.OK)
    public List<FretDTOs> getFretByCltCompte(@RequestParam long cltCmptId)
    {
        return fret.getFretByCltCompte(cltCmptId);
    }

    @GetMapping("/getFretByCode")
    @ResponseStatus(HttpStatus.OK)
    public FretDTOs getFretByCode(@RequestParam String code)
    {
        return fret.getFretByCode(code);
    }

    @PutMapping("/cancel-fret")
    @ResponseStatus(HttpStatus.CREATED)
    public Fret cancel_fret(@RequestBody AnnulationDTO annulationDTO)
    {
        return fret.cancelFret(annulationDTO);
    }
}
