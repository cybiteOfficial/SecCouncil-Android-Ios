package com.example.seccouncil.videoplayer


/** Represents the minimal data needed to display the course content.
 *  In a real-world app, you would adapt this to match your API response more closely.
 */
data class Course(
    val id: String,
    val courseName: String,
    val courseDescription: String,
    val instructorName: String,
    val videos: List<VideoItem>,
    val rating: Float? = null,             // Average rating (optional)
    val userRating: Int? = null,           // Current user's rating (optional)
    val watchedPercentage: Float = 0f      // How much of the course the user has watched (in %)
)

data class VideoItem(
    val videoId: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val durationSeconds: Float
)

    val videoItems = listOf(
        VideoItem(
            videoId = "video1",
            title = "Introduction to the Course",
            description = "Welcome and course overview",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            durationSeconds = 120f
        ),
        VideoItem(
            videoId = "video2",
            title = "Module 1: Basics",
            description = "Fundamental concepts",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            durationSeconds = 300f
        ),
        VideoItem(
            videoId = "video3",
            title = "Module 2: Intermediate",
            description = "Intermediate concepts",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            durationSeconds = 450f
        ),
        VideoItem(
            videoId = "video4",
            title = "Module 3: Advanced",
            description = "Advanced concepts",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            durationSeconds = 600f
        ),
        VideoItem(
            videoId = "video5",
            title = "Conclusion and Next Steps",
            description = "Summary and further learning",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            durationSeconds = 180f
        )
    )
