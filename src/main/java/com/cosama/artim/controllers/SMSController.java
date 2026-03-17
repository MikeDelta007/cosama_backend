package com.cosama.artim.controllers;

import com.cosama.artim.models.SingleMessageDetails;
import com.cosama.artim.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/sms")
@RequiredArgsConstructor
public class SMSController
{
    private final MessageService smsService;

    @PostMapping(value="/send")
    public ResponseEntity<Integer> sendMessage(@RequestParam("token") String token,
                                               @RequestBody SingleMessageDetails singleMessageDetails) throws Exception {

        return ResponseEntity.ok(this.smsService.sendSms(token,singleMessageDetails));
    }
}
