package com.example.ezzahi.nextiot_mqtt
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.BatteryManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener{

    val RequestCODE : Int = 1000
    private var client: MqttAndroidClient?=null
    var actionBar: ActionBar? = null
    var mdrawerLayout:DrawerLayout?=null
    var mToogle:ActionBarDrawerToggle?=null
    var Status:TextView?=null
    var OnLight:ToggleButton?=null
    var OnHeat:ToggleButton?=null
    var OnPrise:ToggleButton?=null
    var OnClim:ToggleButton?=null
    var battery:TextView?=null
    var temp:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        battery = findViewById<View>(R.id.battery) as TextView
        temp = findViewById<View>(R.id.temp) as TextView

        Status        = findViewById<View>(R.id.Status) as TextView
        OnLight       = findViewById<View>(R.id.light) as ToggleButton
        OnHeat        = findViewById<View>(R.id.heat) as ToggleButton
        OnClim        = findViewById<View>(R.id.clim) as ToggleButton
        OnPrise       = findViewById<View>(R.id.prise) as ToggleButton
        actionBar     = supportActionBar
        mdrawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        mToogle       = ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close)
        mToogle!!.syncState()
        mdrawerLayout!!.addDrawerListener(mToogle!!)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ff0099cc")))

        val naView: NavigationView = findViewById<View>(R.id.navView) as NavigationView

        naView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener
        { menuItem:MenuItem ->
            // onNavigationItemSelection(menuItem)
            val id:Int = menuItem.itemId
            if(id == R.id.AddBroker){
                Toast.makeText(applicationContext,"Enter your Broker please!!",Toast.LENGTH_LONG).show()
                //addNewBroker()
                //Toast.makeText(this,"Please enter your Broker", Toast.LENGTH_LONG).show()
                val intent= Intent(this,addBroker::class.java)
                this.startActivityForResult(intent, RequestCODE)
            }
            if(id == R.id.AbouUS){
            }
            if(id == R.id.ShareApp){
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val shareBody = "ENGAND"
                val shareSub = "https://www.youtube.com/channel/UCtXGGLspoIXnT_C0j1852Ag?view_as=subscriber"
                intent.putExtra(Intent.EXTRA_SUBJECT,shareBody)
                intent.putExtra(Intent.EXTRA_TEXT,shareSub)
                this.startActivity(Intent.createChooser(intent, "Share this App using"))
            }
            if (id == R.id.logout){
                finish()
            }

            true
        })
        OnLight!!.setOnCheckedChangeListener(this)
        OnHeat!!.setOnCheckedChangeListener(this)
        OnClim!!.setOnCheckedChangeListener(this)
        OnPrise!!.setOnCheckedChangeListener(this)

        // battery level
        LoadBatteryInfo()

    }

    // batter information
    fun LoadBatteryInfo() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryInfoReceiver, intentFilter)
    }

    fun updateBatteryData(intent: Intent) {
        val present: Boolean = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
        if (present) {
            val level:Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale:Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            var batteryPC:Int?=null
            var temperature:Float?=null

            if(level != -1 && scale!= -1){
                batteryPC = ((level / (scale.toFloat()))*100f).toInt()
                battery!!.text = "$batteryPC %"

            }

            val tempInfo:Int = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)
            if (tempInfo > 0){
                temperature = (tempInfo.toFloat())/10f
                temp!!.text = "$temperature°C"
            }
            if (batteryPC == 100 || temperature!! >= 40){
                val publishTopic = "nextIP"
                val publishMessage1 = "off"
                try {
                    client!!.publish(publishTopic, publishMessage1.toByteArray(), 0, false)
                } catch (e: MqttException) {
                    e.printStackTrace()
                }
                Toast.makeText(this, "Battery is full", Toast.LENGTH_LONG).show()
            }

        }
    }


    private val batteryInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateBatteryData(intent)

        }
    }
    //
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val publishMessage1 = "Lon"
        val publishMessage2 = "Loff"
        val publishMessage3 = "Hon"
        val publishMessage4 = "Hoff"
        val publishMessage5 = "Con"
        val publishMessage6 = "Coff"
        val publishMessage7 = "Pon"
        val publishMessage8 = "Poff"
        val publishTopic = "nextIP"
        ////// lighting ///////////
        if (OnLight!!.isChecked){
            OnLight!!.setBackgroundResource(R.drawable.icon6)
            try {
                client!!.publish(publishTopic, publishMessage1.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }

        }else{
            OnLight!!.setBackgroundResource(R.drawable.icon1)
            try {
                client!!.publish(publishTopic, publishMessage2.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
        ////////// Heating ////////////
        if (OnHeat!!.isChecked){
            OnHeat!!.setBackgroundResource(R.drawable.icon8)

            try {
                client!!.publish(publishTopic, publishMessage3.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }else{
            OnHeat!!.setBackgroundResource(R.drawable.icon7)
            try {
                client!!.publish(publishTopic, publishMessage4.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
        //////// Climatisation /////////////
        if (OnClim!!.isChecked){
            OnClim!!.setBackgroundResource(R.drawable.icon9)
            try {
                client!!.publish(publishTopic, publishMessage5.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }else{
            OnClim!!.setBackgroundResource(R.drawable.icon10)
            try {
                client!!.publish(publishTopic, publishMessage6.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
        ////// prise ////////
        if (OnPrise!!.isChecked){
            OnPrise!!.setBackgroundResource(R.drawable.icon2)

            try {
                client!!.publish(publishTopic, publishMessage7.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }else{
            OnPrise!!.setBackgroundResource(R.drawable.icon3)
            try {
                client!!.publish(publishTopic, publishMessage8.toByteArray(), 0, false)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(mToogle!!.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        //super.onBackPressed()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to exit this app ? ")
        builder.setCancelable(true)
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            finish()
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        val alert: AlertDialog = builder.create()
        alert.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            RequestCODE ->{
                if(resultCode == Activity.RESULT_OK){
                    //Get the message
                    val BROKER:String = data!!.getStringExtra("BROKER")
                    val PORT:String = data.getStringExtra("PORT")
                    val USER:String = data.getStringExtra("USER")
                    val PWD:String = data.getStringExtra("PWD")
                    Toast.makeText(this,"Broker saved",Toast.LENGTH_LONG).show()

                    val MQTT= "tcp://$BROKER:"
                    val clientId = MqttClient.generateClientId()
                    client = MqttAndroidClient(this.applicationContext, MQTT + PORT, clientId)
                    val options= MqttConnectOptions()
                    options.userName = USER
                    options.password = PWD.toCharArray()
                    try {
                        val token = client!!.connect(options)
                        token.actionCallback = object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken) {
                                // We are connected
                                Toast.makeText(this@MainActivity,"Connected",Toast.LENGTH_LONG).show()
                                //Subscribe()
                                Status!!.text = "Broker connected with success"
                            }
                            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                                // Something went wrong e.g. connection timeout or firewall problems
                                Toast.makeText(this@MainActivity,"Connection failed",Toast.LENGTH_LONG).show()
                                Status!!.text = "Try again please!!"
                            }
                        }
                    } catch (ex: MqttException) {
                        ex.printStackTrace()
                    }

                    client!!.setCallback(object : MqttCallback {
                        override fun connectionLost(throwable: Throwable) {}
                        override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                            //Toast.makeText(applicationContext,"Incoming message: " + String(mqttMessage.payload),Toast.LENGTH_LONG).show()



                        }
                        override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
                    })


                }else{
                    Toast.makeText(this,"You must enter a Broker",Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    fun OnLight(v:View){
        val topic1 = "nextIP"
        val mesg_publish = "Lon"
        try {
            client!!.publish(topic1, mesg_publish.toByteArray(),0,false)
            Toast.makeText(this,"Message published",Toast.LENGTH_LONG).show()
        }catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }
}
