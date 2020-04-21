#include <WiFi.h>
#include <HCSR04.h>
#include "FirebaseESP32.h"

#define FIREBASE_HOST "https://smarthomeiot-5a5e3.firebaseio.com/"
#define FIREBASE_AUTH "eybGUOvJ8QswkyoFORmjMfUJhuXIMcTRgL9eD2w4"
#define WIFI_SSID "G149" 
#define WIFI_PASSWORD "149ModalDong"

//INPUT, OUTPUT initialization
UltraSonicDistanceSensor distanceSensor(22, 23 );
#define ldr 36
#define led1 2
#define led2 4


FirebaseData firebaseData;

String path = "/Node1";

int oldDistance;
int newDistance;

int oldAdcLdr;
int newAdcLdr;

void setup() {
  
  Serial.begin(115200);

  pinMode(ldr, INPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);

  initWifi();
  
  oldDistance = distanceSensor.measureDistanceCm();
  oldAdcLdr = analogRead(ldr);
}

void loop() {

  delay(500);

  //ambil value dari sensor LDR dan kirim ke firebase
  newAdcLdr = analogRead(ldr);
  Serial.println(newAdcLdr);
  if(newAdcLdr != oldAdcLdr){
    Firebase.setDouble(firebaseData, path + "/ldr", newAdcLdr);
    oldAdcLdr = newAdcLdr;
  }

  //ambil value /Node1/distance
  newDistance = distanceSensor.measureDistanceCm();
  Serial.println(newDistance);
  if(newDistance != oldDistance){
    Firebase.setInt(firebaseData, path + "/distance", newDistance);
    oldDistance = newDistance; 
  }

  //get value /Node1/led1
  if(Firebase.getInt(firebaseData, path + "/led1")){
    if(firebaseData.intData() == 0)
        digitalWrite(led1,0);
      else
        digitalWrite(led1,1);
  }

  //get value /Node1/led2
  if(Firebase.getInt(firebaseData, path + "/led2")){
    if(firebaseData.intData() == 0)
      digitalWrite(led2,0);
    else
      digitalWrite(led2,1);
  }
}

void initWifi(){
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setReadTimeout(firebaseData, 1000*60);
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
}
