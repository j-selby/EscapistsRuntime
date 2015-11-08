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
    private GlyphLayout compiledStrings;
    private int font;
    private String str;
    //private final TrueTypeFont font;

    public Text(ObjectDefinition def, ObjectInstances.ObjectInstance instanceDef) {
        super(def, instanceDef);
        rawType = ((ObjectCommon) def.properties.properties).partText;

        net.jselby.escapists.data.objects.sections.Text.Paragraph paragraph = rawType.paragraphs[0];
        compiledStrings = EscapistsRuntime.getRuntime().getApplication()
                    .fonts[paragraph.font + 1].value.fontCache
                    .addText(paragraph.value, instanceDef.x, instanceDef.y);
        font = paragraph.font;
        str = paragraph.value;
        //font = new TrueTypeFont(originalFont.value.awtFont, false);
    }

    @Override
    public float getWidth() {
        return rawType.width;
    }

    @Override
    public float getHeight() {
        return rawType.height;
    }

    @Override
    public void tick(EscapistsGame container) {

    }

    @Override
    public void draw(EscapistsGame container, Graphics g) {
        if (!isVisible()) {
            return;
        }

        net.jselby.escapists.data.objects.sections.Text.Paragraph paragraph = rawType.paragraphs[0];

        float width = compiledStrings.width;
        float height = compiledStrings.height;

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
        g.drawString(str, getX() + drawXAdd, getY() + drawYAdd);

        //font.setColor(paragraph.color);
        //font.draw(batch, compiledStrings[i], getX(), getY());
        //compiledStrings[i]
        /*BitmapFont font = EscapistsRuntime.getRuntime()
                .getApplication().fonts[paragraph.font].value.font;
        font.setColor(paragraph.color);
        g.drawString(paragraph.value, getX(), getY() + adjY);
        adjY += 10;*/

        //batch.end();
    }

    public void setString(String msg) {
        if (str.equals(msg)) {
            return;
        }
        str = msg;
        compiledStrings = EscapistsRuntime.getRuntime().getApplication()
                .fonts[font + 1].value.fontCache
                .addText(str, getX(), getY());
    }
}
