package main;

import io.rxtx;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.util.List;

public class MyMain {
    public static void main(String[] args) throws Exception {
        List<CommPortIdentifier> commPortIdentifiers = rxtx.getPort();
        for (CommPortIdentifier commPortIdentifier : commPortIdentifiers) {
            System.out.println(commPortIdentifier.getName());
        }
        final SerialPort serialPort = rxtx.openPort(commPortIdentifiers.get(1), 9600);
        assert serialPort != null;
        rxtx.addListener(serialPort, new rxtx.DataAvailableListener() {
            @Override
            public void dataAvailable() {
                try {
                    byte[] data = rxtx.readPort(serialPort);
                    String str = new String(data);
                    String uid = "";
                    int temp = str.lastIndexOf("Card UID:");
                    if (temp != -1) {
                        uid = str.substring(temp + 10, temp + 21);
                        System.out.println("登录验证成功！UID：" + uid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
