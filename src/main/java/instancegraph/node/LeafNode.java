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
