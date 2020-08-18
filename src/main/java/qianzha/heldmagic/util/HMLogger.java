package qianzha.heldmagic.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import qianzha.heldmagic.common.HMConstants;

public enum HMLogger {
	DEFAULT() {
        @Override
        boolean enabled() {
            return true;
        }
    },
    DEBUG() {
        @Override
        boolean enabled() {
//            return ConfigHandler.general.enableDebugLogging;
        	return true;
        }
    },
    API() {
        @Override
        boolean enabled() {
            return true;
        }
    },
    API_VERBOSE() {
        @Override
        boolean enabled() {
            return true;
        }
    },
    ;

    private final Logger logger;

    HMLogger(String logName) {
        logger = LogManager.getLogger(logName);
    }

    HMLogger() {
        logger = LogManager.getLogger(HMConstants.MODID + "|" + name().replace("_", " "));
    }

    abstract boolean enabled();

    public void info(String input, Object... args) {
        if (enabled())
            logger.info(input, args);
    }

    public void error(String input, Object... args) {
        if (enabled())
            logger.error(input, args);
    }

    public void warn(String input, Object... args) {
        if (enabled())
            logger.warn(input, args);
    }
}
