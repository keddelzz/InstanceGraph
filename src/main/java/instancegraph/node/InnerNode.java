package instancegraph.node;

import instancegraph.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InnerNode extends AbstractNode {

	private final List<Node> children;
	
	public InnerNode(Node parent, List<Node> children, String name, Class<?> type, Object instance, int modifier) {
		super(parent, name, type, instance, modifier);
		this.children = children;
	}
	
	public InnerNode(Node parent, String name, Class<?> type, Object instance, int modifier) {
		this(parent, new ArrayList<>(), name, type, instance, modifier);
	}

	public void addChild(Node child) {
		if (!children.contains(child)) {
			children.add(child);
		}
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public List<Node> children() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public void accept(int depth, Visitor visitor) {
		if (visitor.visit(depth, this)) {
			for (Node child : children) {
				child.accept(depth + 1, visitor);
			}	
			visitor.rerouteSuperTypes(depth, this);
		}
	}

}
