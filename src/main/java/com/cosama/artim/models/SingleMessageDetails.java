package com.cosama.artim.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleMessageDetails
{
    private String signature;
    private String content;
    private String subject;
    private List<SmsRecipient> recipients;
}
