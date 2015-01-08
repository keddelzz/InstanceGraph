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

package instancegraph.visitor.search;

import static instancegraph.visitor.search.ResultType.CLASS_NAME;
import static instancegraph.visitor.search.ResultType.FIELD_NAME;
import static instancegraph.visitor.search.ResultType.HASH_CODE;
import static instancegraph.visitor.search.ResultType.HASH_CODE_HEX;
import static instancegraph.visitor.search.ResultType.NODE_TOSTRING;
import instancegraph.node.AbstractNode;
import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.node.Node;
import instancegraph.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class SearchVisitor implements Visitor {

	private final Pattern pattern;
	private final List<SearchResultImpl> result;
	private final Set<Node> visited;
	
	public SearchVisitor(Pattern pattern) {
		this.pattern = Objects.requireNonNull(pattern, "pattern == null");
		this.result = new ArrayList<>();
		this.visited = new HashSet<>();
	}
	
	public SearchVisitor(String string) {
		this(Pattern.compile(Pattern.quote(string == null ? "null" : string)));
	}
	
	@Override
	public void visit(int depth, LeafNode node) {
		visitGeneral(depth, node);
	}

	@Override
	public boolean visit(int depth, InnerNode node) {
		return visitGeneral(depth, node);
	}
	
	private boolean visitGeneral(int depth, AbstractNode node) {
		if (!visited.contains(node)) {
			
			// Name of the field
			if (matches(node.getName())) {
				addResult(node, FIELD_NAME);
			}

			// Node.toString
			if (matches(node.getInstance() == null ? "null" : node.getInstance().toString())) {
				addResult(node, NODE_TOSTRING);
			}

			// Class-Name
			if (matches(node.getType().getName())) {
				addResult(node, CLASS_NAME);
			}

			// Hash-Code
			if (node.getInstance() != null) {
				int hash = System.identityHashCode(node.getInstance());
				if (matches(String.format("%d", hash))) {
					addResult(node, HASH_CODE);
				}
				if (matches(String.format("%x", hash))) {
					addResult(node, HASH_CODE_HEX);
				}
			}
			
			visited.add(node);
			return true;
		}
		else return false;
	}
	
	private boolean matches(String string) {
		return pattern.matcher(string).matches();
	}
	
	private void addResult(Node node, ResultType type) {
		for (SearchResultImpl res : result) {
			// if node exists
			if (res.getNode().equals(node)) {
				res.matches.add(type);
				return;
			}
		}
		
		// if node not exist
		SearchResultImpl res = new SearchResultImpl(node);
		res.matches.add(type);
		result.add(res);
	}

	public List<SearchResult> getResult() {
		return Collections.unmodifiableList(result);
	}
	
	static class SearchResultImpl implements SearchResult {
		private final Node node;
		private final Set<ResultType> matches;
		
		public SearchResultImpl(Node node) {
			this.node = node;
			this.matches = new HashSet<>();
		}
		
		@Override
		public Node getNode() {
			return node;
		}

		@Override
		public Set<ResultType> getResultTypes() {
			return matches;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (matches.isEmpty()) {
				builder.append("nothing");
			}
			else if (matches.size() == 1) {
				for (ResultType match : matches) {
					builder.append(match);
				}
			}
			else {
				Iterator<ResultType> iter = matches.iterator();
				int ctr = 0;
				while (iter.hasNext()) {
					ResultType match = iter.next();
					ctr++;
					builder.append(match);
					
					if (iter.hasNext()) {
						if (ctr + 1 == matches.size()) {
							builder.append(" and ");
						}
						else {
							builder.append(", ");
						}
					}
				}
			}
			
			return String.format("matched \"%s\"; matched on %s", node.toString(), builder);
		}
	}

}
