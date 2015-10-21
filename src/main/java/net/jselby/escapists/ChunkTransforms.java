package net.jselby.escapists;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Static methods to help with Chunk transformations.
 */
public class ChunkTransforms {
    public static final int TRANS_START = 99;

    // Generated from create_transform(), fed through C function
    public static final byte[] key = {-14, -30, 48, 99, 104, -24, -76, 16, -19, -101, 4, 68, 114, -2, -102, 39, -44, -85,
            7, 66, 116, -3, -102, 55, -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 71, -39, -82, 6, -12,
            -85, 15, 87, 107, -28, -75, 23, -62, -121, 85, 105, 81, -8, -88, 6, -18, -23, 89, 24, 118, -21, -89, 55,
            -39, -67, 76, 115, 104, -18, -89, 19, -40, -85, 24, 69, 41, -67, -9, 86, -111, -107, 3, 67, 119, -23, -65,
            67, -27, -73, 3, 80, 59, -34, -78, 22, -43, -79, 3, 69, 27, -6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int[] key2 = {20, 126, 220, 248, 171, 0, 5, 10, 99, 79, 41, 11, 196, 43, 211, 88, 102, 157, 22, 85,
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
            0, 0};

    static {
        System.out.println(key.length);
    }
    /*static {
        try {
            FileInputStream in = new FileInputStream("keydump");
            key = IOUtils.toByteArray(in);
            in.close();
            System.out.println("Key length: " + key.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(key));
    }*/

    private static int[] z(int[] a, byte[] b, byte c){
        int d;
        byte e;
        byte[] f;
        int g;
        int h;
        char i;
        int j;
        int k;
        int l;
        byte m;
        byte n=c;

        for(int o=0; o<256; o++) {
            a[o]=o;
        }
        d=0;
        k=1;
        e=c;
        a[256]=0;
        a[257]=0;
        f=b;
        g=0;
        m=0;
        l=0;
        do{
            e= (byte) ((e<<7)+(e>>1));
            h=k;
            if(k > 0) {
                n+=((e&1)+2)*f[g];
                h=k;
            }
            i= (char) (e^f[g]);
            if(e==f[g]){
                if(h > 0)
                    d=n==f[g+1] ? 1 : 0;
                e= (byte) ((c>>1)+(c<<7));
                g=0;
                k=0;

                i = (char) (e ^ f[0]);
                //i=e^*f;
            }
            j=a[l];
            m= (byte) (i+j+m);
            a[l]=a[m & 0xFF];
            ++g;
            a[m & 0xFF]=j;
            f=b;
            ++l;
        } while(l<256);
        return a;
    }

    private static byte[] transform(byte[] a, int b, byte[] c) {
        int[] d = new int[key2.length];
        
        //IntBuffer buffer = IntBuffer.wrap(d);
        ///Chunk_cmLibrary.z(buffer, ByteBuffer.wrap(c), (byte) TRANS_START);
        //d = buffer.array();
        //System.out.println(Arrays.toString(d));
        //z(d, c, (byte) TRANS_START

        System.arraycopy(key2, 0, d, 0, key2.length);

        int[] e;
        int f;
        int g;
        int h;
        int i;
        int j;
        int k;
        int l;
        e=d;
        f=d[256];
        g=d[257];
        h=0;

        if(b<=0){
            d[256]=f;
            d[257]=g;
        } else {
            while(true) {
                f=(byte)(f+1);
                ++h;
                i=e[f & 0xFF];
                j=(byte)(i+g);
                l=j;
                k=e[j & 0xFF];
                e[f & 0xFF]=k;
                e[l & 0xFF]=i;
                a[h-1]^=e[(byte)(i+k) & 0xFF]&0xFF;
                if(h>=b)
                    break;
                g=l&0xFF;
            }
            e[257]=l;
            e[256]=f;
        }

        return a;
    }

    public static byte[] transform(byte[] data) {
        // transform(unsigned char * data, int size, unsigned char * trans)
        //ByteBuffer buf = ByteBuffer.wrap(data);
        /*Chunk_cmLibrary.*/return transform(data, data.length, key);
        //return buf.array();
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
