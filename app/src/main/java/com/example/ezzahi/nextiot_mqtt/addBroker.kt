package com.example.ezzahi.nextiot_mqtt

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText

class addBroker : AppCompatActivity() {
    var BrokerURL: EditText?=null
    var Port: EditText?=null
    var UserName: EditText?=null
    var PASSWORD: EditText?=null
    var Register: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_broker)
        returnData()
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id:Int = item!!.itemId
        if(id == android.R.id.home){
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun returnData(){
        Register = findViewById<View>(R.id.Register) as Button
        Register!!.setOnClickListener(View.OnClickListener {
            BrokerURL = findViewById<View>(R.id.BrokerURL) as EditText
            Port = findViewById<View>(R.id.Port) as EditText
            UserName = findViewById<View>(R.id.UserName) as EditText
            PASSWORD = findViewById<View>(R.id.PASSWORD) as EditText

            val BU = BrokerURL!!.text.toString()
            val PB = Port!!.text.toString()
            val UN = UserName!!.text.toString()
            val PW = PASSWORD!!.text.toString()

            // pass data back
            val intent = Intent()
            intent.putExtra("BROKER",BU)
            intent.putExtra("PORT",PB)
            intent.putExtra("USER",UN)
            intent.putExtra("PWD",PW)
            setResult(Activity.RESULT_OK,intent)
            finish()
        })}
}
