package tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import instancegraph.InstanceGraph;
import instancegraph.node.Node;
import instancegraph.visitor.search.ResultType;
import instancegraph.visitor.search.SearchResult;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

public class SearchTest {

	private Node root;
	private static final String SEARCH_1 = "This is a test-title";
	private static final Pattern SEARCH_2 = Pattern.compile(".*thread.*");
	private static final String SEARCH_3 = String.class.getName();
	
	@Before
	public void init() {
		root = InstanceGraph.buildGraph(new JFrame(SEARCH_1));
	}
	
//	@Test
	public void testSimplePath() {
		List<SearchResult> resultList = root.search(SEARCH_1);
		assertEquals(1, resultList.size());
		
		SearchResult result = resultList.get(0);
		assertEquals(new HashSet<>(Arrays.asList(ResultType.NODE_TOSTRING)), result.getResultTypes());
		
		assertEquals(Arrays.asList("title"), result.getNode().getPath());
	}
	
	@Test
	public void testCompexPath() throws Exception {
		List<SearchResult> resultList = root.search(SEARCH_2);
		
		Class<?> nativeThreadSet = Class.forName("sun.nio.ch.NativeThreadSet");
		
		List<SearchResult> filteredList = resultList.stream()
			.filter(result -> result.getNode().getName().equals("threads"))
			.filter(result -> !nativeThreadSet.isInstance(result))
			.collect(Collectors.toList());
		
		if (filteredList.size() > 2)
			System.out.println(nativeThreadSet.isInstance(filteredList.get(2)));
				
		assertEquals(2, filteredList.size());
		
		for (SearchResult res : filteredList) {
			assertEquals(new HashSet<>(Arrays.asList(ResultType.FIELD_NAME)), res.getResultTypes());
		}
		
		List<List<String>> paths = filteredList.stream()
				.map(result -> result.getNode().getPath())
				.sorted((a, b) -> a.toString().compareTo(b.toString()))
				.collect(Collectors.toList());
		
		Optional<Node> maybeTable = root.browse(Arrays.asList("rootPane", "contentPane", "parent", "appContext", "threadGroup2appContext", "m", "table"));
		assertTrue(maybeTable.isPresent());
		
		Node table = maybeTable.get();
		Set<List<String>> tableChildPaths = table.children().stream()
				.map(child -> child.getPath())
				.collect(Collectors.toSet());
		
		paths.stream()
			.filter(path -> tableChildPaths.contains(path))
			.forEach(System.out::println);
		
		assertEquals(Arrays.asList("rootPane", "contentPane", "parent", "appContext", "threadGroup", "groups", "groups[0]", "threads"), paths.get(0));
		assertEquals(Arrays.asList("rootPane", "contentPane", "parent", "appContext", "threadGroup", "threads"), paths.get(1));
	}
	
//	@Test
	public void testFindAllNonNullStrings() {
		List<SearchResult> resultList = root.search(SEARCH_3);
		
		List<SearchResult> result = resultList.stream()
			.filter(res -> !Modifier.isStatic(res.getNode().getModifiers()))
			.filter(res -> res.getNode().getInstance() != null)
			.filter(res -> res.getResultTypes().equals(new HashSet<>(Arrays.asList(ResultType.CLASS_NAME))))
			.sorted((a, b) -> a.toString().compareTo(b.toString()))
			.collect(Collectors.toList());
		
		assertEquals(554, result.size());
	}
		
}
