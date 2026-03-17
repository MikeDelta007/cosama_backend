package com.cosama.artim.services;

import com.cosama.artim.dto.*;
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
public class StatistiqueService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PlaceRepository placeRepository;
    @Autowired
    private final TypeBagageRepository typeBagageRepository;
    @Autowired
    private final VoyagePlaceRepository voyagePlaceRepository;

    public List<RepUserByProfilDTO> countProfilByUser() {
        List<Object[]> results = userRepository.countProfilByUser();

        return results.stream().map(obj -> new RepUserByProfilDTO(
                (String) obj[0],
                ((Number) obj[1]).intValue()
        )).collect(Collectors.toList());
    }

    public List<RepUserByProfilDTO> countUserByAgence() {
        List<Object[]> results = userRepository.countUserByAgence();

        return results.stream().map(obj -> new RepUserByProfilDTO(
                (String) obj[0],
                ((Number) obj[1]).intValue()
        )).collect(Collectors.toList());
    }

    public List<RepTPByLevelDTO> repTPByLevel() {
        List<Object[]> results = placeRepository.countTypesPlaceByNiveau();

        return results.stream().map(obj -> new RepTPByLevelDTO(
                (String) obj[0],
                ((Number) obj[1]).intValue(),
                ((Number) obj[2]).intValue(),
                ((Number) obj[3]).intValue(),
                ((Number) obj[4]).intValue()
        )).collect(Collectors.toList());
    }

    public List<RepTPByLevelDTO> repTPByBateau() {
        List<Object[]> results = placeRepository.countTypesPlaceByBateau();

        return results.stream().map(obj -> new RepTPByLevelDTO(
                (String) obj[0],
                ((Number) obj[1]).intValue(),
                ((Number) obj[2]).intValue(),
                ((Number) obj[3]).intValue(),
                ((Number) obj[4]).intValue()
        )).collect(Collectors.toList());
    }

    public List<RepUserByProfilDTO> countProductByUnite() {
        List<Object[]> results = typeBagageRepository.countProductByUnite();

        return results.stream().map(obj -> new RepUserByProfilDTO(
                (String) obj[0],
                ((Number) obj[1]).intValue()
        )).collect(Collectors.toList());
    }

    public List<StatCheckBilletDTO> countBilletChecked(Long voyId) {
        List<Object[]> results = voyagePlaceRepository.calculateStateBilletOfVoyage(voyId);

        return results.stream().map(obj -> new StatCheckBilletDTO(
                ((Number) obj[0]).intValue(),
                ((Number) obj[1]).intValue(),
                ((Number) obj[2]).intValue()
        )).collect(Collectors.toList());
    }

    public List<PlaceStatDTO> getStats(Long batId, Long voyageId) {
        List<Object[]> results = placeRepository.getPlaceStatsByBateauAndVoyage(batId, voyageId);

        List<PlaceStatDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            PlaceStatDTO dto = new PlaceStatDTO();
            dto.setBateau((String) row[0]);

            dto.setChaiseTotal(((Number) row[1]).intValue());
            dto.setChaiseOccupee(((Number) row[2]).intValue());
            dto.setChaiseRestante(((Number) row[3]).intValue());

            dto.setCabine2Total(((Number) row[4]).intValue());
            dto.setCabine2Occupee(((Number) row[5]).intValue());
            dto.setCabine2Restante(((Number) row[6]).intValue());

            dto.setCabine4Total(((Number) row[7]).intValue());
            dto.setCabine4Occupee(((Number) row[8]).intValue());
            dto.setCabine4Restante(((Number) row[9]).intValue());

            dto.setCabine8Total(((Number) row[10]).intValue());
            dto.setCabine8Occupee(((Number) row[11]).intValue());
            dto.setCabine8Restante(((Number) row[12]).intValue());

            dtos.add(dto);
        }

        return dtos;
    }

}
