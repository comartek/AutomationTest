package core.utils.ie;

import core.JbehaveRunner;
import core.utils.report.AllureJBehaveReporter;
import core.utils.timeout.TimeoutUtils;
import org.apache.log4j.Logger;


public class InternetExplorerStateListener {
    private static String loggerBlock = "[IE state listener] ";

    private static Thread thread = null;
    private static int sleepTime = 10;
    private static int maxAllowedFaultsInTheRow = 2;

    private static String FaultProcessName = "WerFault.exe";
    private static String IEProcessName = "iexplore.exe";
    private static String IEDriverProcessName = "IEDriverServer.exe";


    public static void startListening(){
        if(thread == null || !thread.isAlive()){
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    AllureJBehaveReporter reporter = JbehaveRunner.getJbehaveRunner().allureJBehaveReporter;
                    Logger.getRootLogger().info(loggerBlock + "started listening if '" + FaultProcessName + "' process is running");
                    int faultsFoundInTheRow = 0;

                    WindowsProcess faultProcess = new WindowsProcess(FaultProcessName);
                    boolean isContinue = true;
                    while(isContinue){
                        if(faultProcess.isRunning()) {
                            faultsFoundInTheRow++;
                            Logger.getRootLogger().info(loggerBlock + FaultProcessName + " process is running (IE has stopped working) {" + faultsFoundInTheRow + "}!!!");

                            if (faultsFoundInTheRow > maxAllowedFaultsInTheRow) {
                                try {
                                    reporter.logStoryCancelled("IE has stopped working (process with name '" + FaultProcessName + "' has been found)");
                                } catch (Throwable ignored) {
                                    /*do nothing*/
                                }

                                Logger.getRootLogger().info(loggerBlock + " Killing IE processes: " + FaultProcessName + "; " + IEDriverProcessName);
                                faultProcess.kill();
//                                new WindowsProcess(IEProcessName).kill();
                                new WindowsProcess(IEDriverProcessName).kill();
                                faultsFoundInTheRow = 0;
                            }
                        } else {
                            Logger.getRootLogger().info(loggerBlock + FaultProcessName + " process is not running");
                            faultsFoundInTheRow = 0;
                        }
                        TimeoutUtils.sleep(sleepTime);
                    }
                }
            });
            thread.start();
        }
    }

    public static void stopListening(){
        if(thread != null){
            thread.interrupt();
            thread.stop();

            boolean isAlive = true;
            long start = System.currentTimeMillis();
            while(start + 1000 > System.currentTimeMillis() && (isAlive = thread.isAlive())) {/*do nothing*/}

            if(isAlive){
                Logger.getRootLogger().info(loggerBlock + "FAILED to stop listening if '" + FaultProcessName + "' process is running (listener`s thread is still alive 1 second after trying to stop it)");
            } else {
                Logger.getRootLogger().info(loggerBlock + "stopped listening if '" + FaultProcessName + "' process is running");
            }
            thread = null;
        }
    }
}
