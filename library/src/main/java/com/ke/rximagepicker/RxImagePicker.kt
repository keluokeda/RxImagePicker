package com.ke.rximagepicker

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

class RxImagePicker(fragmentActivity: FragmentActivity) {
    private val tag = BuildConfig.LIBRARY_PACKAGE_NAME + RxImagePicker::class.java.name
    private val delegateFragment: DelegateFragment

    init {
        val fragment = fragmentActivity.supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            delegateFragment = DelegateFragment()
            fragmentActivity.supportFragmentManager.beginTransaction().add(delegateFragment, tag)
                .commitNow()
        } else {
            delegateFragment = fragment as DelegateFragment
        }
    }

    fun pick(source: Int): Observable<Result> {
        delegateFragment.start(source)

        return Observable.just(1)
            .flatMap {
                delegateFragment.resultSubject
            }
    }

    companion object {
        const val SOURCE_CAMERA = 0
        const val SOURCE_GALLERY = 1
    }
}