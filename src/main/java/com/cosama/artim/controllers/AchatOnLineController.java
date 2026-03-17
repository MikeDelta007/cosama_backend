package com.cosama.artim.controllers;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.services.*;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/api/achatOnline")
@RequiredArgsConstructor
public class AchatOnLineController {
    @Autowired
    private final ParametrageService parametrage;
    @Autowired
    private final TarificationService tarification;
    @Autowired
    private final VenteBilletService venteBilletService;
    @Autowired
    private final StatistiqueService statistiqueService;
    @Autowired
    private final FretService fret;
    @Autowired
    private final AchatOnLineService achatBilletOnLine;
    @Autowired
    private final MessageService smsService;
    @Autowired
    private final ReclamationService reclamationService;

    @PostMapping(value="/sendSMS")
    public ResponseEntity<Integer> sendMessage(@RequestParam("token") String token,
                                               @RequestBody SingleMessageDetails singleMessageDetails) throws Exception {

        return ResponseEntity.ok(this.smsService.sendSms(token,singleMessageDetails));
    }

    @PostMapping("/billet-online")
    public AchatOnLine achatBilletOnline(@RequestBody AchatOnLineDTO achatOnLineDTO)
    {
        return achatBilletOnLine.achat_billet_online(achatOnLineDTO);
    }

    @GetMapping("/motif")
    @ResponseStatus(HttpStatus.OK)
    public List<Motif> getMotif()
    {
        return reclamationService.getMotifs();
    }


    @GetMapping("/voyageDpt")
    @ResponseStatus(HttpStatus.OK)
    public Voyage getVoyageDpt(@RequestParam String voyDate, @RequestParam int voyDpt, @RequestParam long batId)
    {
        return parametrage.getVoyByDept(voyDate, voyDpt, batId);
    }

    @GetMapping("/getVoitures")
    @ResponseStatus(HttpStatus.OK)
    public List<CategorieDTO> getVoiture()
    {
        return parametrage.getAllVoitures();
    }

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<Nationalite> getAllCountry()
    {
        return parametrage.getAllCountries();
    }

    @GetMapping("/groupesCriteres")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupeCritereDTO> getAllGroupeCriteres() {
        return tarification.getAllGroupeCriteres();
    }

    @PostMapping("/prixPlace")
    @ResponseStatus(HttpStatus.OK)
    public List<Number> getPrixPlace(@RequestBody TarifRequest tarifRequest) {
        return tarification.tarifPlaceTab(tarifRequest.getTplId(), tarifRequest.getCriteres());
    }

    @GetMapping("/typePlaces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePlacesDTO> getAllTypePlace()
    {
        return parametrage.getAllTypePlace();
    }

    @GetMapping("/findPassager/{cinorpass}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PassagerWithBilletDTO> findPassenger(@PathVariable String cinorpass)
    {
        PassagerWithBilletDTO pass = venteBilletService.findPassenger(cinorpass);
        return ResponseEntity.ok(pass);
    }

    @GetMapping("/niveaux/{batId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Niveau> niveauWithPlaces(@PathVariable long batId)
    {
        return parametrage.getNiveauPlacesBateauId(batId);
    }

    @GetMapping("/isPlaceInVoyagePlace")
    public List<Boolean> checkPlacesInVoyage(@RequestParam List<Long> placeIds,
                                             @RequestParam Long voyId,
                                             @RequestParam Long typePlace) {
        // Appel du service avec le filtre typePlace
        return parametrage.isPlacesInVoyagePlace(placeIds, voyId, typePlace);
    }

    @PostMapping("/createFret")
    @ResponseStatus(HttpStatus.CREATED)
    public FretClient createFret(@RequestBody FretCltDTO fretCltDTO, @RequestParam Long cltCmptId, @RequestParam Long voyId, @RequestParam Long bilId, @RequestParam boolean expEqDest, @RequestParam boolean applyPaye)
    {
        return fret.add_FretOnLine(applyPaye, fretCltDTO, cltCmptId, voyId, bilId, expEqDest);
    }

    @PostMapping("/createReclamation")
    @ResponseStatus(HttpStatus.CREATED)
    public Reclamation createRec(@RequestBody ReclamationDTO reclamationDTO)
    {
        return reclamationService.createRec(reclamationDTO);
    }

    @PutMapping("/updateReclamation")
    @ResponseStatus(HttpStatus.CREATED)
    public Reclamation updateReclamation(@RequestParam long id, @RequestBody ReclamationDTO reclamationDTO)
    {
        return reclamationService.updateRec(id, reclamationDTO);
    }

    @GetMapping("/prix_fret/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Categorie getPriceFret(@PathVariable long catId)
    {
        return fret.getPrixByTbgId(catId);
    }

    @GetMapping("/generateQR")
    public String generateQRCode(@RequestParam String text) {
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

    @GetMapping("/places")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceEditDTO> getAllPlaces()
    {
        return parametrage.getAllPlace();
    }

    @GetMapping("/bateau")
    @ResponseStatus(HttpStatus.OK)
    public List<BateausDTO> getAllBateau()
    {
        return parametrage.getAllBateau();
    }

    /**
    @GetMapping("/voyages")
    @ResponseStatus(HttpStatus.OK)
    public List<VoyageDTO> getAllVoyage()
    {
        return parametrage.getAllVoyage();
    }**/

    @GetMapping("/dispoPlaces")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceStatDTO> getStatDispoP(@RequestParam Long batId, @RequestParam Long voyId)
    {
        return statistiqueService.getStats(batId, voyId);
    }

    @GetMapping("/typePieces")
    @ResponseStatus(HttpStatus.OK)
    public List<TypePieceDTO> getAllPieces()
    {
        return parametrage.getAllTypePiece();
    }

    @GetMapping("/villes")
    @ResponseStatus(HttpStatus.OK)
    public List<VilleDTO> getAllVille()
    {
        return parametrage.getAllVille();
    }

    @GetMapping("/getFretByCode_")
    @ResponseStatus(HttpStatus.OK)
    public FretDTOs getFretByCode_(@RequestParam String code)
    {
        return fret.getFretByCode_(code);
    }

    @GetMapping("/tarifs")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriesDTO> getAllCat() {
        return tarification.getAllCategorie();
    }

    @GetMapping("/planVoyage2")
    @ResponseStatus(HttpStatus.OK)
    public List<PlanVoyageDTO> getPlan2()
    {
        return parametrage.getPlanning2();
    }
}
