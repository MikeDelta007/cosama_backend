package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.services.EtatService;
import com.cosama.artim.services.VenteBilletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/etat")
@RequiredArgsConstructor
public class EtatController {
    @Autowired
    private final EtatService etatService;
    @GetMapping("/manifesteAdulte")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getManifesteAdulte(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteADULTE(voyId, batId);
    }

    @GetMapping("/recapManifAdulte")
    @ResponseStatus(HttpStatus.OK)
    public List<RecapPlacesManifeste> statManifesteAdulte(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.statManifesteAdulte(voyId, batId);
    }

    @GetMapping("/manifesteEnfant")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getManifesteEnfant(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteENFANT(voyId, batId);
    }

    @GetMapping("/recapManifEnfant")
    @ResponseStatus(HttpStatus.OK)
    public List<RecapPlacesManifeste> statManifesteEnfant(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.statManifesteEnfant(voyId, batId);
    }

    @GetMapping("/manifesteBebe")
    @ResponseStatus(HttpStatus.OK)
    public List<BebeDTO> getManifesteBebe(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteBEBE(voyId, batId);
    }

    @GetMapping("/passagersNonEmbarque")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getPassNonEmb(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.passagersNonEmbarq(voyId, batId);
    }

    @GetMapping("/billetsReportes")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getBilletsReportes(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteBilletReportes(voyId, batId);
    }

    @GetMapping("/rapportPax")
    @ResponseStatus(HttpStatus.OK)
    public List<RapportPAX> getRapportPAX(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.rapportPAX(voyId, batId);
    }

    ////

    @GetMapping("/manifesteAdulteC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getManifesteAdulte_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteADULTEC(voyId, batId);
    }

    @GetMapping("/manifesteEnfantC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getManifesteEnfant_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteENFANTC(voyId, batId);
    }

    @GetMapping("/manifesteBebeC")
    @ResponseStatus(HttpStatus.OK)
    public List<BebeDTO> getManifesteBebe_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteBEBEC(voyId, batId);
    }

    @GetMapping("/passagersNonEmbarqueC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getPassNonEmb_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.passagersNonEmbarqC(voyId, batId);
    }

    @GetMapping("/billetsReportesC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteAdulteDTO> getBilletsReportes_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.manifesteBilletReportesC(voyId, batId);
    }

    @GetMapping("/rapportPaxC")
    @ResponseStatus(HttpStatus.OK)
    public List<RapportPAX> getRapportPAX_(@RequestParam long voyId, @RequestParam long batId)
    {
        return etatService.rapportPAXC(voyId, batId);
    }

    //
    @GetMapping("/fretCltCompte")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretCltCompte(@RequestParam long voyId)
    {
        return etatService.fretCltCompte(voyId);
    }

    @GetMapping("/manifesteFret")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> manifesteFret(@RequestParam long voyId)
    {
        return etatService.manifesteFret(voyId);
    }

    @GetMapping("/fretAnnuler")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> fretAnnuler(@RequestParam long voyId)
    {
        return etatService.fretAnnuler(voyId);
    }

    @GetMapping("/fretAttente")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> fretAttente(@RequestParam long voyId)
    {
        return etatService.fretAttente(voyId);
    }

    @GetMapping("/fretTVA")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretTVA(@RequestParam long voyId)
    {
        return etatService.fretTVA(voyId);
    }

    @GetMapping("/fretAPayer")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretAPayer(@RequestParam long voyId)
    {
        return etatService.fretAPayer(voyId);
    }

    //

    @GetMapping("/fretCltCompteC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretCltCompte_(@RequestParam long voyId)
    {
        return etatService.fretCltCompteC(voyId);
    }

    @GetMapping("/manifesteFretC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> manifesteFret_(@RequestParam long voyId)
    {
        return etatService.manifesteFretC(voyId);
    }

    @GetMapping("/fretAnnulerC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> fretAnnuler_(@RequestParam long voyId)
    {
        return etatService.fretAnnulerC(voyId);
    }

    @GetMapping("/fretAttenteC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO_> fretAttente_(@RequestParam long voyId)
    {
        return etatService.fretAttenteC(voyId);
    }

    @GetMapping("/fretTVAC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretTVA_(@RequestParam long voyId)
    {
        return etatService.fretTVAC(voyId);
    }

    @GetMapping("/fretAPayerC")
    @ResponseStatus(HttpStatus.OK)
    public List<ManifesteFretDTO> fretAPayer_(@RequestParam long voyId)
    {
        return etatService.fretAPayerC(voyId);
    }

}
