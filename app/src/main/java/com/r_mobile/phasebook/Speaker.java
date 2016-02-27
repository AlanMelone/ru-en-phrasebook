package com.r_mobile.phasebook;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by AlanMalone on 23.01.2016.
 * Класс-хелпер для воспроизведения фразы
 */

public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech TTS;
    private boolean ready; //Флаг готовности
    private boolean allowed;
    private float speechRate = (float) 1.0; //Скорость воспроизведения

    public Speaker(Context context){
        TTS = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed) {
        this.allowed = allowed;
    }

    //Инициализация и настройка воспроизведения
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            TTS.setLanguage(Locale.US); //Задаем язык
            TTS.setSpeechRate(speechRate); //Задаем скорость воспроизведения
            ready = true;
        } else {
            ready = false;
        }
    }

    //Воспроизведение фразы
    public void speak(String text){
        if(ready && allowed) {
            Bundle bundle = new Bundle();
            bundle.putString("bundleSpeak", TextToSpeech.Engine.KEY_PARAM_STREAM);
            TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void pause(int duration){
        TTS.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void setSpeechRate(float speechRate) {
        this.speechRate = speechRate;
    }
}
