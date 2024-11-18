package com.bibek.coresdk.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bibek.coresdk.feature.dashboard.helper.DashboardPageHelper
import com.bibek.coresdk.feature.dashboard.helper.Post

@Composable
fun DashboardPage() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val posts = DashboardPageHelper.generateSamplePosts()
        items(items = posts) {
            PostCard(post = it, onLikeClicked = {})
        }
    }
}

@Composable
fun PostCard(post: Post, onLikeClicked: (Post) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = post.userName, fontWeight = FontWeight.Bold)

        Box(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            Text(
                text = "Post Image",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Caption
        Text(text = post.caption, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Like Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onLikeClicked(post) }) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Like")
            }
            Text(text = "${post.likes} Likes")
        }
    }
}

@Preview
@Composable
fun DashboardPagePreview() {
    DashboardPage()
}