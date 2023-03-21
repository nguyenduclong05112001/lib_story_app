package com_story.example.stories_app_lib

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stories_app_lib.databinding.MainFragmentBinding
import com_story.example.lib.StoryBuilder
import com_story.example.lib.api.APIServer
import com_story.example.lib.lib_interface.OnStoryChangedListener
import com_story.example.lib.lib_interface.StoryClickListeners
import com_story.example.lib.lib_model.StoryAPI
import com_story.example.lib.lib_model.StoryDocsAPI
import com_story.example.lib.lib_model.StoryLocal
import kotlinx.coroutines.*

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var story: StoryAPI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDatafromAPI()
        binding.btnShowStory.setOnClickListener {
            showStories()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getDatafromAPI() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = APIServer.api.getnewStory()
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    story = result.body()!!
                    Log.d("NEWTAG", "getDatafromAPI: $result")
                    binding.btnShowStory.visibility = View.VISIBLE
                } else {
                    binding.btnShowStory.visibility = View.GONE
                }
            }
        }
    }

    fun showStories() {
        val myStories: ArrayList<StoryLocal?> = ArrayList()

        val urls: ArrayList<StoryDocsAPI> = story.data.news.docs

        Log.d("ádasdasd", "showStories: ${urls.size}")

        urls.forEach {
            Log.d("ádasdasd", "showStories: ${it.video}")
        }

        for (item in urls) {
            if (item.video.isEmpty()) {
                myStories.add(StoryLocal(item.cover, item.video, item.text))
            } else {
                myStories.add(StoryLocal(item.cover, item.video, item.text, 15000L))
            }
        }

        StoryBuilder(childFragmentManager)
            .setStoriesList(myStories)
            .setRtl(false)
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
                }

                override fun onTitleIconClickListener(position: Int) {
                    // do some thing when avatar user
                }
            })
            .setOnStoryChangedCallback(object : OnStoryChangedListener {
                override fun storyChanged(position: Int) {
                }
            })
            .setStartingIndex(0)
            .build()
            .show()
    }
}