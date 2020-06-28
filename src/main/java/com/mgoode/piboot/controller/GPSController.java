package com.mgoode.piboot.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.mgoode.piboot.device.LCD;
import com.mgoode.piboot.utils.NMEAParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class GPSController {

    @Autowired
    LCD lcd;

    @Autowired
    NMEAParser nmeaParser;

    @Value("${device-port}")
    String device;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/start")
    public String start() {
        SerialPort comPort = SerialPort.getCommPort(device);
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String s = new String(newData);
                Stream<String> sentences = s.lines();
                sentences.forEach(p -> displayMessage(p));
            }
        });
        return "Start";
    }

    @GetMapping("/stop")
    public String stop() {
        return "Stop";
    }


    public void displayMessage(String sentence) {
        log.info("Sentence:" + sentence);
        if (nmeaParser.parse(sentence)) {
            lcd.write(0, "lat : " + String.format("%f", nmeaParser.getLatitude()));
            lcd.write(1, "lon : " + String.format("%f", nmeaParser.getLongitude()));
        }
    }








}
