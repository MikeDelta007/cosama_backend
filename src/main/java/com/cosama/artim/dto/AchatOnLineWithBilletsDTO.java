package com.cosama.artim.dto;

import com.cosama.artim.models.AchatOnLine;
import com.cosama.artim.models.Billet;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AchatOnLineWithBilletsDTO {

    private AchatOnLine achatOnLine;
    private List<BilletsDTO> billets;

    public AchatOnLineWithBilletsDTO(AchatOnLine achatOnLine, List<BilletsDTO> billets) {
        this.achatOnLine = achatOnLine;
        this.billets = billets;
    }

}
