package core.utils.ssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;

public class SshUtils {

    public static final int defaultResponseTimeoutInSeconds=1;


    public static Expect openSshConnectionToDB(){
        return openSshConnection(System.getProperty("SSHHOST"),System.getProperty("SSHUSER"),System.getProperty("SSHPASS"));
    }

    public static Expect openSshConnection(String host,String user,String password){
        try {
            SSHClient ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(host);
            ssh.authPassword(user, password);
            Session session = ssh.startSession();
            session.allocateDefaultPTY();
            Session.Shell shell = session.startShell();
            Expect expect = new ExpectBuilder()
                    .withInputFilters(
                            removeColors(),
                            removeNonPrintable()
                    )
                    .withOutput(shell.getOutputStream())
                    .withEchoInput(System.out)
//                     .withEchoOutput(System.out)
                    .withTimeout(defaultResponseTimeoutInSeconds,TimeUnit.SECONDS)
                    .withInputs(shell.getInputStream(), shell.getErrorStream())
                    .build();
            return expect;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
