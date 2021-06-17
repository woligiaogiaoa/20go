package com.aaaaa.a2k

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.migration.AutoMigrationSpec
import com.aaaaa.a2k.databinding.ActivityRoomBinding
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.hmsscankit.WriterException
import com.huawei.hms.ml.scan.HmsBuildBitmapOption
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TestRoomActivity:AppCompatActivity() {

    val REQUEST_CODE_SCAN_DEFAULT_MODE=1002

    val CAMERA_REQ_CODE=45456123

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SCAN_DEFAULT_MODE -> {
                val hmsScan: HmsScan? =
                    data?.getParcelableExtra(ScanUtil.RESULT) // 获取扫码结果 ScanUtil.RESULT
                if (!TextUtils.isEmpty(hmsScan?.getOriginalValue())) {
                    hmsScan?.getOriginalValue().also {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }

                val content = "QR Code Content"
                val type = HmsScan.QRCODE_SCAN_TYPE
                val width = 400
                val height = 400
                val bm = BitmapFactory.decodeResource(resources, R.drawable.ic11111 )
                val options = HmsBuildBitmapOption.Creator().setBitmapMargin(3)
                    .setQRLogoBitmap(bm)
                    .create()

                try {
                    //如果未设置HmsBuildBitmapOption对象，生成二维码参数options置null
                    val qrBitmap = ScanUtil.buildBitmap(content, type, width, height, options)
                    ScreenShot.saveImageToGallery(this, qrBitmap);
                } catch (e: WriterException) {
                    Log.w("buildBitmap", e)
                }

            }

        }
    }

    //实现“onRequestPermissionsResult”函数接收校验权限结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //判断“requestCode”是否为申请权限时设置请求码CAMERA_REQ_CODE，然后校验权限开启状态
        if (requestCode == CAMERA_REQ_CODE && grantResults.size == 2 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            //调用扫码接口，构建扫码能力，需您实现
            fun startDefaultMode(view: View) {
                // 扫码选项参数
                val options =
                    HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
                ScanUtil.startScan(
                    this, REQUEST_CODE_SCAN_DEFAULT_MODE,
                    options
                )
            }
            startDefaultMode(ImageView(this))
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityRoomBinding=DataBindingUtil.setContentView(this, R.layout.activity_room)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-user"
        ).build()

        suspend fun insert(user: User)= withContext(Dispatchers.IO){
            db.userDao().insertAll(User(1, "jin", "shengnan", "121"))
            db.userDao().insertAll(User(3, "jin", "lb", "123"))
        }

        binding.insert.safeClick {
          /*  GlobalScope.launch(Dispatchers.IO) {
                db.userDao().insertAll(User(1,"jin","shengnan","123"))
                db.userDao().insertAll(User(3,"jin","lb","2312"))
            }*/

//CAMERA_REQ_CODE为用户自定义，用于接收权限校验结果
            //CAMERA_REQ_CODE为用户自定义，用于接收权限校验结果
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                CAMERA_REQ_CODE
            )
        }

        lifecycleScope.launch {
            db.userDao().getAll().collect { users ->
                binding.rv.withModels {
                    users.forEach {
                        simpleText {
                            id(it.toString())
                            info(it.toString())
                        }
                    }
                }
            }
        }


    }

    fun View.safeClick(a: (View) -> Unit){

        var lastClick:Long=0
        setOnClickListener { v ->
            val now=System.currentTimeMillis()
            val pass=now-lastClick
            if(pass>=1500){
                lastClick=now
                a.invoke(v)
            }
        }
    }

}



@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "name") val name: String?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

        @Query(
            "SELECT * FROM user WHERE first_name LIKE :first AND " +
                    "last_name LIKE :last LIMIT 1"
        )
    fun findByName(first: String, last: String): User

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}


@Database(
    entities = arrayOf(User::class), version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.MyAutoMigration::class)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    @RenameTable(fromTableName = "database-name", toTableName = "database-user")
    class MyAutoMigration : AutoMigrationSpec
}

