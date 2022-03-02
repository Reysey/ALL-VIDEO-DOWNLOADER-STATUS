@file:Suppress("DEPRECATION")

package com.infusiblecoder.allinonevideodownloader.activities

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.infusiblecoder.allinonevideodownloader.R
import com.infusiblecoder.allinonevideodownloader.models.instawithlogin.CarouselMedia
import com.infusiblecoder.allinonevideodownloader.models.instawithlogin.ModelInstaWithLogin
import com.infusiblecoder.allinonevideodownloader.models.storymodels.ModelEdNode
import com.infusiblecoder.allinonevideodownloader.models.storymodels.ModelGetEdgetoNode
import com.infusiblecoder.allinonevideodownloader.models.storymodels.ModelInstagramResponse
import com.infusiblecoder.allinonevideodownloader.services.ClipboardMonitor
import com.infusiblecoder.allinonevideodownloader.utils.*
import com.infusiblecoder.allinonevideodownloader.utils.iUtils.ShowToast
import com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain
import com.infusiblecoder.allinonevideodownloader.webservices.api.RetrofitApiInterface
import com.infusiblecoder.allinonevideodownloader.webservices.api.RetrofitClient
import com.ironsource.mediationsdk.IronSource
import com.smarteist.autoimageslider.SliderView
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import kotlinx.android.synthetic.main.fragment_download.view.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Type
import java.net.URI
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    companion object {

        public lateinit var myString: String

    }


    val REQUEST_PERMISSION_CODE = 1001
    val REQUEST_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var fbAdView: com.facebook.ads.AdView? = null
    lateinit var chkAutoDownload: AppCompatCheckBox
    lateinit var instaprivatefbprivate: TextView
    lateinit var progressDralogGenaratinglink: ProgressDialog


    private var billingClient: BillingClient? = null
    private var skuDetails: SkuDetails? = null

    var fragment: Fragment? = null


    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences
    private var nn: String? = "nnn"
    private var mRewardedAd: RewardedAd? = null
    private var csRunning = false
    var myVideoUrlIs: String? = null

    var myPhotoUrlIs: String? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main_newdesign)
        MobileAds.initialize(this@MainActivity)


        // setupBillingClient()
        IronSource.init(
            this,
            resources.getString(R.string.ironsource_app_id),
            IronSource.AD_UNIT.INTERSTITIAL
        )

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setUpBillingClient1()
//0,2


        val prefs = getSharedPreferences(
            "whatsapp_pref",
            Context.MODE_PRIVATE
        )
        nn = prefs!!.getString("inappads", "nnn")


        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this@MainActivity,
            resources.getString(R.string.AdmobRewardID),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(ContentValues.TAG, adError.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {

                    mRewardedAd = rewardedAd
                }


            }

        )


        //fb add


        progressDralogGenaratinglink = ProgressDialog(this@MainActivity)
        progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
        progressDralogGenaratinglink.setCancelable(false)

        //  addFbAd()

        pref = getSharedPreferences(Constants.PREF_CLIP, 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)

        createNotificationChannel(
            NotificationManagerCompat.IMPORTANCE_LOW,
            true,
            getString(R.string.app_name),
            getString(R.string.aio_auto)
        )
//TODO





        val nativeadContainer = findViewById<FrameLayout>(R.id.fl_adplaceholder)


        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent


            }
        }


        val whatsappcard = findViewById<CardView>(R.id.whatsappcard)
        chkAutoDownload = findViewById<AppCompatCheckBox>(R.id.switchddService)
        instaprivatefbprivate = findViewById<TextView>(R.id.instaprivatefbprivate)
        val copylinkanddownloadcard = findViewById<CardView>(R.id.copylinkanddownloadcard)

        val videwGllery = findViewById<LinearLayout>(R.id.videwGllery)
        val supportedapps = findViewById<LinearLayout>(R.id.supportedapps)
        val settingpage = findViewById<LinearLayout>(R.id.settingpage)


        videwGllery.setOnClickListener {

            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)

        }

        supportedapps.setOnClickListener {

            val intent = Intent(this, AllSupportedApps::class.java)
            startActivity(intent)

        }

        settingpage.setOnClickListener {

            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }



        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {


            override fun onAdDismissedFullScreenContent() {

                //  Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
                iUtils.ShowToast(this@MainActivity, getString(R.string.completad))
//
                val checked = chkAutoDownload.isChecked
                if (checked) {
                    chkAutoDownload.isChecked = false
                }

            }

            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError?) {
                val checked = chkAutoDownload.isChecked
                if (checked) {
                    chkAutoDownload.isChecked = false
                }
            }

            override fun onAdShowedFullScreenContent() {

                iUtils.ShowToast(this@MainActivity, getString(R.string.completad))
//
                val checked = chkAutoDownload.isChecked
                if (checked) {
                    chkAutoDownload.isChecked = false
                }

                // Called when ad is dismissed.
                // Don't set the ad reference to null to avoid showing the ad a second time.
                mRewardedAd = null
            }
        }





        whatsappcard.setOnClickListener {

            val intent = Intent(this, StatusSaverActivity::class.java)
            intent.putExtra("frag", "status")

            startActivity(intent)

        }
        instaprivatefbprivate.setOnClickListener {

            val intent = Intent(this, StatusSaverActivity::class.java)
            intent.putExtra("frag", "download")
            startActivity(intent)

        }


        copylinkanddownloadcard.setOnClickListener {

            val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipData = clipBoardManager.primaryClip
            val clip = primaryClipData?.getItemAt(0)?.text.toString()

            DownloadVideo(clip)

        }






        isNeedGrantPermission()


//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this@MainActivity)) {
//                val intent = Intent(
//                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + packageName)
//                )
//                startActivityForResult(intent, 1234)
//            }
//        }



        if (csRunning) {
            chkAutoDownload.isChecked = true
            startClipboardMonitor()
        } else {
            chkAutoDownload.isChecked = false
            stopClipboardMonitor()
        }





        chkAutoDownload.setOnClickListener {

            val checked = chkAutoDownload.isChecked
            if (!checked) {
                chkAutoDownload.isChecked = false
                stopClipboardMonitor()
            } else {
                showAdDialog()

            }

        }


        if (intent != null) {
            val action1 = intent!!.action
            val type1 = intent!!.type

            if (Intent.ACTION_SEND == action1 && type1 != null) {
                if ("text/plain" == type1) {
                    handleSendText(intent) // Handle text being sent


                }
            }
        }



        MobileAds.initialize(
            this@MainActivity
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status!!.latency
                    )
                )
            }

            if (Constants.show_Ads) {

                val prefs: SharedPreferences = getSharedPreferences(
                    "whatsapp_pref",
                    Context.MODE_PRIVATE
                )

                val pp =
                    prefs.getString("inappads", "nnn") //"No name defined" is the default value.


                if (pp.equals("nnn")) {
                    Log.d("AdsManager:app ","working")



                    AdsManager.loadAdmobNativeAd(this, nativeadContainer)


                } else {
                    nativeadContainer.visibility = View.GONE
                }
            }
        }


        AppRating.Builder(this@MainActivity)
            .setMinimumLaunchTimes(5)
            .setMinimumDays(7)
            .setMinimumLaunchTimesToShowAgain(5)
            .setMinimumDaysToShowAgain(7)
            .setRatingThreshold(RatingThreshold.FOUR)
            .showIfMeetsConditions()

    }



    fun getMyData(): String? {
        return myString
    }


    fun setmydata(mysa: String) {
        myString = mysa
    }


    override fun onStart() {
        super.onStart()

        try {


            if (iUtils.showDialogUpdateTimesShown < iUtils.showDialogUpdateTimesPerSession) {

                iUtils.showDialogUpdateTimesShown++

                if (iUtils.isNetworkConnected(this@MainActivity)) {

                    val appUpdaterUtils: AppUpdaterUtils = AppUpdaterUtils(this)
                        .withListener(object : AppUpdaterUtils.UpdateListener {
                            override fun onSuccess(
                                update: com.github.javiersantos.appupdater.objects.Update?,
                                isUpdateAvailable: Boolean?
                            ) {

                                println("appupdater error bb " + isUpdateAvailable)
                                println("appupdater error uuu " + update!!.latestVersion)

                                if (isUpdateAvailable!!) {

                                    launchUpdateDialog(update.latestVersion)


                                }


                            }

                            override fun onFailed(error: AppUpdaterError?) {


                            }


                        })
                    appUpdaterUtils.start()


                }
            }
        } catch (e: Exception) {

        }


    }


    private fun launchUpdateDialog(onlineVersion: String) {
        try {
            AlertDialog.Builder(this@MainActivity)
                .setTitle(getString(R.string.updqteavaliable))
                .setCancelable(false)
                .setMessage(
                    getString(R.string.update) + " " + onlineVersion + " " + getString(R.string.updateisavaliabledownload) + getString(
                        R.string.app_name
                    )
                )
                .setPositiveButton(
                    resources.getString(R.string.update_now)
                )
                { dialog, _ ->
                    dialog.dismiss()
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )

                }

                .setNegativeButton(
                    resources.getString(R.string.cancel)
                )
                { dialog, _ ->
                    dialog.dismiss()

                }
                .setIcon(R.drawable.ic_appicon)
                .show()

        } catch (e: java.lang.Exception) {

            println("appupdater error rrrr " + e)
            e.printStackTrace()
        }
    }



    fun handleSendText(intent: Intent) {



        try {

            this.intent = null
            var url = intent.getStringExtra(Intent.EXTRA_TEXT)



            if (url.equals("") && iUtils.checkURL(url)) {
                iUtils.ShowToast(
                    this@MainActivity,
                    this@MainActivity.resources?.getString(R.string.enter_valid)
                )
                return

            }

            if (url?.contains("myjosh.in")!!) {


              try{
                  url = iUtils.extractUrls(url)[0]
              }catch (e:Exception){

                }

                DownloadVideosMain.Start(this@MainActivity, url?.trim(), false)
                if (url != null) {
                    Log.e("downloadFileName12", url.trim())
                }


            } else if (url.contains("chingari")) {


                try{
                    url = iUtils.extractUrls(url)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this@MainActivity, url?.trim(), false)
                if (url != null) {
                    Log.e("downloadFileName12", url.trim())
                }
            } else if (url.contains("bemate")) {

                var mynewval = url
                try{
                    url = iUtils.extractUrls(url)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this@MainActivity, url?.trim(), false)
                if (url != null) {
                    Log.e("downloadFileName12", url.trim())
                }
            } else if (url.contains("instagram.com")) {

                DownloadVideo(url.trim())

                Log.e("downloadFileName12", url)
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var mynewval = url
                try{
                    url = iUtils.extractUrls(url)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this@MainActivity, url?.trim(), false)
                if (url != null) {
                    Log.e("downloadFileName12", url.trim())
                }
            } else {
                var mynewval = url
                try{
                    url = iUtils.extractUrls(url)[0]
                }catch (e:Exception){

                }

                DownloadVideo(mynewval!!.trim())

            }
        } catch (e: Exception) {

        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.e("myhdasbdhf newintent ", intent?.getStringExtra(Intent.EXTRA_TEXT) + "_46237478234")

        intent?.let { newIntent ->

            handleSendText(newIntent)
            Log.e("myhdasbdhf notdownload ", newIntent.getStringExtra(Intent.EXTRA_TEXT) + "")

        }
    }


    private fun isNeedGrantPermission(): Boolean {
        try {
            if (iUtils.hasMarsallow()) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        REQUEST_PERMISSION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            REQUEST_PERMISSION
                        )
                    ) {
                        val msg =
                            String.format(
                                getString(R.string.format_request_permision),
                                getString(R.string.app_name)
                            )

                        val localBuilder = AlertDialog.Builder(this@MainActivity)
                        localBuilder.setTitle(getString(R.string.permission_title))
                        localBuilder
                            .setMessage(msg).setNeutralButton(
                                getString(R.string.grant_option)
                            ) { _, _ ->
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(REQUEST_PERMISSION),
                                    REQUEST_PERMISSION_CODE
                                )
                            }
                            .setNegativeButton(
                                getString(R.string.cancel_option)
                            ) { paramAnonymousDialogInterface, _ ->
                                paramAnonymousDialogInterface.dismiss()
                                finish()
                            }
                        localBuilder.show()

                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(REQUEST_PERMISSION),
                            REQUEST_PERMISSION_CODE
                        )
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    iUtils.ShowToast(this@MainActivity, getString(R.string.info_permission_denied))

                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            iUtils.ShowToast(this@MainActivity, getString(R.string.info_permission_denied))
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.


        menuInflater.inflate(R.menu.main, menu)

        val prefs: SharedPreferences = getSharedPreferences(
            "whatsapp_pref",
            Context.MODE_PRIVATE
        )
        val name = prefs.getString("whatsapp", "main") //"No name defined" is the default value.
        val pp = prefs.getString("inappads", "nnn") //"No name defined" is the default value.

//
//        if (name == "main") {
//            menu.findItem(R.id.action_shwbusinesswhatsapp).isVisible = true
//
//            menu.findItem(R.id.action_shwmainwhatsapp).isVisible = false
//
//        } else if (name == "bus") {
//            menu.findItem(R.id.action_shwbusinesswhatsapp).isVisible = false
//
//            menu.findItem(R.id.action_shwmainwhatsapp).isVisible = true
//
//        }


        if (pp.equals("nnn")) {

            menu.findItem(R.id.action_removeads).isVisible = true

        } else if (pp.equals("ppp")) {

            menu.findItem(R.id.action_removeads).isVisible = false

        }



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_privacy -> {

//                AlertDialog.Builder(this)
//                    .setTitle(getString(R.string.privacy))
//                    .setMessage(R.string.privacy_message)
//                    .setPositiveButton(
//                        android.R.string.yes
//                    ) { dialog, _ -> dialog.dismiss() }
//                    .setIcon(R.drawable.ic_info_black_24dp)
//                    .show()

                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)))
                startActivity(browserIntent)

                true
            }

            R.id.action_downloadtiktok -> {

                val intent = Intent(this, TikTokDownloadWebview::class.java)
                startActivity(intent)


                true
            }


            R.id.action_rate -> {

                try {
                    if(!isFinishing){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(getString(R.string.RateAppTitle))
                        .setMessage(getString(R.string.RateApp))
                        .setCancelable(false)
                        .setPositiveButton(
                            getString(R.string.rate_dialog)
                        ) { _, _ ->
                            val appPackageName = packageName
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: android.content.ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        }
                        .setNegativeButton(getString(R.string.later_btn), null).show()}
                } catch (e: Exception) {
                }
                true
            }


            R.id.ic_whatapp -> {

                val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (launchIntent != null) {

                    startActivity(launchIntent)
                    //  finish()
                } else {

                    iUtils.ShowToast(
                        this@MainActivity,
                        this.resources.getString(R.string.appnotinstalled)
                    )
                }
                true
            }


            R.id.action_language -> {

                if(!isFinishing){
                val dialog = Dialog(this@MainActivity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialog_change_language)

                val l_english = dialog.findViewById(R.id.l_english) as TextView
                l_english.setOnClickListener {

                    LocaleHelper.setLocale(application, "en")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL


                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "en")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_french = dialog.findViewById(R.id.l_french) as TextView
                l_french.setOnClickListener {
                    LocaleHelper.setLocale(application, "fr")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "fr")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_arabic = dialog.findViewById(R.id.l_arabic) as TextView
                l_arabic.setOnClickListener {
                    LocaleHelper.setLocale(application, "ar")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "ar")

                    editor.apply()


                    recreate()
                    dialog.dismiss()

                }
                val l_urdu = dialog.findViewById(R.id.l_urdu) as TextView
                l_urdu.setOnClickListener {
                    LocaleHelper.setLocale(application, "ur")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR


                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "ur")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_german = dialog.findViewById(R.id.l_german) as TextView
                l_german.setOnClickListener {
                    LocaleHelper.setLocale(application, "de")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "de")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_turkey = dialog.findViewById(R.id.l_turkey) as TextView
                l_turkey.setOnClickListener {
                    LocaleHelper.setLocale(application, "tr")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "tr")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_portougese = dialog.findViewById(R.id.l_portougese) as TextView
                l_portougese.setOnClickListener {
                    LocaleHelper.setLocale(application, "pt")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "pt")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }


                val l_chinese = dialog.findViewById(R.id.l_chinese) as TextView
                l_chinese.setOnClickListener {
                    LocaleHelper.setLocale(application, "zh")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "zh")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }





                val l_hindi = dialog.findViewById(R.id.l_hindi) as TextView
                l_hindi.setOnClickListener {
                    LocaleHelper.setLocale(application, "hi")
                    window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "lang_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("lang", "hi")

                    editor.apply()


                    recreate()
                    dialog.dismiss()
                }




                dialog.show()
            }
                true
            }

//            R.id.action_shwbusinesswhatsapp -> {
//
//
//                val editor: SharedPreferences.Editor = getSharedPreferences(
//                    "whatsapp_pref",
//                    Context.MODE_PRIVATE
//                ).edit()
//                editor.putString("whatsapp", "bus")
//
//                editor.apply()
//
//                if (Build.VERSION.SDK_INT >= 11) {
//                    recreate()
//                } else {
//                    val intent = intent
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                    finish()
//                    overridePendingTransition(0, 0)
//                    startActivity(intent)
//                    overridePendingTransition(0, 0)
//                }
//
//
//
//                true
//            }
//
//
//            R.id.action_shwmainwhatsapp -> {
//
//
//                val editor: SharedPreferences.Editor = getSharedPreferences(
//                    "whatsapp_pref",
//                    Context.MODE_PRIVATE
//                ).edit()
//                editor.putString("whatsapp", "main")
//
//                editor.apply()
//
//
//                if (Build.VERSION.SDK_INT >= 11) {
//                    recreate()
//                } else {
//                    val intent = intent
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                    finish()
//                    overridePendingTransition(0, 0)
//                    startActivity(intent)
//                    overridePendingTransition(0, 0)
//                }
//
//
//
//                true
//            }


            R.id.action_removeads -> {
                try {
                    removemyadshere()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }


    fun removemyadshere() {
        try {
            skuDetails?.let {
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()
                billingClient?.launchBillingFlow(this@MainActivity, billingFlowParams)?.responseCode
            } ?: noSKUMessage()

        } catch (e: Exception) {

            iUtils.ShowToastError(this@MainActivity, "close the app and restart");

        }
    }


    override fun onBackPressed() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_ad_exit)
        val yesBtn = dialog.findViewById(R.id.btn_exitdialog_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_exitdialog_no) as Button

//TODO ENABLE EXIT DIALOG AD BY UNCOMMENTING IT
//        val adviewnew = dialog.findViewById(R.id.adView_dia) as AdView
//        val adRequest = AdRequest.Builder().build()
//        adviewnew.loadAd(adRequest)


        yesBtn.setOnClickListener {
            System.exit(0)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("proddddd11111222 $resultCode __" + data)

        if (requestCode == 200 && resultCode == RESULT_OK) {

            println("proddddd11111 $resultCode __" + data)

        }
//
//
//
//        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data)
//        }


        try {
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun noSKUMessage() {
        Toast.makeText(
            this@MainActivity, "No SKU Found", Toast.LENGTH_SHORT
        ).show()

    }

    private fun setUpBillingClient1() {
        billingClient = BillingClient.newBuilder(this@MainActivity)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()


        startConnection()
    }

    private fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.v("TAG_INAPP", "Setup Billing Done")
                    queryAvaliableProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun queryAvaliableProducts() {
        val skuList = ArrayList<String>()
        skuList.add(getString(R.string.productidcode))
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient?.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                for (skuDetails in skuDetailsList) {
                    Log.v("TAG_INAPP", "skuDetailsList : ${skuDetailsList}")
                    //This list should contain the products added above
                    updateUI(skuDetails)
                }
            }
        }
    }

    private fun updateUI(skuDetails: SkuDetails?) {
        skuDetails?.let {
            this.skuDetails = it
//            txt_product_name?.text = skuDetails.title
//            txt_product_description?.text = skuDetails.description
            showUIElements()
        }
    }

    private fun showUIElements() {
//        txt_product_name?.visibility = View.VISIBLE
//        txt_product_description?.visibility = View.VISIBLE
//        txt_product_buy?.visibility = View.VISIBLE
    }

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.v("TAG_INAPP", "billingResult responseCode : ${billingResult.responseCode}")
            try {
                if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
            //                        handlePurchase(purchase)


                    //handleConsumedPurchases(purchase)

                        handleNonConcumablePurchase(purchase)
                    }
                } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                } else if (billingResult.responseCode == BillingResponseCode.ITEM_UNAVAILABLE) {
                    Toast.makeText(
                        this@MainActivity, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT
                    ).show()
                } else if (billingResult.responseCode == BillingResponseCode.ITEM_ALREADY_OWNED) {


                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(getString(R.string.itemowned))
                        .setMessage(getString(R.string.adsareremoved))
                        .setIcon(R.drawable.ic_appicon)
                        .show()


                    Toast.makeText(
                        this@MainActivity, getString(R.string.itemowned), Toast.LENGTH_SHORT
                    ).show()

                    val editor: SharedPreferences.Editor = getSharedPreferences(
                        "whatsapp_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                    editor.putString("inappads", "ppp")

                    editor.apply()
                } else {

                    Toast.makeText(
                        this@MainActivity, "Error has occured", Toast.LENGTH_SHORT
                    ).show()

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun handleConsumedPurchases(purchase: Purchase) {
        Log.d("TAG_INAPP", "handleConsumablePurchasesAsync foreach it is $purchase")


        if (purchase.skus.equals(getString(R.string.productidcode))) {

            val params =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            billingClient?.consumeAsync(params) { billingResult, _ ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {

                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(getString(R.string.purchasedone))
                            .setMessage(getString(R.string.adsareremoved))
                            .setIcon(R.drawable.ic_appicon)
                            .show()

                        Toast.makeText(
                            this@MainActivity, getString(R.string.purchasedone), Toast.LENGTH_SHORT
                        ).show()

                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "whatsapp_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("inappads", "ppp")

                        editor.apply()

                        // Update the appropriate tables/databases to grant user the items
                        Log.d(
                            "TAG_INAPP",
                            " Update the appropriate tables/databases to grant user the items"
                        )
                    }
                    else -> {
                        Log.w("TAG_INAPP", billingResult.debugMessage)
                    }
                }
            }


        }


    }

    private fun handleNonConcumablePurchase(purchase: Purchase) {
        Log.v("TAG_INAPP", "handlePurchase : ${purchase}")

        if (purchase.skus.equals(getString(R.string.productidcode))) {


            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken).build()
                    billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        val billingResponseCode = billingResult.responseCode
                        val billingDebugMessage = billingResult.debugMessage

                        Log.v("TAG_INAPP", "response code: $billingResponseCode")
                        Log.v("TAG_INAPP", "debugMessage : $billingDebugMessage")
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(getString(R.string.purchasedone))
                            .setMessage(getString(R.string.adsareremoved))
                            .setIcon(R.drawable.ic_appicon)
                            .show()

                        Toast.makeText(
                            this@MainActivity, getString(R.string.purchasedone), Toast.LENGTH_SHORT
                        ).show()

                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "whatsapp_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("inappads", "ppp")

                        editor.apply()

                    }
                }
            }
        }
    }


    fun createNotificationChannel(
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.e("loged112211", "Notificaion Channel Created!")
        }
    }

    fun startClipboardMonitor() {
        prefEditor.putBoolean("csRunning", true)
        prefEditor.commit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(
                Intent(
                    this@MainActivity,
                    ClipboardMonitor::class.java
                ).setAction(Constants.STARTFOREGROUND_ACTION)
            )
        } else {
            startService(
                Intent(
                    this@MainActivity,
                    ClipboardMonitor::class.java
                )
            )
        }

    }

    fun stopClipboardMonitor() {
        prefEditor.putBoolean("csRunning", false)
        prefEditor.commit()

        stopService(
            Intent(
                this@MainActivity,
                ClipboardMonitor::class.java
            ).setAction(Constants.STOPFOREGROUND_ACTION)
        )


    }

    fun DownloadVideo(url: String) {
        Log.e("myhdasbdhf urlis  ", url)


        if (url.equals("") && iUtils.checkURL(url)) {
            iUtils.ShowToast(this@MainActivity, resources?.getString(R.string.enter_valid))


        } else {


            val rand = Random()
            val rand_int1 = rand.nextInt(2)
            println("randonvalueis = $rand_int1")

            if (rand_int1 == 0) {
                showAdmobAds()
            } else {
                showAdmobAds_int_video()
            }


            Log.d("mylogissssss", "The interstitial wasn't loaded yet.")



            if (url.contains("instagram.com")) {
                progressDralogGenaratinglink.show()
                startInstaDownload(url)

            } else if (url.contains("myjosh.in")) {

               var mynewval = url
                try{
                    mynewval = iUtils.extractUrls(mynewval)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)


            } else if (url.contains("audiomack")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("zili")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("xhamster")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }


            else if (url.contains("zingmp3")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("vidlit")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("byte.co")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fthis.gr")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("rumble")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("traileraddict")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }
//            else if (url.contains("veer.tv")) {
//                if (progressDralogGenaratinglink != null) {
//                    progressDralogGenaratinglink.dismiss()
//                }
//
//                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
//                intent.putExtra("myurlis", url)
//                startActivityForResult(intent, 2)
//
//            }
            //ojoo video app
            else if (url.contains("bemate")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                var mynewval = url
                try{
                    mynewval = iUtils.extractUrls(mynewval)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
                val intent = Intent(this@MainActivity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", mynewval)
                startActivityForResult(intent, 2)

            } else if (url.contains("chingari")) {


                var mynewval = url
                try{
                    mynewval = iUtils.extractUrls(mynewval)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var mynewval = url
                try{
                    mynewval = iUtils.extractUrls(mynewval)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)

            } else {
                Log.d("mylogissssss33", "Thebbbbbbbloaded yet.")

                var mynewval = url
                try{
                    mynewval = iUtils.extractUrls(mynewval)[0]
                }catch (e:Exception){

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
            }

        }
    }


    private fun showAdDialog() {


        if (Constants.show_Ads) {
            if (nn == "nnn") {


                val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)


                dialogBuilder.setMessage(getString(R.string.doyouseead))

                    .setCancelable(false)

                    .setPositiveButton(
                        getString(R.string.watchad)
                    ) { _, _ ->


                        if (mRewardedAd != null) {
                            mRewardedAd?.show(this@MainActivity, OnUserEarnedRewardListener() {
                                fun onUserEarnedReward(rewardItem: RewardItem) {
                                    var rewardAmount = rewardItem.amount
                                    var rewardType = rewardItem.type
                                    Log.d(ContentValues.TAG, "User earned the reward.")

                                    chkAutoDownload.isChecked = true

                                    startClipboardMonitor()
                                }
                            })
                        } else {


                            iUtils.ShowToast(
                                this@MainActivity,
                                resources?.getString(R.string.videonotavaliabl)
                            )

                            chkAutoDownload.isChecked = true
                            val checked = chkAutoDownload.isChecked

                            if (checked) {
                                Log.e("loged", "testing checked!")
                                startClipboardMonitor()
                            } else {
                                Log.e("loged", "testing unchecked!")


                                stopClipboardMonitor()
                                // setNofication(false);
                            }



                            Log.d("TAG", "The rewarded ad wasn't ready yet.")
                        }


//
//
//                if (mRewardedVideoAd.isLoaded) {
//                    mRewardedVideoAd.show()
//                } else {
//
//
//                }


                    }

                    .setNegativeButton(
                        getString(R.string.cancel)
                    ) { dialog, _ ->
                        dialog.cancel()

                        val checked = chkAutoDownload.isChecked
                        if (checked) {
                            chkAutoDownload.isChecked = false
                        }

                    }


                val alert = dialogBuilder.create()
                alert.setTitle(getString(R.string.enabAuto))
                alert.show()


            } else {


                chkAutoDownload.isChecked = true
                val checked = chkAutoDownload.isChecked

                if (checked) {
                    Log.e("loged", "testing checked!")
                    startClipboardMonitor()
                } else {
                    Log.e("loged", "testing unchecked!")


                    stopClipboardMonitor()
                    // setNofication(false);
                }



                Log.d("TAG", "The rewarded ad wasn't ready yet.")
            }


        } else {


            chkAutoDownload.isChecked = true
            val checked = chkAutoDownload.isChecked

            if (checked) {
                Log.e("loged", "testing checked!")
                startClipboardMonitor()
            } else {
                Log.e("loged", "testing unchecked!")


                stopClipboardMonitor()
                // setNofication(false);
            }



            Log.d("TAG", "The rewarded ad wasn't ready yet.")
        }


    }


    //insta finctions

    fun startInstaDownload(Url: String) {


//         https://www.instagram.com/p/CLBM34Rhxek/?igshid=41v6d50y6u4w
//          https://www.instagram.com/p/CLBM34Rhxek/
//           https://www.instagram.com/p/CLBM34Rhxek/?__a=1
//           https://www.instagram.com/tv/CRyVpDSAE59/

        /*
        * https://www.instagram.com/p/CUs4eKIBscn/?__a=1
        * https://www.instagram.com/p/CUktqS7pieg/?__a=1
        * https://www.instagram.com/p/CSMYRwGna3S/?__a=1
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1
        * */


        var Urlwi: String?
        try {

            val uri = URI(Url)
            Urlwi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,  // Ignore the query part of the input url
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            Urlwi = ""
            progressDralogGenaratinglink.dismiss()
            ShowToast(this@MainActivity, getString(R.string.invalid_url))
            return
        }

        System.err.println("workkkkkkkkk 1122112 " + Url)

        var urlwithoutlettersqp: String? = Urlwi
        System.err.println("workkkkkkkkk 1122112 " + urlwithoutlettersqp)

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1"
        System.err.println("workkkkkkkkk 87878788 " + urlwithoutlettersqp)


        if (urlwithoutlettersqp.contains("/reel/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/reel/", "/p/")
        }

        if (urlwithoutlettersqp.contains("/tv/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/tv/", "/p/")
        }
        System.err.println("workkkkkkkkk 777777 " + urlwithoutlettersqp)

        AlertDialog.Builder(this@MainActivity)
            .setTitle("Select Methode")
            .setCancelable(false)
            .setNegativeButton("Methode 1", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {


                    try {
                        System.err.println("workkkkkkkkk 4")


                        val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivity)
                        val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
                        if (map != null && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != null && map.get(
                                SharedPrefsForInstagram.PREFERENCE_USERID
                            ) != "oopsDintWork" && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != ""
                        ) {
                            System.err.println("workkkkkkkkk 4.7")



                            downloadInstagramImageOrVideodata_old_withlogin(
                                urlwithoutlettersqp, "ds_user_id=" + map.get(
                                    SharedPrefsForInstagram.PREFERENCE_USERID
                                )
                                        + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                            )

                        } else {
                            System.err.println("workkkkkkkkk 4.8")


                            downloadInstagramImageOrVideodata_old(
                                urlwithoutlettersqp,
                                ""
                            )

                        }


                    } catch (e: java.lang.Exception) {
                        progressDralogGenaratinglink.dismiss()
                        System.err.println("workkkkkkkkk 5")
                        e.printStackTrace()
                        ShowToast(this@MainActivity, getString(R.string.error_occ))

                    }

                }
            }).setPositiveButton("Methode 2", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    try {
                        System.err.println("workkkkkkkkk 4")


                        val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivity)
                        val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
                        if (map != null && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != null && map.get(
                                SharedPrefsForInstagram.PREFERENCE_USERID
                            ) != "oopsDintWork" && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != ""
                        ) {
                            System.err.println("workkkkkkkkk 5.2")




                            downloadInstagramImageOrVideodata_withlogin(
                                urlwithoutlettersqp, "ds_user_id=" + map.get(
                                    SharedPrefsForInstagram.PREFERENCE_USERID
                                )
                                        + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                            )

                        } else {

                            System.err.println("workkkkkkkkk 4.5")

                            downloadInstagramImageOrVideodata(
                                urlwithoutlettersqp,
                                iUtils.myInstagramTempCookies
                            )

                        }


                    } catch (e: java.lang.Exception) {
                        progressDralogGenaratinglink.dismiss()
                        System.err.println("workkkkkkkkk 5.1")
                        e.printStackTrace()
                        ShowToast(this@MainActivity, getString(R.string.error_occ))

                    }
                }
            }).show()


    }

    fun downloadInstagramImageOrVideodata_old(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url(URL!!)
                    .method("GET", null)
                    .addHeader("Cookie", Cookie!!)
                    .addHeader(
                        "User-Agent",
                        iUtils.UserAgentsList[j]
                    )
                    .build()
                try {
                    val response = client.newCall(request).execute()

                    System.err.println("workkkkkkkkk 6 " + response.code)

                    if (response.code == 200) {

                        try {
                            val listType: Type =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse = Gson().fromJson(
                                response.body!!.string(),
                                listType
                            )


                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                val modelEdNodeArrayList: List<ModelEdNode> =
                                    modelGetEdgetoNode.modelEdNodes
                                for (i in 0 until modelEdNodeArrayList.size) {
                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                        myVideoUrlIs = modelEdNodeArrayList[i].modelNode.video_url
                                        DownloadFileMain.startDownloading(
                                            this@MainActivity,
                                            myVideoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                            ".mp4"
                                        )
                                        // etText.setText("");


                                        this@MainActivity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })


                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                        DownloadFileMain.startDownloading(
                                            this@MainActivity,
                                            myPhotoUrlIs,
                                            iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        this@MainActivity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })
                                        // etText.setText("");
                                    }
                                }
                            } else {
                                val isVideo =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                if (isVideo) {
                                    myVideoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )
                                    this@MainActivity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myVideoUrlIs = ""
                                } else
                                {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )
                                    this@MainActivity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)

                            try{

                                try {
                                    System.err.println("workkkkkkkkk 4")



                                        downloadInstagramImageOrVideodata(
                                            URL, "")




                                } catch (e: java.lang.Exception) {
                                    progressDralogGenaratinglink.dismiss()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    ShowToast(this@MainActivity, getString(R.string.error_occ))

                                }

                            }catch (e:Exception)
                            {

                                e.printStackTrace()
                                this@MainActivity?.runOnUiThread(Runnable {


                                    progressDralogGenaratinglink.dismiss()


                                    val alertDialog = AlertDialog.Builder(this@MainActivity!!).create()
                                    alertDialog.setTitle(getString(R.string.logininsta))
                                    alertDialog.setMessage(getString(R.string.urlisprivate))
                                    alertDialog.setButton(
                                        AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                    ) { dialog, _ ->
                                        dialog.dismiss()


                                        val intent = Intent(
                                            this@MainActivity,
                                            InstagramLoginActivity::class.java
                                        )
                                        startActivityForResult(intent, 200)

                                    }

                                    alertDialog.setButton(
                                        AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()


                                    }
                                    alertDialog.show()

                                })
                            }



                        }


                    } else {

                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val request: Request = Request.Builder()
                                    .url(URL)
                                    .method("GET", null)
                                    .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                    .addHeader(
                                        "User-Agent",
                                        iUtils.UserAgentsList[j]
                                    ).build()
                                try {


                                    val response1: Response = client.newCall(request).execute()
                                    System.err.println("workkkkkkkkk 6 1 " + response1.code)

                                    if (response1.code == 200) {

                                        try {
                                            val listType: Type =
                                                object :
                                                    TypeToken<ModelInstagramResponse?>() {}.type
                                            val modelInstagramResponse: ModelInstagramResponse =
                                                Gson().fromJson(
                                                    response1.body!!.string(),
                                                    listType
                                                )


                                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                                val modelEdNodeArrayList: List<ModelEdNode> =
                                                    modelGetEdgetoNode.modelEdNodes
                                                for (i in 0 until modelEdNodeArrayList.size) {
                                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.video_url
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivity,
                                                            myVideoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");


                                                        this@MainActivity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })


                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivity,
                                                            myPhotoUrlIs,
                                                            iUtils.getImageFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        this@MainActivity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })
                                                        // etText.setText("");
                                                    }
                                                }
                                            } else {
                                                val isVideo =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                                if (isVideo) {
                                                    myVideoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivity,
                                                        myVideoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                        ".mp4"
                                                    )
                                                    this@MainActivity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivity,
                                                        myPhotoUrlIs,
                                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                                        ".png"
                                                    )
                                                    this@MainActivity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch
                                            (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 4vvv errrr " + e.message)


                                            e.printStackTrace()
                                            this@MainActivity?.runOnUiThread(Runnable {
                                                

                                                progressDralogGenaratinglink.dismiss()


                                            })


                                        }


                                    } else {

                                        System.err.println("workkkkkkkkk 6bbb errrr ")


                                        this@MainActivity?.runOnUiThread(Runnable {
                                            

                                            progressDralogGenaratinglink.dismiss()
                                            val alertDialog =
                                                AlertDialog.Builder(this@MainActivity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                                val intent = Intent(
                                                    this@MainActivity,
                                                    InstagramLoginActivity::class.java
                                                )
                                                startActivityForResult(intent, 200)

                                            }

                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_NEGATIVE,
                                                getString(R.string.cancel)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                            }
                                            alertDialog.show()
                                        })

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()


                    }
                    println("working errpr \t Value: " + response.body!!.string())
                } catch (e: Exception) {

                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        this@MainActivity?.runOnUiThread(Runnable {
                            

                            progressDralogGenaratinglink.dismiss()

                        })


                    } catch (e: Exception) {

                    }
                }
            }
        }.start()


    }

    fun downloadInstagramImageOrVideodata(URL: String?, Coookie: String?) {

        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        var Cookie = Coookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient().create(RetrofitApiInterface::class.java)


        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {
                println("response1122334455 ress :   " + response.body())
                try {


//                                val userdata = response.body()!!.getAsJsonObject("graphql")
//                                    .getAsJsonObject("shortcode_media")
//                                binding.profileFollowersNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_followed_by"
//                                    )["count"].asString
//                                )
//                                binding.profileFollowingNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_follow"
//                                    )["count"].asString
//                                )
//                                binding.profilePostNumberTextview.setText(userdata.getAsJsonObject("edge_owner_to_timeline_media")["count"].asString)
//                                binding.profileLongIdTextview.setText(userdata["username"].asString)
//


                    val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                    val modelInstagramResponse: ModelInstagramResponse? = GsonBuilder().create()
                        .fromJson<ModelInstagramResponse>(
                            response.body().toString(),
                            listType
                        )


                    if (modelInstagramResponse != null) {
                        if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                            val modelGetEdgetoNode: ModelGetEdgetoNode =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                            val modelEdNodeArrayList: List<ModelEdNode> =
                                modelGetEdgetoNode.modelEdNodes
                            for (i in 0 until modelEdNodeArrayList.size) {
                                if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                    myVideoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.video_url
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    // etText.setText("");


                                    this@MainActivity.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })


                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""
                                    this@MainActivity.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    // etText.setText("");
                                }
                            }
                        } else {
                            val isVideo =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                            if (isVideo) {
                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                DownloadFileMain.startDownloading(
                                    this@MainActivity,
                                    myVideoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                this@MainActivity.runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                DownloadFileMain.startDownloading(
                                    this@MainActivity,
                                    myPhotoUrlIs,
                                    iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                this@MainActivity.runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })
                                myPhotoUrlIs = ""
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.somthing),
                            Toast.LENGTH_SHORT
                        ).show()

                        this@MainActivity.runOnUiThread(Runnable {

                            progressDralogGenaratinglink.dismiss()

                        })

                    }


                } catch (e: java.lang.Exception) {


                    try{

                        try {
                            System.err.println("workkkkkkkkk 4")



                            downloadInstagramImageOrVideodata(
                                URL, "")




                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()

                            println("response1122334455 exe 1:   " + e.localizedMessage)

                            this@MainActivity.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(this@MainActivity, getString(R.string.error_occ))

                        }

                    }catch (e:Exception)
                    {

                        e.printStackTrace()
                        this@MainActivity?.runOnUiThread(Runnable {


                            e.printStackTrace()

                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.somthing),
                                Toast.LENGTH_SHORT
                            ).show()
                            println("response1122334455 exe 1:   " + e.localizedMessage)

                            this@MainActivity.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })


                            val alertDialog = AlertDialog.Builder(this@MainActivity!!).create()
                            alertDialog.setTitle(getString(R.string.logininsta))
                            alertDialog.setMessage(getString(R.string.urlisprivate))
                            alertDialog.setButton(
                                AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                            ) { dialog, _ ->
                                dialog.dismiss()


                                val intent = Intent(
                                    this@MainActivity,
                                    InstagramLoginActivity::class.java
                                )
                                startActivityForResult(intent, 200)

                            }

                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                            ) { dialog, _ ->
                                dialog.dismiss()


                            }
                            alertDialog.show()

                        })
                    }




                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                this@MainActivity.runOnUiThread(Runnable {
                    progressDralogGenaratinglink.dismiss()
                })

                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.somthing),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }

    fun downloadInstagramImageOrVideodata_old_withlogin(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url(URL!!)
                    .method("GET", null)
                    .addHeader("Cookie", Cookie!!)
                    .addHeader(
                        "User-Agent",
                        iUtils.UserAgentsList[j]
                    )
                    .build()
                try {
                    val response = client.newCall(request).execute()



                    if (response.code == 200) {

                        var ress = response.body!!.string()
                        println("working errpr \t Value: " + ress)

                        try {
                            val listType: Type =
                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                            val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                                ress,
                                listType
                            )
                            System.out.println("workkkkk777 " + modelInstagramResponse.items.get(0).code)

                            if (modelInstagramResponse.items.get(0).mediaType == 8) {

                                val modelGetEdgetoNode = modelInstagramResponse.items.get(0)


                                val modelEdNodeArrayList: List<CarouselMedia> =
                                    modelGetEdgetoNode.carouselMedia
                                for (i in 0 until modelEdNodeArrayList.size) {
                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                        myVideoUrlIs =
                                            modelEdNodeArrayList[i].videoVersions.get(0).geturl()
                                        DownloadFileMain.startDownloading(
                                            this@MainActivity,
                                            myVideoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                            ".mp4"
                                        )
                                        // etText.setText("");


                                        this@MainActivity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })


                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates.get(0)
                                                .geturl()
                                        DownloadFileMain.startDownloading(
                                            this@MainActivity,
                                            myPhotoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        this@MainActivity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })
                                        // etText.setText("");
                                    }
                                }
                            } else {

                                val modelGetEdgetoNode = modelInstagramResponse.items.get(0)


                                if (modelGetEdgetoNode.mediaType == 2) {
                                    myVideoUrlIs =
                                        modelGetEdgetoNode.videoVersions.get(0).geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )
                                    this@MainActivity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates.get(0).geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivity,
                                        myPhotoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myPhotoUrlIs),

                                        ".png"
                                    )
                                    this@MainActivity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myPhotoUrlIs = ""
                                }
                            }


                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)

                            try{

                                try {
                                    System.err.println("workkkkkkkkk 4")


                                    val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivity)
                                    val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
                                    if (map != null && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != null && map.get(
                                            SharedPrefsForInstagram.PREFERENCE_USERID
                                        ) != "oopsDintWork" && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != ""
                                    ) {
                                        System.err.println("workkkkkkkkk 5.2")




                                        downloadInstagramImageOrVideodata(
                                            URL, "ds_user_id=" + map.get(
                                                SharedPrefsForInstagram.PREFERENCE_USERID
                                            )
                                                    + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                                        )


                                    }else{

                                        progressDralogGenaratinglink.dismiss()
                                        System.err.println("workkkkkkkkk 5.1")
                                        e.printStackTrace()
                                        ShowToast(this@MainActivity, getString(R.string.error_occ))
                                    }


                                } catch (e: java.lang.Exception) {
                                    progressDralogGenaratinglink.dismiss()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    ShowToast(this@MainActivity, getString(R.string.error_occ))

                                }

                            }catch (e:Exception)
                            {

                                e.printStackTrace()
                                this@MainActivity?.runOnUiThread(Runnable {
                                    

                                    progressDralogGenaratinglink.dismiss()


                                    val alertDialog = AlertDialog.Builder(this@MainActivity!!).create()
                                    alertDialog.setTitle(getString(R.string.logininsta))
                                    alertDialog.setMessage(getString(R.string.urlisprivate))
                                    alertDialog.setButton(
                                        AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                    ) { dialog, _ ->
                                        dialog.dismiss()


                                        val intent = Intent(
                                            this@MainActivity,
                                            InstagramLoginActivity::class.java
                                        )
                                        startActivityForResult(intent, 200)

                                    }

                                    alertDialog.setButton(
                                        AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                    ) { dialog, _ ->
                                        dialog.dismiss()


                                    }
                                    alertDialog.show()

                                })
                            }





                        }


                    } else
                    {

                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val request: Request = Request.Builder()
                                    .url(URL)
                                    .method("GET", null)
                                    .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                    .addHeader(
                                        "User-Agent",
                                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
                                    ).build()
                                try {


                                    val response1: Response = client.newCall(request).execute()

                                    if (response1.code == 200) {

                                        try {
                                            val listType: Type =
                                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                                            val modelInstagramResponse: ModelInstaWithLogin =
                                                Gson().fromJson(
                                                    response.body!!.string(),
                                                    listType
                                                )


                                            if (modelInstagramResponse.items.get(0).mediaType == 8) {

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items.get(0)


                                                val modelEdNodeArrayList: List<CarouselMedia> =
                                                    modelGetEdgetoNode.carouselMedia
                                                for (i in 0 until modelEdNodeArrayList.size) {
                                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].videoVersions.get(
                                                                0
                                                            ).geturl()

                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivity,
                                                            myVideoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");



                                                        this@MainActivity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })


                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].imageVersions2.candidates.get(
                                                                0
                                                            ).geturl()
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivity,
                                                            myPhotoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        this@MainActivity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })
                                                        // etText.setText("");
                                                    }
                                                }
                                            } else {

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items.get(0)


                                                if (modelGetEdgetoNode.mediaType == 2) {
                                                    myVideoUrlIs =
                                                        modelGetEdgetoNode.videoVersions.get(0)
                                                            .geturl()
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivity,
                                                        myVideoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                        ".mp4"
                                                    )
                                                    this@MainActivity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelGetEdgetoNode.imageVersions2.candidates.get(
                                                            0
                                                        ).geturl()
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivity,
                                                        myPhotoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                                        ".png"
                                                    )
                                                    this@MainActivity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myPhotoUrlIs = ""
                                                }
                                            }


                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                                            e.printStackTrace()
                                            this@MainActivity?.runOnUiThread(Runnable {
                                                

                                                progressDralogGenaratinglink.dismiss()


                                                val alertDialog =
                                                    AlertDialog.Builder(this@MainActivity!!).create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()


                                                    val intent = Intent(
                                                        this@MainActivity,
                                                        InstagramLoginActivity::class.java
                                                    )
                                                    startActivityForResult(intent, 200)

                                                }

                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()


                                                }
                                                alertDialog.show()

                                            })


                                        }


                                    } else {

                                        System.err.println("workkkkkkkkk 6bbb errrr ")


                                        this@MainActivity?.runOnUiThread(Runnable {
                                            

                                            progressDralogGenaratinglink.dismiss()
                                            val alertDialog =
                                                AlertDialog.Builder(this@MainActivity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                                val intent = Intent(
                                                    this@MainActivity,
                                                    InstagramLoginActivity::class.java
                                                )
                                                startActivityForResult(intent, 200)

                                            }

                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_NEGATIVE,
                                                getString(R.string.cancel)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                            }
                                            alertDialog.show()
                                        })

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()


                    }
                }
                catch (e: Exception)
                {

                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        this@MainActivity?.runOnUiThread(Runnable {
                            

                            progressDralogGenaratinglink.dismiss()

                        })


                    } catch (e: Exception) {

                    }
                }
            }
        }.start()


    }

    fun downloadInstagramImageOrVideodata_withlogin(URL: String?, Cookie: String?) {
/*instagram product types
* product_type
*
* igtv "media_type": 2
* carousel_container  "media_type": 8
* clips  "media_type": 2
* feed   "media_type": 1
* */

        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)

        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient().create(RetrofitApiInterface::class.java)


        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {

                try {
                    val listType: Type =
                        object : TypeToken<ModelInstaWithLogin?>() {}.type
                    val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                        response.body(),
                        listType
                    )
                    System.out.println("workkkkk777 " + modelInstagramResponse.items.get(0).code)

                    if (modelInstagramResponse.items.get(0).mediaType == 8) {

                        val modelGetEdgetoNode = modelInstagramResponse.items.get(0)


                        val modelEdNodeArrayList: List<CarouselMedia> =
                            modelGetEdgetoNode.carouselMedia
                        for (i in 0 until modelEdNodeArrayList.size) {
                            if (modelEdNodeArrayList[i].mediaType == 2) {
                                myVideoUrlIs =
                                    modelEdNodeArrayList[i].videoVersions.get(0).geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivity,
                                    myVideoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                // etText.setText("");


                                this@MainActivity?.runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })


                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelEdNodeArrayList[i].imageVersions2.candidates.get(0)
                                        .geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivity,
                                    myPhotoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                myPhotoUrlIs = ""
                                this@MainActivity?.runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })
                                // etText.setText("");
                            }
                        }
                    } else {

                        val modelGetEdgetoNode = modelInstagramResponse.items.get(0)


                        if (modelGetEdgetoNode.mediaType == 2) {
                            myVideoUrlIs =
                                modelGetEdgetoNode.videoVersions.get(0).geturl()
                            DownloadFileMain.startDownloading(
                                this@MainActivity,
                                myVideoUrlIs,
                                iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                ".mp4"
                            )
                            this@MainActivity?.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })
                            myVideoUrlIs = ""
                        } else {
                            myPhotoUrlIs =
                                modelGetEdgetoNode.imageVersions2.candidates.get(0).geturl()
                            DownloadFileMain.startDownloading(
                                this@MainActivity,
                                myPhotoUrlIs,
                                iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                ".png"
                            )
                            this@MainActivity?.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })
                            myPhotoUrlIs = ""
                        }
                    }


                } catch (e: java.lang.Exception) {
                    System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                    try{

                        try {
                            System.err.println("workkkkkkkkk 4")


                            val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivity)
                            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
                            if (map != null && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != null && map.get(
                                    SharedPrefsForInstagram.PREFERENCE_USERID
                                ) != "oopsDintWork" && map.get(SharedPrefsForInstagram.PREFERENCE_USERID) != ""
                            ) {
                                System.err.println("workkkkkkkkk 5.2")




                                downloadInstagramImageOrVideodata_old(
                                    URL, "ds_user_id=" + map.get(
                                        SharedPrefsForInstagram.PREFERENCE_USERID
                                    )
                                            + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                                )


                            }else{

                                progressDralogGenaratinglink.dismiss()
                                System.err.println("workkkkkkkkk 5.1")
                                e.printStackTrace()
                                ShowToast(this@MainActivity, getString(R.string.error_occ))
                            }


                        } catch (e: java.lang.Exception) {
                            progressDralogGenaratinglink.dismiss()
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(this@MainActivity, getString(R.string.error_occ))

                        }

                    }catch (e:Exception){

                        e.printStackTrace()
                        this@MainActivity?.runOnUiThread(Runnable {
                            

                            progressDralogGenaratinglink.dismiss()


                            val alertDialog = AlertDialog.Builder(this@MainActivity!!).create()
                            alertDialog.setTitle(getString(R.string.logininsta))
                            alertDialog.setMessage(getString(R.string.urlisprivate))
                            alertDialog.setButton(
                                AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                            ) { dialog, _ ->
                                dialog.dismiss()


                                val intent = Intent(
                                    this@MainActivity,
                                    InstagramLoginActivity::class.java
                                )
                                startActivityForResult(intent, 200)

                            }

                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                            ) { dialog, _ ->
                                dialog.dismiss()


                            }
                            alertDialog.show()

                        })
                    }


                }


            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                this@MainActivity.runOnUiThread(Runnable {
                    progressDralogGenaratinglink.dismiss()
                })

                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.somthing),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }




    private fun showAdmobAds_int_video() {
        if (Constants.show_Ads) {
            if (nn == "nnn") {
                if (rewardedInterstitialAd != null) {
                    rewardedInterstitialAd!!.show(this@MainActivity, OnUserEarnedRewardListener {
                        Log.i(ContentValues.TAG, "onUserEarnedReward " + it.amount);

                    })
                } else {
                    Log.i(ContentValues.TAG, "load int video failed;")

                }
            }
        }
    }

    private fun showAdmobAds() {
        if (Constants.show_Ads) {
            if (nn == "nnn") {
                AdsManager.loadInterstitialAd(this@MainActivity)

            }
        }
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }


}