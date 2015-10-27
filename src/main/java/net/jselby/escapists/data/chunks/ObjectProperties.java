package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.data.objects.Backdrop;
import net.jselby.escapists.data.objects.ObjectCommon;
import net.jselby.escapists.data.objects.ObjectDefinitionProperties;
import net.jselby.escapists.data.objects.QuickBackdrop;
import net.jselby.escapists.util.ByteReader;

/**
 * The various properties of an object.
 */
public class ObjectProperties extends Chunk {
    private ByteReader buffer;
    private int bufferLength;

    public boolean isCommon = false;
    public ObjectTypes objectType;

    private ObjectDefinitionProperties properties;

    @Override
    public void init(ByteReader buffer, int length) {
        this.buffer = buffer;
        this.bufferLength = length;
    }

    public void load(short objectTypeRaw) {
        this.objectType = ObjectTypes.getById(objectTypeRaw);

        if (objectType == ObjectTypes.QuickBackdrop) {
            properties = new QuickBackdrop();
        } else if (objectType == ObjectTypes.Backdrop) {
            properties = new Backdrop();
        } else {
            isCommon = true;
            properties = new ObjectCommon();
        }

        properties.read(buffer, bufferLength);

        buffer = null; // Remove ref
    }

    public enum ObjectTypes {
        Player(-7),
        Keyboard(-6),
        Create(-5),
        Timer(-4),
        Game(-3),
        Speaker(-2),
        System(-1),
        QuickBackdrop(0),
        Backdrop(1),
        Active(2),
        Text(3),
        Question(4),
        Score(5),
        Lives(6),
        Counter(7),
        RTF(8),
        SubApplication(9);

        private int id;

        ObjectTypes(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ObjectTypes getById(int id) {
            for (ObjectTypes type : ObjectTypes.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
}
