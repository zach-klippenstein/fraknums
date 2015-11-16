package com.example.fragnums

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.enumsbatter.R

enum class Screen(@LayoutRes private val layoutResId: Int,
                  @StringRes private val titleResId: Int = -1) {

    MAIN(R.layout.main) {
        override fun onBind() {
            findViewById<View>(R.id.enumsmatter).setOnClickListener {
                goTo(ENUMSMATTER)
            }
            findViewById<View>(R.id.tshirt).setOnClickListener {
                goTo(T_SHIRT)
            }
        }
    },

    ENUMSMATTER(R.layout.enumsmatter, R.string.enumsmatter_title),

    T_SHIRT(R.layout.tshirt, R.string.tshirt_title) {
        override fun onBind() {
            val intent = Intent(ACTION_VIEW, Uri.parse("https://teespring.com/enumsmatter"))
            findViewById<View>(R.id.button).setOnClickListener { view ->
                activity.startActivity(intent)
            }
        }
    };

    protected var view: View? = null

    fun inflate(container: ViewGroup): View {
        val context = container.context
        return LayoutInflater.from(context).inflate(layoutResId, container, false)
    }

    fun bind(view: View) {
        this.view = view
        onBind()
    }

    fun unbind() {
        onUnbind()
        view = null
    }

    protected open fun onBind() {
    }

    protected open fun onUnbind() {
    }

    val title: CharSequence?
        get() = if (titleResId == -1) null
        else activity.getString(titleResId)

    protected fun goBack() {
        activity.goBack()
    }

    protected fun goTo(screen: Screen) {
        activity.goTo(screen)
    }

    protected fun <T : View> findViewById(id: Int): T {
        return view!!.findViewById(id) as T
    }

    protected val activity: MainActivity
        get() = view!!.context as MainActivity
}
