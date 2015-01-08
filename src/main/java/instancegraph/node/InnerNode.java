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
