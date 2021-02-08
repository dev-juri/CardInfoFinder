package com.oluwafemi.cardinfofinder.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.oluwafemi.cardinfofinder.R
import com.oluwafemi.cardinfofinder.databinding.ActivityMainBinding
import com.oluwafemi.cardinfofinder.repository.RepositoryImpl
import com.oluwafemi.cardinfofinder.viewmodel.DataState
import com.oluwafemi.cardinfofinder.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var permissionToUseCameraAccepted = false

    private var permissions: Array<String> = arrayOf(android.Manifest.permission.CAMERA)
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageBitmap: Bitmap
    private val repository = RepositoryImpl()
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.ViewModelFactory(repository)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionToUseCameraAccepted = if (requestCode == REQUEST_IMAGE_CAPTURE) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if (!permissionToUseCameraAccepted) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Please grant ${getString(R.string.app_name)} permission to use camera")
                .setPositiveButton("Okay") { d, _ ->
                    requestPermission()
                    dispatchTakePictureIntent()
                    d.dismiss()
                }
                .setNegativeButton("Exit") { d, _ ->
                    d.dismiss()
                }
        } else {
            dispatchTakePictureIntent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.scanCard.setOnClickListener {
            if (checkSelfPermission("android.Manifest.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                requestPermission()
            } else {
                dispatchTakePictureIntent()
            }
        }

        binding.cardNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.cardNumber.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 16) {
                    val userInput = p0.toString().toLong()
                    viewModel.fetchDetails(userInput)
                    binding.cardNumber.error = ""
                } else {
                    binding.cardNumber.error = "Card Number should be 16digits"
                    binding.bottomSheet.visibility = View.GONE
                }
            }
        })

        viewModel.dataState.observe(this, { state ->
            when (state) {
                DataState.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.cardNumber.isClickable = true
                    binding.cardNumber.isFocusable = true
                    binding.bottomSheet.visibility = View.VISIBLE
                }
                DataState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.cardNumber.isClickable = true
                    binding.cardNumber.isFocusable = true
                    binding.bottomSheet.visibility = View.GONE
                    Snackbar.make(
                        binding.progressBar,
                        "Something went wrong",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.bottomSheet.visibility = View.GONE
                    binding.cardNumber.isClickable = false
                    binding.cardNumber.isFocusable = false
                }
            }
        })

        viewModel.cardDetails.observe(this, {
            binding.cardBrand.text = "Card Type: ${it.cardName}"
            binding.cardBank.text = "Bank: ${it.bankName}"
            binding.cardCountry.text = "Country: ${it.countryName}"
            binding.cardType.text = "Card Type: ${it.cardType}"
        })
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            REQUEST_IMAGE_CAPTURE
        )
    }

    private fun detectText(inputBitmap: Bitmap) {
        val recognizer = TextRecognition.getClient()

        recognizer.process(InputImage.fromBitmap(inputBitmap, 0))
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                for (block in visionText.textBlocks) {
                    val blockText = block.text
/*                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox*/
                    for (line in block.lines) {
                        val lineText = line.text
/*                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox*/
                        if (lineText.length == 16) {
                            binding.cardNumber.editText?.apply {
                                setText(lineText)
                            }
                        }
                        for (element in line.elements) {
                            val elementText = element.text
                            /*  val elementCornerPoints = element.cornerPoints
                                val elementFrame = element.boundingBox*/
                        }
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap

            detectText(imageBitmap)
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }
}