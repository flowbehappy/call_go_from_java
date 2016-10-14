import com.sun.jna.Pointer;

import sun.misc.Unsafe;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import test.GoString;

public class GolibUtils {
    public static final Unsafe unsafe;
    private static final Class<?> DIRECT_BYTE_BUFFER_CLASS;
    private static final long DIRECT_BYTE_BUFFER_ADDRESS_OFFSET;

    static {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
            Class<?> clazz = ByteBuffer.allocateDirect(0).getClass();
            DIRECT_BYTE_BUFFER_ADDRESS_OFFSET = unsafe.objectFieldOffset(Buffer.class.getDeclaredField("address"));
            DIRECT_BYTE_BUFFER_CLASS = clazz;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static long getAddress(ByteBuffer buffer) {
        assert buffer.getClass() == DIRECT_BYTE_BUFFER_CLASS;
        return unsafe.getLong(buffer, DIRECT_BYTE_BUFFER_ADDRESS_OFFSET);
    }

    public static GoString.ByValue jstr2gstr(String jstr) {
        try {
            byte[] bytes = jstr.getBytes("utf-8");
            //ByteBuffer bb = ByteBuffer.wrap(bytes);
            ByteBuffer bb = ByteBuffer.allocateDirect(bytes.length);
            bb.put(bytes);
            Pointer p = new Pointer(getAddress(bb));
            GoString.ByValue value = new GoString.ByValue();
            value.n = bytes.length;
            value.p = p;
            return value;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String gstr2jstr(GoString.ByValue gstr) {
        try {
            byte[] bytes = new byte[(int) gstr.n];
            gstr.p.read(0, bytes, 0, bytes.length);
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
