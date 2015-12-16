package net.jselby.escapists.data;

import net.jselby.escapists.data.chunks.ObjectInstances;
import net.jselby.escapists.data.chunks.ObjectProperties;
import net.jselby.escapists.game.ObjectInstance;
import net.jselby.escapists.game.objects.*;

/**
 * An object definition is the raw definition of a object, including name, id, etc.
 *
 * @author j_selby
 */
public class ObjectDefinition {
    public String name;

    public short handle;
    public short objectType;

    public int headerFlags;
    public short headerReserved;

    public long inkEffect;
    public long inkEffectParameter;

    public boolean transparent;

    public long shaderId;
    public byte[][] shaderItems;
    public ObjectProperties properties;

    @Override
    public String toString() {
        return name;
    }

    public ObjectInstance createWorldInstance(ObjectInstances.ObjectInstance info) {
        ObjectInstance newInstance = null;

        if (properties == null || properties.getObjectType() == null) {
            System.err.printf("Warning: Creating null object @ %s.\n", toString());
            return new Empty(this, info);
        }

        switch(properties.getObjectType()) {
            case Backdrop:
                newInstance = new Backdrop(this, info);
                break;
            case Active:
                newInstance = new Active(this, info);
                break;
            case Text:
                newInstance = new Text(this, info);
                break;
            case QuickBackdrop:
                newInstance = new QuickBackdrop(this, info);
                break;
                /*case Player:
                    break;
                case Keyboard:
                    break;
                case Create:
                    break;
                case Timer:
                    break;
                case Game:
                    break;
                case Speaker:
                    break;
                case System:
                    break;
                case Question:
                    break;
                case Score:
                    break;
                case Lives:
                    break;
                case Counter:
                    break;
                case RTF:
                    break;
                case SubApplication:
                    break;*/
            default:
                System.out.println("Object type failed @ creation: " + properties.getObjectType());
        }
        return newInstance;
    }
}
