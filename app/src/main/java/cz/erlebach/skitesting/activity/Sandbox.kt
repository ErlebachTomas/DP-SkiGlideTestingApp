package cz.erlebach.skitesting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.common.utils.wtf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.UUID

@SuppressLint("MissingPermission")
class Sandbox : AppCompatActivity() {

    private val myUUID = "00001101-0000-1000-8000-00805F9B34FB" // get ????
    private var MAC: String? = null;
    private lateinit var textView: TextView

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter.isEnabled

    private lateinit var bluetoothSocket: BluetoothSocket

    private lateinit var outputStream: OutputStream

    private var isDeviceConnected = false
    private var LEDSwitch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sandbox)

        bluetoothManager = applicationContext.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        textView = findViewById<TextView>(R.id.blTV)

        checkPermission()

        findViewById<Button>(R.id.bluetooth).setOnClickListener {

            if (!isDeviceConnected) {
                connectToBluetoothDevice()
            } else {
                controlLEDusingBluetooth()
            }

        }


        findViewById<Button>(R.id.json).setOnClickListener {

            if (!isDeviceConnected) {
                MAC = "10:51:07:73:F8:38"
                connectToBluetoothDevice()
            } else {
                lifecycleScope.launch {

                    readData()

                }
            }


        }


        findViewById<Button>(R.id.picker).setOnClickListener {

            selectDevice { device ->
                if (device != null) {
                    info("Selected ${device.name} MAC address: ${device.address}")
                    toast(this,device.address)
                    MAC = device.address
                } else {
                    info("No device selected")
                }
            }

        }


    }

    private fun readData() {

        if(!isDeviceConnected) {
            err("Device is not connected")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val outputStream = bluetoothSocket.outputStream
                outputStream.write('2'.code)

            } catch (e: IOException) {
                err("error writing")
                wtf("error writing", e)
            }

            try {
                val inputStream = bluetoothSocket.inputStream

                lg("Reading:")
                while (isActive) {
                    // dokud coroutine běží
                    val buffer = ByteArray(1024)
                    val bytes = inputStream.read(buffer)
                    val data = String(buffer, 0, bytes)
                    lg(data)

                    val jsonObject = JSONObject(data)
                    val airTemperature = jsonObject.getDouble("airTemperature")
                    val snowTemperature = jsonObject.getDouble("snowTemperature")
                    val humidity = jsonObject.getDouble("humidity")
                    val responseString = "Air Temperature: $airTemperature\nSnow Temperature: $snowTemperature\nHumidity: $humidity"

                    withContext(Dispatchers.Main) {
                        textView.text = responseString
                    }

                }
            } catch (e: IOException) {
                err("Error reading")
                wtf("Error", e)
            }
        }
    }

    private fun connectToBluetoothDevice() {

        if(MAC == null) {
            err("Please select device MAC")
            toast(this,"Please select device MAC")
            return;
        }

        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(MAC)
        if (device != null) {
            info(device.address + " " + device.name)
        } else {
            err("device not found")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {

                bluetoothSocket =
                    device.createRfcommSocketToServiceRecord(UUID.fromString(myUUID))!!

                bluetoothSocket.connect()

                info("connected")
                outputStream = bluetoothSocket.outputStream

                isDeviceConnected = true

            } catch (e: IOException) {
                err("error connecting")
                wtf("error connecting", e)
            }
        }
        textView.text = "connected"
        toast(this@Sandbox, "connected")
    }

    private fun controlLEDusingBluetooth() {

        if (LEDSwitch) {
            textView.text = "1"
        }
        else {
            textView.text = "0"
        }

        CoroutineScope(Dispatchers.IO).launch {

            try {
                if (LEDSwitch) {
                    info("write 1")
                    outputStream.write('1'.code)
                } else {
                    outputStream.write('0'.code)
                    info("write 0")
                }

                LEDSwitch = !LEDSwitch

            } catch (e: IOException) {
                err("error writing")
                wtf("error writing", e)
            }
        }
    }
    private fun selectDevice(callback: (BluetoothDevice?) -> Unit) {

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val deviceNames = pairedDevices?.map { it.name }?.toTypedArray()

        if (!deviceNames.isNullOrEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select")
            builder.setItems(deviceNames) { _, index ->
                val selectedDevice = pairedDevices.elementAt(index)
                callback(selectedDevice)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                callback(null)
                dialog.dismiss()
            }
            builder.setOnCancelListener { dialog ->
                callback(null)
                dialog.dismiss()
            }
            builder.show()
        } else {
            callback(null)
        }

    }


    private fun checkPermission() {
        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { //callback
        }
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true // pro api 31
            } else true
            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }
    }

    private fun disconnect() {
        try {
            isDeviceConnected = false
            bluetoothSocket.close()
        } catch (e: IOException) {
            err(e.printStackTrace().toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disconnect()
    }

}


