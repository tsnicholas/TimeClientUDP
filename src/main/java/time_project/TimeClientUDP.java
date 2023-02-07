package time_project;

import java.io.IOException;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class TimeClientUDP {
    private static final int PORT_NUM = 37;
    private final DatagramSocket socket;
    private final InetAddress serverIP;

    public TimeClientUDP() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        serverIP = InetAddress.getByName("time.nist.gov");
    }

    public static void main(String[] args) {
        try {
            TimeClientUDP client = new TimeClientUDP();
            byte[] receivedData = client.queryTime();
            // Seconds since the very beginning of 1900 given by the server.
            long seconds_since = Integer.toUnsignedLong(ByteBuffer.wrap(receivedData).getInt());
            DataConverter dataConverter = new DataConverter();
            System.out.println(dataConverter.getTime(seconds_since));
        }
        catch(IOException error) {
            error.printStackTrace();
        }
    }

    private byte[] queryTime() throws IOException {
        DatagramPacket request = new DatagramPacket(new byte[1024], 1024, serverIP, PORT_NUM);
        socket.send(request);
        DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);
        socket.receive(reply);
        socket.close();
        return reply.getData();
    }
}
