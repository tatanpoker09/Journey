package com.journey.central.journey.utils;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.OutputStream;

public class SpeechSynthesizer {
    public static ByteString synthesizeText(String text) throws Exception {
        if(text == null || text.isEmpty()) {
            return null;
        }
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("en-US") // languageCode = "en_us"
                            .setSsmlGender(SsmlVoiceGender.FEMALE) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
                            .build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response

            return response.getAudioContent();
        }
    }
}
