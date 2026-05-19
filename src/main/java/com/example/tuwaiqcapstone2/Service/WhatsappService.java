package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.DTO.TranslateRecipeResponse;
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

    public void sendShoppingList(String phoneNumber, List<String> shoppingList) {
        Twilio.init(accountSid, authToken);

        StringBuilder message = new StringBuilder("🛒 Your Shopping List:\n\n");
        shoppingList.forEach((name) ->
                message.append("- ").append(name).append("\n")
        );

        Message.creator(
                new PhoneNumber("whatsapp:" + phoneNumber),
                new PhoneNumber(fromNumber),
                message.toString()
        ).create();
    }

    public void shareRecipe(TranslateRecipeResponse recipe, String phoneNumber){
        Twilio.init(accountSid, authToken);

        StringBuilder message = new StringBuilder("\uD83C\uDF7D\uFE0F " + recipe.getRecipeName() + ":\n\n");
        message.append("\uD83D\uDCDD Description: ").append(recipe.getDescription()).append("\n");
        message.append("\uD83D\uDCAA Difficulty: ").append(recipe.getDifficulty()).append("\n");
        message.append("\uD83D\uDC65 Servings: ").append(recipe.getServings()).append("\n");
        message.append("⏱\uFE0F Cook Time: ").append(recipe.getCookTime()).append("\n\n");

        message.append("\uD83D\uDED2 Ingredients: ").append("\n");
        recipe.getIngredients().forEach((ingredient) ->
                message.append(ingredient.getName()).append(": ").append(ingredient.getAmount()).append(" ").append(ingredient.getUnit()).append("\n")
        );

        message.append("\uD83D\uDC68\u200D\uD83C\uDF73 Steps: ").append("\n");
        recipe.getSteps().forEach((step) ->
                message.append(step.getStepNumber()).append("- ").append(step.getInstruction()).append("\n")
        );

        Message.creator(
                new PhoneNumber("whatsapp:" + phoneNumber),
                new PhoneNumber(fromNumber),
                message.toString()
        ).create();
    }

    public void sendScheduledRecipe(){

    }
}
