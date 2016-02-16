package net.jselby.escapists.data;

/**
 * Static methods to help with Chunk transformations.
 */
public class ChunkTransforms {

    // Generated from create_transform(), fed through C function
   /*public static final int TRANS_START = 99;
    public static final byte[] KEY = {-14, -30, 48, 99, 104, -24, -76, 16, -19, -101, 4, 68, 114, -2, -102, 39, -44, -85,
            7, 66, 116, -3, -102, 55, -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 71, -39, -82, 6, -12,
            -85, 15, 87, 107, -28, -75, 23, -62, -121, 85, 105, 81, -8, -88, 6, -18, -23, 89, 24, 118, -21, -89, 55,
            -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 41, -67, -9, 86, -111, -107, 3, 67, 119, -23, -65,
            67, -27, -73, 3, 80, 59, -34, -78, 22, -43, -79, 3, 69, 27, -6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};*/
    public static final int[] KEY_D = {
            0x2e, 0x7e, 0xa7, 0x2d, 0x23, 0xee, 0x4, 0x7, 0x6b, 0xb7, 0x19, 0xa6, 0xbc, 0x9b, 0x7f, 0x58, 0x89, 0x51,
            0xce, 0x55, 0xd8, 0x6c, 0x6f, 0x27, 0x71, 0xf4, 0xd, 0xc7, 0x30, 0xbd, 0x67, 0x8c, 0x54, 0xf0, 0x6e, 0x28,
            0x84, 0xfd, 0x88, 0x1d, 0x5f, 0xa9, 0x4b, 0xd9, 0xba, 0x34, 0xc, 0x10, 0xc9, 0x6d, 0x4a, 0x1, 0xed, 0xa4,
            0x52, 0xac, 0x7c, 0x2a, 0x26, 0x3c, 0xd1, 0x42, 0x5c, 0xe5, 0x48, 0x63, 0xa5, 0xd3, 0x83, 0x46, 0x94, 0xda,
            0x5e, 0xd6, 0xa, 0xb1, 0x2b, 0x31, 0xf9, 0x68, 0x25, 0x81, 0xdc, 0xb3, 0xaf, 0xf6, 0xf1, 0x35, 0x92, 0x97,
            0x14, 0xad, 0xb2, 0xbb, 0x2c, 0x50, 0xd2, 0x80, 0x61, 0xb0, 0x70, 0x7b, 0x40, 0x45, 0x0, 0x82, 0xc8, 0x95,
            0xb4, 0x90, 0xa3, 0x11, 0xc5, 0xf7, 0xc4, 0x8f, 0x5b, 0x3f, 0x4d, 0x49, 0xe9, 0xbe, 0x32, 0xeb, 0x4c, 0x56,
            0x3e, 0x15, 0x4e, 0xb, 0xf5, 0x39, 0xb5, 0xca, 0xc6, 0x8a, 0x8b, 0xcd, 0xa1, 0x93, 0x9c, 0x1a, 0x6, 0x3a,
            0x38, 0xe8, 0x8, 0x2f, 0x5, 0xaa, 0xbf, 0x98, 0xe6, 0xb6, 0x43, 0x99, 0xcf, 0xd4, 0x57, 0xa2, 0x8e, 0x9d,
            0x65, 0x33, 0x87, 0x22, 0x37, 0x36, 0xe2, 0xae, 0x1b, 0x8d, 0x18, 0x69, 0xcb, 0x86, 0x91, 0xe1, 0x74, 0xea,
            0x76, 0x2, 0x7a, 0x4f, 0xb8, 0xe, 0xff, 0xef, 0xde, 0xa8, 0xdb, 0xe0, 0x64, 0x85, 0xf3, 0x6a, 0xec, 0xe3,
            0x59, 0x9a, 0x1c, 0x13, 0xf2, 0x3, 0x53, 0x1f, 0x9, 0x3b, 0x77, 0xa0, 0x12, 0x9f, 0xd5, 0x5d, 0x79, 0x7d,
            0x3d, 0x47, 0xf8, 0xfc, 0x21, 0xb9, 0xcc, 0x62, 0x44, 0x75, 0xdd, 0xc0, 0xe7, 0x73, 0x9e, 0x17, 0xd7, 0x16,
            0xf, 0x1e, 0x96, 0xfb, 0xe4, 0xdf, 0x20, 0x24, 0xd0, 0x66, 0xc3, 0x78, 0x29, 0x72, 0x41, 0x60, 0xc1, 0x5a,
            0xfa, 0xab, 0xfe, 0xc2, 0x0, 0x0};

    public static byte[] transform(byte[] data) {
        // Make us a copy of KEY_D, as we modify it during execution.
        int[] d = new int[KEY_D.length];
        System.arraycopy(KEY_D, 0, d, 0, d.length);

        // Below is modified from chunk_cm.cpp, from https://github.com/matpow2/anaconda
        int[] e;
        int f;
        int g;
        int h;
        int i;
        int j;
        int k;
        int l;
        e = d;
        f = d[256];
        g = d[257];
        h = 0;

        if (data.length <= 0) {
            d[256] = f;
            d[257] = g;
        } else {
            while (true) {
                f = (byte) (f + 1);
                ++h;
                i = e[f & 0xFF];
                j = (byte) (i + g);
                l = j;
                k = e[j & 0xFF];
                e[f & 0xFF] = k;
                e[l & 0xFF] = i;
                data[h - 1] ^= e[(byte) (i + k) & 0xFF] & 0xFF;
                if (h >= data.length) {
                    break;
                }
                g = l & 0xFF;
            }
            e[257] = l;
            e[256] = f;
        }

        return data;
    }

    // Below code has not been implemented correctly (yet).
    // TODO: Implement z() (from C code)
    // TODO: Implement prepare_transform()
    /*public static byte[] create_transform_part(byte[] data) {
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

       /* return ret;
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
    }*/
}
