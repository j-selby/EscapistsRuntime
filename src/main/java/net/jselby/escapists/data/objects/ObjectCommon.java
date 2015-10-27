package net.jselby.escapists.data.objects;

import net.jselby.escapists.data.chunks.ObjectProperties;
import net.jselby.escapists.data.objects.sections.*;
import net.jselby.escapists.util.ByteReader;

import java.awt.*;
import java.util.ArrayList;

/**
 * An ObjectCommon is a object definition's properties that is declared as generic.
 */
public class ObjectCommon extends ObjectDefinitionProperties {
    private int objectType;

    public short version;
    public long flags;

    public ArrayList<Short> qualifiers;

    public int newFlags;
    public int preferences;

    public int identifier;
    public Color backColour;

    public int fadeInOffset;
    public int fadeOutOffset;

    public Movements[] movements;
    public AnimationHeader animations;
    public Counter counter;
    public AlterableValues values;
    public AlterableStrings strings;

    public short extensionOldFlags;
    public int extensionVersion;
    public int extensionId;
    public int extensionPrivate;
    public byte[] extensionData;

    public Text partText;

    public ObjectCommon(int objectType) {
        this.objectType = objectType;
    }

    @Override
    public void read(ByteReader buffer, int length) {
        int currentPosition = buffer.position();

        int size = buffer.getInt();

        short animationsOffset = buffer.getShort();
        short movementsOffset = buffer.getShort();

        version = buffer.getShort();
        buffer.skipBytes(2);

        short extensionOffset = buffer.getShort();
        short counterOffset = buffer.getShort();

        flags = buffer.getUnsignedInt();

        int end = buffer.position() + 8 * 2;
        qualifiers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            short qualifier = buffer.getShort();

            if (qualifier == -1) {
                break;
            }

            qualifiers.add(qualifier);
        }

        buffer.position(end);

        short systemObjectOffset = buffer.getShort();

        short valuesOffset = buffer.getShort();
        short stringsOffset = buffer.getShort();

        newFlags = buffer.getUnsignedShort();
        preferences = buffer.getUnsignedShort();

        identifier = buffer.getInt();
        backColour = buffer.getColor();

        fadeInOffset = buffer.getInt();
        fadeOutOffset = buffer.getInt();

        ObjectProperties.ObjectTypes type = ObjectProperties.ObjectTypes.getById(objectType);

        if (movementsOffset != 0) {
            buffer.position(currentPosition + movementsOffset);
            movements = new Movements[] { new Movements(buffer) };
        } else {
            movements = new Movements[0];
        }

        if (valuesOffset != 0) {
            buffer.position(currentPosition + valuesOffset);
            values = new AlterableValues(buffer);
        }

        if (stringsOffset != 0) {
            buffer.position(currentPosition + stringsOffset);
            strings = new AlterableStrings(buffer);
        }

        if (animationsOffset != 0) {
            buffer.position(currentPosition + animationsOffset);
            animations = new AnimationHeader(buffer);
        }

        if (counterOffset != 0) {
            buffer.position(currentPosition + counterOffset);
            counter = new Counter(buffer);
        }

        if (extensionOffset != 0) {
            buffer.position(currentPosition + extensionOffset);
            int dataSize = buffer.getInt() - 20;
            buffer.skipBytes(4);

            extensionVersion = buffer.getInt();
            extensionId = buffer.getInt();
            extensionPrivate = buffer.getInt();

            if (dataSize != 0) {
                extensionData = buffer.getBytes(dataSize);
            }
        }

        if (fadeInOffset != 0) {
            // TODO: Fade in
        }

        if (fadeOutOffset != 0) {
            // TODO: Fade out
        }

        if (systemObjectOffset != 0) {
            buffer.position(currentPosition + systemObjectOffset);

            if (type == ObjectProperties.ObjectTypes.Text || type == ObjectProperties.ObjectTypes.Question) {
                // Text
                partText = new Text(buffer);
            } else if (type == ObjectProperties.ObjectTypes.Score || type == ObjectProperties.ObjectTypes.Lives
                    || type == ObjectProperties.ObjectTypes.Counter) {
                // Counters
            } else if (type == ObjectProperties.ObjectTypes.RTF) {
                // RTFObject
            } else if (type == ObjectProperties.ObjectTypes.SubApplication) {
                // Subapplication
            } else {
                System.out.printf("Unknown type: %s (%d).\n", type, objectType);
            }
        }
        /*

        if systemObjectOffset != 0:
            reader.seek(currentPosition + systemObjectOffset)

            objectType = self.parent.objectType
            if objectType in (TEXT, QUESTION): # also question
                self.text = self.new(Text, reader)
            elif objectType in (SCORE, LIVES, COUNTER):
                self.counters = self.new(Counters, reader)
            # elif objectType == RTF:
                # self.rtf = self.new(RTFObject, reader)
            elif objectType == SUBAPPLICATION:
                self.subApplication = self.new(SubApplication, reader)
            else:
                print 'native noo', objectType

         */
    }
}
