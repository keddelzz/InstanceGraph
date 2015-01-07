package instancegraph;

import instancegraph.node.InnerNode;
import instancegraph.node.LeafNode;
import instancegraph.node.Node;
import instancegraph.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InstanceView {
	private InstanceView() {}
	
	public Node buildTree(Object object) {
		if (object == null) return null;
		try {
			if (object.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(object.getClass())) {
				return new LeafNode(null, "", object.getClass(), object, 0);
			}
			else {
				InnerNode root = new InnerNode(null, "", object.getClass(), object, 0);
				Map<Integer, Node> cache = new HashMap<>();
				cache.put(System.identityHashCode(object), root);
				explode(cache, root);
				
				return root;				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void explode(Map<Integer, Node> cache, InnerNode current) throws Exception {
		if (current.getInstance() == null) return;
		
		if (current.getType().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(current.getType())) {
			return;
		}
		
		if (current.getType().isArray()) {
			int len = Array.getLength(current.getInstance());
			for (int i = 0; i < len; i++) {
				Object fieldInstance = Array.get(current.getInstance(), i);
				String name = String.format("%s[%d]", current.getName(), i);
				
				Node child = null;
				if (fieldInstance == null) {
					child = new LeafNode(current, name, current.getType().getComponentType(), null, 0);
				} else if (fieldInstance.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(fieldInstance.getClass())) {
					child = new LeafNode(current, name, fieldInstance.getClass(), fieldInstance, 0);
				} else {
					int hashCode = System.identityHashCode(fieldInstance);
					
					if (!cache.containsKey(hashCode)) {
						child = new InnerNode(current, name, fieldInstance.getClass(), fieldInstance, 0);
						cache.put(hashCode, child);
						explode(cache, (InnerNode) child);
					}
					else {
						child = cache.get(hashCode);
					}
				}
				current.addChild(child);
			}
		}
		else {
			for (Field field : current.getType().getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) continue;
				if (!field.isAccessible()) field.setAccessible(true);
				
				Object fieldInstance = field.get(current.getInstance());
				
				Node child = null;
				
				if (fieldInstance == null) {
					child = new LeafNode(current, field.getName(), field.getType(), null, field.getModifiers());
				} else if (fieldInstance.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(fieldInstance.getClass())) {
					child = new LeafNode(current, field.getName(), field.getType(), fieldInstance, field.getModifiers());
				} else {
					int hashCode = System.identityHashCode(fieldInstance);
					
					if (!cache.containsKey(hashCode)) {
						child = new InnerNode(current, field.getName(), field.getType(), fieldInstance, field.getModifiers());
						cache.put(hashCode, child);
						explode(cache, (InnerNode) child);
					}
					else {
						child = cache.get(hashCode);
					}
				}
				
				current.addChild(child);
			}
			
			List<Class<?>> supers = ReflectionUtils.getSuperclasses(current.getType());
			supers.remove(Object.class);
			
			for (Class<?> supah : supers) {
				for (Field field : supah.getDeclaredFields()) {
					if (Modifier.isStatic(field.getModifiers())) continue;
					if (!field.isAccessible()) field.setAccessible(true);
					
					Object fieldInstance = field.get(current.getInstance());
					
					Node child = null;
					
					if (fieldInstance == null) {
						child = new LeafNode(current, field.getName(), supah, null, field.getModifiers());
					} else if (fieldInstance.getClass().isPrimitive() || ReflectionUtils.isPrimitiveWrapper(fieldInstance.getClass())) {
						child = new LeafNode(current, field.getName(), field.getType(), fieldInstance, field.getModifiers());
					} else {
						int hashCode = System.identityHashCode(fieldInstance);
						
						if (!cache.containsKey(hashCode)) {
							child = new InnerNode(current, field.getName(), fieldInstance.getClass(), fieldInstance, field.getModifiers());
							cache.put(hashCode, child);
							explode(cache, (InnerNode) child);
						}
						else {
							child = cache.get(hashCode);
						}
					}
					current.addSuperNode(child);
				}
			}
		}
	}
	
}
