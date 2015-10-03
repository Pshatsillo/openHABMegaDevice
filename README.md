Пока работает только на порту 8989
Пока только как Switch

т.е. в настройках меги указываем айпи сервера 192.168.0.1:8989

имя скрипта - любое

в OpenHAB *.items 
<code>
Switch MegaDeviceButton_kitchen 	"Kitchen button" {megadevice="sec:192.168.0.17:0"}
Switch KitchenLamp "Свет над кухней" (Hall, Hall_Lights) {megadevice="sec:192.168.0.17:9"}
</code>
{megadevice="пароль на мегу : айпишник меги : номер порта меги"}


Пример Mega.rules
<code>
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
</code>
пример с таймером:
<code>

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
 	 if (HallLamp.state == OFF){ postUpdate(HallLamp,ON) } else {postUpdate(HallLamp,OFF)}
 	}
 	else if (result > 1 && result < 3){
 		postUpdate(HallLamp,OFF)
 	} else {
 		logInfo("Timer >3 seconds", result.toString)
 	}
end
</code>
Пример default.siemaps

<code>
sitemap default label= "Main Screen"{
	
	Frame label="Кухня" {
		Switch item=KitchenLamp
	}
}
</code>
