package com.caixukun.io;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import io.rxtx;
import junit.framework.TestCase;

import java.io.IOException;

public class rxtxTest extends TestCase {

    public void testReadPort() {
        try {
            CommPortIdentifier commPortIdentifier = rxtx.getPort().get(0);
            SerialPort serialPort = rxtx.openPort(commPortIdentifier, 9600);
            assert serialPort != null;
            rxtx.readPort(serialPort);
        } catch (PortInUseException | UnsupportedCommOperationException | IOException e) {
            e.printStackTrace();
        }
    }
}