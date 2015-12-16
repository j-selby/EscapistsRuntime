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
            for (musicItem in channel.value) {
                if (!musicItem.key.isPlaying) {
                    if (musicItem.value == 1) {
                        channel.value.remove(musicItem.key);
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
        if (!channels.containsKey(channelId)) {
            channels.put(channelId, HashMap());
            channelVolume.put(channelId, volume);
            return;
        }

        val channel = channels[channelId]!!;
        for (musicItem in channel) {
            musicItem.key.volume = volume;
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