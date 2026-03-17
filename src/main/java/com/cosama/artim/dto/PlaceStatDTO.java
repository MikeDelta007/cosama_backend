package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlaceStatDTO {
    public String bateau;

    public int chaiseTotal;
    public int chaiseOccupee;
    public int chaiseRestante;

    public int cabine2Total;
    public int cabine2Occupee;
    public int cabine2Restante;

    public int cabine4Total;
    public int cabine4Occupee;
    public int cabine4Restante;

    public int cabine8Total;
    public int cabine8Occupee;
    public int cabine8Restante;

    // Constructeur, getters et setters
}
