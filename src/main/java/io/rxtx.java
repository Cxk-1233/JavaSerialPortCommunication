package io;

import gnu.io.*;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Log4j
public class rxtx {
    public static List<CommPortIdentifier> getPort() {
        ArrayList<CommPortIdentifier> portIdList = new ArrayList<>();
        Enumeration<CommPortIdentifier> portIds = CommPortIdentifier.getPortIdentifiers();
        while (portIds.hasMoreElements()) {
            portIdList.add(portIds.nextElement());
        }
        return portIdList;
    }

    public static SerialPort openPort(CommPortIdentifier port, int baudRate ) throws PortInUseException, UnsupportedCommOperationException {
        CommPort commPort = port.open(port.getName(), baudRate);
        if (!(commPort instanceof SerialPort)) {
            return null;
        }
        SerialPort serialPort = (SerialPort) commPort;
        // 设置一下串口的波特率等参数
        // 数据位：8
        // 停止位：1
        // 校验位：None
        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        return serialPort;
    }

    public static void sendToPort(SerialPort serialPort, byte[] data) throws IOException {
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(data);
        outputStream.close();
    }

    public static byte[] readPort(SerialPort serialPort) throws IOException {
        InputStream inputStream = serialPort.getInputStream();
        //一次性接收数据长度为1GB
        byte[] buffer = new byte[1024 * 1024 * 1024];
        int dataSize = inputStream.read(buffer);
        byte[] temp = new byte[dataSize];
        System.arraycopy(buffer, 0, temp, 0, dataSize);
        inputStream.close();
        return temp;
    }

    public static void addListener(final SerialPort serialPort, DataAvailableListener listener) throws TooManyListenersException {
        // 给串口添加监听器
        serialPort.addEventListener(new SerialPortListener(listener));
        // 设置当有数据到达时唤醒监听接收线程
        serialPort.notifyOnDataAvailable(true);
        // 设置当通信中断时唤醒中断线程
        serialPort.notifyOnBreakInterrupt(true);
    }

    public static class SerialPortListener implements SerialPortEventListener {
        private final DataAvailableListener dataAvailableListener;
        public SerialPortListener(DataAvailableListener dataAvailableListener) {
            this.dataAvailableListener = dataAvailableListener;
        }
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
                    dataAvailableListener.dataAvailable();
                    break;
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
                    break;
            }
        }
    }

    public interface DataAvailableListener {
        void dataAvailable();
    }
}
