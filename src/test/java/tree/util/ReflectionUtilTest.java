package tree.util;

import static instancegraph.util.ReflectionUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.junit.Test;

public class ReflectionUtilTest {

	@Test
	public void testPrimitiveWrapper() {
		assertTrue(isPrimitiveWrapper(Boolean.class));
		assertTrue(isPrimitiveWrapper(Byte.class));
		assertTrue(isPrimitiveWrapper(Character.class));
		assertTrue(isPrimitiveWrapper(Double.class));
		assertTrue(isPrimitiveWrapper(Float.class));
		assertTrue(isPrimitiveWrapper(Integer.class));
		assertTrue(isPrimitiveWrapper(Short.class));
		assertTrue(isPrimitiveWrapper(Long.class));
		
		assertFalse(isPrimitiveWrapper(ReflectionUtilTest.class));
		assertFalse(isPrimitiveWrapper(Object.class));
		assertFalse(isPrimitiveWrapper(String.class));
		assertFalse(isPrimitiveWrapper(List.class));
		assertFalse(isPrimitiveWrapper(StringBuilder.class));
	}
	
	@Test
	public void testDisplayClassName() {
		assertEquals("", "boolean", getDisplayClassName(boolean.class));
		assertEquals("", "boolean", getDisplayClassName(Boolean.class));
		
		assertEquals("", "byte", getDisplayClassName(byte.class));
		assertEquals("", "byte", getDisplayClassName(Byte.class));
		
		assertEquals("", "char", getDisplayClassName(char.class));
		assertEquals("", "char", getDisplayClassName(Character.class));
		
		assertEquals("", "double", getDisplayClassName(double.class));
		assertEquals("", "double", getDisplayClassName(Double.class));
		
		assertEquals("", "float", getDisplayClassName(float.class));
		assertEquals("", "float", getDisplayClassName(Float.class));
		
		assertEquals("", "int", getDisplayClassName(int.class));
		assertEquals("", "int", getDisplayClassName(Integer.class));
		
		assertEquals("", "short", getDisplayClassName(short.class));
		assertEquals("", "short", getDisplayClassName(Short.class));
		
		assertEquals("", "long", getDisplayClassName(long.class));
		assertEquals("", "long", getDisplayClassName(Long.class));
		
		
		assertEquals("", "tree.util.ReflectionUtilTest", getDisplayClassName(ReflectionUtilTest.class));
		assertEquals("", "java.lang.Object", getDisplayClassName(Object.class));
		assertEquals("", "java.lang.String", getDisplayClassName(String.class));
		assertEquals("", "java.util.List", getDisplayClassName(List.class));
		assertEquals("", "java.lang.StringBuilder", getDisplayClassName(StringBuilder.class));
	}
	
	@Test
	public void testSuperClasses() {
		assertEquals(Arrays.asList(), getSuperclasses(Object.class));
		assertEquals(Arrays.asList(Object.class), getSuperclasses(String.class));

		assertEquals("", Arrays.asList(Frame.class, Window.class, Container.class, Component.class, Object.class), getSuperclasses(JFrame.class));
		assertEquals("", Arrays.asList(Object.class), getSuperclasses(int[].class));
	}
	
	@Test
	public void testReadableTypeOf() {
		assertEquals("", "null", readableTypeOf(null));
		
		assertEquals("", "boolean", readableTypeOf(boolean.class));
		assertEquals("", "boolean", readableTypeOf(Boolean.class));
		
		assertEquals("", "byte", readableTypeOf(byte.class));
		assertEquals("", "byte", readableTypeOf(Byte.class));
		
		assertEquals("", "char", readableTypeOf(char.class));
		assertEquals("", "char", readableTypeOf(Character.class));
		
		assertEquals("", "double", readableTypeOf(double.class));
		assertEquals("", "double", readableTypeOf(Double.class));
		
		assertEquals("", "float", readableTypeOf(float.class));
		assertEquals("", "float", readableTypeOf(Float.class));
		
		assertEquals("", "int", readableTypeOf(int.class));
		assertEquals("", "int", readableTypeOf(Integer.class));
		
		assertEquals("", "short", readableTypeOf(short.class));
		assertEquals("", "short", readableTypeOf(Short.class));
		
		assertEquals("", "long", readableTypeOf(long.class));
		assertEquals("", "long", readableTypeOf(Long.class));
		
		assertEquals("", "tree.util.ReflectionUtilTest", readableTypeOf(ReflectionUtilTest.class));
		assertEquals("", "java.lang.Object", readableTypeOf(Object.class));
		assertEquals("", "java.lang.String", readableTypeOf(String.class));
		assertEquals("", "java.util.List", readableTypeOf(List.class));
		assertEquals("", "java.lang.StringBuilder", readableTypeOf(StringBuilder.class));
		
		assertEquals("", "byte[]", readableTypeOf(byte[].class));
		assertEquals("", "byte[][]", readableTypeOf(byte[][].class));
		assertEquals("", "byte[][][]", readableTypeOf(byte[][][].class));
		assertEquals("", "byte[][][][]", readableTypeOf(byte[][][][].class));
		assertEquals("", "byte[][][][][]", readableTypeOf(byte[][][][][].class));
		
		assertEquals("", "java.lang.StringBuilder[]", readableTypeOf(StringBuilder[].class));
		assertEquals("", "java.lang.StringBuilder[][]", readableTypeOf(StringBuilder[][].class));
		assertEquals("", "java.lang.StringBuilder[][][]", readableTypeOf(StringBuilder[][][].class));
		assertEquals("", "java.lang.StringBuilder[][][][]", readableTypeOf(StringBuilder[][][][].class));
		assertEquals("", "java.lang.StringBuilder[][][][][]", readableTypeOf(StringBuilder[][][][][].class));
	}
	
}
