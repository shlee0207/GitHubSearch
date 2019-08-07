package com.shlee.githubsearch.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.shlee.githubsearch.domain.User

@Entity(tableName = "Bookmark",
        indices = [Index(value = ["uid"], unique = true)])
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uid") val uid: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUlr: String
) {

    companion object {
        fun fromUser(user: User) = Bookmark(
            uid = user.id,
            name = user.name,
            avatarUlr = user.avatarUrl
        )

        fun toUser(bookmark: Bookmark) = User(
            id = bookmark.uid,
            name = bookmark.name,
            avatarUrl = bookmark.avatarUlr,
            isBookmarked = true
        )
    }

}