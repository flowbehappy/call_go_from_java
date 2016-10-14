package test;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : src/main/lib/libtest.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class GoString extends Structure {
	/** C type : const char* */
	public Pointer p;
	/** C type : GoInt */
	public long n;
	public GoString() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("p", "n");
	}
	/**
	 * @param p C type : const char*<br>
	 * @param n C type : GoInt
	 */
	public GoString(Pointer p, long n) {
		super();
		this.p = p;
		this.n = n;
	}
	public GoString(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends GoString implements Structure.ByReference {
		
	};
	public static class ByValue extends GoString implements Structure.ByValue {
		
	};
}
