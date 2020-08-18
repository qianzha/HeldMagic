package qianzha.heldmagic.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.objectweb.asm.Type;

import com.mojang.datafixers.util.Pair;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

public class AnnotationUtil {

	public static <A extends Annotation, I> List<Pair<I, A>> getInstances(Class<A> annotationClass, Class<I> instanceClass) {
		List<Pair<I, A>> instances = new ArrayList<>();
		getAnnotationDatasStream(annotationClass)
			.map(a -> a.getMemberName())
			.forEach(className -> {
				try {
					Class<?> asmClass = Class.forName(className);
					Class<? extends I> asmInstanceClass = asmClass.asSubclass(instanceClass);
					I instance = asmInstanceClass.newInstance();
					instances.add(Pair.of(instance, asmInstanceClass.getAnnotation(annotationClass)));
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | LinkageError e) {
					HMLogger.API.error("Failed to load: {}", className, e);
				}
			});
		return instances;
	}
	
	public static <A extends Annotation> Stream<AnnotationData> getAnnotationDatasStream(Class<A> annotationClass) {
		Type annotationType = Type.getType(annotationClass);
		return ModList.get().getAllScanData().stream()
			.map(ModFileScanData::getAnnotations)
			.flatMap(Collection::stream)
			.filter(a -> annotationType.equals(a.getAnnotationType()));
	}
	
}
