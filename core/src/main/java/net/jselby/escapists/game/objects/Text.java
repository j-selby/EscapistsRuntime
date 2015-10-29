package net.jselby.escapists.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.data.ObjectDefinition;
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
                        .fonts[paragraph.font + 1].value.fontCache
                        .addText(paragraph.value, instanceDef.x, instanceDef.y);
        }
        //font = new TrueTypeFont(originalFont.value.awtFont, false);
    }

    @Override
    public void tick(EscapistsGame container) {

    }

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        if (!isVisible()) {
            return;
        }

        int adjY = 0;

        //SpriteBatch batch = new SpriteBatch();
        //batch.begin();
        for (int i = 0; i < rawType.paragraphs.length; i++) {
            net.jselby.escapists.data.objects.sections.Text.Paragraph paragraph = rawType.paragraphs[i];

            float width = compiledStrings[i].width;
            float height = compiledStrings[i].height;

            float drawXAdd = 0;
            if (paragraph.isCentered()) {
                drawXAdd = rawType.width / 2 - width / 2;
            } else if (paragraph.isRightAligned()) {
                drawXAdd = rawType.width - width;
            }
            float drawYAdd = 0;
            if (paragraph.isVerticallyCentered()) {
                drawYAdd = rawType.height / 2 - height / 2;
            } else if (paragraph.isBottomAligned()) {
                drawYAdd = rawType.height - height;
            }

            BitmapFont font = EscapistsRuntime.getRuntime()
                    .getApplication().fonts[paragraph.font + 1].value.font;
            g.setFont(font);
            g.setColor(paragraph.color);
            g.drawString(paragraph.value, getX() + drawXAdd, getY() + drawYAdd + adjY);
            adjY += height + 1;

            //font.setColor(paragraph.color);
            //font.draw(batch, compiledStrings[i], getX(), getY());
            //compiledStrings[i]
            /*BitmapFont font = EscapistsRuntime.getRuntime()
                    .getApplication().fonts[paragraph.font].value.font;
            font.setColor(paragraph.color);
            g.drawString(paragraph.value, getX(), getY() + adjY);
            adjY += 10;*/
        }
        //batch.end();
    }
}
