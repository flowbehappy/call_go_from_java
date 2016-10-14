import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import org.apache.commons.lang.RandomStringUtils;

import test.TestLibrary;


public class TestCallGolang {

    static {
        NativeLibrary.addSearchPath("test", "lib");
    }

    public static String evaluate(String input) throws Exception {
        Pointer pr = TestLibrary.INSTANCE.Parse(GolibUtils.jstr2gstr(input));
        return GolibUtils.gstr2jstr(pr);
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