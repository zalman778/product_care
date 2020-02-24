package com.hwx.productcare.feature.kkt.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import com.hwx.productcare.R
import com.hwx.productcare.feature.kkt.misc.ReceiptCheckDbRow
import com.hwx.productcare.feature.kkt.viewmodel.QrCodeReaderViewModel
import com.hwx.productcare.ui.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class QrCodeReaderFragment: BaseFragment() {

    private var codeScanner: CodeScanner? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val qrCodeReaderViewModel: QrCodeReaderViewModel by viewModels { viewModelFactory }

    private val rvQrExistsObserver = Observer<ReceiptCheckDbRow> { handleDbResponse(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context)
                .inflateTransition(R.xml.rv_receipt_to_receipt_detail_shared_element_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return view ?: inflater.inflate(
            R.layout.fragment_qr_scanner,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkCameraPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initCameraObject()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun checkCameraPermissions() {
        val activity = requireActivity()
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            initCameraObject()
        }
    }

    private fun initCameraObject() {
        val scannerView = view!!.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner?.formats = arrayListOf(BarcodeFormat.QR_CODE)
        codeScanner?.scanMode = ScanMode.CONTINUOUS

        codeScanner?.errorCallback = ErrorCallback {
            Timber.w(it.localizedMessage)
        }
        codeScanner?.decodeCallback = DecodeCallback {
            handleScannedCode(it.text)

        }
        scannerView.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun handleScannedCode(value: String) {
        Timber.w(value)
        qrCodeReaderViewModel.searchReceiptInHistory(value)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        qrCodeReaderViewModel.lvReceiptInDb.observe(viewLifecycleOwner, rvQrExistsObserver)
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * Here we observing for emits from db requests
     */
    private fun handleDbResponse(receiptCheckDbRow: ReceiptCheckDbRow) {
        if (!receiptCheckDbRow.exists) {
            val action =
                QrCodeReaderFragmentDirections.actionNavigationQrScannerToNavigationProductRecieve(
                    receiptCheckDbRow.receipt
                )
            findNavController().navigate(action)
        } else {
            view?.let { view ->
                showErrorWithAction(view, "Данный чек уже есть в истории") { hideErrorIfShowed() }
            }
        }
    }


    companion object {
        const val MY_PERMISSIONS_REQUEST_CAMERA = 1
    }
}