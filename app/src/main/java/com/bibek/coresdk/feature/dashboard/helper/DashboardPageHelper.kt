package com.bibek.coresdk.feature.dashboard.helper

data class Post(
    val userName: String,
    val imageUrl: String,
    val caption: String,
    var likes: Int
)

object DashboardPageHelper {
    fun generateSamplePosts(): List<Post> {
        return listOf(
            Post("john_doe", "https://via.placeholder.com/150", "A beautiful sunset!", 12),
            Post("jane_doe", "https://via.placeholder.com/150", "Enjoying my vacation.", 5),
            Post("alice_smith", "https://via.placeholder.com/150", "Coffee time!", 25),
        )
    }
}