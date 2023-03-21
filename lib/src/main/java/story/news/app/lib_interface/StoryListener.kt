package story.news.app.lib_interface

interface StoryListener {
    fun startStories()
    fun pauseStories()
    fun nextStory()
    fun onDescriptionClickListener(position: Int)
}