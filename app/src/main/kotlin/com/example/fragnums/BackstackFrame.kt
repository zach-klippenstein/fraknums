package com.example.fragnums

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import android.view.View

/** Creates a [BackstackFrame] with [view]'s state. */
fun backstackFrame(screen: Screen, view: View): BackstackFrame {
    val viewState = SparseArray<Parcelable>()
    view.saveHierarchyState(viewState)
    return BackstackFrame(screen, viewState)
}

data class BackstackFrame(val screen: Screen,
                          val viewState: SparseArray<Parcelable>) : Parcelable {

    fun restore(view: View) {
        view.restoreHierarchyState(viewState);
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(screen.ordinal)
        val bundle = Bundle()
        bundle.putSparseParcelableArray("viewState", viewState)
        dest.writeBundle(bundle)
    }

    companion object {

        val CREATOR: Parcelable.Creator<BackstackFrame> = object : Parcelable.Creator<BackstackFrame> {
            override fun createFromParcel(source: Parcel): BackstackFrame {
                val screen = Screen.values[source.readInt()]
                val bundle = source.readBundle()
                val viewState = bundle.getSparseParcelableArray<Parcelable>("viewState")
                return BackstackFrame(screen, viewState)
            }

            override fun newArray(size: Int): Array<BackstackFrame?> {
                return arrayOfNulls(size)
            }
        }
    }
}
