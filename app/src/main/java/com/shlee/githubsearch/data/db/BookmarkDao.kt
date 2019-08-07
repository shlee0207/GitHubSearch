package com.shlee.githubsearch.data.db

import androidx.room.*
import io.reactivex.Single

@Dao
interface BookmarkDao {

    @Query("SELECT * from Bookmark")
    fun selectAll(): Single<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: Bookmark): Single<Long>

    @Query("DELETE FROM Bookmark WHERE uid = :uid")
    fun delete(uid: Long): Single<Int>

}
