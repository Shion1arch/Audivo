
package com.mardous.booming.extensions

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.Fade
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun DialogFragment.getAlertDialog() = dialog as? AlertDialog

fun DialogFragment.requireAlertDialog() = requireNotNull(getAlertDialog()) {
    "There is not an AlertDialog attached to this DialogFragment"
}

fun AlertDialog.Builder.create(onShow: (AlertDialog) -> Unit) =
    create().onShow(onShow)

fun AlertDialog.Builder.show(onShow: (AlertDialog) -> Unit) =
    show().onShow(onShow)

fun AlertDialog.onShow(onShow: (AlertDialog) -> Unit) = apply {
    setOnShowListener { onShow(this) }
}

fun Fragment.isLandscape() = resources.isLandscape

inline fun <R> Fragment.requestActivity(crossinline consumer: (AppCompatActivity) -> R) =
    (activity as? AppCompatActivity)?.let(consumer)

inline fun <R> Fragment.requestContext(crossinline consumer: (Context) -> R) =
    context?.let(consumer)

inline fun Fragment.runOnUi(crossinline consumer: (View) -> Unit) {
    view?.let { it.post { consumer(it) } }
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T =
    this.apply { arguments = Bundle().apply(argsBuilder) }

fun Fragment.plurals(resId: Int, quantity: Int) = requireContext().plurals(resId, quantity)

fun Fragment.showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) = context?.showToast(textId, duration)

fun Fragment.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) = context?.showToast(text, duration)

@Suppress("DEPRECATION")
inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    value as? T ?: default
}

@Suppress("DEPRECATION")
inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(value as? T ?: default) { key }
}

fun FragmentActivity.currentFragment(navHostId: Int): Fragment? {
    val navHostFragment: NavHostFragment = whichFragment(navHostId)
    return navHostFragment.childFragmentManager.fragments.firstOrNull()
}

@Suppress("UNCHECKED_CAST")
fun <T : Fragment> FragmentActivity.whichFragment(@IdRes id: Int): T {
    return supportFragmentManager.findFragmentById(id) as T
}

@Suppress("UNCHECKED_CAST")
fun <T : Fragment> Fragment.whichFragment(@IdRes id: Int): T {
    return childFragmentManager.findFragmentById(id) as T
}

fun LifecycleOwner.launchAndRepeatWithViewLifecycle(state: Lifecycle.State = Lifecycle.State.STARTED, block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state, block)
    }
}

fun Fragment.setSupportActionBar(toolbar: Toolbar, title: CharSequence? = toolbar.title) {
    requestActivity {
        it.setSupportActionBar(toolbar)
        it.supportActionBar?.title = title
    }
}

fun Fragment.getOnBackPressedDispatcher() = requireActivity().onBackPressedDispatcher

fun Fragment.materialSharedAxis(
    view: View = requireView(),
    direction: Int = MaterialSharedAxis.X,
    prepareTransition: Boolean = true
) {
    MaterialSharedAxis(direction, true).let { enterTransition = it; exitTransition = it }
    MaterialSharedAxis(direction, false).let { returnTransition = it; reenterTransition = it }
    if (prepareTransition) {
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}

fun Fragment.topLevelTransition(view: View = requireView()) {
    Fade().let { enterTransition = it; reenterTransition = it }
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }
}