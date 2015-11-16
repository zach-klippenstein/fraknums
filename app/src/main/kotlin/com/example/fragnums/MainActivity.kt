package com.example.fragnums

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import com.squareup.enumsbatter.R
import java.util.ArrayList
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {

    private var backstack: ArrayList<BackstackFrame> = ArrayList()
    private var currentScreen: Screen = Screen.values.first()

    private val container by lazy { findViewById(R.id.main_container) as FrameLayout }
    private var currentView: View by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentScreen = Screen.values[savedInstanceState.getInt("currentScreen")]
            backstack = savedInstanceState.getParcelableArrayList<BackstackFrame>("backstack")
        }

        currentView = currentScreen.inflate(container)
        container.addView(currentView)
        currentScreen.bind(currentView)
        updateActionBar()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentScreen", currentScreen.ordinal)
        outState.putParcelableArrayList("backstack", backstack)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentScreen.unbind()
    }

    override fun onBackPressed() {
        if (backstack.size > 0) {
            goBack()
            return
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goBack()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun goTo(screen: Screen) {
        currentScreen.unbind()
        currentView.startAnimation(loadAnimation(this, R.anim.exit_forward))
        container.removeView(currentView)
        val backstackFrame = backstackFrame(currentScreen, currentView)
        backstack.add(backstackFrame)

        currentScreen = screen
        currentView = currentScreen.inflate(container)
        currentView.startAnimation(loadAnimation(this, R.anim.enter_forward))
        container.addView(currentView)
        currentScreen.bind(currentView)

        updateActionBar()
    }

    fun goBack() {
        currentScreen.unbind()
        currentView.startAnimation(loadAnimation(this, R.anim.exit_backward))
        container.removeView(currentView)

        val latest = backstack.removeAt(backstack.size - 1)
        currentScreen = latest.screen
        currentView = currentScreen.inflate(container)
        currentView.startAnimation(loadAnimation(this, R.anim.enter_backward))
        container.addView(currentView, 0)
        latest.restore(currentView)
        currentScreen.bind(currentView)

        updateActionBar()
    }

    private fun updateActionBar() {
        val actionBar = supportActionBar
        actionBar.setDisplayHomeAsUpEnabled(backstack.size != 0)
        var title: CharSequence? = currentScreen.title
        if (title == null) {
            title = getTitle()
        }
        actionBar.title = title
    }
}
