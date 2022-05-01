package app.taplock.taplock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView pairedList ;

    BluetoothAdapter bluetoothAdapter;
    OutputStream mmOutStream = null;
    InputStream mmInStream = null;
    BluetoothSocket btSocket = null;
    ConnectedThread socket;

    String token;
    //RelativeLayout loadingLT;

    ArrayList<String> tokensList;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        //unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "Iniciando", Toast.LENGTH_SHORT).show();

        if (getIntent() != null && getIntent().getData() != null)
        {
            Uri uri = getIntent().getData();
            if (uri != null) {
                //loadingLT.setVisibility(View.VISIBLE);
                String url = uri.toString();
                token = url.substring(30);

                loadData();
                if(!tokensList.isEmpty())
                {

                    Boolean already_exist = false;

                    for(int i = 0; i < tokensList.size(); i++)
                    {
                        if(token.equals(tokensList.get(i)))
                        {
                            already_exist = true;
                            break;
                        }
                    }

                    if(already_exist)
                    {
                        Toast.makeText(getApplicationContext(), "Este dato ya existe", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Añadiendo:" + token, Toast.LENGTH_SHORT).show();
                        insertItem(token);
                        saveData();
                        Intent intent = new Intent(this, Menu.class);
                        startActivity(intent);
                        finish();
                    }

                }

                //loadingLT.setVisibility(View.GONE);
            }
        }
        else
        {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
            finish();
        }


        // ===============================
        // ===============================


        /*// Check if support bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth

        }

        // Enable BT
        if (!bluetoothAdapter.isEnabled()) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.enable();
        }

        String test = "0";

        // List paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        String xd = "";
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address+
                test = deviceHardwareAddress;
                //Toast.makeText(getApplicationContext(),deviceName + "" + deviceHardwareAddress,Toast.LENGTH_SHORT).show();
                xd = xd + deviceName + " | " + deviceHardwareAddress + ",   \n";
            }
        }
        pairedList.setText(xd);

        String address = "00:14:03:05:09:B7";
        Toast.makeText(getApplicationContext(),address, Toast.LENGTH_SHORT).show();

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        final BluetoothSocket mmSocket;

        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            //It is a valid MAC address.
            /// Get a BluetoothSocket for a connection with the
            // given BluetoothDevice

            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("ca66b33c-fef7-11eb-9a03-0242ac130003"));
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                tmp = (BluetoothSocket) m.invoke(device, 1);
            } catch (IOException | NoSuchMethodException e) {
                Log.e(TAG, "create() failed", e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            btSocket = tmp;
            try {
                bluetoothAdapter.cancelDiscovery();
                btSocket.connect();
                mmOutStream = btSocket.getOutputStream();
                mmInStream  = btSocket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = new ConnectedThread(btSocket);
            socket.write("hola");
            //socket.run();
            PrimeThread p = new PrimeThread(143);
            socket.start();
            //p.start();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"CACA", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tokensList);
        editor.putString("test02", json);
        editor.apply();
    }

    private boolean loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("test02", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        tokensList = gson.fromJson(json, type);

        if (tokensList == null) {
            tokensList = new ArrayList<>();
        }

        return true;
    }

    private void insertItem(String data) {
        tokensList.add(new String(data));
        //mAdapter.notifyItemInserted(mExampleList.size());
    }

    // ===========================================
    // ===========================================


    class PrimeThread extends Thread {
        long minPrime;
        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            // compute primes larger than minPrime
            //socket.run();
        }
    }


        public void sendData(View view) throws IOException {
            String data = "123";
            mmOutStream.write(data.getBytes());
        }


        String InputMessage = "";
        public void buildMessage(String input)
        {
            InputMessage = InputMessage + input;

            if(InputMessage.length() >= 10)
            {
                Toast.makeText(getApplicationContext(), "Completed: " + InputMessage, Toast.LENGTH_SHORT).show();
                InputMessage = "";
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Building " + InputMessage.length(), Toast.LENGTH_SHORT).show();
            }
        }


    public class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;
            private byte[] mmBuffer; // mmBuffer store for the stream
            Executor mainExecutor = ContextCompat.getMainExecutor(MainActivity.this);


            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the input and output streams; using temp objects because
                // member streams are final.
                try {
                    tmpIn = socket.getInputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating input stream", e);
                }
                try {
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when creating output stream", e);
                }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                mmBuffer = new byte[1024];
                int numBytes; // bytes returned from read()
                int inputBuilder = 0;
                String message = "";

                // Keep listening to the InputStream until an exception occurs.
                while (true) {
                    try {
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        String incomingMessage = new String(mmBuffer, 0, numBytes);


                            mainExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), incomingMessage, Toast.LENGTH_SHORT).show();
                                    //buildMessage(incomingMessage);

                                }
                            });



                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnectedddd", e);
                        Toast.makeText(getApplicationContext(), "Input stream was disconnected", Toast.LENGTH_SHORT).show();

                        break;
                    }
                }
            }

            // Call this from the main activity to send data to the remote device.
            public void write(String data) {
                try {
                    mmOutStream.write(data.getBytes());

                } catch (IOException e) {
                    Log.e(TAG, "Error occurred when sending data", e);

                    Toast.makeText(getApplicationContext(), "Error occurred when sending data", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Couldn't send data to the other device", Toast.LENGTH_SHORT).show();
                }
            }

            // Call this method from the main activity to shut down the connection.
            public void cancel() {
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        }
}