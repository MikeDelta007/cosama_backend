package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.Billet;
import com.cosama.artim.models.VoyagePlace;
import com.cosama.artim.services.ParametrageService;
import com.cosama.artim.services.ReportService;
import com.cosama.artim.services.TarificationService;
import com.cosama.artim.services.VenteBilletService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/chefDeGare")
@RequiredArgsConstructor
public class ChefDeGareController {
    @Autowired
    private final ParametrageService parametrage;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final VenteBilletService venteBilletService;
    @Autowired
    private final ReportService reportBillet;

    @GetMapping("/groupesCriteres")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupeCritereDTO> getAllGroupeCriteres() {
        return tarification.getAllGroupeCriteres2();
    }

    @GetMapping("/places")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceEditDTO> getAllPlaces()
    {
        return parametrage.getAllPlace();
    }

    @GetMapping("/typePlaces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePlacesDTO> getAllTypePlace()
    {
        return parametrage.getAllTypePlace();
    }

    @GetMapping("/bateau")
    @ResponseStatus(HttpStatus.OK)
    public List<BateausDTO> getAllBateau()
    {
        return parametrage.getAllBateau();
    }

    @GetMapping("/voyages")
    @ResponseStatus(HttpStatus.OK)
    public List<VoyageDTO> getAllVoyages()
    {
        return parametrage.getAllVoyage();
    }

    @GetMapping("/voyagesSinceNow")
    @ResponseStatus(HttpStatus.OK)
    public List<VoyageDTO> getAllVoyages_()
    {
        return parametrage.getAllVoySinceNow();
    }

    @GetMapping("/availablePlace")
    public ResponseEntity<List<PlaceDTO>> getAvailablePlaces(
            @RequestParam Long tplcId,
            @RequestParam Long voyId,
            @RequestParam Long batId) {
        List<PlaceDTO> availablePlaces = venteBilletService.getAvailablePlaces(tplcId, voyId, batId);
        if (availablePlaces.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune place n'est disponible
        }
        return ResponseEntity.ok(availablePlaces); // 200 OK avec les places disponibles
    }

    @GetMapping("/getPlaceByVoyAndBat")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<VoyagePlace> getBilletByVoyAndBat(@RequestParam Long voyId, @RequestParam Long batId, @RequestParam Long plcId)
    {
        VoyagePlace vp = venteBilletService.getVPByVoyAndBat(voyId, batId, plcId);
        if (vp == null) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucun billet n'est disponible
        }
        return ResponseEntity.ok(vp); // 200 OK avec le billet disponible
    }

    @GetMapping("/reservedPlace")
    public ResponseEntity<List<PlaceDTO>> getreservedPlaces(
            @RequestParam Long tplcId,
            @RequestParam Long voyId,
            @RequestParam Long batId) {
        List<PlaceDTO> availablePlaces = venteBilletService.getReservedPlaces(tplcId, voyId, batId);
        if (availablePlaces.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune place n'est disponible
        }
        return ResponseEntity.ok(availablePlaces); // 200 OK avec les places disponibles
    }

    @DeleteMapping("/libererPlace")
    @ResponseStatus(HttpStatus.OK)
    public void libererPlace(@RequestParam Long plcId, @RequestParam Long voyId, @RequestParam Long batId)
    {
        venteBilletService.liberationPlace(plcId, voyId, batId);
    }

    @PostMapping(value="/doReservation")
    @ResponseStatus(HttpStatus.CREATED)
    public VoyagePlace doReservation(@RequestParam Long plcId,
                                     @RequestParam Long voyId,
                                     @RequestParam Long batId) {
        return venteBilletService.blocagePlace(plcId, voyId, batId);
    }

    @GetMapping("/typePieces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePieceDTO> getAllTypePiece()
    {
        return parametrage.getAllTypePiece();
    }

    @GetMapping("/villes")
    @ResponseStatus(HttpStatus.OK)
    public List<VilleDTO> getVilles()
    {
        return parametrage.getAllVille();
    }

    @GetMapping("/details-billet")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BilletsDTO> getBilletDetails(@RequestParam String codeBillet)
    {
        BilletsDTO billetDetails = venteBilletService.getBilletDetails(codeBillet);
        return ResponseEntity.ok(billetDetails);
    }

    @PostMapping(value="/reportBillet/{billId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BilletsDTO reportBillet(@PathVariable long billId, @RequestParam boolean updatePsg, @RequestParam long voyId, @RequestBody BilletDTO billetDTO, @RequestParam float penality)
    {
        return reportBillet.reportBillet(billId, updatePsg, voyId, billetDTO, penality);
    }

    @PutMapping("/billet-rembourser/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BilletsDTO> bilRembourser(@PathVariable Long bilId, @RequestParam String motif, @RequestParam Integer penality, @RequestParam String usrRemb)
    {
        BilletsDTO bilRembours = venteBilletService.rembourserBil(bilId, motif, penality, usrRemb);
        return ResponseEntity.ok(bilRembours);
    }

    @DeleteMapping("/shotPlace")
    @ResponseStatus(HttpStatus.OK)
    public void killPlace(@RequestParam Long plcId, @RequestParam Long voyId, @RequestParam Long batId)
    {
        venteBilletService.shotPlace(plcId, voyId, batId);
    }

    @PatchMapping("/billet-cancel/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilCancel(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBilletService.cancelBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @PatchMapping("/billet-isrembrs/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilRemb(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBilletService.autRembBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @PatchMapping("/billet-isreporSur/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilRep(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBilletService.autRepBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @PatchMapping("/billet-isedit/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilEdit(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBilletService.autEditBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @PatchMapping("/billet-iscancel/{bilId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Billet> bilisCancel(@PathVariable Long bilId)
    {
        Billet bilCancel = venteBilletService.autCancBil(bilId);
        return ResponseEntity.ok(bilCancel);
    }

    @GetMapping("/generateQR")
    public String generateQRCode(@RequestParam String text)
    {
        try {
            Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150, hints);
            BufferedImage qrImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64Image;
        } catch (Exception e) {
            return "Error generating QR code: " + e.getMessage();
        }
    }

}
