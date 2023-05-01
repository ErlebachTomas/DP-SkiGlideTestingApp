package cz.erlebach.skitesting.activity

import android.Manifest
import android.annotation.SuppressLint
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
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.common.utils.wtf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@SuppressLint("MissingPermission")
class Sandbox : AppCompatActivity() {

    private val myUUID = "00001101-0000-1000-8000-00805F9B34FB"
    private val MAC = "00:21:07:34:D3:16"
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

        findViewById<Button>(R.id.bluetooth).setOnClickListener {
            if (!isDeviceConnected) {
                connectToBluetoothDevice()
            } else {
                writeOneToBluetoothDevice()
            }
        }

    }

    private fun connectToBluetoothDevice() {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(this.MAC)
        if (device != null) {
            info(device.address + " " + device.name)
        } else {
            err("device not found")
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {

                bluetoothSocket =
                    device?.createRfcommSocketToServiceRecord(UUID.fromString(myUUID))!!
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

    private fun writeOneToBluetoothDevice() {

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

    override fun onDestroy() {
        super.onDestroy()
        try {
            isDeviceConnected = false
            bluetoothSocket.close()
        } catch (e: IOException) {
            err(e.printStackTrace().toString())
        }
    }

}


