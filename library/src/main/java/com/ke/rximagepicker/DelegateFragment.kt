package com.ke.rximagepicker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DelegateFragment : Fragment() {
    private var currentCameraPhotoUri: Uri? = null


    lateinit var resultSubject: PublishSubject<Result>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @SuppressLint("CheckResult")
    fun start(source: Int) {
        resultSubject = PublishSubject.create()

        if (source == RxImagePicker.SOURCE_CAMERA) {
            RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        takeCameraPicture()
                    } else {
                        resultSubject.onNext(Result(null))
                        resultSubject.onComplete()
                    }
                }

        } else {
            takeGalleryPicture()
        }
    }

    private fun takeGalleryPicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun takeCameraPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = createCameraImageFile()
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            requireActivity().packageName + ".image_picker.provider",
            file
        )
        currentCameraPhotoUri = photoUri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            resultSubject.onNext(Result(currentCameraPhotoUri))
            resultSubject.onComplete()

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            resultSubject.onNext(Result(data?.data))
            resultSubject.onComplete()
        } else if (resultCode == Activity.RESULT_CANCELED) {
            resultSubject.onNext(Result(null))
            resultSubject.onComplete()
        }
    }



    @SuppressLint("SimpleDateFormat")
    private fun createCameraImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    companion object {
        const val REQUEST_CODE_CAMERA = 10
        const val REQUEST_CODE_GALLERY = 11
    }
}