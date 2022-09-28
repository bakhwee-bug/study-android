package com.example.ch17_database2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ch17_database2.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val file = File(filesDir, "test.txt")


        binding.WriteBtn.setOnClickListener{
            val writeStream: OutputStreamWriter = file.writer()
            writeStream.write("hello world")
            writeStream.flush()
        }

        binding.CheckBtn.setOnClickListener {
            val readStream: BufferedReader = file.reader().buffered()
            readStream.forEachLine {
                Log.d("bak-hwee", "$it")
            }
        }


        /*openFileOutput("test.txt", Context.MODE_PRIVATE).use{
            it.write("hello world!!".toByteArray())
        }
        openFileInput("test.txt").bufferedReader().forEachLine {
            Log.d("bak-hwee","$it")
        }*/



    }
}