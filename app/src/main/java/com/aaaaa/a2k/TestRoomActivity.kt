package com.aaaaa.a2k

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.migration.AutoMigrationSpec
import com.aaaaa.a2k.databinding.ActivityRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestRoomActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityRoomBinding=DataBindingUtil.setContentView(this,R.layout.activity_room)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-user"
        ).build()

        suspend fun insert(user: User)= withContext(Dispatchers.IO){
            db.userDao().insertAll(User(1,"jin","shengnan","121"))
            db.userDao().insertAll(User(3,"jin","lb","123"))
        }

        binding.insert.safeClick {
            GlobalScope.launch(Dispatchers.IO) {
                db.userDao().insertAll(User(1,"jin","shengnan","123"))
                db.userDao().insertAll(User(3,"jin","lb","2312"))
            }
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

    fun View.safeClick(a:(View) -> Unit){

        var lastClick:Long=0
        setOnClickListener {  v ->
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

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert(onConflict =REPLACE )
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}


@Database(entities = arrayOf(User::class), version = 3,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3,spec = AppDatabase.MyAutoMigration::class)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    @RenameTable(fromTableName = "database-name", toTableName = "database-user")
    class MyAutoMigration : AutoMigrationSpec
}

