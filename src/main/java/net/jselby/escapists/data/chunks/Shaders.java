package net.jselby.escapists.data.chunks;

import net.jselby.escapists.data.Chunk;
import net.jselby.escapists.util.ByteReader;

/**
 * Shaders are lists of graphics instructions that tell a renderer how to render stuff.
    */
    public class Shaders extends Chunk {
    private Shader[] shaders;

    @Override
    public void init(ByteReader buffer, int length) {
        int initialPosition = buffer.position();

        int count = buffer.getInt();
        int[] offsets = new int[count];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = buffer.getInt();
        }

        shaders = new Shader[count];
        for (int i = 0; i < offsets.length; i++) {
            buffer.position(initialPosition + offsets[i]);
            // TODO: Fix buffer underflow - garbage data?
            //shaders[i] = new Shader(buffer);
        }
    }

    /**
     * A Parameter is a param to a shader.
     */
    private class Parameter {
        public String name;
        public byte type;
        public String value;
    }

    /**
     * A shader is a graphics instruction that tell a renderer how to render stuff.
     */
    private class Shader {
        public final Parameter[] params;
        public final String name;
        public final String data;
        public final int bgTexture;

        public Shader(ByteReader buffer) {
            int initialPosition = buffer.position();

            int nameOffset = buffer.getInt();
            int dataOffset = buffer.getInt();
            int paramOffset = buffer.getInt();
            bgTexture = buffer.getInt();

            buffer.position(initialPosition + nameOffset);
            name = buffer.getString();

            buffer.position(initialPosition + dataOffset);
            data = buffer.getString();

            if (paramOffset != 0) {
                paramOffset += initialPosition;
                buffer.position(paramOffset);

                int paramCount = buffer.getInt();

                params = new Parameter[paramCount];
                for (int i = 0; i < params.length; i++) {
                    params[i] = new Parameter();
                }

                int typeOffset = buffer.getInt();
                int namesOffset = buffer.getInt();

                buffer.position(paramCount + typeOffset);
                for (Parameter parameter : params) {
                    parameter.type = buffer.getByte();
                }

                buffer.position(paramCount + namesOffset);
                for (Parameter parameter : params) {
                    parameter.name = buffer.getString();
                }
            } else {
                params = new Parameter[0];
            }

            System.out.println("Shader: " + name);
        }
    }
}