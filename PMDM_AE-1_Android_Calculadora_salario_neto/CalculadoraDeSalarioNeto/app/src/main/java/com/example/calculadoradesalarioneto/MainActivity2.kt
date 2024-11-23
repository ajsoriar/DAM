package com.example.calculadoradesalarioneto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadoradesalarioneto.MainActivity2

// print title function
fun printTitle(title: String) {
    // Print a title with a line above and below

    // start with a break line
    println()
    println("[andres] ${"-".repeat(title.length)}")
    println("[andres] $title")
    println("[andres] ${"-".repeat(title.length)}")

}

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // btnCalculateNetSalary is the id of the button in the layout
        // I need to add the listener to the button
        // and navifate to the MainActivity2

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // get received data from previous activity

        var grossSalary = intent.getStringExtra("grossSalary")
        var numberOfPayments = intent.getIntExtra("numberOfPayments", 0)
        var age = intent.getStringExtra("age")
        var professionalGroupText = intent.getStringExtra("professionalGroupText")
        var professionalGroupIndex = intent.getIntExtra("professionalGroupIndex", 0)
        var disabilityGrade = intent.getBooleanExtra("disabilityGrade", false)
        var civilStatusText = intent.getStringExtra("civilStatusText")
        var civilStatusIndex = intent.getIntExtra("civilStatusIndex", 0)
        var numberOfChildren = intent.getStringExtra("numberOfChildren")

        // Print received data

        printTitle("Received Data")
        println("[andres] Gross Salary: $grossSalary")
        println("[andres] Number of Payments: $numberOfPayments")
        println("[andres] Age: $age")
        println("[andres] Professional Group: $professionalGroupText ($professionalGroupIndex)")
        println("[andres] Disability Grade: $disabilityGrade")
        println("[andres] Civil Status: $civilStatusText ($civilStatusIndex)")
        println("[andres] Number of Children: $numberOfChildren")

        // Print inside android:id="@+id/multiLineTextViewSummary"

        val multiLineTextViewSummary = findViewById<android.widget.TextView>(R.id.multiLineTextViewSummary)
        multiLineTextViewSummary.text = """
            Gross Salary: $grossSalary
            Number of Payments: $numberOfPayments
            Age: $age
            Professional Group: $professionalGroupText ($professionalGroupIndex)
            Disability Grade: $disabilityGrade
            Civil Status: $civilStatusText ($civilStatusIndex)
            Number of Children: $numberOfChildren
        """.trimIndent()

        /*

        Calculate the net salary in Spain
        use: grossSalary, numberOfPayments, age, professionalGroupIndex, disabilityGrade, civilStatusIndex, numberOfChildren

            professionalGroupIndex
                1. Ingenieros y Licenciados
                2. Ingenieros Técnicos, Peritos y Ayudantes Titulados
                3. Jefes Administrativos y de Taller
                4. Ayudantes no Titulados
                5. Oficiales Administrativos
                6. Subalternos
                7. Auxiliares Administrativos
                8. Oficiales de primera y segunda
                9. Oficiales de tercera y Especialistas
                10. Peones
                11. Trabajadores menores de dieciocho años, cualquiera

            civilStatusIndex
                1. Soltero
                2. Casado
                3. Otros

        */

        // ----------------------------------------------------------------
        // Calculate the net salary
        // ----------------------------------------------------------------

        // Convert grossSalary to Double
        val grossSalaryDouble = grossSalary?.toDoubleOrNull() ?: 0.0

        // Calculate Social Security Contributions (6.35% of gross salary)
        val socialSecurityRate = 0.0635
        val socialSecurityContribution = grossSalaryDouble * socialSecurityRate

        // Calculate taxable income after social security deductions
        val taxableIncome = grossSalaryDouble - socialSecurityContribution

        // Determine the base IRPF rate based on taxable income
        var irpfRate = when {
            taxableIncome <= 12450 -> 0.19
            taxableIncome <= 20200 -> 0.24
            taxableIncome <= 35200 -> 0.30
            taxableIncome <= 60000 -> 0.37
            taxableIncome <= 300000 -> 0.45
            else -> 0.47
        }

        // Adjust IRPF rate based on personal circumstances

        // Adjust for age (example adjustment)

        // age, from text to int
        val ageInt = age?.toIntOrNull() ?: 0

        if (ageInt >= 65) {
            irpfRate -= 0.02 // Deduct 2% for being 65 or older
        }

        // Adjust for civil status
        irpfRate -= when (civilStatusIndex) {
            2 -> 0.01 // Married
            3 -> 0.005 // Others
            else -> 0.0 // Single
        }

        // Adjust for number of children

        // numberOfChildren, from text to int
        val numberOfChildrenInt = numberOfChildren?.toIntOrNull() ?: 0

        irpfRate -= numberOfChildrenInt * 0.005 // Deduct 0.5% per child

        // Adjust for disability grade

        // disabilityGrade, from boolean to int
        val disabilityGradeInt = if (disabilityGrade) 100 else 0

        irpfRate -= when (disabilityGradeInt) {
            in 33..65 -> 0.02 // Deduct 2% for disability grade between 33% and 65%
            in 66..100 -> 0.04 // Deduct 4% for disability grade above 66%
            else -> 0.0
        }

        // Ensure IRPF rate is within logical bounds
        if (irpfRate < 0.0) irpfRate = 0.0
        if (irpfRate > 0.47) irpfRate = 0.47

        // Calculate IRPF amount
        val irpfAmount = taxableIncome * irpfRate

        // Calculate net salary
        val netSalary = taxableIncome - irpfAmount

        // Calculate net salary per payment
        val netSalaryPerPayment = if (numberOfPayments > 0) netSalary / numberOfPayments else netSalary

        // Format numbers to two decimal places
        fun format(value: Double) = String.format("%.2f", value)

        // Output results
        printTitle("Net Salary Calculation")
        println("[andres] Gross Salary: €${format(grossSalaryDouble)}")
        println("[andres] Social Security Contribution (6.35%): €${format(socialSecurityContribution)}")
        println("[andres] Taxable Income: €${format(taxableIncome)}")
        println("[andres] Adjusted IRPF Rate: ${format(irpfRate * 100)}%")
        println("[andres] IRPF Amount: €${format(irpfAmount)}")
        println("[andres] Net Salary: €${format(netSalary)}")
        println("[andres] Net Salary per Payment ($numberOfPayments payments): €${format(netSalaryPerPayment)}")

        // ----------------------------------------------------------------
        // Show the net salary
        // ----------------------------------------------------------------

        // Print inside android:id="@+id/multiLineTextViewResults"

        val multiLineTextViewResults = findViewById<android.widget.TextView>(R.id.multiLineTextViewResults)
        multiLineTextViewResults.text = """
            Gross Salary: €${format(grossSalaryDouble)}
            Social Security Contribution (6.35%): €${format(socialSecurityContribution)}
            Taxable Income: €${format(taxableIncome)}
            Adjusted IRPF Rate: ${format(irpfRate * 100)}%
            IRPF Amount: €${format(irpfAmount)}
            Net Salary: €${format(netSalary)}
            Net Salary per Payment ($numberOfPayments payments): €${format(netSalaryPerPayment)}
        """.trimIndent()

        // Print inside android:id="@+id/tvResultAnualGrossSalary"

        val tvResultAnualGrossSalary = findViewById<android.widget.TextView>(R.id.tvResultAnualGrossSalary)
        tvResultAnualGrossSalary.text = "€${format(netSalary)}"

        // Print inside android:id="@+id/tvResultMenstrualGrossSalary"

        val tvResultMenstrualGrossSalary = findViewById<android.widget.TextView>(R.id.tvResultMenstrualGrossSalary)
        val grossSalaryPerPayment = netSalary / numberOfPayments
        tvResultMenstrualGrossSalary.text = "€${format(grossSalaryPerPayment)}"
    }
}