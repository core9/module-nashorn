
var NashornTest = Java.type("io.core9.plugin.nashorn.NashornTest");

print("Javascript: " + NashornTest.calledByJavascript("Java wird von Javascript aufgerufen!"));

function calledByJava(output) {
   print("Javascript: " + output);
   return "Erfolgreich von Java aufgerufen";
}