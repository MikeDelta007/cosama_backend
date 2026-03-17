package com.cosama.artim.dto;

import com.cosama.artim.models.TypePlace;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlaceDTO {
    private long plc_id;
    private String plc_code;
    private boolean plc_etat;
    private String sexe;
    private String situation;
    private String color;

    private long niv_id;
    private long tplc_id;
    private long bat_id;


}
