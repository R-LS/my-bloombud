temp = 0
lightfrombit = 0
lux = 0
humidity = 0

def on_forever():
    global temp, lightfrombit, lux, humidity
    basic.show_icon(IconNames.YES)
    serial.set_baud_rate(BaudRate.BAUD_RATE115200)
    dht11_dht22.query_data(DHTtype.DHT11, DigitalPin.P1, True, False, False)
    basic.pause(1000)
    temp = dht11_dht22.read_data(dataType.TEMPERATURE)
    basic.pause(1000)
    lightfrombit = input.light_level() * 390
    lux = lightfrombit
    basic.pause(1000)
    humidity = dht11_dht22.read_data(dataType.HUMIDITY)
    basic.pause(1000)
    serial.write_line("" + str(humidity) + "," + str(lux) + "," + str(temp))
basic.forever(on_forever)
