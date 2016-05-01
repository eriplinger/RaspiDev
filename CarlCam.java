import java.io.IOException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

public class CarlCam{
    public static void main(String[] args) throws InterruptedException, IOException{
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.HIGH);
        final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);

        led.setShutdownOptions(true, PinState.LOW);
        led.high();
        Thread.sleep(1000);
        led.low();
        
        button.addListener(new GpioPinListenerDigital(){
            @Override
            public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
                System.out.println(System.currentTimeMillis() + " button pressed");
                led.pulse(1000, true);
            }
        });
        

        while(System.in.available() == 0){
            Thread.sleep(500);
        }

        gpio.shutdown();
        gpio.unprovisionPin(led);
        gpio.unprovisionPin(button);
    }
}
