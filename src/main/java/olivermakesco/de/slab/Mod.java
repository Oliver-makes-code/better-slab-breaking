package olivermakesco.de.slab;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Example Mod");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Thanks for installing Better Slab Breaking! <3");
	}
}
