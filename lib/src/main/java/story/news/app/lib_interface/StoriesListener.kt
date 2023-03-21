package story.news.app.lib_interface

interface StoriesListener {
    fun onNext()
    fun onPrev()
    fun onComplete()
}