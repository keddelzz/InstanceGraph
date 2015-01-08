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
