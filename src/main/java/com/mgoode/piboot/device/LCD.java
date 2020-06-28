package com.mgoode.piboot.device;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;



import static com.pi4j.wiringpi.Gpio.wiringPiSetup;
import static com.pi4j.wiringpi.Gpio.wiringPiSetupGpio;

@Component
public class LCD {

    Logger log = LoggerFactory.getLogger(this.getClass());

    int LCD_ROWS = 2;
    int LCD_COLUMNS = 16;

    GpioLcdDisplay lcd;

    @PostConstruct
    public void initialise() {

        int result = wiringPiSetup();

        log.info("wiringPiSetup " + result);



        try {
            lcd = new GpioLcdDisplay(LCD_ROWS, // number of row
                    // supported by
                    // LCD
                    LCD_COLUMNS, // number of columns supported by LCD
                    RaspiPin.GPIO_25, // LCD RS pin
                    RaspiPin.GPIO_24, // LCD strobe pin
                    RaspiPin.GPIO_23, // LCD data bit 1
                    RaspiPin.GPIO_22, // LCD data bit 2
                    RaspiPin.GPIO_21, // LCD data bit 3
                    RaspiPin.GPIO_14); // LCD data bit 4
        } catch (UnsatisfiedLinkError e) {
            System.err.println("platform does not support this driver");
        } catch (Exception e) {
            System.err.println("platform does not support this driver");
        }
    }

    public void write(String msg) {
        lcd.write(msg);
    }

    public void write(int row, String msg) {
        lcd.write(row, msg);
    }

    public void clear() {
        lcd.clear();
    }
}
