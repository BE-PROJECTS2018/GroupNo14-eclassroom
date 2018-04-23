/*
 
  * Project Name:     e-classroom
  * Author List:      Shivani Valecha, Priyanka Wadhwani
  * Filename:         left_esp.ino
  * Functions:        void setup_wifi(), PubSubClient client(), reconnect(), void setup(), void loop(),
  * Global Variables: const char* ssid, const char* password, const char* mqtt_server, const int ledGPIO5, const int ledGPIO4, WiFiClient espClient;
  *
  */


#include <ESP8266WiFi.h>
#include <PubSubClient.h>


const char* ssid = "eclassroom";
const char* password = "12345678";

// Change the variable to your Raspberry Pi IP address, so it connects to your MQTT broker
const char* mqtt_server = "192.168.0.104";


// Initializes the espClient
WiFiClient espClient;

/*
  * Function Name:  PubSubClient client()
  * Input:    client : an instance of Client, typically EthernetClient.
  * Output:   Creates a partially initialised client instance.
  * Example Call:   PubSubClient client(espClient);
  *
  */


PubSubClient client(espClient);

// Connect an LED to each GPIO of your ESP8266
const int ledGPIO5 = 5;
const int ledGPIO4 = 4;

/*
  * Function Name:  void setup_wifi()
  * Input:    None
  * Output:  connects to wifi and mqtt server at the begining 
  * Example Call:   setup_wifi();
  *
  */

// Don't change the function below. This functions connects your ESP8266 to your router

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("WiFi connected - ESP IP address: ");
  Serial.println(WiFi.localIP());
}


/*
  * Function Name:  callback()
  * Input:    topic - the topic the message arrived on (const char[]), payload - the message payload (byte array),length - the length of the message payload (unsigned int)
  * Output:   Publish messages and control gpio pins correspondingly.
  * Logic:   if the client is used to subscribe to topics, a callback function must be provided in the constructor. This function is called when new messages arrive at the client.
  * Example Call:   void callback(String* topic, byte* payload, unsigned int length);
  */

// This functions is executed when some device publishes a message to a topic that your ESP8266 is subscribed to
// Change the function below to add logic to your program, so when a device publishes a message to a topic that 
// your ESP8266 is subscribed you can actually do something


void callback(String topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

  // Feel free to add more if statements to control more GPIOs with MQTT

  // If a message is received on the topic home/office/esp1/gpio2, you check if the message is either 1 or 0. Turns the ESP GPIO according to the message
  if(topic=="left"){
      Serial.print("Changing GPIO 4 to ");
      if(messageTemp == "1"){
        digitalWrite(ledGPIO4, LOW);
        Serial.print("On");
      }
      else if(messageTemp == "0"){
        digitalWrite(ledGPIO4, HIGH);
        Serial.print("Off");
      }
  }
  if(topic=="leftled"){
      Serial.print("Changing GPIO 5 to ");
      if(messageTemp == "1"){
        digitalWrite(ledGPIO5, LOW);
        Serial.print("On");
      }
      else if(messageTemp == "0"){
        digitalWrite(ledGPIO5, HIGH);
        Serial.print("Off");
      }
  }
  Serial.println();
}

/*
  * Function Name:  reconnect()
  * Input:    None
  * Output:   if connection is failed or lost it reconnects
  * Logic:     connects to wifi using client.connected() inbuild function in arduino,
  *            checks status of wifi using Wifi.status inbuild function in arduino,
  *            connects to mqtt server,
  *            subscribe to server after connecting to server 
  *     
  * Example Call:   reconnect();
  *
  */

// This functions reconnects your ESP8266 to your MQTT broker
// Change the function below if you want to subscribe to more topics with your ESP8266 

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
     /*
     YOU  NEED TO CHANGE THIS NEXT LINE, IF YOU'RE HAVING PROBLEMS WITH MQTT MULTIPLE CONNECTIONS
     To change the ESP device ID, you will have to give a unique name to the ESP8266.
     Here's how it looks like now:
       if (client.connect("ESP8266Client")) {
     If you want more devices connected to the MQTT broker, you can do it like this:
       if (client.connect("ESPOffice")) {
     Then, for the other ESP:
       if (client.connect("ESPGarage")) {
      That should solve your MQTT multiple connections problem

     THE SECTION IN loop() function should match your device name
    */
    if (client.connect("ESP8266C")) {
      Serial.println("connected");  
      // Subscribe or resubscribe to a topic
      // You can subscribe to more topics (to control more LEDs in this example)
      client.subscribe("left");
      client.subscribe("leftled");
      
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

/*
  * Function Name:  void setup()
  * Input:    None
  * Output:   Initialize the GPio pins of esp and connects to wifi and mqtt server at the begining 
  * Example Call:   Called automatically by the System
  *
  */
  
// The setup function sets your ESP GPIOs to Outputs, starts the serial communication at a baud rate of 115200
// Sets your mqtt broker and sets the callback function
// The callback function is what receives messages and actually controls the LEDs

void setup() {
  pinMode(ledGPIO4, OUTPUT);
  pinMode(ledGPIO5, OUTPUT);
  
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

  /*
  * Function Name:  void loop()
  * Input:    None
  * Output:   checks all the time whether wifi is connected or not if not then it reconnects by calling reconnect function 
  * Logic:   checks status of wifi using Wifi.status() and client.connected() 
  * Example Call:   Called automatically by the System both are inbuild function 
  */

// For this project, you don't need to change anything in the loop function. 
// Basically it ensures that you ESP is connected to your broker

void loop() {
  //if(WiFi.status() != WL_CONNECTED)
    //setup_wifi();
  if (!client.connected()) {
    reconnect();
  }
  if(!client.loop())
     /*
     YOU  NEED TO CHANGE THIS NEXT LINE, IF YOU'RE HAVING PROBLEMS WITH MQTT MULTIPLE CONNECTIONS
     To change the ESP device ID, you will have to give a unique name to the ESP8266.
     Here's how it looks like now:
       client.connect("ESP8266Client");
     If you want more devices connected to the MQTT broker, you can do it like this:
       client.connect("ESPOffice");
     Then, for the other ESP:
       client.connect("ESPGarage");
      That should solve your MQTT multiple connections problem

     THE SECTION IN recionnect() function should match your device name
    */
    client.connect("ESP8266C");
}

