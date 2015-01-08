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

package instancegraph;

import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.node.Node;
import instancegraph.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 
 * @author keddelzz
 */
public final class InstanceGraph {
	private InstanceGraph() {}
	
	public static Node buildGraph(Object object) {
		if (object == null) return new LeafNode(null, null, null, null, 0);
		else if (object.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(object.getClass())) {
			return new LeafNode(null, "", object.getClass(), object, 0);
		}
		else {
			InnerNode root = new InnerNode(null, "", object.getClass(), object, 0);
			Map<Integer, Node> cache = new HashMap<>();
			cache.put(System.identityHashCode(object), root);
			try {
				explode(cache, root);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return root;
		}
	}
	
	private static void explode(Map<Integer, Node> cache, InnerNode current) throws Exception {
		if (current.getInstance() == null) return;
		
		if (current.getType().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(current.getType())) {
			return;
		}
		
		if (current.getType().isArray()) {
			for (int i = 0; i < Array.getLength(current.getInstance()); i++) {
				Object fieldInstance = Array.get(current.getInstance(), i);
				String name = String.format("%s[%d]", current.getName(), i);
				
				if (fieldInstance == null) {
					explodeNull(current, name, current.getType().getComponentType(), 0);
				} else if (fieldInstance.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(fieldInstance.getClass())) {
					explodePrimitive(current, name, fieldInstance.getClass(), fieldInstance, 0);
				} else {
					explodeComplex(current, name, fieldInstance.getClass(), fieldInstance, current::addChild, cache, 0);
				}
			}
		}
		else {
			handleType(current, current.getType(), cache, current::addChild);
			
			//super-types
			List<Class<?>> supers = ReflectionUtils.getSuperclasses(current.getType());
			supers.remove(Object.class);
			
			for (Class<?> type : supers) {
				handleType(current, type, cache, current::addSuperNode);
			}
		}
	}
	
	private static void handleType(InnerNode current, Class<?> type, Map<Integer, Node> cache, Consumer<Node> addTo) throws Exception {
		for (Field field : type.getDeclaredFields()) {
			if (!field.isAccessible()) field.setAccessible(true);
			
			Object fieldInstance = field.get(current.getInstance());
			String name = field.getName();
			
			if (fieldInstance == null) {
				explodeNull(current, name, field.getType(), field.getModifiers());
			} else if (fieldInstance.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(fieldInstance.getClass())) {
				explodePrimitive(current, name, fieldInstance.getClass(), fieldInstance, field.getModifiers());
			} else {
				explodeComplex(current, name, fieldInstance.getClass(), fieldInstance, addTo, cache, field.getModifiers());
			}
		}
	}
	
	private static void explodeNull(InnerNode parent, String name, Class<?> type, int modifier) {
		parent.addChild(new LeafNode(parent, name, type, null, modifier));
	}
	
	private static void explodePrimitive(InnerNode parent, String name, Class<?> type, Object instance, int modifier) {
		parent.addChild(new LeafNode(parent, name, type, instance, modifier));
	}
	
	private static void explodeComplex(InnerNode parent, String name, Class<?> type, Object instance, Consumer<Node> addTo, Map<Integer, Node> cache, int modifier) throws Exception {
		int hashCode = System.identityHashCode(instance);
		
		if (!cache.containsKey(hashCode)) {
			InnerNode child = new InnerNode(parent, name, type, instance, modifier);
			addTo.accept(child);
			cache.put(hashCode, child);
			explode(cache, child);
		}
		else {
			addTo.accept(cache.get(hashCode));
		}
	}
}
