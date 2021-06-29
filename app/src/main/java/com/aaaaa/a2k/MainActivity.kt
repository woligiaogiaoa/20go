package com.aaaaa.a2k

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentResolverCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.aaaaa.a2k.ScreenShot.saveImageToGallery
import com.aaaaa.a2k.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.lang.StringBuilder
import java.util.*


class MainActivity : AppCompatActivity() {

    val code=213

    /*   1
    * ----------    = (e -z)` *
    * 1 +  e -z
    * */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main)


        requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE),
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
                            click { v ->
                                val name = getVideoFormatName(video.data).also {
                                    Log.e(
                                        TAG,
                                        "video format${it}",
                                    )
                                }

                                //showToast(name)

                                GlobalScope.launch((Dispatchers.Default)) {

                                    val bitmap =
                                        Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
                                    if (showVideoPreview(video.data.also {
                                            Log.e(TAG, "videoFileUrl${it}")
                                        }, bitmap)) {
                                            runOnUiThread {
                                                showToast("true")
                                            }

                                        withContext(Dispatchers.Main) {
                                            binding.iv.setImageBitmap(bitmap).also {
                                                Log.e(TAG, "fuckckckck${bitmap}")
                                                saveImageToGallery(this@MainActivity, bitmap);
                                            }
                                        }
                                    }
                                    else{
                                        runOnUiThread {
                                            showToast("false")
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun a(){
        val a =PriorityQueue<Int>(100){ i: Int, i1: Int ->
            i-i1
        }
        a.poll()
    }

    val handler= Handler(Looper.getMainLooper())

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
                            //root previllage
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
        init    {
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

//aabaafa
//aaf
fun strStr(haystack: String, needle: String): Int {
    if(needle.isEmpty()) return 0
    //        end
    if(haystack.length<needle.length) return -1


    val endIndex=haystack.length-1
    val last=endIndex-(needle.length-1)

    for(i in 0..last){
        if(haystack.substring(i,i+needle.length).equals(needle))
            return i
    }

    return -1
}

fun reverseLeftWords(s: String, n: Int): String {
    fun reverse(array:CharArray,head:Int,end:Int){
        var l=head
        var r=end
        while(l<r) {
            val j = array[r]
            array[r] = array[l]
            array[l] = j
            l++
            r--
        }
    }

    var data=s.toCharArray()
    reverse(data,0,n-1)
    reverse(data,0,data.size-1)
    reverse(data,0,data.size-n-1)
    return String(data)
}

// 输入: s = "abcdefg", k = 2 输出: "cdefgab" //bacdefg

fun reverse(array:CharArray,head:Int,end:Int){
    var l=head
    var r=end
    while(l<r) {
        val j = array[r]
        array[r] = array[l]
        array[l] = j
        l++
        r--
    }
}

fun reverseWords(s: String): String {
    var l=0
    var r=s.length-1

    var array=s.toCharArray()
    while(l<r) {
        val j = array[r]
        array[r] = array[l]
        array[l] = j
        l++
        r--
    }

    val builder=StringBuilder()
    //   weqwew qwewqew qwewqe  qwe qwe qwe



    var head=0

    while(head<=array.size-1){
        while(head<=array.size-1 && array[head]==' '){
            head++
        }
        if(head>array.size-1)
            break

        var end=head
        while(end+1<array.size && array[end+1]!=' '){
            end++
        }
        reverse(array,head,end)
        builder.append(" "+ String(array,head,end-head+1))
        head=end
        head++
    }
    return builder.toString().trim()
}

fun replaceSpace(s: String): String {
    // " " to "%20"
    s.replace(" ","%20",true)
    val chars: Array<Char> = Array(s.length*3){
        ' '
    }
    var idex=0
    s.forEach {
        if(it==' '){
            chars[idex++]='%'
            chars[idex++]='2'
            chars[idex++]='0'
        }else{
            chars[idex++]=it
        }
    }

    return String(chars.toCharArray()).substring(0,idex)
}


val TAG=""



fun reverseString(s: CharArray): Unit {
    var i =0
    var j =s.size-1
}


/*Line 46: Char 5: error: expecting member declaration
    while(index<=s.length-1){
    ^
Line 46: Char 10: error: expecting member declaration
    while(index<=s.length-1){
         ^
Line 46: Char 11: error: expecting member declaration
    while(index<=s.length-1){
          ^
Line 46: Char 16: error: expecting member declaration
    while(index<=s.length-1){
               ^
Line 46: Char 18: error: expecting member declaration
    while(index<=s.length-1){
                 ^
Line 46: Char 19: error: expecting member declaration
    while(index<=s.length-1){
                  ^
Line 46: Char 20: error: expecting member declaration
    while(index<=s.length-1){
                   ^
Line 46: Char 26: error: expecting member declaration
    while(index<=s.length-1){
                         ^
Line 46: Char 27: error: expecting member declaration
    while(index<=s.length-1){
                          ^
Line 46: Char 28: error: expecting member declaration
    while(index<=s.length-1){
                           ^
Line 46: Char 29: error: expecting member declaration
    while(index<=s.length-1){
                            ^
Line 46: Char 29: error: function declaration must have a name
    while(index<=s.length-1){
                            ^
Line 56: Char 5: error: expecting member declaration
    return res
    ^
Line 56: Char 12: error: expecting member declaration
    return res
           ^
Line 58: Char 1: error: expecting a top level declaration
}
^
Line 42: Char 13: error: unresolved reference: s
    var res=s
            ^
Line 49: Char 23: error: unresolved reference: k
        var end=index+k-1
                      ^
Line 51: Char 16: error: unresolved reference: s
        if(end>s.length-1) end=s.length-1
               ^
Line 51: Char 32: error: unresolved reference: s
        if(end>s.length-1) end=s.length-1
                               ^
Line 52: Char 13: error: unresolved reference: reverseIJ
        res=reverseIJ(res,start,end)
            ^
Line 53: Char 18: error: unresolved reference: k
        index+=2*k
                 ^*/

/*public String reverseStr(String s, int k) {
        StringBuffer res = new StringBuffer();
        int length = s.length();
        int start = 0;
        while (start < length) {
            // 找到k处和2k处
            StringBuffer temp = new StringBuffer();
            // 与length进行判断，如果大于length了，那就将其置为length
            int firstK = (start + k > length) ? length : start + k;
            int secondK = (start + (2 * k) > length) ? length : start + (2 * k);

            //无论start所处位置，至少会反转一次
            temp.append(s.substring(start, firstK));
            res.append(temp.reverse());

            // 如果firstK到secondK之间有元素，这些元素直接放入res里即可。
            if (firstK < secondK) { //此时剩余长度一定大于k。
                res.append(s.substring(firstK, secondK));
            }
            start += (2 * k);
        }
        return res.toString();
    }*/

fun reverseStr(s: String, k: Int): String {

    fun reverseIJ(s:String,i1 :Int,j1:Int):String{
        var s1=s.toCharArray()
        var i =i1
        var j =j1
        while(i<j){

            var jchar=s1[j]
            s1[j]=s1[i]
            s1[i]=jchar

            i++
            j--
        }

        return String(s1)
    }


    var res: String =s

    var index=0

    while(index< res.length){
        //index...index+k-1

            //12121123213213

        var end=index+k-1
        var start=index
        if(end>res.length-1) end=res.length-1
        res=reverseIJ(res,start,end)
        index += 2*k
    }

    return res
}


fun fourSum(nums: IntArray, target: Int): List<List<Int>> {
    if (nums.size < 4) return emptyList()
    nums.sort()
    val resSet= mutableSetOf<MutableList<Int>>()
    val res= mutableListOf<MutableList<Int>>()
    repeat(nums.size){ i ->
       for(j in i+1..nums.size-1){
           var k=j+1
           var q=nums.size-1
           if(k>=q) break
           while(k!=q){
               val sum =nums[i]+nums[j]+nums[k]+nums[q]
               if(sum==target){
                   val new= mutableListOf<Int>().apply {
                       add(nums[i])
                       add(nums[j])
                       add(nums[k])
                       add(nums[q])
                   }
                   new.sort()
                   if(!resSet.contains(new)){
                       resSet.add(new)
                       res.add(new)
                   }
                   k++
               }else if(sum>target){
                   q--
               }else{
                   k++
               }
           }
       }
    }
    return res
}

fun threeSum2(nums: IntArray): List<List<Int>> {

    /*输入：
[-1,0,1,2,-1,-4] -4 -1 -1 0 1 2
输出：
[]
预期结果：
[[-1,-1,2],[-1,0,1]]*/

    if (nums.size < 3) return emptyList()

    val resSet= mutableSetOf<MutableList<Int>>()

    val res= mutableListOf<MutableList<Int>>()

    nums.sort()
    repeat(nums.size){ i ->
        var j =i+1
        var k=nums.size-1
        if(j>=k){
            return@repeat
        }
        while(j!=k){
            val sum=nums[i]+nums[j]+nums[k]
            if(sum==0){
               val new= mutableListOf<Int>().apply {
                   add(nums[i])
                   add(nums[j])
                   add(nums[k])
               }
                new.sort()
                if(!resSet.contains(new)){
                    resSet.add(new)
                    res.add(new)
                }
                j++
            }else if(sum>0){
                k--
            }else{
                j++
            }
        }
    }
    return res
}

fun threeSum1(nums: IntArray): List<List<Int>> {

    if (nums.size < 3) return emptyList()

    val resSet= mutableSetOf<MutableList<Int>>()
    val res= mutableListOf<MutableList<Int>>()

    repeat(nums.size) { i ->
        for (j in i + 1..nums.size - 1) {
            for(k in j+1..nums.size-1){
                if(nums[i]+nums[j]+nums[k]==0){
                    val new=mutableListOf<Int>().apply {
                        add(nums[i])
                        add(nums[j])
                        add(nums[k])
                    }
                    new.sort()
                    if(!resSet.contains(new)){
                        resSet.add(new)
                        res.add(new)
                    }
                }
            }
        }
    }

    return  res
}


/*输入
[-1,0,1,2,-1,-4]
输出
[[1,-1,0],[0,-1,1],[-1,-1,2],[-1,0,1],[1,0,-1],[0,1,-1]]
预期结果
[[-1,-1,2],[-1,0,1]]*/
fun threeSum(nums: IntArray): List<List<Int>> {

    if (nums.size < 3) return emptyList()

    val indexSumMap = mutableMapOf<Pair<Int, Int>, Int>()

    repeat(nums.size) { i ->
        for (j in i + 1..nums.size - 1) {
                indexSumMap[Pair(i, j)] = nums[i] + nums[j]
        }
    }
    val res = mutableListOf<MutableList<Int>>()

    val listSet= mutableSetOf<MutableList<Int>>()

    val resSet= mutableSetOf<MutableList<Int>>()

    for (mutableEntry in indexSumMap) {
        val sum12 = mutableEntry.value

        var indexa = 0

        nums.forEach {
            if (it == -sum12) {

                if (indexa != mutableEntry.key.first && indexa != mutableEntry.key.second) {

                        val sortList= mutableListOf<Int>()
                        sortList.add(indexa)
                        sortList.add(mutableEntry.key.first)
                        sortList.add(mutableEntry.key.second)
                        sortList.sort()
                        if(!listSet.contains(sortList)){
                            listSet.add(sortList)
                            Log.e(TAG, "threeSum:${sortList} in ${listSet} " )
                            val new=mutableListOf<Int>().apply {
                                add(nums[indexa])
                                add(nums[mutableEntry.key.first])
                                add(nums[mutableEntry.key.second])
                            }
                            new.sort()

                            if(!resSet.contains(new)){
                                resSet.add(new)
                                res.add(new)
                            }
                        }
                }
            }
            indexa++
        }
    }
    return res
}

fun canConstruct(ransomNote: String, magazine: String): Boolean {


    val datasMap=IntArray(26){0}

    magazine.forEach {
        val index=it-'a'
        datasMap[index]++
    }
    ransomNote.forEach {
        datasMap[it-'a']--
        if(datasMap[it-'a']<0)
            return false
    }
    return true
}




//digema
fun intersection(nums1: IntArray, nums2: IntArray): IntArray {

    if(nums1.size==0 || nums2.size==0)
        return emptyArray<Int>().toIntArray()

    val set1= mutableSetOf<Int>()
    val set2= mutableSetOf<Int>()

    nums1.forEach {
        set1.add(it)
    }
    nums2.forEach {
        if(set1.contains(it))
        set2.add(it)
    }

    val res = arrayListOf<Int>()

    var index=0
    set2.forEach {
        res.add(index++,it)
    }

    return  res.toIntArray()
}

@RequiresApi(Build.VERSION_CODES.N)
fun isAnagram(s: String, t: String): Boolean {

    if(s.length!=t.length) return false
    val map= mutableMapOf<Char,Int>()
    s.forEach {
        var num=map.getOrDefault(it,0)
        map[it]=++num
    }
    t.forEach {
        var num=map.getOrDefault(it,0)
        map[it]=--num
        if(map[it]!!<=-1){
            return false
        }
    }

    return true
}

//2x +2 y = x+ y + n (y +z)
//x + y =n(y+z)
fun detectCycle(head: ListNode?): ListNode? {
    var slow=head
    var fast=head
    while(slow?.next!=null && fast?.next!=null){
        slow=slow.next
        fast= fast.next!!.next
        if(fast!=null && slow===fast){
            var go=head
            var go1 =fast
            while(!(go===go1)){
                go=go!!.next
                go1=go1!!.next
            }
            return  go
        }
    }

    return null
}

// 1 2 3 4      //  2 1 3
fun swapPairs(head: ListNode?): ListNode? {

    head ?: return head

    var first: ListNode? =ListNode(-1)
    var second: ListNode? =ListNode(-1)
    first!!.next=second
    second!!.next=head

    var newHead: ListNode? =null

    while(second!!.next!=null && second.next!!.next!=null){
        first=second.next
        second=second!!.next!!.next

        val pairTailNext=second!!.next

        second.next=first
        first!!.next=pairTailNext

        val newSecond=first
        first=second
        second=newSecond
        if(newHead==null){
            newHead=first
        }
    }
    return if(newHead==null) head else newHead

}


fun reverseList(head: ListNode?): ListNode? {
    var curr: ListNode? =head
    var pre: ListNode? =null
    while(curr!=null){
        val next=curr.next
        curr.next=pre
        pre=curr
        curr=next
    }
    return pre
}


fun fourSumCount(nums1: IntArray, nums2: IntArray, nums3: IntArray, nums4: IntArray): Int {

    val sum12times= mutableMapOf<Int,Int>()
    val sum34times= mutableMapOf<Int,Int>()

    var num=0


    nums1.forEach {  a ->
        nums2.forEach {  b ->
            if(sum12times[a+b]==null)
                sum12times[a+b]=0
           sum12times[a+b] = sum12times[a+b]!! + 1
        }
    }

    nums3.forEach {  a ->
        nums4.forEach {  b ->
            if(sum34times[a+b]==null)
                sum34times[a+b]=0
            sum34times[a+b] = sum34times[a+b]!! + 1
        }
    }

    for (sum12time in sum12times) {
        if(sum34times.containsKey(-1*sum12time.key)){
            num += sum12time.value * sum34times[-1*sum12time.key]!!
        }
    }
    return num

}


//                   [1]        [1][3]      [1][2][3]
//["MyLinkedList","addAtHead","addAtTail","addAtIndex","get","deleteAtIndex","get"]
//[[],[1],[3],[1,2],[1],[1],[1]]

//
class MyLinkedList() {

    /** Initialize your data structure here. */

    var head: Node? = null

    //var tail: Node? =head

    var length = 0

    class Node(var `val`: Int ,
               var next: Node? = null)

    /** Get the value of the index-th node in the linked list. If the index is invalid, return -1. */
    fun get(index: Int): Int {
        if (length == 0) return -1
        if (index < 0 || index > length - 1) return -1

        var iterator: Node? = head
        repeat(index) {
            iterator = iterator!!.next
        }
        return iterator!!.`val`
    }

    /** Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list. */
    fun addAtHead(`val`: Int) {
        val add = Node(`val`)
        length++
        if (length == 0) {
            head = add
        } else {
            add.next = head
            head = add
        }
    }

    /** Append a node of value val to the last element of the linked list. */
    fun addAtTail(`val`: Int) {
        if (length == 0) {
            addAtHead(`val`)
        } else {
            var tail = head
            repeat(length - 1) {
                tail = tail!!.next
            }
            Node(`val`).also {
                tail!!.next = it
            }
            length++
        }
    }


    //[1,3]
    /** Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted. */
    fun addAtIndex(index: Int, `val`: Int) {

        //[0 ,length]
        if (index > length) return


        var targetIndex = -1
        targetIndex = index
        if (targetIndex < 0) targetIndex = 0

        if (length == 0) {
            addAtHead(`val`)
            return
        }


        var ite: Node? = head
        repeat(targetIndex) {
            ite = ite!!.next
        }

        if (ite == null) {
            addAtTail(`val`)
            return
        }


        var itePre: Node? = head

        if (targetIndex >= 1) {

            val insert = Node(`val`).apply {
                next = ite
            }

            repeat(targetIndex - 1) {
                itePre = itePre!!.next
            }
            itePre!!.next = insert
            length++

        } else {
            addAtHead(`val`)
        }
    }

    /** Delete the index-th node in the linked list, if the index is valid. */
    fun deleteAtIndex(index: Int) {
        if (index < 0 || index >= length) {
            return
        }

        var toDelete = head //to delete
        repeat(index) {
            toDelete = toDelete!!.next
        }

        var ite1 = head

        if (index >= 1) {
            repeat(index - 1) {
                ite1 = ite1!!.next
            }
            ite1!!.next = toDelete!!.next

        } else {
            val new = toDelete!!.next
            head = new

        }
        length--

    }

}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * var obj = MyLinkedList()
 * var param_1 = obj.get(index)
 * obj.addAtHead(`val`)
 * obj.addAtTail(`val`)
 * obj.addAtIndex(index,`val`)
 * obj.deleteAtIndex(index)
 */


class ListNode(var `val`: Int) {
         var next: ListNode?=null
     }

fun removeElements(head: ListNode?, `val`: Int): ListNode? {

    var virtualHead:ListNode?=ListNode(-1).apply {
        next = head
    }
    val rememberVirtual: ListNode =virtualHead!!

    while(virtualHead!!.next!=null){

        var node: ListNode? =virtualHead.next!!    //1
                                                   //2
                                                   //3
        node.also {                                //4
            if(node!!.`val`==`val`){
                val next: ListNode? =node!!.next
                virtualHead!!.next=next
                node=null
            }
            else{
                virtualHead=virtualHead!!.next!!
            }
        }
    }

    return rememberVirtual.next

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

    /**
     * Example:
     * var li = ListNode(5)
     * var v = li.`val`
     * Definition for singly-linked list.
     * class ListNode(var `val`: Int) {
     *     var next: ListNode? = null
     * }
     */




    fun generateMatrix(n: Int): Array<IntArray> {

        val matrix =
            mutableListOf<IntArray>().apply {
                repeat(n) {
                    add(mutableListOf<Int>().apply {
                        repeat(n){
                            add(0)
                        }
                    }.toIntArray())
                }
            }

        var loop = n/2

        if(n % 2 >0) loop++

        var start=0

        var data=1

        while(loop -- > 0){
            for(i in start..n-1-start){
                //first loop
                matrix[start][i]=data++

            }

            if(start <n-1-start)
            for(i in start+1..n-1-start){
                matrix[i][n-1-start]=data++
            }

            if(start <n-1-start)
            for(i in n-1-start-1 downTo start step 1){
                matrix[n-1-start][i]=data++
            }

            if(start <n-1-start-1)
                for(i in n-1-start-1 downTo start+1 step 1){
                    matrix[i][start]=data++
                }
            start++
        }

        return  mutableListOf<IntArray>().apply {
            repeat(n){
                add(matrix[it])
            }
        }.toTypedArray()


    }

    fun removeElements1(nums: IntArray, `val`: Int): Int {

        var  idle=-1
        repeat(nums.size){ index ->
            if(nums[index]!=`val`){
                nums[++idle]=nums[index]
            }
        }
        return idle+1
    }

    fun minSubArrayLen1(target: Int, nums: IntArray): Int {

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

    fun twoSum(nums: IntArray, target: Int): IntArray {

        val map= mutableMapOf<Int,Int>()

        var  index=0
        nums.forEach {
            map.put(it,index++)
        }

        var index1 =0
        nums.forEach {  candidate ->
            if(map.contains(target-candidate)){
                val b =map[target-candidate]!!
                if(b !=index1)
                return IntArray(2){ resIndex ->
                    if(resIndex==0) index1 else b
                }
            }

            index1++
        }

        return emptyArray<Int>().toIntArray()
    }

    fun twoSum1(nums: IntArray, target: Int): IntArray {
        repeat(nums.size){ i ->

            for(j in i+1..nums.size-1){
                if(nums[i]+nums[j]==target)
                    return IntArray(2){ index ->
                        if(index==0) i else j
                    }
            }
        }
        return emptyArray<Int>().toIntArray()
    }

    fun isHappy(n: Int): Boolean {

        if(n==0) return false

        fun getNext(n:Int): Int {
            var now=n
            var res =0
            while(now>0){
                val temp=now%10
                res+= temp * temp
                now =now /10
            }
            return res
        }

        val set= mutableSetOf<Int>()

        var now=n.also {
            set.add(it)
        }

        while(now!=1){

            now=getNext(now)
            if(now==1) {
                return true
            }else{
                if(set.contains(now)) return false
                set.add(now)
            }
        }

        return true
    }

    /*
    *
    *  private int getNextNumber(int n) {
            int res = 0;
            while (n > 0) {
                int temp = n % 10;
                res += temp * temp;
                n = n / 10;
            }
            return res;
        }*/

    fun minSubArrayLen(target: Int, nums: IntArray): Int {
            var start=0
            var end=0
            var min=Int.MAX_VALUE
            var sum=0
            while(end <=nums.size-1){
                sum+=nums[end]
                while(sum>=target){
                    val length= end-start+1
                    min=kotlin.math.min(min,length)
                    sum-=nums[start]
                    start++
                }
                //finally
                end++
            }
            return if(min== Int.MAX_VALUE) 0 else min
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