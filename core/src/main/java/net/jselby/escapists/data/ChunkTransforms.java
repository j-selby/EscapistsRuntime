package net.jselby.escapists.data;

/**
 * Static methods to help with Chunk transformations.
 */
public class ChunkTransforms {

    // Generated from create_transform(), fed through C function
    public static final int TRANS_START = 99;
    public static final byte[] KEY = {-14, -30, 48, 99, 104, -24, -76, 16, -19, -101, 4, 68, 114, -2, -102, 39, -44, -85,
            7, 66, 116, -3, -102, 55, -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 71, -39, -82, 6, -12,
            -85, 15, 87, 107, -28, -75, 23, -62, -121, 85, 105, 81, -8, -88, 6, -18, -23, 89, 24, 118, -21, -89, 55,
            -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 41, -67, -9, 86, -111, -107, 3, 67, 119, -23, -65,
            67, -27, -73, 3, 80, 59, -34, -78, 22, -43, -79, 3, 69, 27, -6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int[] KEY_D = {0x43, 0x7e, 0x36, 0x2d, 0x23, 0xd4, 0xe7, 0x7, 0x6b, 0xb7, 0x45, 0xa6,
            0x1b, 0x9b, 0x37, 0x72, 0x8c, 0x51, 0xde, 0x55, 0x59, 0x76, 0xbf, 0xd7, 0x96, 0x1c,
            0xd, 0xc7, 0x30, 0x7f, 0x22, 0xcf, 0xd6, 0x14, 0x91, 0x28, 0x84, 0x12, 0x8b
            , 0x1d, 0x5f, 0xb6, 0x4b, 0x81, 0x75, 0x34, 0x99, 0x3b, 0x1, 0x38, 0xfb, 0x3e, 0x4,
            0xb4, 0xdc, 0xac, 0x53, 0xe1, 0xa5, 0x3c, 0xe3, 0xef, 0x82, 0x7c, 0x0, 0x69,
            0x5c, 0x65, 0x92, 0x2a, 0xc8, 0x6d, 0x4f, 0xfd, 0x2, 0x35, 0x70, 0x6, 0xeb, 0xb
            , 0xa1, 0xbd, 0x3d, 0x47, 0xe8, 0x86, 0xfc, 0xc5, 0xa, 0xd8, 0xa9, 0x7b, 0xfa, 0x63,
            0xf5, 0xab, 0x41, 0x1e, 0xc3, 0x40, 0x26, 0x16, 0xdf, 0x24, 0xd5, 0x6c, 0x42, 0xc0, 0xc6,
            0xa7, 0x57, 0x58, 0xed, 0x7a, 0xe5, 0x44, 0x18, 0x66, 0xb1, 0xf1,
            0xdb, 0x9f, 0x9d, 0xd9, 0xa3, 0xd2, 0x98, 0x8a, 0x2f, 0x93, 0xe2, 0x21, 0xfe, 0xbc,
            0xd1, 0x29, 0x2b, 0x94, 0xbe, 0x73, 0xb5, 0xda, 0xb8, 0x20, 0x62, 0xc1, 0xc4, 0x5b, 0x33,
            0x90, 0x4a, 0x79, 0x71, 0x7d, 0x2c, 0x78, 0xea, 0x50, 0x60, 0xc,
            0x13, 0x67, 0xe9, 0x2e, 0x61, 0x64, 0x9, 0x8f, 0xad, 0xc2, 0xe0, 0xec, 0xca, 0xf7, 0x54, 0xf8,
            0x9c, 0x4d, 0xa0, 0x6e, 0x87, 0xcd, 0xae, 0xa8, 0x46, 0x6f, 0x9e,
            0xcb, 0xff, 0x52, 0xb9, 0xaf, 0xdd, 0xb3, 0xb2, 0x74, 0x31, 0xd3, 0xf0, 0x5, 0xbb,
            0x95, 0x1a, 0x85, 0x48, 0x9a, 0x39, 0x11, 0xa2, 0x68, 0x6a, 0x19, 0xe, 0xf3,
            0x5e, 0x27, 0x88, 0xee, 0x5d, 0x15, 0x83, 0xaa, 0xce, 0xb0, 0x1f, 0x49, 0x3, 0x3f,
            0xc9, 0x3a, 0x89, 0xf9, 0x25, 0x80, 0xd0, 0xe4, 0x4c, 0x77, 0x8e, 0x8, 0xa4,
            0xe6, 0x10, 0x5a, 0xf6, 0xcc, 0xf2, 0xf, 0x4e, 0x56, 0xba, 0x8d, 0x32, 0x17, 0x97, 0xf4, 0x0, 0x0,/*20, 126, 220, 248, 171, 0, 5, 10, 99, 79, 41, 11, 196, 43, 211, 88, 102, 157, 22, 85,
            119, 250, 188, 42, 235, 19, 181, 95, 55, 194, 241, 208, 89, 48, 123, 40, 226, 136, 160, 44, 138, 252, 129,
            244, 164, 58, 130, 63, 232, 238, 192, 174, 251, 165, 73, 229, 124, 207, 69, 140, 80, 115, 108, 182, 215,
            135, 142, 46, 109, 72, 93, 254, 219, 64, 149, 83, 227, 105, 101, 183, 121, 106, 71, 15, 170, 143, 94, 213,
            68, 52, 144, 54, 16, 125, 221, 147, 236, 78, 66, 107, 145, 198, 176, 162, 167, 117, 139, 103, 148, 132, 30,
            3, 212, 13, 134, 242, 33, 201, 24, 74, 56, 90, 190, 70, 159, 34, 210, 28, 137, 32, 195, 12, 158, 239, 86, 17,
            206, 245, 37, 91, 233, 180, 53, 35, 104, 218, 116, 203, 156, 216, 8, 29, 18, 247, 113, 184, 21, 2, 100, 199,
            146, 120, 204, 82, 75, 224, 166, 98, 249, 141, 200, 152, 151, 127, 253, 38, 205, 76, 172, 225, 31, 110, 1,
            185, 255, 189, 240, 197, 87, 97, 25, 246, 214, 175, 173, 228, 27, 237, 133, 243, 153, 47, 51, 179, 6, 202, 36,
            230, 84, 26, 9, 62, 231, 191, 59, 60, 67, 209, 161, 61, 81, 163, 4, 112, 150, 131, 128, 111, 57, 50, 154, 217,
            187, 49, 234, 178, 223, 14, 177, 169, 186, 65, 92, 193, 155, 168, 122, 222, 23, 118, 77, 96, 7, 39, 114, 45,
            0, 0*/};

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
