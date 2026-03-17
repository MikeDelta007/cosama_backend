package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RepTPByLevelDTO
{
    private String niveau;
    private Integer chaise;
    private Integer C2;
    private Integer C4;
    private Integer C8;
}
