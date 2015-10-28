package net.jselby.escapists.data.objects;

import com.badlogic.gdx.graphics.Color;
import net.jselby.escapists.util.ByteReader;

/**
 * A shape is a basic object with textures.
 *
 * @author j_selby
 */
public class Shape {
    public final short borderSize;
    public final Color borderColor;
    public final ShapeTypes shape;
    public final FillTypes fillType;

    public int lineFlags;
    public short gradientFlags;

    public Color color1;
    public Color color2;
    public short image;

    public Shape(ByteReader buffer) {
        borderSize = buffer.getShort();
        borderColor = buffer.getColor();
        shape = ShapeTypes.getTypeById(buffer.getShort());
        fillType = FillTypes.getTypeById(buffer.getShort());

        if (shape == ShapeTypes.Line) {
            lineFlags = buffer.getUnsignedShort();
        } else if (fillType == FillTypes.SolidFill) {
            color1 = buffer.getColor();
        } else if (fillType == FillTypes.GradientFill) {
            color1 = buffer.getColor();
            color2 = buffer.getColor();
            gradientFlags = buffer.getShort();
        } else if (fillType == FillTypes.MotifFill) {
            image = buffer.getShort();
        }
    }

    public enum FillTypes {
        NoneFill(0),
        SolidFill(1),
        GradientFill(2),
        MotifFill(3);

        private int id;

        FillTypes(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static FillTypes getTypeById(int id) {
            for (FillTypes type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum ShapeTypes {
        Line(1),
        Rectangle(2),
        Ellipse(3);

        private int id;

        ShapeTypes(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ShapeTypes getTypeById(int id) {
            for (ShapeTypes type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
}
