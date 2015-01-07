package instancegraph.visitor;

import instancegraph.node.AbstractNode;
import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.node.Node;

/**
 * Visitor-Interface for iterating through the nodes of the instance-graph.
 * @author keddelzz
 */
public interface Visitor {

	/**
	 * Visit-Method for {@link LeafNode}s
	 * @param depth the depth of the node
	 * @param node the {@link LeafNode}
	 */
	public void visit(int depth, LeafNode node);
	
	/**
	 * Visit-Method for {@link InnerNode}s
	 * @param depth the depth of the node
	 * @param node the {@link InnerNode}
	 * @return Returns true if the children-nodes should be visited
	 */
	public boolean visit(int depth, InnerNode node);

	/**
	 * Reroutes the super-types back to the visit-methods
	 * @param depth the depth of the node
	 * @param node the note to reroute
	 */
	default void rerouteSuperTypes(int depth, AbstractNode node) {
		for (Node supah : node.superNodes()) {
			supah.accept(depth + 1, this);
		}
	}
}
