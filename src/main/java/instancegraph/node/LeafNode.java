package instancegraph.node;

import instancegraph.visitor.Visitor;

import java.util.List;

public final class LeafNode extends AbstractNode {

	public LeafNode(Node parent, String name, Class<?> type, Object instance, int modifier) {
		super(parent, name, type, instance, modifier);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public List<Node> children() {
		throw new RuntimeException("LeafNode.children()");
	}

	@Override
	public void accept(int depth, Visitor visitor) {
		visitor.visit(depth, this);
		visitor.rerouteSuperTypes(depth, this);
	}
	
}
