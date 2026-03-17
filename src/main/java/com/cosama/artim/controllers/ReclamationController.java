package com.cosama.artim.controllers;

import com.cosama.artim.dto.FretDTOs;
import com.cosama.artim.dto.ReclamationDTO;
import com.cosama.artim.models.Reclamation;
import com.cosama.artim.models.Voyage;
import com.cosama.artim.services.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/rec")
@RequiredArgsConstructor
public class ReclamationController
{
    @Autowired
    private final ReclamationService reclamationService;

    @GetMapping("/getReclamations")
    @ResponseStatus(HttpStatus.OK)
    public List<Reclamation> getRec()
    {
        return reclamationService.getAllRec();
    }

    @GetMapping("/getReclamations_")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<ReclamationDTO>> getReclamations_()
    {
        return reclamationService.getAllRec_();
    }
}

