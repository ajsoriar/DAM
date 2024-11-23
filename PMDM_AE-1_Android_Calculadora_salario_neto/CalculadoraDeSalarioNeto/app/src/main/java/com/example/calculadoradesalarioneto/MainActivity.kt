package com.example.calculadoradesalarioneto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // btnCalculateNetSalary is the id of the button in the layout
        // I need to add the listener to the button
        // and navifate to the MainActivity2

        val btnCalculateNetSalary = findViewById<Button>(R.id.btnCalculateNetSalary)
        btnCalculateNetSalary.setOnClickListener {

            // --------------------------------------------------
            // Get the inputs from the UI
            // --------------------------------------------------

            /*
                UI Inputs:
                ▸ Salario bruto anual.
                ▸ Numero de pagas.
                ▸ Edad.
                ▸ Grupo profesional.
                ▸ Grado de discapacidad.
                ▸ Estado civil.
                ▸ Número de hijos.
            */

            // ----
            // 1.
            // ----

            var grossSalary = findViewById<EditText>(R.id.etGrossSalary).text.toString()

            // ----
            // 2. - Get the selected RadioButton value
            // ----
            var numberOfPayments = 0

                // Get the RadioGroup
                val radioGroup = findViewById<RadioGroup>(R.id.rgNumberOfPayments)

                // Get the selected RadioButton ID
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId

                // Check if a RadioButton is selected
                if (selectedRadioButtonId != -1) {
                    // Find the selected RadioButton
                    val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

                    // Get the text of the selected RadioButton
                    if (selectedRadioButton.text.toString() == "14 pagas") {
                        numberOfPayments = 14
                    } else if (selectedRadioButton.text.toString() == "12 pagas") {
                        numberOfPayments = 12
                    }

                    // Use the value (convert to integer if needed)
                    val numericValue = numberOfPayments
                    println("Number of payments: $numericValue")
                } else {
                    println("No RadioButton is selected")
                }

            // ----
            // 3.
            // ----

            var age = findViewById<EditText>(R.id.etAge).text.toString()
                //val professionalGroup = findViewById<EditText>(R.id.spProfessionalGroup).text.toString()

            // ----
            // 4.
            // ----

                //var professionalGroupText = findViewById<EditText>(R.id.rgProfessionalGroup)
                //val professionalGroupIndex = findViewById<Number>(R.id.rgProfessionalGroup)

                // Find the Spinner
                val professionalGroupSpinner = findViewById<Spinner>(R.id.spProfessionalGroup)

                // Get the selected item's text (as a String)
                val professionalGroupText = professionalGroupSpinner.selectedItem.toString()

                // Get the selected item's index (position)
                val professionalGroupIndex = professionalGroupSpinner.selectedItemPosition

            // ----
            // 5.
            // ----

                val checkBox = findViewById<CheckBox>(R.id.cbDisabilityGrade)// Find the CheckBox
                val isChecked = checkBox.isChecked // Check if the CheckBox is checked
                val text = checkBox.text.toString() // Retrieve the text of the CheckBox

                var disabilityGrade = false // Initialize the variable
                // Use the values as needed
                if (isChecked) {
                    println("CheckBox is checked: $text")
                    disabilityGrade = true
                } else {
                    println("CheckBox is not checked")
                    disabilityGrade = false
                }

            // ----
            // 6.
            // ----

                // Find the Spinner
                val civilStatusSpinner = findViewById<Spinner>(R.id.spCivilStatus)

                // Get the selected item's text (as a String)
                val civilStatusText = civilStatusSpinner.selectedItem.toString()

                // Get the selected item's index (position)
                val civilStatusIndex = civilStatusSpinner.selectedItemPosition

            // ----
            // 7.
            // ----

                val numberOfChildren = findViewById<EditText>(R.id.etNumberOfChildren).text.toString()

            // --------------------------------------------------
            // console log all the inputs
            // --------------------------------------------------

            println("[andres] grossSalary: $grossSalary")
            println("[andres] numberOfPayments: $numberOfPayments")
            println("[andres] age: $age")
            println("[andres] professionalGroup Text: $professionalGroupText")
            println("[andres] professionalGroup Index: $professionalGroupIndex")
            println("[andres] disabilityGrade: $disabilityGrade")
            println("[andres] civilStatus Text: $civilStatusText")
            println("[andres] civilStatus Index: $civilStatusIndex")
            println("[andres] numberOfChildren: $numberOfChildren")

            // --------------------------------------------------
            // Calculate the net salary
            // --------------------------------------------------

            // Navigate to the MainActivity2
            val intent = Intent(this, MainActivity2::class.java)

            // Add all the data to the intent in some kind of structure
            intent.putExtra("grossSalary", grossSalary)
            intent.putExtra("numberOfPayments", numberOfPayments)
            intent.putExtra("age", age)
            intent.putExtra("professionalGroupText", professionalGroupText)
            intent.putExtra("professionalGroupIndex", professionalGroupIndex)
            intent.putExtra("disabilityGrade", disabilityGrade)
            intent.putExtra("civilStatusText", civilStatusText)
            intent.putExtra("civilStatusIndex", civilStatusIndex)
            intent.putExtra("numberOfChildren", numberOfChildren)

            // Start the activity
            startActivity(intent)
        }

    }
}