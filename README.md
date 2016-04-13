- Работает как Switch, Dimmer, Number, String 
 - В настройках меги указываем айпи сервера 192.168.0.1:8989 
 - имя скрипта - любое

В самой меге для корректной работы с кнопками нужно выставить на входах режим порта P+R. Тогда на сервер будет приходить сигнал нажатия и отжатия

## Чтобы изменить номер порта вставляем эти строчки в конец конфига Openhub: ##

    ################################ Megadevice Binding #######################################
    #
    # IP address of a Http port for megadevice defaut value is 8989
    megadevice:httpserverport=8585


**Общий принцип:**

    {megadevice="пароль на мегу : айпишник меги : номер порта меги"}
    
Начиная с версии 0.1.0.2 добавилс новый параметр- интервал опроса. По умолчанию порты меги опрашиваются только при старте сервера. Далее нужно указать интервал опроса в секундах после двоеточия. Если параметр не указан - обновления **не будет**:

	{megadevice="пароль на мегу : айпишник меги : номер порта меги: интервал опроса"}
	

Диммер - Dimmer. 
Работает в процентах от 0 до 100. 
Конвертация работает по принципу, предложенному Selecta ([http://www.ab-log.ru/forum/viewtopic.php?f=1&t=635&p=18923#p18923](http://www.ab-log.ru/forum/viewtopic.php?f=1&t=635&p=18923#p18923)) - данные в процентах * 2.55 с округлением в бОльшую сторону, т.е. 10 процентов - это 26

**Пример item:**

    Dimmer DimmedLight	"Dimmer [%d %%]"	{megadevice="sec:192.168.0.14:10"}

**Пример sitemap:**

    Slider item=DimmedLight



**Для получения значений АЦП используем Number**

**Пример item:**

    Number ADCPort15 "Значение АЦП порт 15 мегадевайса 1: [%d]" {megadevice="sec:192.168.0.14:15"}

обработку "v" и "dir" не делал. 

**Для получения температуры используем Number:**

    {megadevice="sec:192.168.0.17:0,dht11,t"}
Начиная с версии 0.1.0.2 добавляем интервал опроса в секундах:
	
	{megadevice="sec:192.168.0.17:0,dht11,t:30"}


т.е. после айпи адреса указываем через запятую порт, тип датчика и желаемый ответ

##### t- температура
##### h- влажность. (для 1w этого параметра нет)
типы датчиков, выставляются аналогично меги
##### dht11
##### dht22
##### 1w

Начиная с версии 0.1.2.0 добавился параметр "**r**".

Данные приходят не распарсенные в текстовом виде через "/" в любом случае, не зависимо от прошивки, далее можно самостоятельно получить нужное значение. Сделал это для того, чтобы можно было избежать проблемы двойного опроса датчика и как следствие 00.0 вместо корректного отображения температуры и влажности.

**item:**

	String MegaTempHumParse         "Parse string [%s]"        {megadevice="sec:192.168.0.17:3,dht,r:30"}

**rule:**

	rule "Mega DHT Temp/Hum Parser"
	when 
	    Item MegaTempHumParse received update
	then
	    val parse = MegaTempHumParse.state.toString.split("/")
	    val parsedtemperature = parse.get(0)
	    val parsedhumidity = parse.get(1)
	    var temperature = new Double(parsedtemperature)
	    var humidity = new Double(parsedhumidity)
   
	    postUpdate(TempDHT, temperature)
	    postUpdate(HumDHT, humidity)
   
	    if(temperature == 5.0){
           logInfo("Test", "5.0")
	    }
	end

Есть возможность получить температуру меги - аналогично запросу **http://192.168.0.14/sec/?tget=1**

для этого достаточно вместо порта указать tget. 

**Пример item:**

    Number MegaDeviceOneTemp "Mega 1 temperature" {megadevice="sec:192.168.0.17:tget"}
    
   Начиная с версии 0.1.0.2 добавляем интервал опроса в секундах:
    
    Number MegaDeviceOneTemp "Mega 1 temperature" {megadevice="sec:192.168.0.17:tget:30"}

С помощью параметра at, устройство сообщает на сервер о превышении порога температуры встроенного сенсора

**Пример item:**

    Number AlarmTempValue "Превышение температуры мегадевайса 1: [%d]" {megadevice="sec:192.168.0.14:at"}

# ПРИМЕРЫ #

### в OpenHAB *.items:

    Switch MegaDeviceButton_kitchen 	"Kitchen button" {megadevice="sec:192.168.0.17:0"}
    Switch KitchenLamp "Свет над кухней" (Hall, Hall_Lights) {megadevice="sec:192.168.0.17:9"}
    Dimmer DimmedLight	"Dimmer [%d %%]"	{megadevice="sec:192.168.0.17:0"}


#### Пример Mega.rules:

    rule "MegadeviceKitchenButtonPress"
    when Item MegaDeviceButton_kitchen changed to ON
    then if (KitchenLamp.state == OFF) {
	postUpdate(KitchenLamp, ON)
	sendCommand(KitchenLamp, ON)
    } else {
	postUpdate(KitchenLamp, OFF)
	sendCommand(KitchenLamp, OFF)
    }
    end

    rule "MegaDeviceBellButtonPress"
    when Item MegaDeviceBell_Button changed to ON
    then
	postUpdate(Bell, ON)
	sendCommand(Bell, ON)
    end

    rule "MegaDeviceBellButtonRelease"
    when Item MegaDeviceBell_Button changed to OFF
    then
	postUpdate(Bell, OFF)
	sendCommand(Bell, OFF)
    end


### Пример с таймером:

    var int Timeout

    rule "Test"
    when Item Megadevice_button changed to ON then
     Timeout = now.getMillisOfDay
     logInfo("StartTime", Timeout.toString)
    end

    rule "Test1"
    when Item Megadevice_button changed to OFF then
    var int Stop = now.getMillisOfDay
    logInfo("StopTime", Timeout.toString)
    var int result = Stop - Timeout
    result = result / 1000
 
     if (result <= 1) {
     	logInfo("Timer 1 second ", result.toString)
     	if (HallLamp.state == OFF){
            postUpdate(HallLamp,ON) 
        } else {
          postUpdate(HallLamp,OFF)
        }
     }
     else if (result > 1 && result < 3){
     		postUpdate(HallLamp,OFF)
     	  } else {
     		logInfo("Timer >3 seconds", result.toString)
     	  }
    end

### Пример default.siemaps:


    sitemap default label= "Main Screen"{
		Frame label="Кухня" {
			Switch item=KitchenLamp
			Slider item=DimmedLight
		}
    }

По многочисленным просьбам - Donate:

[Paypal](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=P38VCVDQMSMYQ) 

[Yandex.Money](https://money.yandex.ru/to/410011024847033)

Спасибо!
