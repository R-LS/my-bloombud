import serial
#import requests
import urllib.request
import time

ser = serial.Serial()
ser.baudrate = 115200
ser.port = "COM4"
ser.open()
global isWaiting, humidityValue, lightValue, tempValue
isWaiting = False
humidityValue = None
lightValue = None
tempValue = None

# while True:
#     data1 = str(ser.readline())
#     print(data1)

def clearValues():
    isWaiting = True
    humidityValue = None
    lightValue = None
    tempValue = None
    time.sleep(15)
    isWaiting = False

while True and isWaiting == False:
    data1 = str(ser.readline())
    data1 = data1.replace("b","")
    data1 = data1.replace("'","")
    data1 = data1.replace("\\r\\n","")
    print(data1)
    data1Count = data1.split(',')
    count = len(data1Count)
    if "," in data1 and "\\" not in data1 and count == 3:
        humidityValue, lightValue, tempValue = data1.split(",")
    if (lightValue and tempValue and humidityValue) != None:
        lightValue = lightValue.replace(" ", "")
        tempValue = tempValue.replace(" ", "")
        humidityValue = humidityValue.replace(" ", "")
        print (lightValue)
        print (tempValue)
        print (humidityValue)
        urllib.request.urlopen('https://api.thingspeak.com/update?api_key=TXIQDJP9X5Z3T3Y1&field1=' + tempValue + '&field2=' + lightValue + '&field3=' + humidityValue)
        print("\nYour data has successfully been uploaded!")
        clearValues()

        

    
