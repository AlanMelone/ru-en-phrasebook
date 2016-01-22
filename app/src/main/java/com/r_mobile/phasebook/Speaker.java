package com.r_mobile.phasebook;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by AlanMalone on 23.01.2016.
 */
public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech TTS;
    private boolean ready;
    private boolean allowed;

    public Speaker(Context context){
        TTS = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed) {
        this.allowed = allowed;
    }

        @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            TTS.setLanguage(Locale.US);
            TTS.setSpeechRate((float) 2);
            ready = true;
        } else {
            ready = false;
        }
    }

    public void speak(String text){
        if(ready && allowed) {
            //HashMap<String, String> hash = new HashMap<String,String>();
            //hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
            //        String.valueOf(AudioManager.STREAM_NOTIFICATION));
            Bundle bundle = new Bundle();
            bundle.putString("bundleSpeak", TextToSpeech.Engine.KEY_PARAM_STREAM);
            TTS.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, "texttospeech");
            //TTS.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void pause(int duration){
        TTS.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }
}
