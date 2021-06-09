package com.example.basket
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {

        Looper.prepare()

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, p0.notification?.title, Toast.LENGTH_LONG).show()
        }
        
        Looper.loop()
    }

}