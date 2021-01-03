package au.edu.jcu.cp3406.smartereveryday.audio;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import au.edu.jcu.cp3406.smartereveryday.R;

public class AudioManager {
    private Map<Sound, Integer> soundIds;
    private SoundPool soundPool;
    private int loadId;
    private boolean loadedOkay;
    private int soundsLoaded = 0;
    private int totalSounds;

    public AudioManager(Context context) {
        soundIds = new HashMap<>();
        soundPool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedOkay = status == 0;
                if (loadedOkay) {
                    Sound sound = Sound.values()[loadId++];
                    Log.i("AudioManager", "sound: " + sound);
                    soundIds.put(sound, sampleId);
                    soundsLoaded++;
                }
            }
        });
        Field[] fields = R.raw.class.getFields();
        totalSounds = fields.length;
        for (Field field : fields) {
            int id = context.getResources().getIdentifier(field.getName(), "raw", context.getPackageName());
            soundPool.load(context, id, 0);
            Log.i("AudioManager", "sound: " + field.getName() + " id: " + id);
        }

    }

    public void playSound(Sound sound) {
        if (!loadedOkay) return;
        Integer id = soundIds.get(sound);
        assert id != null;
        soundPool.play(id, 1, 1, 1, 0, 1);
    }

    public boolean loadingDone() {
        return soundsLoaded >= totalSounds;
    }
}

