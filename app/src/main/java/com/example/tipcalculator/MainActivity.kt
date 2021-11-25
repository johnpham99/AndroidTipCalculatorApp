package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
//  Create a private variable named etBaseAmount of type EditText. lateinit = late initialization (will be initialized in the onCreate(), not in constructor)
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvNumPeople: TextView
    private lateinit var btnUp: ImageView
    private lateinit var btnDown: ImageView
    private lateinit var tvBillAmount: TextView
    private lateinit var tvFinalTotalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvNumPeople = findViewById(R.id.tvNumPeople)
        btnUp = findViewById(R.id.btnUp)
        btnDown = findViewById(R.id.btnDown)
        tvBillAmount = findViewById(R.id.tvBillAmount)
        tvFinalTotalAmount = findViewById(R.id.tvFinalTotalAmount)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text =  "$progress%"
                computeValues()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeValues()
            }

        })

        var numPeople = 1;
        btnUp.setOnClickListener() {
            numPeople++
            tvNumPeople.text = numPeople.toString()
            computeValues()
        }

        btnDown.setOnClickListener() {
            if(numPeople != 1) {
                numPeople--
                tvNumPeople.text = numPeople.toString();
                computeValues()
            }
        }
    }

    private fun updateTipDescription(progress: Int) {
        val tipDescription = when(progress) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
            tvTipDescription.text = tipDescription

            val color = ArgbEvaluator().evaluate(
                progress.toFloat() / seekBarTip.max,
                ContextCompat.getColor(this, R.color.color_worst_tip),
                ContextCompat.getColor(this, R.color.color_best_tip)
            ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeValues() {
        if (etBaseAmount.text.isEmpty()) {
            tvBillAmount.text = " "
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvFinalTotalAmount.text = " "
            return
        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress.toDouble()
        val numPeople = tvNumPeople.text.toString().toDouble()

        val billAmount = baseAmount/numPeople;
        val tipAmount = (baseAmount * (tipPercent / 100))/numPeople;
        val totalAmount = billAmount + tipAmount
        val finalTotalAmount = baseAmount + (baseAmount * (tipPercent / 100))

        tvBillAmount.text = "%.2f".format(billAmount)
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvFinalTotalAmount.text = "%.2f".format(finalTotalAmount)
    }
}