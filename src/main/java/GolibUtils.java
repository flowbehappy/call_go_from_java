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

    // "len|content"
    public static String gstr2jstr(Pointer gstr) {
        try {
            int len = -1;
            for (int i = 0; i < 10; i++) {
                if (gstr.getByte(i) == '|') {
                    len = i;
                    break;
                }
            }
            if (len == -1) {
                return null;
            }
            byte[] lenStr = new byte[len];
            gstr.read(0, lenStr, 0, len);
            int contentLen = Integer.parseInt(new String(lenStr, "utf-8"));
            if (contentLen < 0 || contentLen > 1000000) {
                // illegal len, something went wrong.
                return null;
            }
            byte[] content = new byte[contentLen];
            gstr.read(len + 1, content, 0, contentLen);
            return new String(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            unsafe.freeMemory(Pointer.nativeValue(gstr));
        }
    }
}
