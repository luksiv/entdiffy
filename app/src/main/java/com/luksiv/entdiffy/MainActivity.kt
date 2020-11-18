package com.luksiv.entdiffy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luksiv.entdiffy.entities.Person
import com.luksiv.entdiffy.entities.PersonDiffResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val person_1 = Person("Lukas", "Sivickas", "Vilnius", "Lithuania", "22")
        val person_2 = Person("Edgar", "Zigis", "Kaunas", "Poland", "30")

        val a = PersonDiffResult(false, false, false, false, false)
    }
}