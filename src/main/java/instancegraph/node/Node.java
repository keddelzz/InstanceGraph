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
import instancegraph.visitor.leafcount.LeafCountVisitor;
import instancegraph.visitor.print.PrintVisitor;
import instancegraph.visitor.print.StringifyVisitor;
import instancegraph.visitor.search.SearchResult;
import instancegraph.visitor.search.SearchVisitor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public interface Node {
		
	public boolean isLeaf();
	
	default boolean isInnerNode() {
		return !isLeaf();
	}
	
	public List<Node> children();
	
	public Object getInstance();
	
	public Node parent();
	
	public String getName();
	
	public int getModifiers();
	
	public void addSuperNode(Node node);
	
	public List<Node> superNodes();
	
	public Class<?> getType();
	
	default void accept(Visitor visitor) {
		accept(0, visitor);
	}
	
	public void accept(int depth, Visitor visitor);
	
	default List<String> getPath() {
		List<String> path = new ArrayList<>();
		
		Node part = this;
		while ((part.parent() != null)) {
			path.add(part.getName());
			part = part.parent();
		}
		
		Collections.reverse(path);
		
		return path;
	}
	
	default List<SearchResult> search(String string) {
		SearchVisitor search = new SearchVisitor(string);
		accept(search);
		return search.getResult();
	}
	
	default List<SearchResult> search(Pattern pattern) {
		SearchVisitor search = new SearchVisitor(pattern);
		accept(search);
		return search.getResult();
	}
	
	default int leafCount() {
		LeafCountVisitor counter = new LeafCountVisitor();
		accept(counter);
		return counter.getCount();
	}
	
	default void print() {
		print(System.out);
	}
	
	default void print(PrintStream printStream) {
		PrintVisitor printer = new PrintVisitor(printStream);
		accept(printer);
	}
	
	default String asString() {
		StringifyVisitor stringify = new StringifyVisitor();
		accept(stringify);
		return stringify.toString();
	}
	
	default boolean contains(Node node) {
		if (isLeaf()) return false;
		else for (Node child : children()) {
			if (child == node) return true;
		}
		return false;
	}
	
	default Optional<Node> browse(List<String> path) {
		Node node = this;
		int counter = 0;
		
		outer: while (counter < path.size()) {
			String pathElement = path.get(counter++);
			if (isInnerNode()) {
				for (Node child : node.children()) {
					if (child.getName().equals(pathElement)) {
						node = child;
						continue outer;
					}
				}
				for (Node supah : node.superNodes()) {
					if (supah.getName().equals(pathElement)) {
						node = supah;
						continue outer;
					}
				}
				return Optional.empty();
			}
			else {
				return Optional.empty();
			}
		}
		
		return Optional.of(node);
	}
	
}
