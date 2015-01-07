package instancegraph.visitor.search;

import instancegraph.node.Node;

import java.util.Set;

public interface SearchResult {

	public Node getNode();
	
	public Set<ResultType> getResultTypes();
	
}
