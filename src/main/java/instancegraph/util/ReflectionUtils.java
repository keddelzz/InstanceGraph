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

package instancegraph.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ReflectionUtils {
	
	private static final Map<Class<?>, Class<?>> PRIMITIVE_MAPPING;
	private static final Map<Class<?>, String> READABLE_TYPE_MAPPING;
	
	static {
		PRIMITIVE_MAPPING = new HashMap<>();
		
		PRIMITIVE_MAPPING.put(Boolean.class, boolean.class);
		PRIMITIVE_MAPPING.put(Byte.class, byte.class);
		PRIMITIVE_MAPPING.put(Character.class, char.class);
		PRIMITIVE_MAPPING.put(Double.class, double.class);
		PRIMITIVE_MAPPING.put(Float.class, float.class);
		PRIMITIVE_MAPPING.put(Integer.class, int.class);
		PRIMITIVE_MAPPING.put(Short.class, short.class);
		PRIMITIVE_MAPPING.put(Long.class, long.class);
		
		READABLE_TYPE_MAPPING = new HashMap<>();
	}
	
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		return PRIMITIVE_MAPPING.containsKey(clazz);
	}
	
	public static String getDisplayClassName(Class<?> clazz) {
		return isPrimitiveWrapper(clazz) ? PRIMITIVE_MAPPING.get(clazz).getSimpleName()
				: clazz.getName();
	}
	
	public static List<Class<?>> getSuperclasses(Class<?> clazz) {
		Class<?> supah = Objects.requireNonNull(clazz, "clazz == null");
		List<Class<?>> list = new ArrayList<>();
		while ((supah = supah.getSuperclass()) != null) {
			list.add(supah);
		}
		return list;
	}
	
	public static String readableTypeOf(Object object) {
		return object == null ? "null" : readableTypeOf(object.getClass());
	}
	
	public static String readableTypeOf(Class<?> clazz) {
		if (clazz == null) return "null";
		
		if (!READABLE_TYPE_MAPPING.containsKey(clazz)) {
			StringBuilder builder = new StringBuilder();
			
			while (clazz.isArray()) {
				builder.append("[]");
				clazz = clazz.getComponentType();
			}
			builder.insert(0, ReflectionUtils.getDisplayClassName(clazz));
			
			String readableType = builder.toString();
			READABLE_TYPE_MAPPING.put(clazz, readableType);
			return readableType;
		}
		
		return READABLE_TYPE_MAPPING.get(clazz);
	}
	
}
