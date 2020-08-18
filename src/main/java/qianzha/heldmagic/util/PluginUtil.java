package qianzha.heldmagic.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import qianzha.heldmagic.api.HeldMagicPlugin;
import qianzha.heldmagic.api.IHeldMagicAPI;
import qianzha.heldmagic.api.IHeldMagicPlugin;
import qianzha.heldmagic.common.api.impl.HeldMagicAPI;
import qianzha.heldmagic.common.plugin.test.HMPlugin;

public class PluginUtil {

	public static void load() {		
		for(Pair<IHeldMagicPlugin, HeldMagicPlugin> plugin : getModPlugins()) {
			plugin.getFirst().register(HeldMagicAPI.INSTANCE);
		}
		HMLogger.API.info("Found {} plugins.", getModPlugins().size());
		inject(HeldMagicPlugin.ApiInstance.class, IHeldMagicAPI.class, HeldMagicAPI.INSTANCE);
		HMLogger.DEBUG.info("HMPlugin.API: {}", HMPlugin.API);
	}
	
	public static List<Pair<IHeldMagicPlugin, HeldMagicPlugin>> getModPlugins() {
		final List<Pair<IHeldMagicPlugin, HeldMagicPlugin>> plugins =
				AnnotationUtil.getInstances(HeldMagicPlugin.class, IHeldMagicPlugin.class);
		return plugins;
	}
	
	static <A extends Annotation, I> void inject(Class<A> annotationClass, Class<I> instanceClass, I instance) {
		AnnotationUtil.getAnnotationDatasStream(annotationClass)
			.forEach(a -> {
				try {
					Class<?> clazz = Class.forName(a.getClassType().getClassName());
					Field field = clazz.getDeclaredField(a.getMemberName());
					int modifiers = field.getModifiers();
					if(Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
						try {
							field.set(null, HeldMagicAPI.INSTANCE);
							HMLogger.API.info("Injected API instance to {}#{}", 
									a.getClassType().getClassName(), a.getMemberName());
						} catch (IllegalArgumentException | IllegalAccessException e) {
							HMLogger.API.error(e.getMessage());
						}
					}
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			});
			;
	}
	
	
}
