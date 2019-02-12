package com.leo.camera2demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidget()
    }

    private fun initWidget() {
        btn_surface_view.setOnClickListener {
            startActivity(Intent(this, SurfaceViewActivity::class.java))
        }
    }

}
