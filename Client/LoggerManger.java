package Client;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManger {
    private Logger logger;

    public LoggerManger(int ClientID) {
        this.logger = Logger.getLogger("Client-" + ClientID);
        configureLogger();
    }

    private void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(logger.getName() + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (SecurityException | IOException e) {
            System.err.println("Error while configuring logger");
            e.printStackTrace();
        }
    }

    public void log(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.severe(message);
    }

    public void logWarning(String message) {
        logger.warning(message);
    }
    
}
