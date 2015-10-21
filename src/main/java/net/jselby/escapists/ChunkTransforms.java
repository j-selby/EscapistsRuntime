package net.jselby.escapists;

import chunk_cm.Chunk_cmLibrary;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Static methods to help with Chunk transformations.
 */
public class ChunkTransforms {
    public static final int TRANS_START = 99;

    // Generated from create_transform(), fed through C function
    public static byte[] key = {-1, -1, -2, -1, 84, 104, 101, 32, 69, 115, 99, 97, 112, 105, 115, 116, 115,
            -1, -1, -2, -1, 67, 104, 114, 105, 115, 32, 68, 97, 118, 105, 115, -1, -1, -2, -1, 50, 48, 49,
            53, 32, 77, 111, 117, 108, 100, 121, 32, 84, 111, 111, 102, 32, 83, 116, 117, 100, 105, 111, 115,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0};
/*{78, 39, -110, -55, 79, -27, -93, 67, -12, -85, 15, 87, 107, -28, -75, 23, -62,
            39, -109, -56, -28, -50, -82, 17, -40, -85, 76, 114, 122, -5, -81, 16, 78, 39, -
            110, -55, 41, -67, -9, 86, -111, -107, 3, 67, 119, -23, -65, 67, -27, -73, 3, 80
            , 59, -34, -78, 22, -43, -79, 3, 69, 27, -115, -58, 99, -79, -40, 108, 54, 27, -
            115, -58, 99, -79, -40, 108, 54, 27, -115, -58, 99, -79, -40, 108, 54, 27, -115,
            -58, 99, -79, -40, 108, 54, 27, -115, -58, 99, -79, -40, 108, 54, 27, -115, -58
            , 99, -79, -40, 108, 54, 27, -115, -58, 99, -79, -40, 108, 54, 27, -115, -58, 99
            , -79, -40, 108, 54, 27, -115, -58, 99};*/



    static {
        try {
            FileInputStream in = new FileInputStream("keydump");
            key = IOUtils.toByteArray(in);
            in.close();
            System.out.println("Key length: " + key.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] transform(byte[] data) {
        // transform(unsigned char * data, int size, unsigned char * trans)
        ByteBuffer buf = ByteBuffer.wrap(data);
        Chunk_cmLibrary.transform(buf, data.length, ByteBuffer.wrap(key));
        return buf.array();
    }

    public static byte[] create_transform_part(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //byte > char > short?

        for (int i = 0; i < data.length; i++) {
            short vv = (short) ((char) data[i]);

            if ((vv & 0xFF) != 0) {
                out.write((char) (vv & 0xFF));
            }

            vv = (short) (vv >> 8);

            if (vv != 0) {
                out.write((char) (vv));
            }
        }

        return out.toByteArray();
    }

    public static byte[] create_transform(byte[] editor, byte[] name, byte[] copyright) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        editor = toUTF16LE(new String(editor, "UTF-8"));
        name = toUTF16LE(new String(name, "UTF-8"));
        copyright = toUTF16LE(new String(copyright, "UTF-8"));
        out.write(create_transform_part(editor));
        out.write(create_transform_part(name));
        out.write(create_transform_part(copyright));
        byte[] ret = out.toByteArray();

        byte[] ret1 = new byte[128];
        System.arraycopy(ret, 0, ret1, 0, ret.length < 127 ? ret.length : 127);
        ret = ret1;

        out = new ByteArrayOutputStream();
        out.write(ret);
        out.write('\00' * Math.max(0, 256 - ret.length));
        ret = out.toByteArray();

        /*cdef bytearray ret_arr = bytearray(ret)
        cdef unsigned char * ret_c = ret_arr
        prepare_transform(ret_arr, l)*/

        return ret;
    }

    public static byte[] create_transform() throws IOException {
        return create_transform(
                "The Escapists".getBytes("UTF-8"),
                "Chris Davis".getBytes("UTF-8"),
                "2015 Mouldy Toof Studios".getBytes("UTF-8"));
    }

    public static byte[] toUTF16LE(String string) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(string.length() * 2 + 2);
        byteArrayOutputStream.write(new byte[]{(byte)0xFF,(byte)0xFE});
        byteArrayOutputStream.write(string.getBytes("UTF-16LE"));
        return byteArrayOutputStream.toByteArray();
    }
}
