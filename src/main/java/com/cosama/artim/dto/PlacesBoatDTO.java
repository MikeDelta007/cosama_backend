package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlacesBoatDTO {
    private long plc_id;
    private long tplc_id;
    private long bat_id;
    private String situation;
}
