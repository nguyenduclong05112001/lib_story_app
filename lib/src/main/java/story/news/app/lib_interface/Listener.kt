package story.news.app.lib_interface

interface Listener {
    fun onDismissed()
    fun onShouldInterceptTouchEvent(): Boolean
}