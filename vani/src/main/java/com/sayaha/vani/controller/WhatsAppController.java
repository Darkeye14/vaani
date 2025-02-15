package com.sayaha.vani.controller;

import com.sayaha.vani.model.Complaint;
import com.sayaha.vani.service.ComplaintService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class WhatsAppController {

    @Autowired
    private ComplaintService complaintService;

    @Value("${twilio.accountSid}")
    private String accountSid ;

    @Value("${twilio.authToken}")
    private String authToken ;

    @PostMapping(value = "/whatsapp-webhook", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String handleWhatsAppMessage(@RequestBody MultiValueMap<String, String> map) throws IOException {
        String incomingMessage = map.getFirst("Body");
        String userPhoneNumber = map.getFirst("From");
        String mediaUrl = map.getFirst("MediaUrl0");

        Complaint complaint = new Complaint();
        complaint.setWhatsappNumber(userPhoneNumber);
        complaint.setStatus("Submitted");

        if (incomingMessage != null) {
            // Basic parsing for MVP (improve later):
            String[] parts = incomingMessage.split(":"); // Example: "Category:Pothole,Description:Big pothole..."
            for (String part : parts) {
                String[] keyValue = part.split(",");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    if (key.equals("Category")) {
                        complaint.setCategory(value);
                    } else if (key.equals("Description")) {
                        complaint.setDescription(value);
                    } else if (key.equals("Location")){
                        complaint.setLocation(value);
                    }
                }
            }
        }
        if (mediaUrl != null) {
            complaint.setMediaUrl(mediaUrl);
        }

        complaint = complaintService.createComplaint(complaint); // Use the service

        sendWhatsAppMessage(userPhoneNumber, "Your complaint has been received! Tracking ID: " + complaint.getId());
        return "OK";
    }

    private void sendWhatsAppMessage(String to, String message) throws IOException {
        Twilio.init(accountSid, authToken);

        Message msg = Message.creator(
                        new PhoneNumber("whatsapp:+"),
                        new PhoneNumber("whatsapp:+"),// Replace with your Twilio WhatsApp Number
                        message)
                .create();

        System.out.println(msg.getSid());
    }
}
