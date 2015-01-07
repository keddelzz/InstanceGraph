package instancegraph.node;

import instancegraph.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractNode implements Node {

	private final Node parent;
	
	private final String name;
	private final Class<?> type;
	private final Object instance;
	private final int modifier;
	
	private final List<Node> superNodes;
	
	public AbstractNode(Node parent, String name, Class<?> type, Object instance, int modifier) {
		this.name = name;
		this.instance = instance;
		this.parent = parent;
		this.superNodes = new ArrayList<>();
		this.type = type;
		this.modifier = modifier;
	}
	
	@Override
	public Object getInstance() {
		return instance;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Node parent() {
		return parent;
	}
	
	@Override
	public void addSuperNode(Node node) {
		superNodes.add(node);
	}
	
	@Override
	public int getModifiers() {
		return modifier;
	}
	
	@Override
	public List<Node> superNodes() {
		return Collections.unmodifiableList(superNodes);
	}
	
	@Override
	public Class<?> getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s = %s", ReflectionUtils.readableTypeOf(getInstance()), getName(), printInstance(getInstance())).trim();
	}
	
	private String printInstance(Object instance) {
		if (instance == null) return "null";
		else if (instance.getClass().isArray()) {
			StringBuilder builder = new StringBuilder("[");
			
			final int len = Array.getLength(instance);
			for (int i = 0; i < len; i++) {
				builder.append(printInstance(Array.get(instance, i)));
				if (i < len - 1) {
					builder.append(", ");
				}
			}
			
			builder.append("]");
			return builder.toString();
		}
		else return instance.toString();
	}
}
