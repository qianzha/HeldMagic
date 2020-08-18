package qianzha.heldmagic.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface HeldMagicPlugin {

	@Target(ElementType.FIELD)
	@interface ApiInstance {
		
	}
}
