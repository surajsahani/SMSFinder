package com.martialcoder.smsfinder

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.martialcoder.smsfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , InterfacePresenter.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var PermissionLauncher: ActivityResultLauncher<String>
    private lateinit var presenter: Persenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = Persenter(this, this)
        UI(binding.root, this)

        PermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    presenter.fetch(
                        binding.mobileNumber.text.toString(),
                        binding.daysCount.text.toString()
                    )
                } else {
                    showToast(getString(R.string.permission_error))
                    return@registerForActivityResult
                }
            }

        binding.mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.tvCount.isVisible)
                    binding.tvCount.visibility = View.GONE
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.daysCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.tvCount.isVisible)
                    binding.tvCount.visibility = View.GONE
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.submit.setOnClickListener {
            GetSMS()

        }
    }

    private fun GetSMS() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                presenter.fetch(
                    binding.mobileNumber.text.toString(),
                    binding.daysCount.text.toString()
                )
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS) -> {
                PermissionLauncher.launch(android.Manifest.permission.READ_SMS)
            }
            else -> {
                PermissionLauncher.launch(android.Manifest.permission.READ_SMS)
            }
        }
    }


    override fun Mobile(message: String) {
        showLongToast(message)
    }

    override fun Days(message: String) {
        showToast(message)
    }

    override fun invalid(message: String) {
        showToast(message)
    }

    override fun Sucess(message: String) {
        binding.tvCount.visibility = View.VISIBLE
        binding.tvCount.text= message
        binding.tvCount.setTextColor(getColor(R.color.success))
    }

    override fun Failure(message: String) {
        binding.tvCount.visibility =  View.VISIBLE
        binding.tvCount.text = message
        binding.tvCount.setTextColor(getColor(R.color.error))
    }
}
