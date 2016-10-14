import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import org.apache.commons.lang.RandomStringUtils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import test.GoString;
import test.TestLibrary;


public class ParseLogs {


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

    public static long getAddress(ByteBuffer buffer) {
        assert buffer.getClass() == DIRECT_BYTE_BUFFER_CLASS;
        return unsafe.getLong(buffer, DIRECT_BYTE_BUFFER_ADDRESS_OFFSET);
    }

    static {
        NativeLibrary.addSearchPath("test", "lib");
    }

    public static String evaluate(String input) throws Exception {
        byte[] bytes = input.getBytes("utf-8");
        ByteBuffer bb = ByteBuffer.allocateDirect(bytes.length);
        bb.put(bytes);

        Pointer p = new Pointer(getAddress(bb));
        GoString.ByValue value = new GoString.ByValue();
        value.n = bytes.length;
        value.p = p;
        GoString.ByValue rt = TestLibrary.INSTANCE.Parse(value);
        byte[] rtBytes = new byte[(int) rt.n];
        rt.p.read(0, rtBytes, 0, rtBytes.length);

        return new String(rtBytes, "utf-8");
    }


    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            String s = RandomStringUtils.random(i);
            if (!s.equals(evaluate(s))) {
                System.out.println("fail: " + i + " , s :" + s);
                break;
            }
        }
        System.out.println(evaluate("fffffuck!"));
    }
}