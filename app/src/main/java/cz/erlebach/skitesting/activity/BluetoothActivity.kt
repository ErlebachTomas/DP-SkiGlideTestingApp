package cz.erlebach.skitesting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
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
import cz.erlebach.skitesting.databinding.ActivityBluetoothBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBluetoothBinding

    companion object  {
        val myUUID = "00001101-0000-1000-8000-00805F9B34FB" // get ????
        val TAG = "BluetoothActivity"
    }

    private lateinit var textView: TextView

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter.isEnabled

    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var outputStream: OutputStream

    private var isDeviceConnected = false
    private var LEDSwitch = true

    private var responseJSON: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothManager = applicationContext.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        textView = findViewById<TextView>(R.id.bl_output)

        checkPermission()

        binding.blOk.setOnClickListener {
            closeAndReturn()
        }

        binding.blLed.setOnClickListener {
                controlLEDusingBluetooth()
        }


        binding.blJson.setOnClickListener {
          lifecycleScope.launch {
                    readData()
                }
        }

        findViewById<Button>(R.id.bl_btn_connect).setOnClickListener {

            if (!isDeviceConnected) {
                // vyber MAC a pripojeni
                selectDevice { device ->
                    if (device != null) {
                        info("Selected ${device.name} MAC address: ${device.address}")
                        connectToBluetoothDevice(device.address)
                    } else {
                        info("No device selected")
                    }
                }
            } else {
                disconnect()
            }
        }
    }
    /** Návratová funkce s výsledkem */
    private fun closeAndReturn() {
        val intent = Intent()
        intent.putExtra(TAG, responseJSON)
        setResult(Activity.RESULT_OK, intent)
        finish()
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
                    responseJSON = data

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

    private fun connectToBluetoothDevice(MAC: String?) {

        if(MAC == null) {
            err("Please select device MAC")
            toast(this,getString(R.string.bluetooth_not_mac))
            return;
        }

        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(MAC)
        if (device != null) {
            info(device.address + " " + device.name)
        } else {
            err("device not found")
            toast(this,getString(R.string.bluetooth_not_mac))
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

                withContext(Dispatchers.Main) {
                    binding.blLed.isEnabled = true
                    binding.blJson.isEnabled = true
                    binding.blBtnConnect.text = getString( R.string.bluetooth_disconnect)

                    textView.text = getString(R.string.bluetooth_connected)
                    toast(this@BluetoothActivity, getString(R.string.bluetooth_connected))

                }

            } catch (e: IOException) {
                err("error connecting")
                wtf("error connecting", e)
                withContext(Dispatchers.Main) {

                    toast(this@BluetoothActivity, getString(R.string.connection_error))

                }
            }
        }

    }

    private fun controlLEDusingBluetooth() {

        if (LEDSwitch) {
            textView.text = "LED ON"
        }
        else {
            textView.text = "LED OFF"
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
            builder.setTitle(getString(R.string.bluetooth_select))
            builder.setItems(deviceNames) { _, index ->
                val selectedDevice = pairedDevices.elementAt(index)
                callback(selectedDevice)
            }
            builder.setNegativeButton(getString(R.string.bluetooth_cancel)) { dialog, _ ->
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
            bluetoothSocket.close()

            isDeviceConnected = false
            textView.text = getString(R.string.bluetooth_disconnected)
            binding.blJson.isEnabled = false
            binding.blLed.isEnabled = false
            binding.blBtnConnect.text = getString(R.string.bluetooth_connect)
        } catch (e: IOException) {
            err(e.printStackTrace().toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isDeviceConnected) {
            disconnect()
        }
    }

}


