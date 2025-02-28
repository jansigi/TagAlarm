//package ch.js.tagalarm
//
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.os.Vibrator
//import androidx.activity.ComponentActivity
//
//open class VibratorDependent(activity: Compo, savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//    var vibrator: Vibrator? = null
//
//    init {
//        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
//    }
//
//    fun stopVibrator() {
//        vibrator?.cancel()
//        vibrator = null
//    }
//}
