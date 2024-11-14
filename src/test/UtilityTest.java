package test;

import it.pack.utility.logger.BasicLogger;
import it.pack.utility.timer.Chronometer;

public class UtilityTest {
    public static void main(String[] args) {
        Chronometer timer = new Chronometer();
        BasicLogger logger = BasicLogger.getLogger(UtilityTest.class, BasicLogger.DEFAULT_LOG_FOLDER);

        logger.info("Start Timer");
        timer.start();

        int j = 0;


        logger.info("Stop Timer. Elapsed Time " + timer.stop().getFormattedTime());
    }
}
