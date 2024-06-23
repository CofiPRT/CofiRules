package ro.cofi.cofirules;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CofiRules implements DedicatedServerModInitializer {

    private static final String MOD_ID = "cofi-rules";
    private static final String MOD_NAME = "Cofi Rules";

    private static final Logger LOGGER = LoggerFactory.getLogger(CofiRules.class);

    @Override
    public void onInitializeServer() {
        LOGGER.info("Initializing {}", MOD_NAME);

        LOGGER.info("Successfully initialized {}", MOD_NAME);
    }

}
