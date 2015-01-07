package tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import instancegraph.InstanceGraph;
import instancegraph.node.Node;

import java.util.Arrays;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

public class BrowseTest {

	private static final String TITLE = "Hello World!";
	private Node root;
	
	@Before
	public void init() {
		root = InstanceGraph.buildGraph(new JFrame(TITLE));
	}
	
	@Test
	public void testSimpleBrowse() {
		Optional<Node> maybeNode = root.browse(Arrays.asList("title"));
		assertTrue(maybeNode.isPresent());
		
		Node node = maybeNode.get();
		assertEquals(TITLE, node.getInstance());
	}
	
	@Test
	public void testBrowse() {
		Optional<Node> maybeNode = root.browse(Arrays.asList("rootPane", "glassPane"));
		assertTrue(maybeNode.isPresent());
		
		Node node = maybeNode.get();
		assertEquals(JPanel.class, node.getType());
	}
	
	@Test
	public void testEmptyPath() {
		Optional<Node> maybeNode = root.browse(Arrays.asList());
		assertTrue(maybeNode.isPresent());
		assertEquals(root, maybeNode.get());
	}
	
}
