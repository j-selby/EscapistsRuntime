package net.jselby.escapists.data.chunks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;
import net.jselby.escapists.util.CompressionUtils;

/**
 * Fonts used by the application.
 *
 * @author j_selby
 */
public class FontBank extends Chunk {
    public FontItem[] fonts;

    @Override
    public void init(ByteReader buffer, int length) {
        int num = buffer.getInt();

        int offset = -1;

        fonts = new FontItem[num];
        int highestHandle = 0;
        for (int i = 0; i < num; i++) {
            fonts[i] = new FontItem(buffer);
            if (fonts[i].handle > highestHandle) {
                highestHandle = (int) fonts[i].handle;
            }
        }

        // Sort by handle
        FontItem[] newFonts = new FontItem[highestHandle + 1];
        for (FontItem item : fonts) {
            newFonts[((int) item.handle)] = item;
        }
        fonts = newFonts;
    }

    public class FontItem {
        public final long handle;
        public final int checksum;
        public final int references;
        public final LogFont value;

        public FontItem(ByteReader buffer) {
            handle = buffer.getUnsignedInt();

            buffer = CompressionUtils.decompress(buffer);

            int position = buffer.position();

            checksum = buffer.getInt();
            references = buffer.getInt();

            int size = buffer.getInt();

            value = new LogFont(buffer);
        }
    }

    public class LogFont {
        public final int height;
        public final int width;

        public final int escapement;

        public final int orientation;
        public final int weight;

        public final byte italic;
        public final byte underline;

        public final byte strikeOut;
        public final byte charSet;

        public final byte outPrecision;
        public final byte clipPrecision;

        public final byte quality;

        public final byte pitchAndFamily;

        public String faceName;

        public final BitmapFont font;
        public final BitmapFontCache fontCache;

        //public Font awtFont;

        public LogFont(ByteReader buffer) {
            height = -buffer.getInt();
            width = buffer.getInt();

            escapement = buffer.getInt();

            orientation = buffer.getInt();
            weight = buffer.getInt();

            italic = buffer.getByte();
            underline = buffer.getByte();

            strikeOut = buffer.getByte();
            charSet = buffer.getByte();

            outPrecision = buffer.getByte();
            clipPrecision = buffer.getByte();

            quality = buffer.getByte();

            pitchAndFamily = buffer.getByte();

            faceName = buffer.getString(32);
            System.out.println("Loading: " + "assets/fonts/" + faceName + ".ttf");

            if (faceName.equalsIgnoreCase("Small Fonts")) {
                faceName = "Escapists";
            }
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + faceName + ".ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = height;

            font = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose();

            // Also create a cache for this font
            fontCache = new BitmapFontCache(font);

            // Convert to a slick font
            //if (faceName.equalsIgnoreCase("Escapists")) {
            /*    System.out.println("Loading manual font from OS:");
                InputStream inputStream	= ResourceLoader.getResourceAsStream("Escapists.ttf");
                try {
                    awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                    awtFont = awtFont.deriveFont((float) (height));
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                awtFont = new Font(faceName, Font.BOLD, height);
            }*/
        }
    }
}
