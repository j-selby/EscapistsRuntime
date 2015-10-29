package net.jselby.escapists.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
import net.jselby.escapists.data.chunks.FontBank;
import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.data.objects.ObjectCommon;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.ObjectInstance;
import org.mini2Dx.core.graphics.Graphics;

/**
 * Text is a simple String renderer on the screen.
 *
 * @author j_selby
 */
public class Text extends ObjectInstance {
    private final net.jselby.escapists.data.objects.sections.Text rawType;
    private final GlyphLayout[] compiledStrings;
    //private final TrueTypeFont font;

    public Text(ObjectDefinition def, ObjectInstances.ObjectInstance instanceDef) {
        super(def, instanceDef);
        rawType = ((ObjectCommon) def.properties.properties).partText;

        compiledStrings = new GlyphLayout[rawType.paragraphs.length];
        net.jselby.escapists.data.objects.sections.Text.Paragraph[] paragraphs = rawType.paragraphs;
        for (int i = 0; i < paragraphs.length; i++) {
            net.jselby.escapists.data.objects.sections.Text.Paragraph paragraph = paragraphs[i];
            compiledStrings[i] = EscapistsRuntime.getRuntime().getApplication()
                        .fonts[paragraph.font].value.fontCache
                        .addText(paragraph.value, instanceDef.x, instanceDef.y);
        }
        //font = new TrueTypeFont(originalFont.value.awtFont, false);
    }

    @Override
    public void tick(EscapistsGame container) {

    }

    /*
    PARAGRAPH_FLAGS = BitDict(
        'HorizontalCenter',
        'RightAligned',
        'VerticalCenter',
        'BottomAligned',
        None, None, None, None,
        'Correct',
        'Relief'
    )
     */

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        int adjY = 0;
        for (GlyphLayout layout : compiledStrings) {
            // Get the font for this paragraph
            g.
            /*BitmapFont font = EscapistsRuntime.getRuntime()
                    .getApplication().fonts[paragraph.font].value.font;
            font.setColor(paragraph.color);
            g.drawString(paragraph.value, getX(), getY() + adjY);
            adjY += 10;*/
        }
        //g.resetFont();
    }
}
