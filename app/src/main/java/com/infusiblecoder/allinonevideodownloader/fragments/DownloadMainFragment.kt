@file:Suppress("DEPRECATION")
package com.infusiblecoder.allinonevideodownloader.fragments

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.infusiblecoder.allinonevideodownloader.*
import com.infusiblecoder.allinonevideodownloader.interfaces.UserListInStoryListner
import com.infusiblecoder.allinonevideodownloader.R
import com.infusiblecoder.allinonevideodownloader.activities.*
import com.infusiblecoder.allinonevideodownloader.adapters.ListAllStoriesOfUserAdapter
import com.infusiblecoder.allinonevideodownloader.adapters.StoryUsersListAdapter
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbadapters.FBstoryAdapter
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbadapters.FBuserRecyclerAdapter
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbinterfaces.OnFbUserClicked
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels.FBStory
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels.FBUserData
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils.FBhelper
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils.Facebookprefloader
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils.LoginWithFB
import com.infusiblecoder.allinonevideodownloader.models.instawithlogin.CarouselMedia
import com.infusiblecoder.allinonevideodownloader.models.instawithlogin.ModelInstaWithLogin
import com.infusiblecoder.allinonevideodownloader.models.storymodels.*
import com.infusiblecoder.allinonevideodownloader.services.ClipboardMonitor
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain
import com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain
import com.infusiblecoder.allinonevideodownloader.utils.AdsManager
import com.infusiblecoder.allinonevideodownloader.utils.Constants
import com.infusiblecoder.allinonevideodownloader.utils.Constants.PREF_CLIP
import com.infusiblecoder.allinonevideodownloader.utils.Constants.STARTFOREGROUND_ACTION
import com.infusiblecoder.allinonevideodownloader.utils.Constants.STOPFOREGROUND_ACTION
import com.infusiblecoder.allinonevideodownloader.utils.SharedPrefsForInstagram
import com.infusiblecoder.allinonevideodownloader.utils.iUtils
import com.infusiblecoder.allinonevideodownloader.utils.iUtils.ShowToast
import com.infusiblecoder.allinonevideodownloader.webservices.api.RetrofitApiInterface
import com.infusiblecoder.allinonevideodownloader.webservices.api.RetrofitClient
import kotlinx.android.synthetic.main.fragment_download.*
import kotlinx.android.synthetic.main.fragment_download.view.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Type
import java.net.URI
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
@Keep
class DownloadMainFragment : Fragment(), UserListInStoryListner {
    private var nn: String? = "nnn"
    private lateinit var fbstory_adapter: FBstoryAdapter
    private lateinit var fbuserlistadapter: FBuserRecyclerAdapter
    private lateinit var listAllStoriesOfUserAdapter: ListAllStoriesOfUserAdapter
    private var storyUsersListAdapter: StoryUsersListAdapter? = null
    private var mRewardedAd: RewardedAd? = null
    private var NotifyID = 1001

    private var csRunning = false
    lateinit var progressDralogGenaratinglink: ProgressDialog

    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences


    var myVideoUrlIs: String? = null

    var myPhotoUrlIs: String? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null



    var fbstorieslist: List<FBStory> = ArrayList()


    private var linlayoutInstaStories: LinearLayout? = null
    private var recUserList: RecyclerView? = null
    private var recStoriesList: RecyclerView? = null


    private var fbsearch: SearchView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_download, container, false)

        val prefs = activity?.getSharedPreferences(
            "whatsapp_pref",
            Context.MODE_PRIVATE
        )
        nn = prefs!!.getString("inappads", "nnn")



        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            activity,
            resources.getString(R.string.AdmobRewardID),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {

                    mRewardedAd = rewardedAd
                }


            }

        )




        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {


            override fun onAdDismissedFullScreenContent() {

                //  Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
                iUtils.ShowToast(activity, getString(R.string.completad))
//
                val checked = view.chkAutoDownload?.isChecked
                if (checked!!) {
                    view.chkAutoDownload?.isChecked = false
                }

            }

            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError?) {
                val checked = view.chkAutoDownload?.isChecked
                if (checked!!) {
                    view.chkAutoDownload?.isChecked = false
                }
            }

            override fun onAdShowedFullScreenContent() {

                iUtils.ShowToast(activity, getString(R.string.completad))
//
                val checked = view.chkAutoDownload?.isChecked
                if (checked!!) {
                    view.chkAutoDownload?.isChecked = false
                }

                // Called when ad is dismissed.
                // Don't set the ad reference to null to avoid showing the ad a second time.
                mRewardedAd = null
            }
        }


        //fb add


        progressDralogGenaratinglink = ProgressDialog(activity)
        progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
        progressDralogGenaratinglink.setCancelable(false)
        fbsearch = view.findViewById(R.id.search_fbstory)

        //  addFbAd()

        pref = requireActivity().getSharedPreferences(PREF_CLIP, 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)

        createNotificationChannel(
            requireActivity(),
            NotificationManagerCompat.IMPORTANCE_LOW,
            true,
            getString(R.string.app_name),
            getString(R.string.aio_auto)
        )
//TODO






        linlayoutInstaStories = view.findViewById(R.id.linlayout_insta_stories) as LinearLayout


        view.btnDownload.setOnClickListener { _ ->

//TODO Facebook uncomment the finction call code below
//            if (Constants.show_facebookads) {
//                addFbAd()
//            }

            val url = view.etURL.text.toString()
            DownloadVideo(url)


        }



        if (activity != null) {

            val activity: StatusSaverActivity? = activity as StatusSaverActivity?
            val strtext: String? = activity?.getMyData()

            println("mydatvgg222 " + strtext)
            if (strtext != null && !strtext.equals("")) {

                activity.setmydata("")
                view.etURL.setText(strtext)
                val url = view.etURL.text.toString()
                DownloadVideo(url)

            }
        }




        view.llFacebook.setOnClickListener { _ ->
            openAppFromPackedge(
                "com.facebook.katana",
                "facebook:/newsfeed",
                requireActivity().resources.getString(R.string.install_fb)
            )


        }
        view.llTikTok.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.zhiliaoapp.musically",
                "https://www.tiktok.com/",
                requireActivity().resources.getString(R.string.install_tik)
            )


        }
        view.llInstagram.setOnClickListener { _ ->


            openAppFromPackedge(
                "com.instagram.android",
                "https://www.instagram.com/",
                requireActivity().resources.getString(R.string.install_ins)
            )


        }
        view.llTwitter.setOnClickListener { _ ->

            openAppFromPackedge(
                "com.twitter.android",
                "https://www.twitter.com/",
                requireActivity().resources.getString(R.string.install_twi)
            )


        }

        if (!Constants.showyoutube) {
            view.llytdbtn.visibility = View.GONE
        }

        view.llytdbtn.setOnClickListener { _ ->


            openAppFromPackedge(
                "com.google.android.youtube",
                "https://www.youtube.com/",
                requireActivity().resources.getString(R.string.install_ytd)
            )


        }

        view.rvGallery.setOnClickListener { _ ->


            startActivity(Intent(context, GalleryActivity::class.java))


        }


        view.llroposo.setOnClickListener { _ ->


            openAppFromPackedge(
                "com.roposo.android",
                "https://www.roposo.com/",
                requireActivity().resources.getString(R.string.install_roposo)
            )


        }

        view.llsharechat.setOnClickListener { _ ->

            openAppFromPackedge(
                "in.mohalla.sharechat",
                "https://www.sharechat.com/",
                requireActivity().resources.getString(R.string.install_sharechat)
            )
        }

        view.likee.setOnClickListener { _ ->

            openAppFromPackedge(
                "video.like",
                "https://likee.com/",
                requireActivity().resources.getString(R.string.install_likee)
            )


        }

        view.videomore_btn.setOnClickListener { _ ->


            val intent = Intent(context, AllSupportedApps::class.java)

            startActivity(intent)


        }




        view.ivLink.setOnClickListener(fun(_: View) {
            val clipBoardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipData = clipBoardManager.primaryClip
            val clip = primaryClipData?.getItemAt(0)?.text.toString()

            view.etURL.text = Editable.Factory.getInstance().newEditable(clip)
            DownloadVideo(clip)
        })


        if (csRunning) {
            view.chkAutoDownload.isChecked = true
            startClipboardMonitor()
        } else {
            view.chkAutoDownload.isChecked = false
            stopClipboardMonitor()
        }





        view.chkAutoDownload.setOnClickListener { view ->

            val checked = view?.chkAutoDownload?.isChecked
            if (!checked!!) {
                view.chkAutoDownload?.isChecked = false
                stopClipboardMonitor()
            } else {
                showAdDialog()

            }

        }


        val sharedPrefsFor = SharedPrefsForInstagram(activity)
        if (sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE) == null) {
            sharedPrefsFor.clearSharePrefs()
        }


        val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
        if (map != null) {

            if (map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN).equals("true")) {
                view.chkdownload_private_media.isChecked = true
                linlayoutInstaStories?.visibility = View.VISIBLE
                getallstoriesapicall()
            } else {
                view.chkdownload_private_media.isChecked = false
                linlayoutInstaStories?.visibility = View.GONE

            }
        }


        val sharedPrefsForfb = Facebookprefloader(activity)
        if (sharedPrefsForfb.getPreference(Facebookprefloader.fb_pref_cookie) == null) {
            sharedPrefsForfb.MakePrefEmpty()
        }


        val LoadPrefString = sharedPrefsForfb.LoadPrefString()
        val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)


        if (logedin != null && logedin != "") {
            println("mydataiiii=" + logedin)
            if (logedin.equals("true")) {
                println("mydataiiiiddddd=" + logedin)
                view.chkdownload_fbstories.isChecked = true
                view.linlayout_fb_stories?.visibility = View.VISIBLE
                loadUserData()
            } else {

                println("mydataiiiidfalse=" + logedin)
                view.chkdownload_fbstories.isChecked = false
                view.linlayout_fb_stories?.visibility = View.GONE

            }
        } else {
            view.chkdownload_fbstories.isChecked = false
            view.linlayout_fb_stories?.visibility = View.GONE
        }





        view.chkdownload_private_media.setOnClickListener { view ->


            val sharedPrefsForInstagram = SharedPrefsForInstagram(activity)

            val map = sharedPrefsForInstagram.getPreference(SharedPrefsForInstagram.PREFERENCE)


            if (map != null && !map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                    .equals("true")
            ) {
                val intent = Intent(
                    activity,
                    InstagramLoginActivity::class.java
                )
                startActivityForResult(intent, 200)
            } else {
                val ab = AlertDialog.Builder(
                    requireActivity()
                )
                ab.setPositiveButton(resources.getString(R.string.yes), object :
                    DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        val sharedPrefsForInstagram2 = SharedPrefsForInstagram(activity)
                        val map2 =
                            sharedPrefsForInstagram2.getPreference(SharedPrefsForInstagram.PREFERENCE)

                        if (sharedPrefsForInstagram2.getPreference(SharedPrefsForInstagram.PREFERENCE) != null) {
                            sharedPrefsForInstagram2.clearSharePrefs()

                            linlayoutInstaStories?.visibility = View.GONE

                            if (map2 != null && map2.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                                    .equals("true")
                            ) {
                                view.chkdownload_private_media.isChecked = true
                            } else {
                                view.chkdownload_private_media.isChecked = false
                                recUserList?.visibility = View.GONE
                                recStoriesList?.visibility = View.GONE

                            }
                            p0?.dismiss()

                            view.chkdownload_private_media.isChecked = false

                        } else {
                            sharedPrefsForInstagram2.clearSharePrefs()

                        }

                    }
                })

                ab.setNegativeButton(
                    resources.getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            dialog.cancel()
                            val asfd: Boolean = view.chkdownload_private_media.isChecked
                            view.chkdownload_private_media.isChecked = !asfd
                        }
                    })
                val alert = ab.create()
                alert.setTitle(getString(R.string.noprivatedownload))
                alert.setMessage(getString(R.string.no_private_insta))
                alert.show()


            }

        }



        view.chkdownload_fbstories.setOnClickListener { _ ->

            val sharedPrefsForfb = Facebookprefloader(activity)

            Log.d(TAG, "Inte 0")


            val LoadPrefString = sharedPrefsForfb.LoadPrefString()
            val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

            if (!logedin.equals("true") && logedin != "") {
                val intent = Intent(
                    activity,
                    LoginWithFB::class.java
                )
                startActivityForResult(intent, 201)
            } else {
                val ab = AlertDialog.Builder(
                    requireActivity()
                )
                ab.setPositiveButton(resources.getString(R.string.yes), object :
                    DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {


                        val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                        val logedin = LoadPrefString.get(Facebookprefloader.fb_pref_isloggedin)

                        if (logedin != null && logedin != "") {
                            sharedPrefsForfb.MakePrefEmpty()

                            view.linlayout_fb_stories?.visibility = View.GONE

                            if (logedin.equals("true") && logedin != ""
                            ) {
                                view.chkdownload_fbstories.isChecked = true
                                loadUserData()
                            } else {
                                view.chkdownload_fbstories.isChecked = false
                                view.rec_user_fblist?.visibility = View.GONE
                                view.rec_stories_fblist?.visibility = View.GONE

                            }
                            p0?.dismiss()

                            view.chkdownload_fbstories.isChecked = false

                        } else {
                            sharedPrefsForfb.MakePrefEmpty()

                        }


                    }
                })

                ab.setNegativeButton(
                    resources.getString(R.string.cancel),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            dialog.cancel()
                            logout()
                            val asfd: Boolean = view.chkdownload_fbstories.isChecked
                            view.chkdownload_fbstories.isChecked = !asfd
                        }
                    })
                val alert = ab.create()
                alert.setTitle(getString(R.string.fb_story))
                alert.setMessage(getString(R.string.no_fb_story))
                alert.show()


            }

        }



        view.bulb_icon.setOnClickListener {

            // getAllDataFormLink("", true)
            //  callGetShareChatDataURL().execute("")

            //   murl("http://sck.io/p/n1AcOs7M",activity)
        }


        view.search_story.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null && !text.equals("") && storyUsersListAdapter != null) {
                    storyUsersListAdapter!!.filter.filter(text)
                }
                return true
            }
        })



        fbsearch!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                println("dhsahdhashdk ss " + text)

                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {

                if (text != null && !text.equals("")) {

                    println("dhsahdhashdk " + text)


                    fbuserlistadapter.filter.filter(text)
                }
                return true
            }
        })



        return view
    }


    fun loadUserData() {
        view?.progress_loading_fbbar?.visibility = View.VISIBLE

        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")
            iUtils.ShowToast(activity, getString(R.string.cookiesnotvalid))
            return
        }

        val sharedPrefsForfb = Facebookprefloader(activity)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.get(Facebookprefloader.fb_pref_key)
        //     = sharedPrefsForfb.LoadPrefString( "key")

        Log.e("tag299", "cookie is not valid " + LoadPrefString)

        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")
        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
            .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
            .addHeaders("cookie", cookie)
            .addHeaders(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
            )
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("fb_dtsg", LoadPrefString)
            .addBodyParameter(
                "variables",
                "{\"bucketsCount\":200,\"initialBucketID\":null,\"pinnedIDs\":[\"\"],\"scale\":3}"
            )
            .addBodyParameter("doc_id", "2893638314007950")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("tag55", response.toString())
                    val parse = FBUserData.parse(response.toString())
                    if (parse != null) {
                        Log.e("tag1", "data succeed")
                        showFBSTORYData(parse)
                    }
                    view?.progress_loading_fbbar?.visibility = View.GONE

                }

                override fun onError(error: ANError) {
                    Log.e("tag1", "data faild$error")
                    view?.progress_loading_fbbar?.visibility = View.GONE

                }
            })
    }


    fun loadFriendStories(str: String) {

        view?.progress_loading_fbbar?.visibility = View.VISIBLE

        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")

            return
        }

        val sharedPrefsForfb = Facebookprefloader(activity)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.get(Facebookprefloader.fb_pref_key)
        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")
        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
            .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
            .addHeaders("cookie", cookie)
            .addHeaders(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
            )
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("fb_dtsg", LoadPrefString)
            .addBodyParameter(
                "variables",
                "{\"bucketID\":\"$str\",\"initialBucketID\":\"$str\",\"initialLoad\":false,\"scale\":5}"
            )
            .addBodyParameter("doc_id", "2558148157622405")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("tag55", response.toString())

                    fbstorieslist = FBStory.parseBulk(response.toString())
                    if (fbstorieslist != null) {

                        fbstory_adapter = FBstoryAdapter(

                            activity,
                            fbstorieslist
                        )
                        view?.rec_stories_fblist?.layoutManager = GridLayoutManager(context, 3)
                        view?.rec_stories_fblist?.adapter = fbstory_adapter

                        view?.progress_loading_fbbar?.visibility = View.GONE

                    }
                }

                override fun onError(error: ANError) {
                    ShowToast(activity, "Failed to load stories")
                    Log.e("tag1", "data faild$error")
                    view?.progress_loading_fbbar?.visibility = View.GONE

                }
            })
    }

    override fun onResume() {
        super.onResume()

        if (activity != null) {

            val activity: StatusSaverActivity? = activity as StatusSaverActivity?
            val strtext: String? = activity?.getMyData()

            println("mydatvgg222 " + strtext)
            if (strtext != null && !strtext.equals("")) {

                activity.setmydata("")
                view?.etURL?.setText(strtext)
                DownloadVideo(strtext)

            }
        }
        Log.e("Frontales", "resume")
    }


    fun showFBSTORYData(FBUserData: FBUserData) {

        this.rec_user_fblist.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        this.fbuserlistadapter =
            FBuserRecyclerAdapter(
                activity,
                FBUserData.friends,
                OnFbUserClicked { id: String? ->

                    loadFriendStories(id!!)

                })
        this.rec_user_fblist.adapter = this.fbuserlistadapter

        view?.progress_loading_fbbar?.visibility = View.GONE

    }


    private fun logout() {
        if (Build.VERSION.SDK_INT >= 22) {
            CookieManager.getInstance().removeAllCookies(null as ValueCallback<Boolean>?)
            CookieManager.getInstance().flush()
        } else {
            val createInstance = CookieSyncManager.createInstance(activity)
            createInstance.startSync()
            val instance: CookieManager = CookieManager.getInstance()
            instance.removeAllCookie()
            instance.removeSessionCookie()
            createInstance.stopSync()
            createInstance.sync()
        }

        val sharedPrefsForfb = Facebookprefloader(activity)

        sharedPrefsForfb.MakePrefEmpty()

    }


    fun openAppFromPackedge(packedgename: String, urlofwebsite: String, installappmessage: String) {


        if (iUtils.isMyPackedgeInstalled(activity, packedgename)) {

            try {
                val pm: PackageManager = requireActivity().packageManager
                val launchIntent: Intent = pm.getLaunchIntentForPackage(packedgename)!!

                requireActivity().startActivity(launchIntent)
            } catch (e: ActivityNotFoundException) {
                iUtils.ShowToast(
                    activity,
                    activity?.resources?.getString(R.string.error_occord_while)
                )


                val uri = urlofwebsite
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                requireActivity().startActivity(intent)

            }


        } else {
            iUtils.ShowToast(activity, installappmessage)
            val appPackageName = packedgename
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

    }



    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.e("loged112211", "Notificaion Channel Created!")
        }
    }

    fun startClipboardMonitor() {
        prefEditor.putBoolean("csRunning", true)
        prefEditor.commit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                ).setAction(STARTFOREGROUND_ACTION)
            )
        } else {
            requireActivity().startService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                )
            )
        }

    }

    fun stopClipboardMonitor() {
        prefEditor.putBoolean("csRunning", false)
        prefEditor.commit()

        requireActivity().stopService(
            Intent(
                requireContext(),
                ClipboardMonitor::class.java
            ).setAction(STOPFOREGROUND_ACTION)
        )


    }

    fun DownloadVideo(url: String) {
        Log.e("myhdasbdhf urlis  ", url)


        if (url.equals("") && iUtils.checkURL(url)) {
            iUtils.ShowToast(activity, activity?.resources?.getString(R.string.enter_valid))


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
                var myurl = url
                myurl = myurl.substring(myurl.indexOf("http"))
                myurl = myurl.substring(
                    myurl.indexOf("http://share.myjosh.in/"),
                    myurl.indexOf("Download Josh for more videos like this!")
                )


                DownloadVideosMain.Start(activity, myurl.trim(), false)
                Log.e("downloadFileName12", url.trim())
            } else if (url.contains("audiomack")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("zili")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("xhamster")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }

//            else if (url.contains("tiktok")) {
//                if (progressDralogGenaratinglink != null) {
//                    progressDralogGenaratinglink.dismiss()
//                }
//
////                val intent = Intent(activity, GetTiktokLinkThroughWebview::class.java)
////                intent.putExtra("myurlis", url)
////                startActivityForResult(intent, 2)
//
//                val intent = Intent(activity, TikTokDownloadWebview::class.java)
//                intent.putExtra("myvidurl", url)
//                startActivityForResult(intent, 2)
//
//            }

            else if (url.contains("zingmp3")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("vidlit")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("byte.co")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fthis.gr")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("rumble")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("traileraddict")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }
//            else if (url.contains("veer.tv")) {
//                if (progressDralogGenaratinglink != null) {
//                    progressDralogGenaratinglink.dismiss()
//                }
//
//                val intent = Intent(activity, GetLinkThroughWebview::class.java)
//                intent.putExtra("myurlis", url)
//                startActivityForResult(intent, 2)
//
//            }
            //ojoo video app
            else if (url.contains("bemate")) {
                if (progressDralogGenaratinglink != null) {
                    progressDralogGenaratinglink.dismiss()
                }

                val urlq = url.substring(url.indexOf("https"), url.length)
                val intent = Intent(activity, GetLinkThroughWebview::class.java)
                intent.putExtra("myurlis", urlq)
                startActivityForResult(intent, 2)

            } else if (url.contains("chingari")) {
                var myurl = url




                myurl = myurl.substring(
                    myurl.indexOf("https://chingari.io/"),
                    myurl.indexOf("For more such entertaining")
                )


                DownloadVideosMain.Start(activity, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var myurl = url
                try {
                    if (myurl.length > 30) {
                        myurl = myurl.substring(
                            myurl.indexOf("http"),
                            myurl.indexOf("Click this")
                        )
                    }
                } catch (e: Exception) {

                }

                DownloadVideosMain.Start(activity, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())
            } else {
                Log.d("mylogissssss33", "Thebbbbbbbloaded yet.")

                var myurl = url
                try {
                    myurl = myurl.substring(myurl.indexOf("http")).trim()
                } catch (e: Exception) {

                }
                //  Log.e("downloadFileName12", myurl.trim())

                DownloadVideosMain.Start(activity, myurl, false)
            }

        }
    }



    private fun showAdDialog() {


        if (Constants.show_Ads) {
            if (nn == "nnn") {


                val dialogBuilder = AlertDialog.Builder(requireActivity())


                dialogBuilder.setMessage(getString(R.string.doyouseead))

                    .setCancelable(false)

                    .setPositiveButton(
                        getString(R.string.watchad)
                    ) { _, _ ->


                        if (mRewardedAd != null) {
                            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                                fun onUserEarnedReward(rewardItem: RewardItem) {
                                    var rewardAmount = rewardItem.amount
                                    var rewardType = rewardItem.type
                                    Log.d(TAG, "User earned the reward.")

                                    view?.chkAutoDownload?.isChecked = true

                                    startClipboardMonitor()
                                }
                            })
                        } else {


                            iUtils.ShowToast(
                                activity,
                                activity?.resources?.getString(R.string.videonotavaliabl)
                            )

                            view?.chkAutoDownload?.isChecked = true
                            val checked = view?.chkAutoDownload?.isChecked

                            if (checked!!) {
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

                        val checked = view?.chkAutoDownload?.isChecked
                        if (checked!!) {
                            view?.chkAutoDownload?.isChecked = false
                        }

                    }


                val alert = dialogBuilder.create()
                alert.setTitle(getString(R.string.enabAuto))
                alert.show()


            } else {


                view?.chkAutoDownload?.isChecked = true
                val checked = view?.chkAutoDownload?.isChecked

                if (checked!!) {
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


            view?.chkAutoDownload?.isChecked = true
            val checked = view?.chkAutoDownload?.isChecked

            if (checked!!) {
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
            ShowToast(requireActivity(), getString(R.string.invalid_url))
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

        AlertDialog.Builder(requireActivity())
            .setTitle("Select Methode")
            .setCancelable(false)
            .setNegativeButton("Methode 1", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {


                    try {
                        System.err.println("workkkkkkkkk 4")


                        val sharedPrefsFor = SharedPrefsForInstagram(requireActivity())
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
                        ShowToast(requireActivity(), getString(R.string.error_occ))

                    }

                }
            }).setPositiveButton("Methode 2", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    try {
                        System.err.println("workkkkkkkkk 4")


                        val sharedPrefsFor = SharedPrefsForInstagram(requireActivity())
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
                        ShowToast(requireActivity(), getString(R.string.error_occ))

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
                                            activity,
                                            myVideoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                            ".mp4"
                                        )
                                        // etText.setText("");


                                        activity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })


                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                        DownloadFileMain.startDownloading(
                                            activity,
                                            myPhotoUrlIs,
                                            iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        activity?.runOnUiThread(Runnable {

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
                                        activity,
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )
                                    activity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        activity,
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )
                                    activity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                            e.printStackTrace()
                            activity?.runOnUiThread(Runnable {
                                view?.progress_loading_bar?.visibility = View.GONE

                                progressDralogGenaratinglink.dismiss()


                                val alertDialog = AlertDialog.Builder(activity!!).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()


                                    val intent = Intent(
                                        activity,
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
                                                            activity,
                                                            myVideoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");


                                                        activity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })


                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                                        DownloadFileMain.startDownloading(
                                                            activity,
                                                            myPhotoUrlIs,
                                                            iUtils.getImageFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        activity?.runOnUiThread(Runnable {

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
                                                        activity,
                                                        myVideoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                        ".mp4"
                                                    )
                                                    activity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                                    DownloadFileMain.startDownloading(
                                                        activity,
                                                        myPhotoUrlIs,
                                                        iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                                        ".png"
                                                    )
                                                    activity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch
                                            (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 4vvv errrr " + e.message)


                                            e.printStackTrace()
                                            activity?.runOnUiThread(Runnable {
                                                view?.progress_loading_bar?.visibility = View.GONE

                                                progressDralogGenaratinglink.dismiss()


                                            })


                                        }


                                    } else {

                                        System.err.println("workkkkkkkkk 6bbb errrr ")


                                        activity?.runOnUiThread(Runnable {
                                            view?.progress_loading_bar?.visibility = View.GONE

                                            progressDralogGenaratinglink.dismiss()
                                            val alertDialog =
                                                AlertDialog.Builder(activity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                                val intent = Intent(
                                                    activity,
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
                        activity?.runOnUiThread(Runnable {
                            view?.progress_loading_bar?.visibility = View.GONE

                            progressDralogGenaratinglink.dismiss()

                        })


                    } catch (e: Exception) {

                    }
                }
            }
        }.start()


    }

    fun downloadInstagramImageOrVideodata(URL: String?, Cookie: String?) {

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
                                        requireActivity(),
                                        myVideoUrlIs,
                                        iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    // etText.setText("");


                                    requireActivity().runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })


                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        requireActivity(),
                                        myPhotoUrlIs,
                                        iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""
                                    requireActivity().runOnUiThread(Runnable {

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
                                    requireActivity(),
                                    myVideoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                requireActivity().runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                DownloadFileMain.startDownloading(
                                    requireActivity(),
                                    myPhotoUrlIs,
                                    iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                requireActivity().runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })
                                myPhotoUrlIs = ""
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            resources.getString(R.string.somthing),
                            Toast.LENGTH_SHORT
                        ).show()

                        requireActivity().runOnUiThread(Runnable {

                            progressDralogGenaratinglink.dismiss()

                        })

                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()

                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.somthing),
                        Toast.LENGTH_SHORT
                    ).show()
                    println("response1122334455 exe 1:   " + e.localizedMessage)

                    requireActivity().runOnUiThread(Runnable {

                        progressDralogGenaratinglink.dismiss()

                    })
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                requireActivity().runOnUiThread(Runnable {
                    progressDralogGenaratinglink.dismiss()
                })

                Toast.makeText(
                    requireActivity(),
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
                                            activity,
                                            myVideoUrlIs,
                                            modelGetEdgetoNode.title + "_" + System.currentTimeMillis(),
                                            ".mp4"
                                        )
                                        // etText.setText("");


                                        activity?.runOnUiThread(Runnable {

                                            progressDralogGenaratinglink.dismiss()

                                        })


                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates.get(0)
                                                .geturl()
                                        DownloadFileMain.startDownloading(
                                            activity,
                                            myPhotoUrlIs,
                                            modelGetEdgetoNode.title + "_" + System.currentTimeMillis(),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        activity?.runOnUiThread(Runnable {

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
                                        activity,
                                        myVideoUrlIs,
                                        modelGetEdgetoNode.title + "_" + System.currentTimeMillis(),
                                        ".mp4"
                                    )
                                    activity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates.get(0).geturl()
                                    DownloadFileMain.startDownloading(
                                        activity,
                                        myPhotoUrlIs,
                                        modelGetEdgetoNode.title + "_" + System.currentTimeMillis(),
                                        ".png"
                                    )
                                    activity?.runOnUiThread(Runnable {

                                        progressDralogGenaratinglink.dismiss()

                                    })
                                    myPhotoUrlIs = ""
                                }
                            }


                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                            e.printStackTrace()
                            activity?.runOnUiThread(Runnable {
                                view?.progress_loading_bar?.visibility = View.GONE

                                progressDralogGenaratinglink.dismiss()


                                val alertDialog = AlertDialog.Builder(activity!!).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()


                                    val intent = Intent(
                                        activity,
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
                                                            activity,
                                                            myVideoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");



                                                        activity?.runOnUiThread(Runnable {

                                                            progressDralogGenaratinglink.dismiss()

                                                        })


                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].imageVersions2.candidates.get(
                                                                0
                                                            ).geturl()
                                                        DownloadFileMain.startDownloading(
                                                            activity,
                                                            myPhotoUrlIs,
                                                            iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        activity?.runOnUiThread(Runnable {

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
                                                        activity,
                                                        myVideoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                                        ".mp4"
                                                    )
                                                    activity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelGetEdgetoNode.imageVersions2.candidates.get(
                                                            0
                                                        ).geturl()
                                                    DownloadFileMain.startDownloading(
                                                        activity,
                                                        myPhotoUrlIs,
                                                        iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                                        ".png"
                                                    )
                                                    activity?.runOnUiThread(Runnable {

                                                        progressDralogGenaratinglink.dismiss()

                                                    })
                                                    myPhotoUrlIs = ""
                                                }
                                            }


                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                                            e.printStackTrace()
                                            activity?.runOnUiThread(Runnable {
                                                view?.progress_loading_bar?.visibility = View.GONE

                                                progressDralogGenaratinglink.dismiss()


                                                val alertDialog =
                                                    AlertDialog.Builder(activity!!).create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()


                                                    val intent = Intent(
                                                        activity,
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


                                        activity?.runOnUiThread(Runnable {
                                            view?.progress_loading_bar?.visibility = View.GONE

                                            progressDralogGenaratinglink.dismiss()
                                            val alertDialog =
                                                AlertDialog.Builder(activity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()


                                                val intent = Intent(
                                                    activity,
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
                } catch (e: Exception) {

                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        activity?.runOnUiThread(Runnable {
                            view?.progress_loading_bar?.visibility = View.GONE

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
                                    activity,
                                    myVideoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                // etText.setText("");


                                activity?.runOnUiThread(Runnable {

                                    progressDralogGenaratinglink.dismiss()

                                })


                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelEdNodeArrayList[i].imageVersions2.candidates.get(0)
                                        .geturl()
                                DownloadFileMain.startDownloading(
                                    activity,
                                    myPhotoUrlIs,
                                    iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                myPhotoUrlIs = ""
                                activity?.runOnUiThread(Runnable {

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
                                activity,
                                myVideoUrlIs,
                                iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                ".mp4"
                            )
                            activity?.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })
                            myVideoUrlIs = ""
                        } else {
                            myPhotoUrlIs =
                                modelGetEdgetoNode.imageVersions2.candidates.get(0).geturl()
                            DownloadFileMain.startDownloading(
                                activity,
                                myPhotoUrlIs,
                                iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                ".png"
                            )
                            activity?.runOnUiThread(Runnable {

                                progressDralogGenaratinglink.dismiss()

                            })
                            myPhotoUrlIs = ""
                        }
                    }


                } catch (e: java.lang.Exception) {
                    System.err.println("workkkkkkkkk 5nn errrr " + e.message)


                    e.printStackTrace()
                    activity?.runOnUiThread(Runnable {
                        view?.progress_loading_bar?.visibility = View.GONE

                        progressDralogGenaratinglink.dismiss()


                        val alertDialog = AlertDialog.Builder(activity!!).create()
                        alertDialog.setTitle(getString(R.string.logininsta))
                        alertDialog.setMessage(getString(R.string.urlisprivate))
                        alertDialog.setButton(
                            AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                        ) { dialog, _ ->
                            dialog.dismiss()


                            val intent = Intent(
                                activity,
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

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                requireActivity().runOnUiThread(Runnable {
                    progressDralogGenaratinglink.dismiss()
                })

                Toast.makeText(
                    requireActivity(),
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
                    rewardedInterstitialAd!!.show(context as Activity, OnUserEarnedRewardListener {
                        Log.i(TAG, "onUserEarnedReward " + it.amount);

                    })
                } else {
                    Log.i(TAG, "load int video failed;")

                }
            }
        }
    }

    private fun showAdmobAds() {
        if (Constants.show_Ads) {
            if (nn == "nnn") {
                AdsManager.loadInterstitialAd(activity as Activity?)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("proddddd11111222 $resultCode __" + data)

        if (requestCode == 200 && resultCode == RESULT_OK) {

            println("proddddd11111200 $resultCode __" + data)

            val sharedPrefsForInstagram = SharedPrefsForInstagram(activity)

            val map =
                sharedPrefsForInstagram.getPreference(SharedPrefsForInstagram.PREFERENCE)

            if (map != null) {
                println("proddddd11111  " + map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN))

                if (!map.get(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN)
                        .equals("true")
                ) {
                    view?.chkdownload_private_media?.isChecked = false
                    linlayoutInstaStories?.visibility = View.GONE
                } else {
                    view?.chkdownload_private_media?.isChecked = true
                    linlayoutInstaStories?.visibility = View.VISIBLE
                    getallstoriesapicall()
                }
            }
        }

        if (requestCode == 201 && resultCode == RESULT_OK) {

            println("proddddd11111201 $resultCode __" + data)

            val sharedPrefsForfb = Facebookprefloader(activity)
            val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
            val logedin = LoadPrefStringol.get(Facebookprefloader.fb_pref_isloggedin)

            println("proddddd11111201-1=" + logedin)
            if (logedin != null && logedin != "") {

                if (logedin.equals("true")) {
                    view?.chkdownload_fbstories?.isChecked = true
                    view?.linlayout_fb_stories?.visibility = View.VISIBLE
                    loadUserData()
                } else {
                    view?.chkdownload_fbstories?.isChecked = false
                    view?.linlayout_fb_stories?.visibility = View.GONE
                }

            } else {
                view?.chkdownload_fbstories?.isChecked = false
                view?.linlayout_fb_stories?.visibility = View.GONE
            }


        }

    }


    private fun callStoriesDetailApi(UserId: String) {
        try {
            view?.progress_loading_bar?.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(activity)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                getFullDetailsOfClickedFeed(
                    UserId,
                    "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID)
                        .toString() + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            if (progressDralogGenaratinglink != null) {
                progressDralogGenaratinglink.dismiss()
            }
            System.err.println("workkkkkkkkk 5")
            iUtils.ShowToast(activity, getString(R.string.error_occ))
        }
    }


    private fun getallstoriesapicall() {
        try {
            view?.progress_loading_bar?.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(activity)
            val map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE)
            if (map != null) {

                getallStories(
                    "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID)
                        .toString() + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID)
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun getallStories(Cookie: String?) {
        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        println("mycookies are = " + Cookie)

        AndroidNetworking.get("https://i.instagram.com/api/v1/feed/reels_tray/")
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
            )
            .build()
            .getAsObject(
                InstaStoryModelClass::class.java,
                object : ParsedRequestListener<InstaStoryModelClass> {
                    override fun onResponse(response: InstaStoryModelClass) {
                        // do anything with response


                        try {

                            println(
                                "response1122334455_story:  " + response.tray
                            )

                            view?.rec_user_list?.visibility = View.VISIBLE
                            view?.progress_loading_bar?.visibility = View.GONE
                            storyUsersListAdapter = StoryUsersListAdapter(
                                activity,
                                response.tray, this@DownloadMainFragment
                            )
                            val linearLayoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                            view?.rec_user_list?.layoutManager = linearLayoutManager
                            view?.rec_user_list?.adapter = storyUsersListAdapter
                            storyUsersListAdapter!!.notifyDataSetChanged()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455_storyERROR:  " + e.message)

                            view?.progress_loading_bar?.visibility = View.GONE
                        }


                    }

                    override fun onError(anError: ANError) {
                        // handle error
                    }
                })


    }


    fun getFullDetailsOfClickedFeed(UserId: String, Cookie: String?) {


        AndroidNetworking.get("https://i.instagram.com/api/v1/users/$UserId/full_detail_info?max_id=")
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
            )
            .build()
            .getAsObject(
                ModelFullDetailsInstagram::class.java,
                object : ParsedRequestListener<ModelFullDetailsInstagram> {
                    override fun onResponse(response: ModelFullDetailsInstagram) {
                        // do anything with response

                        try {

                            view?.rec_user_list?.visibility = View.VISIBLE
                            view?.progress_loading_bar?.visibility = View.GONE
                            println("response1122334455_fulldetails:   ${response.reel_feed}")



                            if (response.reel_feed.items.size == 0) {
                                iUtils.ShowToast(activity, getString(R.string.nostoryfound))
                            }

                            listAllStoriesOfUserAdapter = ListAllStoriesOfUserAdapter(
                                activity,
                                response.reel_feed.items
                            )
                            view?.rec_stories_list?.visibility = View.VISIBLE

                            val gridLayoutManager = GridLayoutManager(context, 3)


                            view?.rec_stories_list?.layoutManager = gridLayoutManager
                            view?.rec_stories_list?.isNestedScrollingEnabled = true
                            view?.rec_stories_list?.adapter = listAllStoriesOfUserAdapter
                            listAllStoriesOfUserAdapter.notifyDataSetChanged()
                        } catch (e: java.lang.Exception) {
                            view?.rec_stories_list?.visibility = View.GONE
                            e.printStackTrace()
                            view?.progress_loading_bar?.visibility = View.GONE
                            iUtils.ShowToast(activity, getString(R.string.nostoryfound))

                        }

                    }

                    override fun onError(anError: ANError) {
                        println("response1122334455:   " + "Failed2")
                        view?.progress_loading_bar?.visibility = View.GONE


                    }
                })


    }


    override fun onclickUserStoryListeItem(position: Int, modelUsrTray: ModelUsrTray?) {

        println("response1122ff334455:   " + modelUsrTray + position)

        callStoriesDetailApi(modelUsrTray?.user?.pk.toString())

    }


}