package net.jselby.escapists.game.events.functions;

import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.game.events.Action;
import net.jselby.escapists.game.events.Condition;
import net.jselby.escapists.game.events.FunctionCollection;

import java.io.File;

/**
 * Audio functions play & modify samples and music files.
 */
public class Audio extends FunctionCollection {
    @Condition(subId = -2, id = -1)
    public boolean SampleNotPlaying(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Condition(subId = -2, id = -8)
    public boolean ChannelNotPlaying(int id, int value) {
        // TODO: Unknown value
        return true;
    }

    @Action(subId = -2, id = 29)
    public void PlayLoopingChannelFileSample(String location, int channel, int times) {
        scope.getGame().getAudio().playFile(location, channel, times);
    }

    @Action(subId = -2, id = 11)
    public void PlayChannelSample(int unknown1, int unknown2, String name, int channel) {
        scope.getGame().getAudio().playFile(EscapistsRuntime.getRuntime().getGamePath().getAbsolutePath()
                + File.separator + "audio" + File.separator + name + ".wav", channel, 1);
    }

    public void SetChannelVolume(int channel, int volume) {
        System.out.println("Volume: " + volume);
        scope.getGame().getAudio().setVolume(channel, volume);
    }

    @Action(subId = -2, id = 15)
    public void StopChannel(int channel) {
        scope.getGame().getAudio().stopChannel(channel);
    }

}
