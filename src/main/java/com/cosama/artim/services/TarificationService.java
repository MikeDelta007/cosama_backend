package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.*;
import com.cosama.artim.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class TarificationService {

    @Autowired
    private final CritereRepository critereRepository;
    @Autowired
    private final CategorieRepository categorieRepository;
    @Autowired
    private final TypePlaceRepository typePlaceRepository;
    @Autowired
    private final TypeBagageRepository typeBagageRepository;
    @Autowired
    private final GroupeCritereRepository groupeCritereRepository;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FretService.class);

    public Critere createCritere(CritereDTO critereDTO)
    {
        Critere critere = Critere.builder()
                .crt_nom(critereDTO.getCrt_nom())
                .build();

        return critereRepository.save(critere);
    }

    public Critere updateCritere(long id, CritereDTO critereDTO){
        Critere critere = this.critereRepository.findCritereBycrtId(id);
        if (critere != null) {
            critere.setCrt_nom(critereDTO.getCrt_nom());
            return critereRepository.save(critere);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    public void deleteCritere(long id){
        if(critereRepository.existsById(id)){
            critereRepository.deleteById(id);
        }else {
            throw new NotFoundException("Critere with ID "+id+" is not found");
        }
    }

    public List<Critere> getAllCritere()
    {
        return critereRepository.findAll();
    }

    public Critere getCritereyId(Long id)
    {
        Critere critere = this.critereRepository.findById(id).orElse(null);

        if(critere!= null)
        {
            return critere;
        }
        else
        {
            return null;
        }
    }

    public Categorie createCategoriePlace(CategorieDTO categorieDTO)
    {
        TypePlace typePlace = this.typePlaceRepository.findBateauBytplcId(categorieDTO.getTplc_id());
        Categorie categorie = Categorie.builder()
                .catNom(categorieDTO.getCat_nom())
                .catPrix(categorieDTO.getCat_prix())
                .catTaxe(categorieDTO.getCat_taxe())
                .catPrixTtc(categorieDTO.getCat_prix_ttc())
                .tauxRemise(categorieDTO.getTaux_remise())
                .catRemise(categorieDTO.getCat_remise())
                .place(categorieDTO.isPlace())
                .bagage(categorieDTO.isBagage())
                .originPax(categorieDTO.getOriginPax())
                .age(categorieDTO.getAge())
                .typePlace(typePlace)
                .build();

        // Retrieve and set the Criteres associated with the Categorie using a for loop
        if (categorieDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : categorieDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            categorie.setCriteres(criteres);
        }

        return categorieRepository.save(categorie);
    }


    public Categorie updateCategoriePlace(long catId, CategorieDTO categorieDTO) {
        // Retrieve the existing Categorie from the repository
        Categorie existingCategorie = categorieRepository.findCategorieBycatId(catId);
        TypePlace typ = typePlaceRepository.findById(categorieDTO.getTplc_id()).orElse(null);
        logger.info(String.valueOf(categorieDTO));

        // Update the fields of the existing Categorie
        existingCategorie.setCatNom(categorieDTO.getCat_nom());
        existingCategorie.setCatPrix(categorieDTO.getCat_prix());
        existingCategorie.setCatTaxe(categorieDTO.getCat_taxe());
        existingCategorie.setCatRemise(categorieDTO.getCat_remise());
        existingCategorie.setTauxRemise(categorieDTO.getTaux_remise());
        existingCategorie.setCatPrixTtc(categorieDTO.getCat_prix_ttc());
        existingCategorie.setTypePlace(typ);

        // Retrieve and set the Criteres associated with the Categorie using a for loop
        if (categorieDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : categorieDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            existingCategorie.setCriteres(criteres);
        }

        // Save and return the updated Categorie entity
        return categorieRepository.save(existingCategorie);
    }

    public Categorie createCategorieBagage(CategorieDTO categorieDTO)
    {
        TypeBagage typeBagage = this.typeBagageRepository.findById(categorieDTO.getTbg_id()).orElse(null);
        Categorie categorie = Categorie.builder()
                .catNom(categorieDTO.getCat_nom())
                .typeBagage(typeBagage)
                .code(categorieDTO.getCode())
                .catPrix(categorieDTO.getCat_prix())
                .catPrixTtc(categorieDTO.getCat_prix_ttc())
                .tauxRemise(categorieDTO.getTaux_remise())
                .catRemise(categorieDTO.getCat_remise())
                .fraisMag(categorieDTO.getFrais_mag())
                .place(categorieDTO.isPlace())
                .bagage(categorieDTO.isBagage())
                .originPax(categorieDTO.getOriginPax())
                .age(categorieDTO.getAge())
                .build();

        // Retrieve and set the Criteres associated with the Categorie using a for loop
        if (categorieDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : categorieDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            categorie.setCriteres(criteres);
        }

        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorieBagage(long catId, CategorieDTO categorieDTO) {
        // Retrieve the existing Categorie from the repository
        Categorie existingCategorie = categorieRepository.findCategorieBycatId(catId);
        TypeBagage typB = typeBagageRepository.findById(categorieDTO.getTbg_id()).orElse(null);

        // Update the fields of the existing Categorie
        existingCategorie.setCode(categorieDTO.getCode());
        existingCategorie.setCatNom(categorieDTO.getCat_nom());
        existingCategorie.setCatPrix(categorieDTO.getCat_prix());
        existingCategorie.setCatRemise(categorieDTO.getCat_remise());
        existingCategorie.setTauxRemise(categorieDTO.getTaux_remise());
        existingCategorie.setCatPrixTtc(categorieDTO.getCat_prix_ttc());
        existingCategorie.setFraisMag(categorieDTO.getFrais_mag());
        existingCategorie.setTypeBagage(typB);

        // Retrieve and set the Criteres associated with the Categorie using a for loop
        if (categorieDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : categorieDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            existingCategorie.setCriteres(criteres);
        }

        // Save and return the updated Categorie entity
        return categorieRepository.save(existingCategorie);
    }

    public void deleteCategorie(long id){
        if(categorieRepository.existsById(id)){
            categorieRepository.deleteById(id);
        }else {
            throw new NotFoundException("Categorie with ID "+id+" is not found");
        }
    }

    //public List<Categorie> getAllCategorie()
    //{
    //    //return categorieRepository.findAll();
    //    return categorieRepository.findAllWithDetails();
    //}

    public List<CategoriesDTO> getAllCategorie() {
        List<Categorie> categories = categorieRepository.findAllWithDetails();
        return categories.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }
    private CategoriesDTO convertToDTO1(Categorie categorie) {
        CategoriesDTO dto = new CategoriesDTO();
        dto.setCode(categorie.getCode());
        dto.setCat_id(categorie.getCatId());
        dto.setCat_nom(categorie.getCatNom());
        dto.setCat_prix(categorie.getCatPrix());
        dto.setCat_prix_ttc(categorie.getCatPrixTtc());
        dto.setCat_taxe(categorie.getCatTaxe());
        dto.setCat_forfait(categorie.getCatForfait());
        dto.setCat_remise(categorie.getCatRemise());
        dto.setFrais_mag(categorie.getFraisMag());
        dto.setTaux_remise(categorie.getTauxRemise());
        dto.setPlace(categorie.isPlace());
        dto.setBagage(categorie.isBagage());
        if (categorie.getTypePlace() != null)
        {
            dto.setTplc_id(categorie.getTypePlace().getTplcId());
        }
        else
        {
            dto.setTplc_id(null); // Ou une valeur par défaut si nécessaire
        }
        if (categorie.getTypeBagage() != null)
        {
            dto.setTbg_id(categorie.getTypeBagage().getTbgId());
        }
        else
        {
            dto.setTbg_id(null); // Ou une autre valeur par défaut
        }

        TypePlaceDTO typePlaceDTO = new TypePlaceDTO();
        TypeBagageDTO typeBagageDTO = new TypeBagageDTO();

        if(categorie.isPlace() && categorie.getTypePlace()!=null)
        {
            TypePlace typ = typePlaceRepository.findById(categorie.getTypePlace().getTplcId()).orElse(null);
            logger.info("Place:" + typ.getTplcId());
            typePlaceDTO.setTplcNom(typ.getTplcNom());
            dto.setTypePlace(typePlaceDTO);
        }
        else
        {
            typePlaceDTO.setTplcNom("Pas de valeur");
            if(categorie.isBagage() && categorie.getTypeBagage() != null)
            {
                TypeBagage typeB = typeBagageRepository.findById(categorie.getTypeBagage().getTbgId()).orElse(null);
                logger.info(String.valueOf(typeB.getTbgId()));
                typeBagageDTO.setTbg_id(typeB.getTbgId());
                typeBagageDTO.setTbg_nom(typeB.getTbgNom());

                UniteDTO uniteDTO = new UniteDTO();
                uniteDTO.setUnite_id(typeB.getUnite().getUniteId());
                uniteDTO.setUnite_nom(typeB.getUnite().getUniteNom());
                uniteDTO.setUnite_code(typeB.getUnite().getUniteCode());
                uniteDTO.setVol_id(typeB.getUnite().getVolume().getVolId());

                typeBagageDTO.setUnite(uniteDTO);

                dto.setTypeBagage(typeBagageDTO);
            }
            else
            {
                typeBagageDTO.setTbg_nom("Pas de valeur");
            }
        }

        // Initialisation correcte de la liste
        List<CritereDTO> criteres = new ArrayList<>();

        // Boucle pour ajouter les éléments à la liste
        for (Critere crt : categorie.getCriteres()) {
            CritereDTO crtDTO = new CritereDTO();
            crtDTO.setCrtId(crt.getCrtId());
            crtDTO.setCrt_nom(crt.getCrt_nom());
            criteres.add(crtDTO);
        }

        dto.setCriteres(criteres);

        return dto;
    }

    public GroupeCritere createGroupeCritere(GroupeCritereDTO groupeCritereDTO)
    {
        GroupeCritere groupeCritere = GroupeCritere.builder()
                .grpcrtNom(groupeCritereDTO.getGrpcrt_nom())
                .onLine(0)
                //.bagage(groupeCritereDTO.isBagage())
                .build();

        // Retrieve and set the Criteres associated with the Categorie using a for loop
        if (groupeCritereDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : groupeCritereDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            groupeCritere.setCriteres(criteres);
        }

        return groupeCritereRepository.save(groupeCritere);
    }

    public GroupeCritere updateGroupeCritere(long grpcrtId, GroupeCritereDTO groupeCritereDTO) {
        // Retrieve the existing GroupeCritere from the repository
        GroupeCritere existingGroupeCritere = groupeCritereRepository.findGroupeCritereBygrpcrtId(grpcrtId);

        // Update the fields of the existing GroupeCritere
        existingGroupeCritere.setGrpcrtNom(groupeCritereDTO.getGrpcrt_nom());
        existingGroupeCritere.setOnLine(groupeCritereDTO.getOn_line());

        // Retrieve and set the Criteres associated with the GroupeCritere using a for loop
        if (groupeCritereDTO.getCriteres() != null) {
            List<Critere> criteres = new ArrayList<>();
            for (CritereDTO crtId : groupeCritereDTO.getCriteres()) {
                Critere critere = critereRepository.findCritereBycrtId(crtId.getCrtId());
                criteres.add(critere);
            }
            existingGroupeCritere.setCriteres(criteres);
        }

        // Save and return the updated GroupeCritere entity
        return groupeCritereRepository.save(existingGroupeCritere);
    }

    public void deleteGroupeCritere(long id){
        if(groupeCritereRepository.existsById(id)){
            groupeCritereRepository.deleteById(id);
        }else {
            throw new NotFoundException("Groupe Critere with ID "+id+" is not found");
        }
    }

    public List<GroupeCritereDTO> getAllGroupeCriteres()
    {
        List<GroupeCritere> typePlaces = groupeCritereRepository.findOnlineGC();
        return typePlaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GroupeCritereDTO> getAllGroupeCriteresParam()
    {
        List<GroupeCritere> typePlaces = groupeCritereRepository.findAll();
        return typePlaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GroupeCritereDTO convertToDTO(GroupeCritere groupeCritere) {
        GroupeCritereDTO dto = new GroupeCritereDTO();
        dto.setGrpcrt_id(groupeCritere.getGrpcrtId());
        dto.setGrpcrt_nom(groupeCritere.getGrpcrtNom());
        dto.setOn_line(groupeCritere.getOnLine());

        // Initialisation correcte de la liste
        List<CritereDTO> criteres = new ArrayList<>();

        // Boucle pour ajouter les éléments à la liste
        for (Critere crt : groupeCritere.getCriteres()) {
            CritereDTO crtDTO = new CritereDTO();
            crtDTO.setCrtId(crt.getCrtId());
            crtDTO.setCrt_nom(crt.getCrt_nom());
            criteres.add(crtDTO);
        }
        dto.setCriteres(criteres);
        return dto;
    }

    public List<GroupeCritereDTO> getAllGroupeCriteres2()
    {
        List<GroupeCritere> typePlaces = groupeCritereRepository.findAll();
        return typePlaces.stream()
                .map(this::convertToDTO1)
                .collect(Collectors.toList());
    }

    private GroupeCritereDTO convertToDTO1(GroupeCritere groupeCritere) {
        GroupeCritereDTO dto = new GroupeCritereDTO();
        dto.setGrpcrt_id(groupeCritere.getGrpcrtId());
        dto.setGrpcrt_nom(groupeCritere.getGrpcrtNom());
        dto.setOn_line(groupeCritere.getOnLine());

        // Initialisation correcte de la liste
        List<CritereDTO> criteres = new ArrayList<>();

        // Boucle pour ajouter les éléments à la liste
        for (Critere crt : groupeCritere.getCriteres()) {
            CritereDTO crtDTO = new CritereDTO();
            crtDTO.setCrtId(crt.getCrtId());
            crtDTO.setCrt_nom(crt.getCrt_nom());
            criteres.add(crtDTO);
        }
        dto.setCriteres(criteres);
        return dto;
    }

    public GroupeCritere updateEtatGCPlace(long id, GroupeCritereDTO groupeCritereDTO){
        GroupeCritere groupeCritere = this.groupeCritereRepository.findGroupeCritereBygrpcrtId(id);
        if (groupeCritere != null) {
            groupeCritere.setOnLine(groupeCritereDTO.getOn_line());
            return groupeCritereRepository.save(groupeCritere);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }

    /**
    public GroupeCritere updateEtatGCBagage(long id, GroupeCritereDTO groupeCritereDTO){
        GroupeCritere groupeCritere = this.groupeCritereRepository.findGroupeCritereBygrpcrtId(id);
        if (groupeCritere != null) {
            groupeCritere.setBagage(groupeCritereDTO.isBagage());
            return groupeCritereRepository.save(groupeCritere);
        }
        else
        {
            //log.error("Agence not found for IUF: {}", applicant.getIUF());
            return null;
        }
    }**/

    //Tarification normale
    public List<Number> tarifPlaceTab(Long tplId, List<Critere> criteres)
    {
        //logger.info(criteres.toString());
        float pttc = 0.0F;
        logger.info("Etape1");
        List<Number> prixDefaut = new ArrayList<>();
        // Obtenir le tarif par défaut
        Map<String, Object> defaultTypePlace = jdbcTemplate.queryForMap("SELECT * FROM type_place WHERE tplc_id = ?", tplId);
        float tplPrix = (float) defaultTypePlace.get("tplc_prix");
        float tplRemise = 0.0F;
        float prixAvantRemise = tplPrix - (tplPrix * tplRemise / 100);
        float remise = tplPrix * tplRemise / 100;
        pttc = prixAvantRemise + taxe(prixAvantRemise);
        prixDefaut.add(tplPrix); // Prix0
        prixDefaut.add(remise); // Remise1
        prixDefaut.add(taxe(prixAvantRemise)); // Prix avec taxe2
        prixDefaut.add(tplRemise); // Remise3
        prixDefaut.add(pttc); // Prix total avec taxe4
        prixDefaut.add((Long) defaultTypePlace.get("cat_id")); // Catégorie ID


        if (criteres == null || criteres.isEmpty()) {
            logger.info("Etape2");
            return prixDefaut; // Retourner le tarif par défaut si aucun critère n'est spécifié
        }

        // Obtenir le tarif en fonction des critères
        List<Map<String, Object>> categories = jdbcTemplate.queryForList("SELECT * FROM categorie WHERE place = true AND tplc_id = ?", tplId);
        for (Map<String, Object> category : categories)
        {
            boolean criteresMatched = true;
            Long catId = (Long) category.get("cat_id");
            logger.info("Etape3");
            if (!criteres.isEmpty()) {
                for (Critere critere : criteres) {
                    if (existeCrt(catId, critere.getCrtId()) == 0) {
                        criteresMatched = false;
                        break;
                    }
                }
            }

            int nbCriteres = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categorie_critere WHERE cat_id = ?", Integer.class, catId);
            if (nbCriteres != 0 && nbCriteres != criteres.size()) {
                logger.info("Etape4");
                criteresMatched = false;
            }

            if (criteresMatched) {
                logger.info("Etape5");
                float catPrix = (float) category.get("cat_prix");
                logger.info(String.valueOf(catPrix));
                float catRemise = (float) category.get("cat_remise");
                logger.info(String.valueOf(catRemise));
                float catTaxe = (float) category.get("cat_taxe");
                logger.info(String.valueOf(catTaxe));
                pttc = (float) category.get("cat_prix_ttc");
                logger.info(String.valueOf(pttc));
                List<Number> prix = new ArrayList<>();
                prix.add(catPrix); // Prix0
                prix.add(catRemise); // Remise1
                prix.add(catTaxe); // Taxe2
                prix.add(pttc); // Remise3
                prix.add(pttc); // Prix total avec taxe4
                prix.add((long) category.get("cat_id")); // Catégorie ID
                logger.info(prix.toString());
                return prix;
            }
            logger.info("Ici...");
        }
        logger.info("A ce niveau !!!");
        return prixDefaut; // Retourner le tarif par défaut si aucune catégorie ne correspond aux critères
    }

    //Tarification pour les bagages
    public List<Number> tarifBagoTab(Long catId, List<Critere> criteres) {
        // Obtenir le tarif en fonction des critères
        List<Map<String, Object>> categories = jdbcTemplate.queryForList("SELECT * FROM categorie WHERE bagage = true AND cat_id = ?", catId);
        List<Number> prix = null;
        for (Map<String, Object> category : categories) {
            logger.info("Etape1");
            boolean criteresMatched = true;
            //Long catId = (Long) category.get("cat_id");

            if (criteres != null && !criteres.isEmpty()) {
                for (Critere critere : criteres) {
                    if (existeCrt(catId, critere.getCrtId()) == 0)
                    {
                        criteresMatched = false;
                        break;
                    }
                }
            }

            int nbCriteres = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categorie_critere WHERE cat_id = ?", Integer.class, catId);
            if (nbCriteres != 0 && nbCriteres != criteres.size())
            {
                logger.info("Etape2");
                criteresMatched = false;
            }

            if (criteresMatched) {
                logger.info("Etape3");
                float catPrix = (float) category.get("cat_prix");
                float catRemise = (float) category.get("cat_remise");
                float catTaxe = (float) category.get("cat_taxe");
                float catForfait = (float) category.get("cat_forfait");
                float prixCatAvecRemise = catPrix - catRemise - catForfait;
                prix = new ArrayList<>();
                prix.add(catPrix); // Prix0
                prix.add(catRemise); // Remise1
                prix.add(catTaxe); // Taxe2
                prix.add(prixCatAvecRemise); // Remise3
                prix.add(prixCatAvecRemise + catTaxe); // Prix total avec taxe4
                prix.add((long) category.get("cat_id")); // Catégorie ID
                logger.info("Tarif delivered");

            }
        }
        return prix;
    }

    //Tarification pour le report ou surclassement
    public List<Number> tarifPlaceTabReport(Long tplId, List<Critere> criteres) {
        logger.info("Etape1");
        List<Number> prixDefaut = new ArrayList<>();
        // Obtenir le tarif par défaut
        Map<String, Object> defaultTypePlace = jdbcTemplate.queryForMap("SELECT * FROM type_place WHERE tplc_id = ?", tplId);
        float tplPrix = (float) defaultTypePlace.get("tplc_prix");
        float tplRemise = 0.0F;
        float prixAvantRemise = tplPrix - (tplPrix * tplRemise / 100);
        float remise = tplPrix * tplRemise / 100;
        prixDefaut.add(tplPrix); // Prix0
        prixDefaut.add(remise); // Remise1
        prixDefaut.add(taxe(prixAvantRemise)); // Prix avec taxe2
        prixDefaut.add(tplRemise); // Remise3
        prixDefaut.add(prixAvantRemise + taxe(prixAvantRemise)); // Prix total avec taxe4
        //prixDefaut.add((Long) defaultTypePlace.get("cat_id")); // Catégorie ID


        if (criteres == null || criteres.isEmpty()) {
            logger.info("Etape2");
            return prixDefaut; // Retourner le tarif par défaut si aucun critère n'est spécifié
        }

        // Obtenir le tarif en fonction des critères
        List<Map<String, Object>> categories = jdbcTemplate.queryForList("SELECT * FROM categorie WHERE place = true AND tplc_id = ?", tplId);
        for (Map<String, Object> category : categories) {
            logger.info("Etape3");
            boolean criteresMatched = true;
            Long catId = (Long) category.get("cat_id");

            if (!criteres.isEmpty()) {
                for (Critere critere : criteres) {
                    if (existeCrt(catId, critere.getCrtId()) == 0) {
                        criteresMatched = false;
                        break;
                    }
                }
            }

            int nbCriteres = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categorie_critere WHERE cat_id = ?", Integer.class, catId);
            if (nbCriteres != 0 && nbCriteres != criteres.size()) {
                logger.info("Etape4");
                criteresMatched = false;
            }

            if (criteresMatched) {
                logger.info("Etape5");
                float catPrix = (float) category.get("cat_prix");
                float catRemise = (float) category.get("cat_remise");
                float catTaxe = (float) category.get("cat_taxe");
                float prixCatAvecRemise = catPrix - (catPrix * catRemise / 100);
                List<Number> prix = new ArrayList<>();
                prix.add(catPrix); // Prix0
                prix.add(catRemise); // Remise1
                prix.add(catTaxe); // Taxe2
                prix.add(prixCatAvecRemise); // Remise3
                prix.add(prixCatAvecRemise + catTaxe); // Prix total avec taxe4
                prix.add((long) category.get("cat_id")); // Catégorie ID
                return prix;

            }
        }
        return prixDefaut; // Retourner le tarif par défaut si aucune catégorie ne correspond aux critères
    }

    private float taxe(double prix) {
        // Implémenter votre logique de calcul de taxe ici
        float taxe = (float) (0 * prix);
        return taxe; // Pour l'exemple, retourne 0.0
    }

    private int existeCrt(Long catId, Long critereId) {
        String sql = "SELECT COUNT(*) FROM categorie_critere WHERE cat_id = ? AND crt_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, catId, critereId);
        return count > 0 ? 1 : 0;
    }







}
