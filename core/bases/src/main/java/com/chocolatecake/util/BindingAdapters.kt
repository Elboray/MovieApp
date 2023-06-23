package com.chocolatecake.util

import android.app.UiModeManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chocolatecake.bases.BaseAdapter
import com.chocolatecake.bases.R
import com.google.android.material.progressindicator.LinearProgressIndicator

@BindingAdapter(value = ["app:items"])
fun <T> RecyclerView.setRecyclerItems(items: List<T>?) {
    (adapter as BaseAdapter<T>).setItems(items ?: emptyList())
}

@BindingAdapter(value = ["app:isVisible"])
fun View.isVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

@BindingAdapter(value = ["app:isVisibleOrGone"])
fun View.isVisibleOrGone(isVisible: Boolean?) {
    if (isVisible == true) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:hideWhenNotLoggedIn"])
fun View.hideWhenNotLoggedIn(hideWhenNotLoggedIn: Boolean?) {
    if (hideWhenNotLoggedIn == false) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}


@BindingAdapter("app:setTipError")
fun EditText.setTipError(errorMessage: String?) {
    if (errorMessage == null) return
    else error = errorMessage
}

@BindingAdapter(value = ["app:imageUrl"])
fun ImageView.loadImage(imageUrl: String?) {
    if (imageUrl == "https://image.tmdb.org/t/p/w500null") {
        Glide.with(context)
            .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
            .thumbnail(Glide.with(context).load(R.raw.loading_images))
            .centerCrop()
            .into(this)
    } else Glide.with(context)
        .load(imageUrl)
        .thumbnail(Glide.with(context).load(R.raw.loading_images))
        .centerCrop()
        .into(this)

}

@BindingAdapter(value = ["app:profileUrl"])
fun ImageView.loadProfileImage(profileUrl: String?) {
    if (profileUrl=="https://image.tmdb.org/t/p/w500null"){
        Glide.with(context)
            .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
            .fitCenter()
            .centerCrop()
            .into(this)
    }
    else Glide.with(context)
        .load(profileUrl)
        .fitCenter()
        .centerCrop()
        .into(this)

}

@BindingAdapter(value = ["app:hideResult", "app:query"])
fun <T> View.hideResult(list: List<T>?, text: String) {
    if (list.isNullOrEmpty() && text.isNotBlank()) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:hideWhenNoResult"])
fun <T> View.hideWhenNoResult(list: List<T>?) {
    if (list.isNullOrEmpty()) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}

@BindingAdapter(value = ["app:hideWhenEmpty"])
fun <T> View.hideWhenEmpty(list: List<T>?) {
    if (list.isNullOrEmpty()) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}
@BindingAdapter(value = ["app:loading"])
fun LinearProgressIndicator.isLoading(isLoading: Boolean?) {
    if (isLoading == true) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:showWhenQueryEmpty"])
fun View.showWhenEmptyData(query: String?) {
    if (query?.isEmpty() == true) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:showWhenNoResult"])
fun <T> View.showWhenNoResult(list: List<T>?) {
    if (list.isNullOrEmpty()) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("app:showWhenError")
fun <T> View.showWhenError(list: List<T>?) {
    if (list?.isNotEmpty() == true) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:showWhenQueryEmpty", "app:showWhenFailure"])
fun View.showBasedOnState(
    query: String?,
    error: List<String>?
) {
    visibility = when {
        query.isNullOrEmpty() && error.isNullOrEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter(value = ["app:showWhenFailure"])
fun View.showBasedOnState(
    error: List<String>?
) {
    visibility = when {
        error?.isNotEmpty() == true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("app:toggleUiMode")
fun SwitchCompat.toggleUiMode(uiModeManager: UiModeManager) {
    this.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
        } else {
            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES
        }
    }
}

@BindingAdapter("app:onClickNavigation")
fun androidx.appcompat.widget.Toolbar.addNavigationListener(onClick: () -> Unit) {
    this.setNavigationOnClickListener {
        onClick()
    }
}

@BindingAdapter("app:onClickNavigation")
fun com.google.android.material.appbar.MaterialToolbar.addNavigationListener(onClick: () -> Unit) {
    this.setNavigationOnClickListener {
        onClick()
    }
}