package com.android.nobetcieczane.util

import android.view.View
import androidx.core.widget.NestedScrollView


object Tools {

    fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
        nested.post { nested.scrollTo(500, targetView.bottom) }
    }
}