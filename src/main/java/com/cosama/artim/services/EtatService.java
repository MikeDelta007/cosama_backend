package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.repositories.BilletRepository;
import com.cosama.artim.repositories.EnfantRepository;
import com.cosama.artim.repositories.FretRepository;
import com.cosama.artim.repositories.VoyageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtatService {
    @Autowired
    private final BilletRepository billetRepository;
    @Autowired
    private final EnfantRepository enfantRepository;
    @Autowired
    private final FretRepository fretRepository;

    public List<ManifesteAdulteDTO> manifesteADULTE(long voyId, long batId) {
        List<Object[]> results = billetRepository.manifesteAdulte(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<RecapPlacesManifeste> statManifesteAdulte(long voyId, long batId)
    {
        List<Object[]> results = billetRepository.statManifesteAdulte(voyId, batId);

        return results.stream().map(obj -> new RecapPlacesManifeste(
                ((Number) obj[0]).intValue(),
                ((Number) obj[1]).intValue(),
                ((Number) obj[2]).intValue(),
                ((Number) obj[3]).intValue(),
                ((Number) obj[4]).intValue(),
                ((Number) obj[5]).intValue(),
                ((Number) obj[6]).intValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> manifesteENFANT(long voyId, long batId) {
        List<Object[]> results = billetRepository.manifesteEnfant(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<RecapPlacesManifeste> statManifesteEnfant(long voyId, long batId)
    {
        List<Object[]> results = billetRepository.statManifesteEnfant(voyId, batId);

        return results.stream().map(obj -> new RecapPlacesManifeste(
                ((Number) obj[0]).intValue(),
                ((Number) obj[1]).intValue(),
                ((Number) obj[2]).intValue(),
                ((Number) obj[3]).intValue(),
                ((Number) obj[4]).intValue(),
                ((Number) obj[5]).intValue(),
                ((Number) obj[6]).intValue()
        )).collect(Collectors.toList());
    }

    public List<BebeDTO> manifesteBEBE(long voyId, long batId) {
        List<Object[]> results = enfantRepository.manifesteBebe(voyId, batId);

        return results.stream().map(obj -> new BebeDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                ((Number) obj[4]).intValue(),
                (String) obj[5],
                ((Number) obj[6]).longValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> passagersNonEmbarq(long voyId, long batId) {
        List<Object[]> results = billetRepository.passagersNonEmbarque(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> manifesteBilletReportes(long voyId, long batId) {
        List<Object[]> results = billetRepository.billetsReportes(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<RapportPAX> rapportPAX(long voyId, long batId) {
        List<Object[]> results = billetRepository.rapportPAX(voyId, batId);

        return results.stream().map(obj -> new RapportPAX(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                (String) obj[5],
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue(),
                ((Number) obj[10]).intValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> manifesteADULTEC(long voyId, long batId) {
        List<Object[]> results = billetRepository.manifesteAdulteCarab(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> manifesteENFANTC(long voyId, long batId) {
        List<Object[]> results = billetRepository.manifesteEnfantCarab(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<BebeDTO> manifesteBEBEC(long voyId, long batId) {
        List<Object[]> results = enfantRepository.manifesteBebeCarab(voyId, batId);

        return results.stream().map(obj -> new BebeDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                ((Number) obj[4]).intValue(),
                (String) obj[5],
                ((Number) obj[6]).longValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> passagersNonEmbarqC(long voyId, long batId) {
        List<Object[]> results = billetRepository.passagersNonEmbarqueCarab(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<ManifesteAdulteDTO> manifesteBilletReportesC(long voyId, long batId) {
        List<Object[]> results = billetRepository.billetsReportesCarab(voyId, batId);

        return results.stream().map(obj -> new ManifesteAdulteDTO(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                ((Number) obj[5]).longValue(),
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue()
        )).collect(Collectors.toList());
    }

    public List<RapportPAX> rapportPAXC(long voyId, long batId) {
        List<Object[]> results = billetRepository.rapportPAXCarab(voyId, batId);

        return results.stream().map(obj -> new RapportPAX(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (String) obj[4],
                (String) obj[5],
                ((Number) obj[6]).longValue(),
                ((Number) obj[7]).longValue(),
                ((Number) obj[8]).longValue(),
                ((Number) obj[9]).longValue(),
                ((Number) obj[10]).intValue()
        )).collect(Collectors.toList());
    }

    //

    public List<ManifesteFretDTO> fretCltCompte(long voyId) {
        List<Object[]> results = fretRepository.fretCltCompte(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> manifesteFret(long voyId) {
        List<Object[]> results = fretRepository.manifesteFret(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> fretAnnuler(long voyId) {
        List<Object[]> results = fretRepository.fretAnnuler(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> fretAttente(long voyId) {
        List<Object[]> results = fretRepository.fretAttente(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO> fretTVA(long voyId) {
        List<Object[]> results = fretRepository.fretTVA(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO> fretAPayer(long voyId) {
        List<Object[]> results = fretRepository.fretAPayer(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }

    //

    public List<ManifesteFretDTO> fretCltCompteC(long voyId) {
        List<Object[]> results = fretRepository.fretCltCompteC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> manifesteFretC(long voyId) {
        List<Object[]> results = fretRepository.manifesteFretC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> fretAnnulerC(long voyId) {
        List<Object[]> results = fretRepository.fretAnnulerC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO_> fretAttenteC(long voyId) {
        List<Object[]> results = fretRepository.fretAttenteC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO_(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16],
                (String) obj[17],
                ((Number) obj[18]).intValue(),
                ((Number) obj[19]).floatValue(),
                ((Number) obj[20]).floatValue(),
                (String) obj[21]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO> fretTVAC(long voyId) {
        List<Object[]> results = fretRepository.fretTVAC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }

    public List<ManifesteFretDTO> fretAPayerC(long voyId) {
        List<Object[]> results = fretRepository.fretAPayerC(voyId);

        return results.stream().map(obj -> new ManifesteFretDTO(
                (String) obj[0],
                ((Number) obj[1]).floatValue(),
                ((Number) obj[2]).floatValue(),
                ((Number) obj[3]).floatValue(),
                ((Date) obj[4]),
                ((Date) obj[5]),
                (String) obj[6],
                (String) obj[7],
                (String) obj[8],
                (String) obj[9],
                (String) obj[10],
                ((Number) obj[11]).floatValue(),
                (String) obj[12],
                (String) obj[13],
                (String) obj[14],
                (String) obj[15],
                (String) obj[16]
        )).collect(Collectors.toList());
    }
}
