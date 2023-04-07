package com.example.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlin.properties.Delegates

/**
 * Used to turn on GPS location if turned off and get the device's location containing lat/long
 */
object LocationHelper {
    const val REQUEST_CHECK_SETTINGS = 123456789
    const val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION

    private var locationCallback by Delegates.notNull<LocationRequestCallback>()
    private var locationRequest by Delegates.notNull<LocationRequest>()

    private var alertDialog: AlertDialog? = null

    fun Activity.checkCanUseLocation(
        callback: LocationRequestCallback
    ) {
        locationCallback = callback
        locationRequest = createLocationRequest()
        val locationSettingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingsBuilder.build())
            .addOnCompleteListener {
                try {
                    it.result.locationSettingsStates
                        ?.takeIf { nnState ->
                            nnState.isGpsPresent && nnState.isLocationUsable && nnState.isLocationPresent
                        }
                        ?.apply { locationCallback.onCanUseLocation() }
                } catch (exception: Exception) {
                    // With more time, this would be logged. onFailure handles showing the dialog
                }
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        showGoToTurnOnLocationDialog {
                            exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                        }
                    } catch (sendException: IntentSender.SendIntentException) {
                        //google says to ignore this... just log and tell the UI to get manual location
                    }
                }
            }
    }

    fun Activity.tryGetLastLocation(
        locationClient: FusedLocationProviderClient
    ) {
        // If location permission granted
        if (checkSelfPermission(coarseLocation) == PackageManager.PERMISSION_GRANTED) {
            // Get last location
            locationClient.getLastLocation(LastLocationRequest.Builder().build())
                .addOnCompleteListener { task ->
                    task.result
                        // Can, in fact, be null
                        .takeUnless { it == null }
                        ?.let { lastLocation ->
                            locationCallback.onLastLocationFound(lastLocation)
                        }
                        ?: locationCallback.onLastLocationNotFound()
                }
                .addOnFailureListener {
                    locationCallback.onLastLocationNotFound()
                }
        } else {
            locationCallback.onLocationPermissionsNotGranted()
        }
    }

    /**
     * TODO: Gross. Extract string resources
     **/
    fun Activity.showGoToLocationPermissionsSettingsDialog() {
        makeDialog(
            this,
            "Open Settings",
            { takeToLocationSetting() },
            "Refuse",
            null,
            "Location Permission Denied",
            "To get local weather information, allow this app access to your device's location. " +
                    "Otherwise, you must set the location manually in the search field."
        )
    }

    private fun Activity.showGoToTurnOnLocationDialog(positiveButtonAction: () -> Unit) {
        makeDialog(
            this,
            positiveButtonText = "Turn On",
            positiveButtonAction = positiveButtonAction,
            negativeButtonText = "Cancel",
            title = "Location not available",
            description = "Please turn on your device's location to get local weather information"
        )
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(5000L)
            .setPriority(Priority.PRIORITY_LOW_POWER)
            .setMaxUpdates(5)
            .setWaitForAccurateLocation(false)
            .build()
    }

    private fun makeDialog(
        context: Context,
        positiveButtonText: String = "",
        positiveButtonAction: (() -> Unit)? = null,
        negativeButtonText: String = "",
        negativeButtonAction: (() -> Unit)? = null,
        title: String,
        description: String
    ) {

        alertDialog?.dismiss()

        with(AlertDialog.Builder(context)) {
            setPositiveButton(positiveButtonText) { _, _ ->
                if (positiveButtonAction != null) {
                    positiveButtonAction()
                }
            }
            setNegativeButton(negativeButtonText) { _, _ ->
                if (negativeButtonAction != null) {
                    negativeButtonAction()
                }
                alertDialog?.cancel()
            }
            setTitle(title)
            setMessage(description)
            setCancelable(true)
            this
        }.let { dialogBuilder ->
            alertDialog = dialogBuilder.create()
            dialogBuilder.show()
        }
    }

    private fun Activity.takeToLocationSetting() {
        packageName.takeUnless { it.isNullOrEmpty() }
            ?.let { packageName ->
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(this, intent, null)
            }
    }
}