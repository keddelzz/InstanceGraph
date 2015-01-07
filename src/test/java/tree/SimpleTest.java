package tree;

import instancegraph.InstanceGraph;
import instancegraph.node.Node;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleTest {
	
	public static final String TEST_TOSTRING_OUTPUT = 
			 ("tree.SimpleTest$A\n"
			+ "    extends java.lang.Object\n"
			+ "\n"
			+ "    java.lang.String title =\n"
			+ "        char[] value =\n"
			+ "            char value[0] = T\n"
			+ "            char value[1] = e\n"
			+ "            char value[2] = s\n"
			+ "            char value[3] = t\n"
			+ "        int hash = 0\n"
			+ "        long serialVersionUID = -6849794470754667710"
			+ "        java.io.ObjectStreamField[] serialPersistentFields ="
			+ "        java.lang.String$CaseInsensitiveComparator CASE_INSENSITIVE_ORDER ="
			+ "            long serialVersionUID = 8575799808933029326\n"
			+ "\n").replaceAll("\\s+", "");
	
	static class A {
		String title;
	}
	
	private Node root;
	private String nodeString;
	
	@Before
	public void init() {
		A a = new A();
		a.title = "Test";
		
		root = InstanceGraph.buildGraph(a);
		nodeString = root.asString().replaceAll("\\s+", "");
	}
	
	@Test
	public void testToString() {
		assertEquals("", TEST_TOSTRING_OUTPUT, nodeString);
		assertEquals(TEST_TOSTRING_OUTPUT.hashCode(), nodeString.hashCode());
	}
	
	@Test
	public void testLeafCount() {
		// 4 letters + hashCode from String
		assertEquals(7, root.leafCount());
	}

}
