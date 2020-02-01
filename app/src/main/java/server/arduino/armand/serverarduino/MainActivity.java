package server.arduino.armand.serverarduino;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private final String host = "192.168.43.103";
    private final int port = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void toggleSensor1(View view) {
        new NoParamTask().execute("TOGGLE_SENSOR1");
    }

    public void toggleSensor2(View view) {
        new NoParamTask().execute("TOGGLE_SENSOR2");
    }

    public void toggleSensor3(View view) {
        new NoParamTask().execute("TOGGLE_SENSOR3");
    }

    public void toggleSensor4(View view) {
        new NoParamTask().execute("TOGGLE_SENSOR4");
    }

    public void toggleLed1(View view) {
        new NoParamTask().execute("TOGGLE_LED1");
    }

    public void toggleLed2(View view) {
        new NoParamTask().execute("TOGGLE_LED2");
    }

    public void toggleLed3(View view) {
        new NoParamTask().execute("TOGGLE_LED3");
    }

    public void toggleLed4(View view) {
        new NoParamTask().execute("TOGGLE_LED4");
    }

    private class NoParamTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                Socket client = new Socket(host, port);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                Log.d("SENT_PARAM", params[0]);
                writer.write(params[0]);
                writer.flush();

                if (params[0].startsWith("TOGGLE_LED")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String result = reader.readLine();
                    Log.d("RECV_PARAM", result);

                    String[] tokens = result.split("_");
                    final int btnIndex = Integer.parseInt(tokens[0]);
                    final String state = tokens[1];

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button btn;
                            switch (btnIndex) {
                                case 1: btn = findViewById(R.id.toggleBtn1); break;
                                case 2: btn = findViewById(R.id.toggleBtn2); break;
                                case 3: btn = findViewById(R.id.toggleBtn3); break;
                                case 4: btn = findViewById(R.id.toggleBtn4); break;
                                default: btn = null;
                            }
                            if (state.equals("ON")) {
                                btn.setBackgroundColor(Color.GREEN);
                            } else {
                                btn.setBackgroundColor(Color.RED);
                            }
                        }
                    });
                }

                client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
