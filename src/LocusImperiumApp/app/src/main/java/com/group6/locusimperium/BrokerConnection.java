/****************************************************************************
 Code base on "DIT113MqttWorkshop".
 Author: Nicole Quinstedt
 Source: https://github.com/Quinstedt/DIT113MqttWorkshop/blob/main/SpeechToText/app/src/main/java/com/quinstedt/speechtotext/BrokerConnection.java
 *****************************************************************************/

package com.group6.locusimperium;

import static com.group6.locusimperium.ConnectActivity.IPADDRESS;
import static com.group6.locusimperium.SettingsActivity.HUMIDITY;
import static com.group6.locusimperium.SettingsActivity.LOUDNESS;
import static com.group6.locusimperium.SettingsActivity.PEOPLE;
import static com.group6.locusimperium.SettingsActivity.SHARED_PREFS;
import static com.group6.locusimperium.SettingsActivity.TEMPERATURE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class BrokerConnection extends AppCompatActivity {

    //Application subscription, will receive everything from this topic.
    public static final String SUPER_SUBSCRIPTION_TOPIC = "LocusImperium/WIO/";
    public static final String PUB_TOPIC = "LocusImperium/APP/";

    public static final String MAX_SETTINGS_PUB_TOPIC = PUB_TOPIC + "maxSettings";

    public static String LOCALHOST;
    public static String MQTT_SERVER;
    public static final String CLIENT_ID = "LocusImperium-Application";
    public static final int QOS = 0;
    private static boolean isConnected = false;
    private MqttClient mqttClient;
    Context context;

    //TextView elements
    public TextView peopleCount;
    public TextView temperatureValue;
    public TextView humidityValue;
    public TextView loudnessValue;


    public String getMaxPeople() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(PEOPLE,"");
    }

    public String getMaxLoudness() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(LOUDNESS,"");
    }
    public String getMaxHumidity() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(HUMIDITY,"");
    }

    public String getMaxTemperature() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(TEMPERATURE,"");
    }
    public BrokerConnection(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        LOCALHOST = sharedPreferences.getString(IPADDRESS, "");
        MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
        this.context = context;
        mqttClient = new MqttClient(context, MQTT_SERVER, CLIENT_ID);
        connectToMqttBroker();
    }

    /**
     * Establishes connection to the mqtt broker.
     * @see MqttClient
     * @return void
     */
    public void connectToMqttBroker() {
        if (!isConnected) {
            mqttClient.connect(CLIENT_ID, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(CLIENT_ID, successfulConnection);
                    Toast.makeText(context, successfulConnection, Toast.LENGTH_SHORT).show();
                    // Added "+ '#'" to subscribe to all subtopics under the super one.
                    mqttClient.subscribe(SUPER_SUBSCRIPTION_TOPIC + '#', QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(CLIENT_ID, failedConnection);
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(CLIENT_ID, connectionLost);
                }
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String messageMQTT = message.toString();
                    String[] topicArray = messageMQTT.split(",");
                    //The position of the different settings values are predefined in the array.
                    peopleCountArrived(topicArray[0]);
                    humidityValueArrived(topicArray[1]);
                    temperatureValueArrived(topicArray[2]);
                    loudnessValueArrived(topicArray[3]);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(CLIENT_ID, "Message delivered");
                }
            });
        }
    }
    /**
     * updates the people counter textview value, color is gray if the value is below the max value, red if above. The max value is set in the settings.
     * @return void
     */
    public void peopleCountArrived(String message) {
        peopleCount.setText(message);
        if (Integer.parseInt(message) > Integer.parseInt(getMaxPeople())) {
            peopleCount.setText(message);
            peopleCount.setTextColor(Color.RED);
        }
        else {
            peopleCount.setText(message);
            peopleCount.setTextColor(Color.GRAY);
        }
    }

    /**
     * updates the humidity textview value, color is gray if the value is below the max value, red if above. The max value is set in the settings.
     * @return void
     */
    public void humidityValueArrived(String message) {
        humidityValue.setText(message);
        if (Integer.parseInt(message) > Integer.parseInt(getMaxHumidity())) {
            humidityValue.setText(message);
            humidityValue.setTextColor(Color.RED);
        }
        else {
            humidityValue.setText(message);
            humidityValue.setTextColor(Color.GRAY);
        }
    }

    /**
     * updates the temperature textview value, color is gray if the value is below the max value, red if above. The max value is set in the settings.
     * @return void
     */
    public void temperatureValueArrived(String message) {
        temperatureValue.setText(message);
        if (Integer.parseInt(message) > Integer.parseInt(getMaxTemperature())) {
            temperatureValue.setText(message);
            temperatureValue.setTextColor(Color.RED);
        }
        else {
            temperatureValue.setText(message);
            temperatureValue.setTextColor(Color.GRAY);
        }
    }

    /**
     * updates the loudness textview value, color is gray if the value is below the max value, red if above. The max value is set in the settings.
     * @return void
     */
    public void loudnessValueArrived(String message) {
        int loudness = 0;
        if (getMaxLoudness().equals("Quiet")) {
            loudness = 50;
        }
        else if (getMaxLoudness().equals("Moderate")) {
            loudness = 60;
        }
        else if (getMaxLoudness().equals("Loud")) {
            loudness = 70;
        }

        if (Integer.parseInt(message) < loudness) {
            loudnessValue.setText(message);
            loudnessValue.setTextColor(Color.GRAY);
        }
        else {
            loudnessValue.setText(message);
            loudnessValue.setTextColor(Color.RED);
        }
    }

    /**
     * Publishes the settings to the broker.
     * @return void
     */
    public void publishSettings() {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(CLIENT_ID, notConnected);
            Toast.makeText(context, notConnected, Toast.LENGTH_SHORT).show();
        } else {
            final String connected = "Connected";
            Log.e(CLIENT_ID, connected);
            mqttClient.publish(MAX_SETTINGS_PUB_TOPIC, getMaxPeople() + "," + getMaxHumidity() + "," + getMaxTemperature() + "," + getMaxLoudness(), QOS, null);
        }
    }

    // Methods to link TextView object to actual element on the screen on startup.


    /**
     * Updates the text of the peopleCount TextView.
     * @param textView the new text
     * @return void 
     */
    public void setPeopleCount(TextView textView) {
        this.peopleCount = textView;
    }

    /**
     * Updates the text of the temperatureValue TextView.
     * @param textView the new text
     * @return void
     */
    public void setTemperatureValue(TextView textView) {
        this.temperatureValue = textView;
    }

    /**
     * Updates the text of the humidityValue TextView.
     * @param textView the new text
     * @return void
     */
    public void setHumidityValue(TextView textView) {
        this.humidityValue = textView;
    }

    /**
     * Updates the text of the loudnessValue TextView.
     * @param textView the new text
     * @return void
     */
    public void setLoudnessValue(TextView textView) { this.loudnessValue = textView; }







    /**
     * Gets the corresponding MqttClient object of the BrokerConnection object.
     * @return MqttClient object
     */
    public MqttClient getMqttClient() {
        return mqttClient;
    }

    /**
     * Gets the current connection status
     * @return boolean
     */
    public boolean getConnectionStatus() {return isConnected; }

    /**
     * Sets the connection status used ONLY for testing
     */
    public static void setConnectionStatus(boolean status){isConnected = status; } //made isConnection static
}