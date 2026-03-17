package com.cosama.artim.controllers;

import com.cosama.artim.dto.ManifesteFretDTO_;
import com.cosama.artim.dto.RepTPByLevelDTO;
import com.cosama.artim.dto.RepUserByProfilDTO;
import com.cosama.artim.dto.StatCheckBilletDTO;
import com.cosama.artim.models.Billet;
import com.cosama.artim.services.AchatOnLineService;
import com.cosama.artim.services.ReportService;
import com.cosama.artim.services.StatistiqueService;
import com.cosama.artim.services.VenteBilletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/stats")
@RequiredArgsConstructor
public class StatsController {
    @Autowired
    private final StatistiqueService statService;

    @GetMapping("/admin/countProfilByUser")
    @ResponseStatus(HttpStatus.OK)
    public List<RepUserByProfilDTO> getUserByProfil()
    {
        return statService.countProfilByUser();
    }

    @GetMapping("/admin/countUserByAgence")
    @ResponseStatus(HttpStatus.OK)
    public List<RepUserByProfilDTO> getUserByAgence()
    {
        return statService.countUserByAgence();
    }

    @GetMapping("/admin/countTPByLevel")
    @ResponseStatus(HttpStatus.OK)
    public List<RepTPByLevelDTO> countTPByLevel()
    {
        return statService.repTPByLevel();
    }

    @GetMapping("/admin/countTPByBateau")
    @ResponseStatus(HttpStatus.OK)
    public List<RepTPByLevelDTO> countTPByBateau()
    {
        return statService.repTPByBateau();
    }

    @GetMapping("/admin/countProductByUnite")
    @ResponseStatus(HttpStatus.OK)
    public List<RepUserByProfilDTO> countProductByUnite()
    {
        return statService.countProductByUnite();
    }

    @GetMapping("/billets/countBilletChecked")
    @ResponseStatus(HttpStatus.OK)
    public List<StatCheckBilletDTO> countBilletChecked(@RequestParam Long voyId)
    {
        return statService.countBilletChecked(voyId);
    }


}
