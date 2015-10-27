package net.jselby.escapists.graphics;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.*;
import net.jselby.escapists.data.chunks.Frame;
import net.jselby.escapists.util.ChunkUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glTexParameteri;

/**
 * The graphical frontend for the engine.
 *
 * @author j_selby
 */
public class EscapistsGame extends BasicGame {
    private final List<Chunk> chunks;

    private final AppHeader chunkHeader;
    private final ImageBank chunkImages;
    private final ObjectDefinition[] objectDefs;

    private Frame currentFrame;

    private Map<Integer, Image> images;

    private int oldWidth;
    private int oldHeight;

    public EscapistsGame(List<Chunk> chunks) {
        super(((AppName) ChunkUtils.getChunk(chunks, AppName.class)).getContent());
        this.chunks = chunks;

        // Pop important chunks
        chunkHeader = ((AppHeader) ChunkUtils.getChunk(chunks, AppHeader.class));
        chunkImages = ((ImageBank) ChunkUtils.getChunk(chunks, ImageBank.class));
        objectDefs = ((FrameItems) ChunkUtils.getChunk(chunks, FrameItems.class)).info;

    }

    @Override
    public void init(GameContainer container) throws SlickException {
        oldWidth = container.getWidth();
        oldHeight = container.getHeight();

        // Get the app icon
        AppIcon icon = (AppIcon) ChunkUtils.getChunk(chunks, AppIcon.class);
        ImageIOImageData data = new ImageIOImageData();
        ByteBuffer[] list = new ByteBuffer[] {
            loadIconInstance(icon.image, 16),
            loadIconInstance(icon.image, 24),
            loadIconInstance(icon.image, 32)
        };
        Display.setIcon(list);

        container.setTargetFrameRate((int) chunkHeader.frameRate);

        // Convert images to Slick format
        try {
            ImageBank.ImageItem[] imageItems = chunkImages.images;
            images = new HashMap<>();

            for (int i = 0; i < imageItems.length; i++) {
                ImageBank.ImageItem imageItem = imageItems[i];

                Texture texture = BufferedImageUtil.getTexture("", imageItem.image);
                Image slickImage = new Image(texture.getImageWidth(), texture.getImageHeight());
                slickImage.setTexture(texture);
                slickImage.setFilter(Image.FILTER_NEAREST);
                images.put(imageItem.handle, slickImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChunkUtils.popChunk(chunks, Frame.class);
        ChunkUtils.popChunk(chunks, Frame.class);

        loadFrame(container, (Frame) ChunkUtils.getChunk(chunks, Frame.class));
    }

    @Override
    public void update(GameContainer container, int i) throws SlickException {
        if(oldWidth != Display.getWidth() || oldHeight != Display.getHeight()) {
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            oldWidth = Display.getWidth();
            oldHeight = Display.getHeight();
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setColor(awtToSlickColor(currentFrame.background));
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        g.scale(((float) container.getWidth()) / ((float) chunkHeader.windowWidth),
               ((float) container.getHeight()) / ((float) chunkHeader.windowHeight));


        int mouseX = container.getInput().getMouseX();
        int mouseY = container.getInput().getMouseY();

        Layers.Layer[] layers = currentFrame.layers.layers;
        for (int i = 0; i < layers.length; i++) {
            Layers.Layer layer = layers[i];
            //System.out.printf("Name: %s, %s:%s, %s, %s.\n", layer.name, layer.xCoefficient,
            //        layer.yCoefficient, layer.flags, layer.backgroundIndex);
            //if (layer.backgroundIndex != 0) {
            //    g.drawImage(images[layer. - 1], 0, 0);
            //}


            //System.out.println(layer.backgroundIndex + ":" + layer.numberOfBackgrounds);


            for (ObjectInstances.ObjectInstance instance : currentFrame.objects.instances) {
                if (instance.layer == i) {
                    //handle
                    float x = instance.x;
                    float y = instance.y;
                    int targetId = instance.objectInfo;
                    //if (targetId == 24) {
                    //    targetId = 61;
                    //} else {
                    //    continue;
                    //}
                    //if (instance.objectInfo == 62) {
                        //System.out.printf("%d:%d, %d, %d, %d, %d (layer %d).\n", instance.x, instance.y, instance.objectInfo,
                        //        instance.handle, instance.parentHandle, instance.parentType, i);
                        if (images.containsKey(targetId)) {

                            g.drawImage(images.get(targetId), x, y);
                        }
                    //}
                }

            }
        }

        g.resetTransform();
        g.setColor(Color.white);

        for (ObjectInstances.ObjectInstance instance : currentFrame.objects.instances) {
            float x = instance.x * ((float) container.getWidth()) / ((float) chunkHeader.windowWidth);
            float y = instance.y * ((float) container.getHeight()) / ((float) chunkHeader.windowHeight);
            //System.out.println(instance);
            g.drawString(instance.x + ":" + instance.y + ":" + objectDefs[instance.objectInfo], x, y);
        }

        g.drawString("FPS: " + container.getFPS(), 5, 5);
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension)
    {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio = 1;
        if(image.getWidth() > scaledIcon.getWidth())
        {
            ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        }
        else
        {
            ratio = (int) (scaledIcon.getWidth() / image.getWidth());
        }
        if(image.getHeight() > scaledIcon.getHeight())
        {
            double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
            if(r2 < ratio)
            {
                ratio = r2;
            }
        }
        else
        {
            double r2 =  (int) (scaledIcon.getHeight() / image.getHeight());
            if(r2 < ratio)
            {
                ratio = r2;
            }
        }
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) (width), (int) (height), null);
        g.dispose();

        byte[] imageBuffer = new byte[dimension*dimension*4];
        int counter = 0;
        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter + 0] =(byte)((colorSpace << 8) >> 24 );
                imageBuffer[counter + 1] =(byte)((colorSpace << 16) >> 24 );
                imageBuffer[counter + 2] =(byte)((colorSpace << 24) >> 24 );
                imageBuffer[counter + 3] =(byte)(colorSpace >> 24 );
                counter += 4;
            }
        }
        return ByteBuffer.wrap(imageBuffer);
    }

    private Color awtToSlickColor(java.awt.Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private void loadFrame(GameContainer container, Frame frame) {
        currentFrame = frame;

        System.out.println("Launching frame: " + currentFrame.name);
        for (Layers.Layer layer : frame.layers.layers) {
            System.out.printf("Name: %s, %s:%s, %s, %s.\n", layer.name, layer.xCoefficient,
                    layer.yCoefficient, layer.flags, layer.backgroundIndex);
        }
    }
}
