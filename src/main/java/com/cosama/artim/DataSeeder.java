package com.cosama.artim;

import com.cosama.artim.dto.CritereDTO;
import com.cosama.artim.dto.GroupeCritereDTO;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration
public class DataSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TypePlaceRepository typePlaceRepository;
    @Autowired
    private TypePieceRepository typePieceRepository;
    @Autowired
    private VolumeRepository volumeRepository;
    @Autowired
    private UniteRepository uniteRepository;
    @Autowired
    private BateauRepository bateauRepository;
    @Autowired
    private CritereRepository critereRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private CltModeReglementRepository cltModeReglementRepository;
    @Autowired
    private TypeBagageRepository typeBagageRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private GroupeCritereRepository groupeCritereRepository;
    @Autowired
    private PassagerRepository passagerRepository;
    @Autowired
    private ClientEnCompteRepository clientEnCompteRepository;

    @Value("${file.path.excel}")
    private String filePath;

    @Value("${file2.path.excel}")
    private String filePath2;

    @Value("${file3.path.excel}")
    private String filePath3;

    @Value("${file4.path.excel}")
    private String filePath4;

    @Value("${file5.path.excel}")
    private String filePath5;

    @Value("${file6.path.excel}")
    private String filePath6;

    @Value("${file7.path.excel}")
    private String filePath7;

    @Value("${file8.path.excel}")
    private String filePath8;

    @Value("${file9.path.excel}")
    private String filePath9;

    public String getCellValue(Cell cell)
    {
        if (cell == null)
        {
            return null;
        }

        switch (cell.getCellType())
        {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                {
                    return cell.getDateCellValue().toString(); // Si c'est une date
                }
                return String.valueOf((long) cell.getNumericCellValue()); // Nombre entier
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // ou cell.getStringCellValue() selon le cas
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }


    @Bean
    @Transactional
    public CommandLineRunner seedDatabase(Environment env, AgenceRepository agenceRepository, ProfilRepository profilRepository, VilleRepository villeRepository) {

        return args -> {
            String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto");
            if ("create".equalsIgnoreCase(ddlAuto))
            {
                CltModeReglement clmdr = CltModeReglement.builder()
                        .libelleModeReglm("30 JOURS")
                        .dureeJr(30)
                        .actif(true)
                        .build();
                cltModeReglementRepository.save(clmdr);

                CltModeReglement clmdr2 = CltModeReglement.builder()
                        .libelleModeReglm("Huitaine")
                        .dureeJr(8)
                        .actif(true)
                        .build();
                cltModeReglementRepository.save(clmdr2);

                Ville ville1 = Ville.builder()
                        .vilId(1)
                        .vilCode("DK")
                        .vilNom("Dakar")
                        .build();
                Ville v1 = villeRepository.save(ville1);
                Ville ville2 = Ville.builder()
                        .vilId(2)
                        .vilCode("ZG")
                        .vilNom("Ziguinchor")
                        .build();
                Ville v2 = villeRepository.save(ville2);
                Ville ville3 = Ville.builder()
                        .vilId(3)
                        .vilCode("CB")
                        .vilNom("Carabane")
                        .build();
                Ville v3 = villeRepository.save(ville3);
                logger.info("Villes initialisées avec succès.");

                Agence agence = Agence.builder()
                        .agcNom("Gare Maritime de Dakar")
                        .agcTel("+221338212900")
                        .agcFax("+221338212901")
                        .agcMail("")
                        .agcResp("CHEF DE GARE")
                        .sigle("GMD")
                        .codeAgc("GMID")
                        .ville(v1)
                        .build();
                Agence gmd = agenceRepository.save(agence);
                Agence agence2 = Agence.builder()
                        .agcNom("Gare Maritime de Ziguinchor")
                        .agcTel("+221339917200")
                        .agcFax("+221339917201")
                        .agcMail("")
                        .agcResp("CHEF DE GARE")
                        .sigle("GMZ")
                        .codeAgc("GMZ")
                        .ville(v2)
                        .build();
                agenceRepository.save(agence2);
                Agence agence3 = Agence.builder()
                        .agcNom("Escale de Carabane")
                        .agcTel("+221339843190")
                        .agcFax("+221339843190")
                        .agcMail("")
                        .agcResp("CHEF DE GARE")
                        .sigle("GMK")
                        .codeAgc("GMC")
                        .ville(v3)
                        .build();
                agenceRepository.save(agence3);
                Agence agence4 = Agence.builder()
                        .agcNom("COSAMA")
                        .agcTel("+221338213434")
                        .agcFax("+221338213440")
                        .agcMail("")
                        .agcResp("DG/DEC")
                        .sigle("SIEGE")
                        .codeAgc("SIEGE")
                        .ville(v1)
                        .build();
                Agence cosama = agenceRepository.save(agence4);
                logger.info("Agences initialisées avec succès.");

                Profil profil = Profil.builder()
                        .prflLibelle("Administrateur")
                        .actif(true)
                        .addBillet(true)
                        .editBillet(true)
                        .cancelBillet(true)
                        .addCheckBillet(true)
                        .addEmbarqment(true)
                        .doRemboursement(true)
                        .doRepSurclassment(true)
                        .addFret(true)
                        .editFret(true)
                        .cancelFret(true)
                        .payeFret(true)
                        .viewStat(true)
                        .addVoyage(true)
                        .viewVoyage(true)
                        .editVoyage(true)
                        .pointerVoyage(true)
                        .planVoyage(true)
                        .editParam(true)
                        .valide(true)
                        .rechercher(true)
                        .addCltCompte(true)
                        .editCltCompte(true)
                        .delCltCompte(true)
                        .addFacture(true)
                        .editFacture(true)
                        .delFacture(true)
                        .addReglement(true)
                        .editReglement(true)
                        .delReglement(true)
                        .bloqPlaces(true)
                        .addPassager(true)
                        .editPassager(true)
                        .delPassager(true)
                        .addNavData(true)
                        .editNavData(true)
                        .viewEtat(true)
                        .campagne(true)
                        .reclamation(true)
                        .cancelVoyage(true)
                        .checkFret(true)
                        .delFretDetails(true)
                        .edition(true)
                        .build();
                Profil prf = profilRepository.save(profil);

                User adminAccount = userRepository.findByRole(Role.ADMIN);
                if (null == adminAccount)
                {
                    User user = new User();
                    user.setFirstname("admin");
                    user.setLastname("admin");
                    user.setLogin("admin");
                    user.setAgence(cosama);
                    user.setProfil(prf);
                    user.setRole(Role.ADMIN);
                    user.setPassword(new BCryptPasswordEncoder().encode("admin"));
                    user.setEtat(true);
                    userRepository.save(user);
                }
                TypePlace tplc = TypePlace.builder()
                        .tplcNom("Chaise")
                        .tplcCode("CH")
                        .tplcPrix(5000)
                        .build();
                typePlaceRepository.save(tplc);

                TypePlace tplc2 = TypePlace.builder()
                        .tplcNom("Cabine 2 places")
                        .tplcCode("C2")
                        .tplcPrix(26500)
                        .build();
                typePlaceRepository.save(tplc2);

                TypePlace tplc3 = TypePlace.builder()
                        .tplcNom("Cabine 4 places")
                        .tplcCode("C4")
                        .tplcPrix(24500)
                        .build();
                typePlaceRepository.save(tplc3);

                TypePlace tplc4 = TypePlace.builder()
                        .tplcNom("Cabine 8 places")
                        .tplcCode("C8")
                        .tplcPrix(12500)
                        .build();
                typePlaceRepository.save(tplc4);

                TypePiece tpiece = TypePiece.builder()
                        .tpieceNom("Passport")
                        .dispo(true)
                        .build();
                typePieceRepository.save(tpiece);

                TypePiece tpiece2 = TypePiece.builder()
                        .tpieceNom("CNI")
                        .dispo(true)
                        .build();
                typePieceRepository.save(tpiece2);

                TypePiece tpiece3 = TypePiece.builder()
                        .tpieceNom("Permis")
                        .dispo(true)
                        .build();
                typePieceRepository.save(tpiece3);

                TypePiece tpiece4 = TypePiece.builder()
                        .tpieceNom("Extrait")
                        .dispo(true)
                        .build();
                typePieceRepository.save(tpiece4);

                TypePiece tpiece5 = TypePiece.builder()
                        .tpieceNom("Autres")
                        .dispo(true)
                        .build();
                typePieceRepository.save(tpiece5);

                Volume vol = Volume.builder()
                        .volNom("Quantité")
                        .build();
                Volume q = volumeRepository.save(vol);

                Volume vol2 = Volume.builder()
                        .volNom("Poids")
                        .build();
                Volume p = volumeRepository.save(vol2);

                Volume vol3 = Volume.builder()
                        .volNom("Volume")
                        .build();
                Volume v = volumeRepository.save(vol3);

                Unite unit1 = Unite.builder()
                        .uniteNom("Metre Cube")
                        .uniteCode("M3")
                        .volume(v)
                        .build();
                uniteRepository.save(unit1);

                Unite unit2 = Unite.builder()
                        .uniteNom("Sacs")
                        .uniteCode("SACS")
                        .volume(q)
                        .build();
                uniteRepository.save(unit2);

                Unite unit3 = Unite.builder()
                        .uniteNom("Kilogrammes")
                        .uniteCode("KG")
                        .volume(p)
                        .build();
                uniteRepository.save(unit3);

                Unite unit4 = Unite.builder()
                        .uniteNom("Tonnes")
                        .uniteCode("T")
                        .volume(p)
                        .build();
                uniteRepository.save(unit4);

                Unite unit5 = Unite.builder()
                        .uniteNom("A l'unité")
                        .uniteCode("UNITE")
                        .volume(q)
                        .build();
                uniteRepository.save(unit5);

                Unite unit6 = Unite.builder()
                        .uniteNom("Casiers")
                        .uniteCode("CASIER")
                        .volume(q)
                        .build();
                uniteRepository.save(unit6);

                Unite unit7 = Unite.builder()
                        .uniteNom("Metre linéaire")
                        .uniteCode("ML")
                        .volume(q)
                        .build();
                uniteRepository.save(unit7);

                Unite unit8 = Unite.builder()
                        .uniteNom("Aller vide/Retour plein")
                        .uniteCode("AV/RP")
                        .volume(v)
                        .build();
                uniteRepository.save(unit8);

                Unite unit9 = Unite.builder()
                        .uniteNom("Aller plein/Retour vide")
                        .uniteCode("AP/RV")
                        .volume(v)
                        .build();
                uniteRepository.save(unit9);

                Unite unit10 = Unite.builder()
                        .uniteNom("Aller plein/Retour plein")
                        .uniteCode("AP/RP")
                        .volume(v)
                        .build();
                uniteRepository.save(unit10);

                Unite unit11 = Unite.builder()
                        .uniteNom("Place")
                        .uniteCode("PLACE")
                        .volume(v)
                        .build();
                uniteRepository.save(unit11);

                Unite unit12 = Unite.builder()
                        .uniteNom("Aller vide")
                        .uniteCode("AV")
                        .volume(v)
                        .build();
                uniteRepository.save(unit12);

                Unite unit13 = Unite.builder()
                        .uniteNom("Retour plein")
                        .uniteCode("RP")
                        .volume(v)
                        .build();
                uniteRepository.save(unit13);

                Bateau bat1 = Bateau.builder()
                        .batRef("ASD")
                        .batNom("Aline Sitoé Diatta")
                        .batNbplace(484)
                        .agence(gmd)
                        .batEtat(true)
                        .batMarkeur("#00FF00")
                        .build();
                bateauRepository.save(bat1);

                Bateau bat2 = Bateau.builder()
                        .batRef("AGN")
                        .batNom("Aguène")
                        .batNbplace(206)
                        .agence(gmd)
                        .batEtat(true)
                        .batMarkeur("#FFFF00")
                        .build();
                bateauRepository.save(bat2);

                Bateau bat3 = Bateau.builder()
                        .batRef("DBG")
                        .batNom("Diambogne")
                        .batNbplace(206)
                        .agence(gmd)
                        .batEtat(true)
                        .batMarkeur("#FF0000")
                        .build();
                bateauRepository.save(bat3);

                Critere crt1 = Critere.builder()
                        .crt_nom("Enfant")
                        .build();
                critereRepository.save(crt1);

                Critere crt2 = Critere.builder()
                        .crt_nom("Etranger")
                        .build();
                critereRepository.save(crt2);

                Critere crt3 = Critere.builder()
                        .crt_nom("Adulte")
                        .build();
                critereRepository.save(crt3);

                Critere crt4 = Critere.builder()
                        .crt_nom("Résident")
                        .build();
                critereRepository.save(crt4);

                Critere crt5 = Critere.builder()
                        .crt_nom("Etudiant")
                        .build();
                critereRepository.save(crt5);

                Critere crt6 = Critere.builder()
                        .crt_nom("Service")
                        .build();
                critereRepository.save(crt6);

                Critere crt7 = Critere.builder()
                        .crt_nom("VIP")
                        .build();
                critereRepository.save(crt7);

                Critere crt8 = Critere.builder()
                        .crt_nom("Report")
                        .build();
                critereRepository.save(crt8);

                Critere crt9 = Critere.builder()
                        .crt_nom("P->C2")
                        .build();
                critereRepository.save(crt9);

                Critere crt10 = Critere.builder()
                        .crt_nom("P->C4")
                        .build();
                critereRepository.save(crt10);

                Critere crt11 = Critere.builder()
                        .crt_nom("P->C8")
                        .build();
                critereRepository.save(crt11);

                Critere crt12 = Critere.builder()
                        .crt_nom("C4->C2")
                        .build();
                critereRepository.save(crt12);

                Critere crt13 = Critere.builder()
                        .crt_nom("C8->C2")
                        .build();
                critereRepository.save(crt13);

                Critere crt14 = Critere.builder()
                        .crt_nom("C8->C4")
                        .build();
                critereRepository.save(crt14);

                Critere crt15 = Critere.builder()
                        .crt_nom("ACCP")
                        .build();
                critereRepository.save(crt15);

                Critere crt16 = Critere.builder()
                        .crt_nom("C2->VIP")
                        .build();
                critereRepository.save(crt16);

                Critere crt17 = Critere.builder()
                        .crt_nom("Etranger-Résident")
                        .build();
                critereRepository.save(crt17);

                Critere crt18 = Critere.builder()
                        .crt_nom("Ecart Etranger et Résident")
                        .build();
                critereRepository.save(crt18);

                Critere crt19 = Critere.builder()
                        .crt_nom("Carabane")
                        .build();
                critereRepository.save(crt19);

                Critere crt20 = Critere.builder()
                        .crt_nom("C4->VIP")
                        .build();
                critereRepository.save(crt20);

                Critere crt21 = Critere.builder()
                        .crt_nom("C8->VIP")
                        .build();
                critereRepository.save(crt21);

                Critere crt22 = Critere.builder()
                        .crt_nom("Ecart Etranger-Résident et Etranger")
                        .build();
                critereRepository.save(crt22);

                Critere crt23 = Critere.builder()
                        .crt_nom("P->VIP")
                        .build();
                critereRepository.save(crt23);

                Niveau niv1 = Niveau.builder()
                        .nivNom("Pont 2")
                        .build();
                niveauRepository.save(niv1);

                Niveau niv2 = Niveau.builder()
                        .nivNom("Pont 3")
                        .build();
                niveauRepository.save(niv2);

                Niveau niv3 = Niveau.builder()
                        .nivNom("Pont 4")
                        .build();
                niveauRepository.save(niv3);

                Niveau avant = Niveau.builder()
                        .nivNom("Avant")
                        .build();
                niveauRepository.save(avant);

                Niveau arriere = Niveau.builder()
                        .nivNom("Arrière")
                        .build();
                niveauRepository.save(arriere);

                try (FileInputStream file = new FileInputStream(filePath))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet placeSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorPlace = placeSheet.iterator();
                    while (iteratorPlace.hasNext()) {
                        Row row = iteratorPlace.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        TypePlace tp = typePlaceRepository.findById(Long.valueOf(getCellValue(row.getCell(1)))).orElse(null);
                        Niveau nv = niveauRepository.findById(Long.valueOf(getCellValue(row.getCell(2)))).orElse(null);
                        Bateau bt = bateauRepository.findById(Long.valueOf(getCellValue(row.getCell(3)))).orElse(null);

                        Place place = new Place();
                        place.setPlcCode(getCellValue(row.getCell(0)));
                        place.setTypePlace(tp);
                        place.setNiveau(nv);
                        place.setBateau(bt);
                        place.setPlcEtat(Boolean.parseBoolean(getCellValue(row.getCell(4))));
                        place.setSexe(getCellValue(row.getCell(5)));
                        place.setSituation(getCellValue(row.getCell(6)));
                        place.setIsCarabane(Boolean.parseBoolean(getCellValue(row.getCell(7))));
                        placeRepository.save(place);
                    }

                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath2))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet nationalitySheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorNationality = nationalitySheet.iterator();
                    while (iteratorNationality.hasNext()) {
                        Row row = iteratorNationality.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        Nationalite nat = new Nationalite();
                        nat.setCode(getCellValue(row.getCell(0)));
                        nat.setName(getCellValue(row.getCell(1)));
                        nationaliteRepository.save(nat);
                    }

                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath3))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet typeBagageSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorTypeBagage = typeBagageSheet.iterator();
                    while (iteratorTypeBagage.hasNext()) {
                        Row row = iteratorTypeBagage.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        TypeBagage tb = new TypeBagage();
                        tb.setTbgNom(getCellValue(row.getCell(0)));
                        Unite unite = uniteRepository.findById(Long.valueOf(getCellValue(row.getCell(1)))).orElse(null);
                        tb.setUnite(unite);
                        tb.setVolId(unite.getVolume().getVolId());
                        tb.setEtat(true);
                        typeBagageRepository.save(tb);
                    }
                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath4))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet tarifBagageSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorTarifBago = tarifBagageSheet.iterator();
                    while (iteratorTarifBago.hasNext()) {
                        Row row = iteratorTarifBago.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        Categorie ct = new Categorie();
                        ct.setCatNom(getCellValue(row.getCell(0)));
                        TypeBagage tb = typeBagageRepository.findById(Long.valueOf(getCellValue(row.getCell(1)))).orElse(null);
                        ct.setTypeBagage(tb);
                        ct.setCode(getCellValue(row.getCell(2)));
                        ct.setCatPrix(Float.valueOf(getCellValue(row.getCell(3))));
                        ct.setCatPrixTtc(Float.valueOf(getCellValue(row.getCell(4))));
                        ct.setTauxRemise(Float.valueOf(getCellValue(row.getCell(5))));
                        ct.setFraisMag(Integer.valueOf(getCellValue(row.getCell(7))));
                        ct.setBagage(Boolean.parseBoolean(getCellValue(row.getCell(9))));
                        categorieRepository.save(ct);
                    }
                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream fis = new FileInputStream(filePath5);
                     Workbook workbook = new XSSFWorkbook(fis)) {
                        Sheet sheet = workbook.getSheetAt(0);
                        for (Row row : sheet) {
                            if (row.getRowNum() == 0) continue; // skip header

                            String nom = getCellValue(row.getCell(0));
                            String criteresStr = getCellValue(row.getCell(1)); // ex: "1,2,5"
                            Integer onLine = Integer.valueOf(getCellValue(row.getCell(2)));

                            GroupeCritere dto = new GroupeCritere();
                            dto.setGrpcrtNom(nom);
                            dto.setOnLine(onLine);

                            if (criteresStr != null && !criteresStr.isEmpty())
                            {
                                String[] ids = criteresStr.split(";");
                                logger.info(Arrays.toString(ids));
                                List<Critere> critereDTOList = new ArrayList<>();

                                for (String idStr : ids) {
                                    Critere critereDTO = new Critere();
                                    critereDTO.setCrtId(Long.parseLong(idStr.trim()));
                                    critereDTOList.add(critereDTO);
                                }

                                dto.setCriteres(critereDTOList);
                            }
                            groupeCritereRepository.save(dto);
                        }
                        workbook.close();
                        System.out.println("GroupeCritere seeding completed.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream fis = new FileInputStream(filePath6);
                     Workbook workbook = new XSSFWorkbook(fis)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) continue; // skip header

                        Categorie ct = new Categorie();
                        ct.setCatNom(getCellValue(row.getCell(0)));
                        ct.setPlace(Boolean.parseBoolean(getCellValue(row.getCell(1))));
                        ct.setCatPrix(Float.valueOf(getCellValue(row.getCell(2))));
                        ct.setCatPrixTtc(Float.valueOf(getCellValue(row.getCell(3))));
                        ct.setTauxRemise(Float.valueOf(getCellValue(row.getCell(4))));
                        ct.setCatRemise(Float.valueOf(getCellValue(row.getCell(5))));
                        ct.setCatForfait(Float.valueOf(getCellValue(row.getCell(6))));
                        ct.setCatTaxe(Float.valueOf(getCellValue(row.getCell(7))));

                        TypePlace tp = typePlaceRepository.findById(Long.valueOf(getCellValue(row.getCell(8)))).orElse(null);
                        ct.setTypePlace(tp);

                        ct.setCatCommission(Float.valueOf(getCellValue(row.getCell(9))));

                        String criteresStr = getCellValue(row.getCell(10)); // ex: "1,2,5"

                        if (criteresStr != null && !criteresStr.isEmpty())
                        {
                            String[] ids = criteresStr.split(";");
                            logger.info(Arrays.toString(ids));
                            List<Critere> critereDTOList = new ArrayList<>();

                            for (String idStr : ids) {
                                Critere critereDTO = new Critere();
                                critereDTO.setCrtId(Long.parseLong(idStr.trim()));
                                critereDTOList.add(critereDTO);
                            }

                            ct.setCriteres(critereDTOList);
                        }
                        categorieRepository.save(ct);
                    }
                    workbook.close();
                    System.out.println("Places seeding completed.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath7))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet passagerSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorPassagers = passagerSheet.iterator();
                    while (iteratorPassagers.hasNext()) {
                        Row row = iteratorPassagers.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        Passager pas = new Passager();
                        pas.setCivilite(getCellValue(row.getCell(1)));
                        Nationalite nat = nationaliteRepository.findById(Long.valueOf(getCellValue(row.getCell(9)))).orElse(null);
                        pas.setNationalite(nat);
                        pas.setLastName(getCellValue(row.getCell(2)));
                        pas.setFirstName(getCellValue(row.getCell(3)));
                        TypePiece tp_ = typePieceRepository.findById(Long.valueOf(getCellValue(row.getCell(4)))).orElse(null);
                        pas.setTypePiece(tp_);
                        pas.setNumeropiece(getCellValue(row.getCell(5)));
                        pas.setPhone(getCellValue(row.getCell(6)));
                        pas.setMail(getCellValue(row.getCell(7)));
                        pas.setAdresse(getCellValue(row.getCell(8)));
                        passagerRepository.save(pas);
                    }
                    System.out.println("Passagers seeding completed.");
                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath8))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet cltEnCompteSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorCltComptes = cltEnCompteSheet.iterator();
                    while (iteratorCltComptes.hasNext()) {
                        Row row = iteratorCltComptes.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        ClientEnCompte cltCompte = new ClientEnCompte();
                        cltCompte.setPlafond(Float.valueOf(getCellValue(row.getCell(1))));
                        cltCompte.setSoldeCompte(Float.valueOf(getCellValue(row.getCell(2))));
                        cltCompte.setRaisonSocial(getCellValue(row.getCell(4)));
                        cltCompte.setFirstnameContact(getCellValue(row.getCell(5)));
                        cltCompte.setLastnameContact(getCellValue(row.getCell(6)));
                        cltCompte.setContact(getCellValue(row.getCell(7)));
                        cltCompte.setMail(getCellValue(row.getCell(8)));
                        cltCompte.setCptgen_compta(getCellValue(row.getCell(9)));
                        cltCompte.setCpttiers_compta(getCellValue(row.getCell(10)));

                        CltModeReglement cltMdr = cltModeReglementRepository.findById(Long.valueOf(getCellValue(row.getCell(11)))).orElse(null);
                        cltCompte.setCltModeReglement(cltMdr);

                        clientEnCompteRepository.save(cltCompte);
                    }
                    System.out.println("Client en compte seeding completed.");
                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try (FileInputStream file = new FileInputStream(filePath9))
                {
                    Workbook workbook = new XSSFWorkbook(file);
                    // Lire les agences
                    Sheet profilsSheet = workbook.getSheetAt(0);
                    Iterator<Row> iteratorProfils = profilsSheet.iterator();
                    while (iteratorProfils.hasNext()) {
                        Row row = iteratorProfils.next();
                        if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-têtes)

                        Profil prf_ = new Profil();
                        prf_.setPrflLibelle(getCellValue(row.getCell(0)));
                        prf_.setActif(Boolean.parseBoolean(getCellValue(row.getCell(1))));
                        prf_.setAddBillet(Boolean.parseBoolean(getCellValue(row.getCell(2))));
                        prf_.setAddCheckBillet(Boolean.parseBoolean(getCellValue(row.getCell(3))));
                        prf_.setAddCltCompte(Boolean.parseBoolean(getCellValue(row.getCell(4))));
                        prf_.setAddEmbarqment(Boolean.parseBoolean(getCellValue(row.getCell(5))));
                        prf_.setAddFacture(Boolean.parseBoolean(getCellValue(row.getCell(6))));
                        prf_.setAddFret(Boolean.parseBoolean(getCellValue(row.getCell(7))));
                        prf_.setAddNavData(Boolean.parseBoolean(getCellValue(row.getCell(8))));
                        prf_.setAddPassager(Boolean.parseBoolean(getCellValue(row.getCell(9))));
                        prf_.setAddReglement(Boolean.parseBoolean(getCellValue(row.getCell(10))));
                        prf_.setAddVoyage(Boolean.parseBoolean(getCellValue(row.getCell(11))));
                        prf_.setBloqPlaces(Boolean.parseBoolean(getCellValue(row.getCell(12))));
                        prf_.setCampagne(Boolean.parseBoolean(getCellValue(row.getCell(13))));
                        prf_.setCancelBillet(Boolean.parseBoolean(getCellValue(row.getCell(14))));
                        prf_.setCancelFret(Boolean.parseBoolean(getCellValue(row.getCell(15))));
                        prf_.setCancelVoyage(Boolean.parseBoolean(getCellValue(row.getCell(16))));
                        prf_.setCheckFret(Boolean.parseBoolean(getCellValue(row.getCell(17))));
                        prf_.setDelCltCompte(Boolean.parseBoolean(getCellValue(row.getCell(18))));
                        prf_.setDelFacture(Boolean.parseBoolean(getCellValue(row.getCell(19))));
                        prf_.setDelFretDetails(Boolean.parseBoolean(getCellValue(row.getCell(20))));
                        prf_.setDelPassager(Boolean.parseBoolean(getCellValue(row.getCell(21))));
                        prf_.setDelReglement(Boolean.parseBoolean(getCellValue(row.getCell(22))));
                        prf_.setDoRemboursement(Boolean.parseBoolean(getCellValue(row.getCell(23))));
                        prf_.setDoRepSurclassment(Boolean.parseBoolean(getCellValue(row.getCell(24))));
                        prf_.setEditBillet(Boolean.parseBoolean(getCellValue(row.getCell(25))));
                        prf_.setEditCltCompte(Boolean.parseBoolean(getCellValue(row.getCell(26))));
                        prf_.setEditFacture(Boolean.parseBoolean(getCellValue(row.getCell(27))));
                        prf_.setEditFret(Boolean.parseBoolean(getCellValue(row.getCell(28))));
                        prf_.setEditNavData(Boolean.parseBoolean(getCellValue(row.getCell(29))));
                        prf_.setEditParam(Boolean.parseBoolean(getCellValue(row.getCell(30))));
                        prf_.setEditPassager(Boolean.parseBoolean(getCellValue(row.getCell(31))));
                        prf_.setEditReglement(Boolean.parseBoolean(getCellValue(row.getCell(32))));
                        prf_.setEditVoyage(Boolean.parseBoolean(getCellValue(row.getCell(33))));
                        prf_.setEdition(Boolean.parseBoolean(getCellValue(row.getCell(34))));
                        prf_.setPayeFret(Boolean.parseBoolean(getCellValue(row.getCell(35))));
                        prf_.setPlanVoyage(Boolean.parseBoolean(getCellValue(row.getCell(36))));
                        prf_.setPointerVoyage(Boolean.parseBoolean(getCellValue(row.getCell(37))));
                        prf_.setRechercher(Boolean.parseBoolean(getCellValue(row.getCell(38))));
                        prf_.setReclamation(Boolean.parseBoolean(getCellValue(row.getCell(39))));
                        prf_.setValide(Boolean.parseBoolean(getCellValue(row.getCell(40))));
                        prf_.setViewEtat(Boolean.parseBoolean(getCellValue(row.getCell(41))));
                        prf_.setViewStat(Boolean.parseBoolean(getCellValue(row.getCell(42))));
                        prf_.setViewVoyage(Boolean.parseBoolean(getCellValue(row.getCell(43))));

                        profilRepository.save(prf_);
                    }
                    System.out.println("Profils seeding completed.");
                    workbook.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


            }
        };
    }
}
