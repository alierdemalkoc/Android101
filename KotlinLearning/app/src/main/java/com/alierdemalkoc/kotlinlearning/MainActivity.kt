package com.alierdemalkoc.kotlinlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
    lateinit var str: String
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(val i = 0; i <= str.length; i++) {
            for(var j = i+1; j <= str.length; j++) {
            if(str[j] == str[i]) {
                return str[j];
            }
        }
        }
    }

    //val myArray = arrayOf(5,4,2,3)
        // myArray.sortedArray()
        //println(myArray[0])
        //var min = 1
        //val b = A.sortedArray()

    }

    }
