package instancegraph.visitor.print;

import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.visitor.Visitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringifyVisitor implements Visitor {

	private final ByteArrayOutputStream buffer;
	private final PrintVisitor printer;
	
	public StringifyVisitor() {
		this.buffer = new ByteArrayOutputStream();
		this.printer = new PrintVisitor(new PrintStream(buffer));
	}

	@Override
	public void visit(int depth, LeafNode node) {
		printer.visit(depth, node);
	}

	@Override
	public boolean visit(int depth, InnerNode node) {
		return printer.visit(depth, node);
	}
	
	@Override
	public String toString() {
		return new String(buffer.toByteArray());
	}

}
