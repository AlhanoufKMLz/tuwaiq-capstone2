package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Enums.LanguageCode;
import org.springframework.beans.factory.annotation.Value;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    @Value("${google.translate.api-key}")
    private String apiKey;

    public String translate(String text, LanguageCode targetLanguage) {
        Translate translate = TranslateOptions.newBuilder()
                .setApiKey(apiKey)
                .build()
                .getService();

        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage.toString()));

        return translation.getTranslatedText();
    }
}
