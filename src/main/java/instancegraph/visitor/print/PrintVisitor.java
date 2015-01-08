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
import instancegraph.node.Node;
import instancegraph.util.ReflectionUtils;
import instancegraph.visitor.Visitor;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class PrintVisitor implements Visitor {
	
	private final PrintStream out;
	private String ident;
	private final Set<Node> visited;
	
	public PrintVisitor(PrintStream out) {
		this.out = out;
		this.ident = "    ";
		this.visited = new HashSet<>();
	}

	@Override
	public void visit(int depth, LeafNode node) {
		Object instance = node.getInstance();
		if (instance == null) {
			if (node.getName() == null || node.getName().isEmpty()) {
			}
			else {
				makeIdent(depth);
				if (node.getType() == null) {
					out.println(String.format("%s = null", node.getName()));
				}
				else {
					out.println(String.format("%s %s = null", ReflectionUtils.readableTypeOf(node.getType()), node.getName()));
				}
			}
		}
		else {
			Class<?> clazz = node.getType();
			
			if (clazz.isPrimitive() || ReflectionUtils.isPrimitiveWrapper(clazz)) {
				makeIdent(depth);
				if (node.getName() == null || node.getName().isEmpty()) {
					out.println(String.format("(%s): %s", ReflectionUtils.readableTypeOf(clazz), instance));
				}
				else {
					out.println(String.format("%s %s = %s", ReflectionUtils.readableTypeOf(clazz), node.getName(), instance));
				}
			}
		}
	
	}
	
	@Override
	public boolean visit(int depth, InnerNode node) {
		if (!visited.contains(node)) {
			
			makeIdent(depth);
			Object instance = node.getInstance();
			if (node.getName() == null || node.getName().isEmpty()) {
				out.println(ReflectionUtils.readableTypeOf(instance));
				Class<?> supah = node.getType();
				
				while ((supah = supah.getSuperclass()) != null) {
					makeIdent(depth + 1);
					out.println(String.format("extends %s", supah.getName()));
				}
				out.println();
			}
			else {
				out.println(String.format("%s %s =", ReflectionUtils.readableTypeOf(instance), node.getName()));
			}
			visited.add(node);
			return true;
		}
		else return false;
	}
	
	private void makeIdent(int depth) {
		out.print(new String(new char[depth]).replaceAll("\0", ident));
	}

	public void setIdent(String ident) {
		if (ident == null) return;
		this.ident = ident;
	}

}
