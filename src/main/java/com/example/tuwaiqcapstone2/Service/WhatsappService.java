package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Enums.AllergenType;
import com.example.tuwaiqcapstone2.Model.Recipe;
import com.example.tuwaiqcapstone2.Model.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String fromNumber;

    public void sendAllergenWarning(User user, Recipe recipe, List<AllergenType> commonAllergens) {
        Twilio.init(accountSid, authToken);

        String message = "⚠️ Allergen Warning!\n" +
                "The recipe \"" + recipe.getName() + "\" contains allergens you are sensitive to:\n" +
                commonAllergens.toString();

        Message.creator(
                new PhoneNumber("whatsapp:" + user.getPhoneNumber()),
                new PhoneNumber(fromNumber),
                message
        ).create();
    }
}
