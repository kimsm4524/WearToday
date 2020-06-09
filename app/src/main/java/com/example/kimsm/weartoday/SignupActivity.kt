package com.example.kimsm.weartoday

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    val gender = arrayListOf("남","여")
    val style = arrayListOf("minimal","casual")
    val figure = arrayListOf("마름","보통","건장")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        var genderAdater = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, gender)
        var styleAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, style)
        var figureAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, figure)
        gendert.adapter = genderAdater
        stylet.adapter = styleAdapter
        figuret.adapter = figureAdapter

        signup_check.setOnClickListener {
            if(new_pass.text.toString()==pass_check.text.toString())
            {
                val intent = Intent(this,SendSignup::class.java)
                intent.putExtra("id", new_id.text.toString())
                intent.putExtra("pass", new_pass.text.toString())
                intent.putExtra("nickname",nicknamet.text.toString())
                intent.putExtra("age",aget.text.toString())
                intent.putExtra("gender",gender[gendert.selectedItemPosition])
                intent.putExtra("style",style[stylet.selectedItemPosition])
                intent.putExtra("figure",figure[figuret.selectedItemPosition])
                startActivity(intent)
            }
            else
            {
                new_pass.setText("")
                pass_check.setText("")
                Toast.makeText(this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
