package com.cosama.artim.dto;

import com.cosama.artim.models.Fret;
import com.cosama.artim.models.FretClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FretClientResponseDTO
{
    private FretClient fretClient;
    private Fret fret;
}