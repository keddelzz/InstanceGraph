package instancegraph.visitor.leafcount;

import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.node.Node;
import instancegraph.visitor.Visitor;

import java.util.HashSet;
import java.util.Set;

public class LeafCountVisitor implements Visitor {

	private int count;
	private final Set<Node> visited;
	
	public LeafCountVisitor() {
		this.visited = new HashSet<>();
	}
	
	@Override
	public void visit(int depth, LeafNode node) {
		count++;
	}

	@Override
	public boolean visit(int depth, InnerNode node) {
		if (!visited.contains(node)) {
			visited.add(node);
			return true;
		}
		else return false;
	}
	
	public int getCount() {
		return count;
	}

}
