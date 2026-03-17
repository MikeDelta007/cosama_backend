package com.cosama.artim.dto;

import com.cosama.artim.models.GroupeCritere;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GroupeCritereDTO
{
    private long grpcrt_id;
    @Column(length = 100)
    private String grpcrt_nom;
    private int on_line;

    private List<CritereDTO> criteres;

}
