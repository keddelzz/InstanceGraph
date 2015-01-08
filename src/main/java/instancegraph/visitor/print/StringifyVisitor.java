 /*
 * This file is part of InstanceGraph.
 * 
 * InstanceGraph is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * InstanceGraph is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * InstanceGraph. If not, see <http://www.gnu.org/licenses/>.
 */

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
