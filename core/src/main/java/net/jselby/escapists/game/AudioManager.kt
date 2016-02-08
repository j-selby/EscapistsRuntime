package net.jselby.escapists.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import java.util.*

/**
 * The audio manager manages channels of audio, volume, track looping, and so on.
 *
 * @author j_selby
 */
class AudioManager {
    // Channel id = (Song + Loops)
    private val channels : HashMap<Int, HashMap<Music, Int>> = HashMap();

    // Channel id = Volume
    private val channelVolume : HashMap<Int, Float> = HashMap();

    fun playFile(file : String, channel : Int, times : Int = 1) {
        val song = Gdx.audio.newMusic(Gdx.files.absolute(file));

        if (!channels.containsKey(channel)) {
            channels.put(channel, HashMap());
            channelVolume.put(channel, 1f);
        }

        song.volume = channelVolume[channel]!!
        song.play()

        val channelSet = channels[channel];
        channelSet!!.put(song, times);
    }

    fun tick() {
        for (channel in channels) {
            for (musicItem in channel.value.entries.toMutableList()) {
                if (!musicItem.key.isPlaying) {
                    if (musicItem.value == 1) {
                        channel.value.remove(musicItem.key);
                        musicItem.key.dispose();
                        continue;
                    } else if (musicItem.value != 0) {
                        channel.value.put(musicItem.key, musicItem.value - 1);
                    }

                    musicItem.key.play();
                }
            }
        }
    }

    fun setVolume(channelId : Int, volume : Float) {
        val checkedVolume : Float;
        if (volume < 0) {
            checkedVolume = 0f;
        } else {
            checkedVolume = volume;
        }

        if (!channels.containsKey(channelId)) {
            channels.put(channelId, HashMap());
            channelVolume.put(channelId, checkedVolume);
            return;
        }

        val channel = channels[channelId]!!;
        for (musicItem in channel) {
            musicItem.key.volume = checkedVolume;
        }
    }

    fun stopChannel(channelId : Int) {
        if (!channels.containsKey(channelId)) {
            return;
        }

        val channel = channels[channelId]!!;
        for (musicItem in channel) {
            musicItem.key.stop();
        }
        channel.clear();
    }
}