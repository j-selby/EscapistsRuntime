package net.jselby.escapists.data.objects;

import net.jselby.escapists.util.ByteReader;

import java.awt.*;
import java.util.ArrayList;

/**
 * An ObjectCommon is a object definition's properties that is declared as generic.
 */
public class ObjectCommon extends ObjectDefinitionProperties {
    private short version;
    private long flags;

    private ArrayList<Short> qualifiers;

    private int newFlags;
    private int preferences;

    private int identifier;
    private Color backColour;

    private int fadeInOffset;
    private int fadeOutOffset;

    private Movements[] movements;
    private AnimationHeader animations;
    private Counter counter;

    private short extensionOldFlags;
    private short extensionVersion;
    private int extensionId;
    private int extensionPrivate;
    private byte[] extensionData;

    @Override
    public void read(ByteReader buffer, int length) {
        int currentPosition = buffer.position();

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

        if (movementsOffset != 0) {
            buffer.position(currentPosition + movementsOffset);
            movements = new Movements[] { new Movements(buffer) };
        } else {
            movements = new Movements[0];
        }

        if (valuesOffset != 0) {
            System.out.println("values");
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
            System.out.println("Extension!");
            buffer.position(currentPosition + extensionOffset);
            int dataSize = buffer.getShort() - 8;
            buffer.skipBytes(2);
            extensionOldFlags = buffer.getShort();
            extensionVersion = buffer.getShort();
            extensionId = 0;
            extensionPrivate = 0;
            if (dataSize != 0) {
                extensionData = buffer.getBytes(dataSize);
            }
        }

        if (fadeInOffset != 0) {
            System.out.println("fadein");
        }

        if (fadeOutOffset != 0) {
            System.out.println("fadeout");
        }

        if (systemObjectOffset != 0) {
            buffer.position(currentPosition + systemObjectOffset);

            objectType = well, i'm fucked.
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

    private class Movements {
        public Movements(ByteReader buffer) {

        }
    }

    private class AnimationHeader {
        public AnimationHeader(ByteReader buffer) {

        }
    }

    private class Counter {
        public Counter(ByteReader buffer) {
        }
    }
}
