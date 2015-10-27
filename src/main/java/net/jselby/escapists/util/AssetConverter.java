package net.jselby.escapists.util;

import net.jselby.escapists.EscapistsRuntime;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Converts assets from java.awt or other formats to a LWJGL/Slick compatible format.
 *
 * @author j_selby
 */
public class AssetConverter {
    private static EscapistsRuntime runtime;

    /**
     * Converts a java.awt.image.BufferedImage to a org.newdawn.slick.Image, ready for OpenGL.
     *
     * This thread will wait for the OpenGL thread to process the asset, if required.
     *
     * @param image The image to convert
     * @return A slick image
     * @throws SlickException
     * @throws IOException
     */
    public static Image awtToSlickImage(BufferedImage image) throws SlickException, IOException {
        Texture texture = BufferedImageUtil.getTexture("", image);
        Image slickImage = new Image(texture.getImageWidth(), texture.getImageHeight());
        slickImage.setTexture(texture);
        slickImage.setFilter(Image.FILTER_NEAREST);
        return slickImage;
    }

    /**
     * Callback from
     * @param runtime The runtime main.
     */
    public static void setRuntime(EscapistsRuntime runtime) {
        if (AssetConverter.runtime != null) {
            throw new IllegalStateException("Cannot change Runtime during execution!");
        }
        AssetConverter.runtime = runtime;
    }
}
