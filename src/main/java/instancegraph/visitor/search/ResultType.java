package instancegraph.visitor.search;

public enum ResultType {
	FIELD_NAME("field name"),
	NODE_TOSTRING("node.toString"),
	CLASS_NAME("class name"),
	HASH_CODE("hashCode"),
	HASH_CODE_HEX("hashCode as hexadezimal number");
	
	private final String description;
	ResultType(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
