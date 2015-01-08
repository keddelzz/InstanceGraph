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
