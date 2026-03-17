package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.AchatOnLine;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.EtatBillet;
import com.cosama.artim.models.Passager;
import com.cosama.artim.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/billetterie")
@RequiredArgsConstructor
public class BilleterieController {
    @Autowired
    private final VenteBilletService venteBillet;
    @Autowired
    private final ReportService reportBillet;
    @Autowired
    private final AchatOnLineService achatBilletOnline;

    //@PostMapping(value="/createBillet/{cinorpass}")
    //@ResponseStatus(HttpStatus.CREATED)
    //public Passager createPassager(@RequestBody PassagerWithBilletDTO passagerWithBilletDTO)
    //{
    //    return venteBillet.createPassagerWithBillets(passagerWithBilletDTO);
    //}

    @PostMapping(value="/reportBillet/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BilletsDTO reportBillet(@PathVariable long billId, @RequestParam boolean updatePsg, @RequestParam long voyId, @RequestBody BilletDTO billetDTO, @RequestParam float penality)
    {
        return reportBillet.reportBillet(billId, updatePsg, voyId, billetDTO, penality);
    }

    @GetMapping("/billetDetails/{billId}")
    @ResponseStatus(HttpStatus.OK)
    public Billet getBilletBillet(@PathVariable long billId)
    {
        return venteBillet.getBilletWithCriteres(billId);
    }

    @GetMapping("/billet-state")
    @ResponseStatus(HttpStatus.OK)
    public List<EtatBilletDTO> getDetailsBillet(@RequestParam String bilCode)
    {
        return venteBillet.getEtatBillet(bilCode);
    }

    //@PostMapping("/billet-online")
    //public AchatOnLine achatBilletOnline(@RequestBody AchatOnLineDTO achatOnLineDTO)
    //{
    //    return achatBilletOnline.achat_billet_online(achatOnLineDTO);
    //}

    //@GetMapping("/reservation")
    //@ResponseStatus(HttpStatus.OK)
    //public AchatOnLine getAchatOnLine(@RequestParam long aolId) {
    //    return achatBilletOnline.getAchatDetails(aolId);
    //}

    @GetMapping("/detailsAchatBillet")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AchatOnLineWithBilletsDTO> getAchatDetails(@RequestParam String codeAchat) {
        AchatOnLineWithBilletsDTO achatDetails = achatBilletOnline.getAchatWithBillets(codeAchat);
        return ResponseEntity.ok(achatDetails);
    }

    @GetMapping("/details-billet")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BilletsDTO> getBilletDetails(@RequestParam String codeBillet)
    {
        BilletsDTO billetDetails = venteBillet.getBilletDetails(codeBillet);
        return ResponseEntity.ok(billetDetails);
    }

    @PatchMapping("/billet-check")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> patchBilletCheck(@RequestBody Map<String, Object> payload) {

        Long bilId = Long.valueOf(payload.get("bilId").toString());
        Boolean checkIn = Boolean.valueOf(payload.get("checkIn").toString());

        Billet updatedBillet = venteBillet.patchBilCheckIn(bilId, checkIn);
        return ResponseEntity.ok(updatedBillet);
    }

    @PatchMapping("/billet-inboard/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> patchBilletCheck(@PathVariable Long bilId) {

        Billet updatedBillet = venteBillet.patchBilInboard(bilId);
        return ResponseEntity.ok(updatedBillet);
    }

    @PatchMapping("/billet-print/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> patchBilletPrint(@PathVariable Long bilId, @RequestBody PrintBilletDTO printBilletDTO)
    {
        Billet updatedBillet = venteBillet.patchBilPrint(bilId, printBilletDTO);
        return ResponseEntity.ok(updatedBillet);
    }

    @PatchMapping("/billet-cancel/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilCancel(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBillet.cancelBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @PutMapping("/billet-rembourser/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BilletsDTO> bilRembourser(@PathVariable Long bilId, @RequestParam String motif, @RequestParam Integer penality, @RequestParam String usrRemb)
    {
        BilletsDTO bilRembours = venteBillet.rembourserBil(bilId, motif, penality, usrRemb);
        return ResponseEntity.ok(bilRembours);
    }

    @GetMapping("/findPassager/{cinorpass}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PassagerWithBilletDTO> findPassenger(@PathVariable String cinorpass)
    {
        PassagerWithBilletDTO pass = venteBillet.findPassenger(cinorpass);
        return ResponseEntity.ok(pass);
    }

    @PostMapping("/billet-online")
    public BilletsDTO achatBillet(@RequestBody PassagerWithBilletDTO passagerWithBilletDTO, @RequestParam Long voyId)
    {
        return venteBillet.createDirectBillet(passagerWithBilletDTO, voyId);
    }

    @GetMapping("/getFretByBillet")
    @ResponseStatus(HttpStatus.OK)
    public FretDTOs getFretByBillet(@RequestParam String billet)
    {
        return venteBillet.getFretByBillet(billet);
    }

}
