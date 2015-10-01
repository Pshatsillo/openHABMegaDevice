Пока работает только на порту 8989
Пока только на прием. и только как Switch


т.е. в настройках меги указываем айпи сервера 192.168.0.1:8989

имя скрипта - любое

в OpenHAB *.items 

Switch Megad "Свет на кухне" {megadevice="sec:localhost:9"}

{megadevice="пароль на мегу : айпишник меги : номер порта меги"}

пример с таймером:


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