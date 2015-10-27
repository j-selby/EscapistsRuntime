package net.jselby.escapists.data.objects;

import net.jselby.escapists.util.ByteReader;

import java.awt.*;

/**
 * A shape is a basic object with textures.
 *
 * @author j_selby
 */
public class Shape {
    private final short borderSize;
    private final Color borderColor;
    private final ShapeTypes shape;
    private final FillTypes fillType;

    private int lineFlags;
    private short gradientFlags;

    private Color color1;
    private Color color2;
    private short image;

    public Shape(ByteReader buffer, int length) {
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
