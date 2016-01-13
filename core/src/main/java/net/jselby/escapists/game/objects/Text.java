package net.jselby.escapists.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
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
    private GlyphLayout[] compiledStrings;
    private FontBank.LogFont font;
    private String str;

    public Text(ObjectDefinition def, ObjectInstances.ObjectInstance instanceDef) {
        super(def, instanceDef);
        rawType = ((ObjectCommon) def.properties.getProperties()).partText;

        net.jselby.escapists.data.objects.sections.Text.Paragraph paragraph = rawType.paragraphs[0];

        font = EscapistsRuntime.getRuntime().getApplication()
                .fonts[paragraph.font + 1].getValue();
        compileString(font,
                paragraph.value, instanceDef.getX(), instanceDef.getY());
        str = paragraph.value;
    }

    private void compileString(FontBank.LogFont value, String msg, float x, float y) {
        String[] msgs = msg.split("\n");
        compiledStrings = new GlyphLayout[msgs.length];

        int alignment = Align.left;
        if (rawType.paragraphs[0].isCentered()) {
            alignment = Align.center;
        } else if (rawType.paragraphs[0].isRightAligned()) {
            alignment = Align.right;
        }

        for (int i = 0; i < msgs.length; i++) {
            compiledStrings[i] = value.getFontCache().addText(msgs[i], x, y, getWidth(), alignment, true);
        }
    }

    private void decompileString(FontBank.LogFont value, String msg, float x, float y) {
        for (GlyphLayout layout : compiledStrings) {
            value.getFontCache().getLayouts().removeValue(layout, false);
        }
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

        BitmapFont originalFont = g.getFont();
        BitmapFont font = EscapistsRuntime.getRuntime()
                .getApplication().fonts[paragraph.font + 1].getValue().getFont();
        g.setFont(font);

        float oldAlpha = paragraph.color.a;
        paragraph.color.a *= ((float) getImageAlpha()) / 256f;
        g.setColor(paragraph.color);

        // Compute height
        float totalHeight = 0;
        float height = 0;

        for (GlyphLayout compiledString : compiledStrings) {
            height += (font.getLineHeight() * compiledString.runs.size);
        }

        int alignment = Align.left;
        if (paragraph.isCentered()) {
            alignment = Align.center;
        } else if (paragraph.isRightAligned()) {
            alignment = Align.right;
        }

        float drawYAdd = font.getLineHeight() / 8; // TODO: Much hack, such wow
        if (paragraph.isVerticallyCentered()) {
            drawYAdd += getHeight() / 2 - height / 2;
        } else if (paragraph.isBottomAligned()) {
            drawYAdd += getHeight() - height;
        }

        // Render
        String[] msgs = str.split("\n");
        for (int i = 0; i < compiledStrings.length; i++) {
            g.drawString(msgs[i], getX(), getY() + drawYAdd + totalHeight, getWidth(), alignment);
            totalHeight += (font.getLineHeight() * compiledStrings[i].runs.size);
        }

        g.setFont(originalFont);
        paragraph.color.a = oldAlpha;

    }

    public void setString(String msg) {
        msg = msg;
        if (str.equals(msg)) {
            return;
        }
        decompileString(font, str, getX(), getY());
        str = msg;
        compileString(font, str, getX(), getY());
    }

    public Object getString() {
        return str;
    }
}
