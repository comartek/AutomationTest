package core.utils.ie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsProcess
{
    private String processName;

    public WindowsProcess(String processName)
    {
        this.processName = processName;
    }

    public void kill()
    {
        if (isRunning())
        {
            try {
                getRuntime().exec("taskkill /F /IM " + processName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isRunning()
    {
        Process listTasksProcess = null;
        try {
            listTasksProcess = getRuntime().exec("tasklist");
            BufferedReader tasksListReader = new BufferedReader(
                    new InputStreamReader(listTasksProcess.getInputStream()));

            String tasksLine;

            while ((tasksLine = tasksListReader.readLine()) != null)
            {
                if (tasksLine.contains(processName))
                {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Runtime getRuntime()
    {
        return Runtime.getRuntime();
    }
}