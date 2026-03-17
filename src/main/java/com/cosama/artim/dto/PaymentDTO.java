package com.cosama.artim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentDTO {
    private long fretId;
    private float fretAcompte;
    private float fretMontant;
    private float fretTva;
    private float fretRemiseTaux;
    private float fretRemise;
    private float fretMontant_ht;
    private boolean applyTVA;
    private boolean applyPayment;
    private long cltcmpt_id;
    private String fretPayUsr;
    private String paymentMethod;
}
