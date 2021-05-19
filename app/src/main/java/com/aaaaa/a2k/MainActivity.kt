package com.aaaaa.a2k

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentResolverCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.aaaaa.a2k.ScreenShot.saveImageToGallery
import com.aaaaa.a2k.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    val code=213

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main)


        requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                code)

        val TAG="VIDEOS"

        var index=1
        var index1=1

        val lock=Any()

        videos.observeForever {
            synchronized(lock) {

                //findViewById<EpoxyRecyclerView>(R.id.rv).layoutManager= LinearLayoutManager(this)

                binding.rv.withModels {

                    testdb {
                        id("testdb")
                        click {  _ ->
                            //todo :test db

                        }
                    }

                    it.forEach { video ->
                        video {
                            id(index++)
                            click {  v ->
                                val name=getVideoFormatName(video.data).also {
                                    Log.e(
                                        TAG,
                                        "video format${it}",
                                    ) }

                                showToast(name)

                                GlobalScope.launch((Dispatchers.Default)) {

                                    val bitmap= Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888)
                                    if(showVideoPreview(video.data, bitmap))
                                    withContext(Dispatchers.Main){
                                            binding.iv.setImageBitmap(bitmap).also {
                                                Log.e(TAG, "fuckckckck${bitmap}", )
                                                saveImageToGallery(this@MainActivity,bitmap);
                                            }
                                    }
                                }
                            }
                            videoInfo(video)
                        }
                        Log.e(TAG + "${index++}", video.toString())
                    }
                }
            }
        }
    }

    fun Context.showToast(data:String)=Toast.makeText(this,data,Toast.LENGTH_SHORT).show()

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String


    external fun getVideoFormatName(path:String):String


    external fun showVideoPreview(path:String,bitmap: Bitmap):Boolean

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 438) {
            if (Environment.isExternalStorageManager()) {
                getVideos()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            code -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        //检查是否已经有权限
                        if (!Environment.isExternalStorageManager()) {
                            //跳转新页面申请权限
                            startActivityForResult(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), 438)
                        }
                        else{
                            getVideos()
                        }
                    }
                    else{
                        GlobalScope.launch(Dispatchers.IO) {
                            getVideos()
                        }
                    }

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
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


    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            // avutil avformat avcodec swscale avdevice avfilter swresample
            System.loadLibrary("native-lib")
            System.loadLibrary("avutil")
            System.loadLibrary("avformat")
            System.loadLibrary("avcodec")
            System.loadLibrary("swscale")
            System.loadLibrary("avdevice")
            System.loadLibrary("avfilter")
            System.loadLibrary("swresample")
        }
    }

    // Need the READ_EXTERNAL_STORAGE permission if accessing video files that your
// app didn't create.

    // Container for information about each video.
    data class Video(val uri: Uri,
                     val name: String,
                     val data: String,
                     val duration: Int,
                     val size: Int
    )

    val videoList = mutableListOf<Video>()


    val videos=MutableLiveData<List<Video>>()

    fun getVideos(){


        val collection: Uri =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(
                            MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }

        val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA
        )

        // Show only videos that are at least 5 minutes in duration.
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
                java.util.concurrent.TimeUnit.MILLISECONDS.convert(5, java.util.concurrent.TimeUnit.SECONDS).toString()
        )

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        val query = ContentResolverCompat.query(
                contentResolver,
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder, null
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val data = cursor.getString(dataColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList += Video(contentUri, name,data, duration, size)
            }

            videos.postValue((videoList))
        }
    }

}

class Solutions {
    fun search(nums: IntArray, target: Int): Int {

        var start = 0;
        var end = nums.size - 1;

        if (target > nums[end] || target < nums[start]) return -1;

        while (start != end) {
            /* if(start+1==end){
                return if(nums[start]==target) start else if(nums[end]==target) end else -1
            }*/
            val splitIndex = (end + start) / 2

            if (nums[splitIndex] == target) return splitIndex

            if (nums[splitIndex] > target) {
                end = splitIndex - 1
            } else {
                start = splitIndex + 1
            }


        }
        return if (target == nums[start]) start else -1
    }

   // 3

    //1 2 3 3 3 3 1 2 3 2 1 3 2 1 3

    fun removeElements(nums: IntArray, `val`: Int): Int {

        var  idle=-1
        repeat(nums.size){ index ->
            if(nums[index]!=`val`){
                nums[++idle]=nums[index]
            }
        }
        return idle+1
    }

    fun minSubArrayLen(target: Int, nums: IntArray): Int {

        var find=false
        var minLength=Int.MAX_VALUE
        repeat(nums.size){ startIndex ->

            var sum=0
            for ( i in startIndex..nums.size-1){
                sum +=nums[i]
                if(sum>=target){
                    val res=i-startIndex+1
                    minLength= kotlin.math.min(minLength,res)
                    find=true
                    break
                }
            }
        }

        return if(!find) 0 else{
            minLength
        }
    }

/*
    fun search1(nums: IntArray, target: Int): Int {

        //qweasdasdasa
        //qwesada

        //return searchRange1(nums,0,nums.size-1,target)
        return 0
    }

    fun searchRange1(nums:IntArray,start:Int,end:Int,target: Int):Int{

        if(target>nums[end] || target<nums[start]) return -1;

        if(start==end && nums[start]==target) return  start

        if(start+1==end){
            return if(nums[start]==target) start else if(nums[end]==target) end else -1
        }

        val splitIndex = (end-start+1)/2

        if(nums[splitIndex]==target) return  splitIndex


        if(nums[splitIndex]>target){
            val newEnd=splitIndex-1
            return searchRange1(nums,start,newEnd,target)
        } else{
            return searchRange1(nums,splitIndex+1,end,target)
        }

    }*/


        //[-1,0,3,5,9,12] size:6
        //9

        // no repeat valid data set
        /* fun search2(nums: IntArray, target: Int): Int {
        if(target<nums[0] || target>nums[nums.size-1]) return  -1

        if(nums.size==1 && target==nums[0]) return 0

        if(nums.size==2){
            return if(nums[0]==target) 0 else if(nums[1]==target) 1 else -1
        }

        //nums>3
        val splitIndex=nums.size/2

        if(nums[splitIndex]==target) return splitIndex

        if(nums[splitIndex]>target){
            return search(mutableListOf<Int>().run {
                 repeat(splitIndex){ index ->
                     add(nums[index])
                 }
                 toIntArray()
            },target)
        }
        else{
            return search(mutableListOf<Int>().run {
                for(i in splitIndex..nums.size-1){
                    add(nums[i])
                }
                toIntArray()
            },target)
        }
    }
*/

        //[-1,0,3,5,9,12] size:6
        //9

}