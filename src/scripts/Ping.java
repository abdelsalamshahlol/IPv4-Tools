/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;

/**
 * This is a custom class made by user jgregoire108 on stackoverflow http://stackoverflow.com/users/2821263/jgregoire108
 * to build ICMP ping on windows because Java have predefined class [InetAddress.isReachable()] to do that but in windows it requires
 * administrative privileges.
 * @author Abdelsalam Shahlol
 */
public class Ping {
    int reply;
    String ss;
    //
    public interface InetAddr extends StdCallLibrary {
        InetAddr INSTANCE = (InetAddr) Native.loadLibrary("wsock32.dll", InetAddr.class);
        ULONG inet_addr(String cp);                     //in_addr creator. Creates the in_addr C struct used below
    }

    public interface IcmpEcho extends StdCallLibrary {

        IcmpEcho INSTANCE = (IcmpEcho) Native.loadLibrary("iphlpapi.dll", IcmpEcho.class);

        int IcmpSendEcho(
                HANDLE IcmpHandle, //Handle to the ICMP
                ULONG DestinationAddress, //Destination address, in the form of an in_addr C Struct defaulted to ULONG
                Pointer RequestData, //Pointer to the buffer where my Message to be sent is
                short RequestSize, //size of the above buffer. sizeof(Message)
                byte[] RequestOptions, //OPTIONAL!! Can set this to NULL
                Pointer ReplyBuffer, //Pointer to the buffer where the replied echo is written to
                int ReplySize, //size of the above buffer. Normally its set to the sizeof(ICMP_ECHO_REPLY), but arbitrarily set it to 256 bytes
                int Timeout);                           //time, as int, for timeout

        HANDLE IcmpCreateFile();                        //win32 ICMP Handle creator

        boolean IcmpCloseHandle(HANDLE IcmpHandle);     //win32 ICMP Handle destroyer
    }

    public void SendReply(String ipAddress) {
             ss= ipAddress;

        final int timeout=200;
        final IcmpEcho icmpecho = IcmpEcho.INSTANCE;
        final InetAddr inetAddr = InetAddr.INSTANCE;
        HANDLE icmpHandle = icmpecho.IcmpCreateFile();
        byte[] message = new String("thisIsMyMessage!".toCharArray()).getBytes();
        Memory messageData = new Memory(32);                    //In C/C++ this would be: void *messageData = (void*) malloc(message.length);
        messageData.write(0, message, 0, message.length);       //but ignored the length and set it to 32 bytes instead for now
        Pointer requestData = messageData;
        Pointer replyBuffer = new Memory(256);
        replyBuffer.clear(256);

        // HERE IS THE NATIVE CALL!!
        reply = icmpecho.IcmpSendEcho(icmpHandle,
                inetAddr.inet_addr(ipAddress),
                requestData,
                (short) 32,
                null,
                replyBuffer,
                256,
                timeout);
        // NATIVE CALL DONE, CHECK REPLY!!

        icmpecho.IcmpCloseHandle(icmpHandle);
    }

    public boolean IsReachable() {
        return (reply > 0);
    }

}
