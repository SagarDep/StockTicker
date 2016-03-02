package com.github.premnirmal.ticker

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import rx.Observable
import rx.android.app.AppObservable
import rx.android.lifecycle.LifecycleEvent
import rx.android.lifecycle.LifecycleObservable
import rx.subjects.BehaviorSubject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by premnirmal on 2/26/16.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

    private fun lifecycle(): Observable<LifecycleEvent> {
        return lifecycleSubject.asObservable()
    }

    /**
     * Using this to automatically unsubscribe from observables on lifecycle events
     * @param observable
     * *
     * @param
     * *
     * @return
     */
    protected fun <T> bind(observable: Observable<T>): Observable<T> {
        val boundObservable = AppObservable.bindActivity(this, observable)
        return LifecycleObservable.bindActivityLifecycle(lifecycle(), boundObservable)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(LifecycleEvent.START)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(LifecycleEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(LifecycleEvent.RESUME)
    }

    protected fun showDialog(message: String, listener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }): AlertDialog {
        return AlertDialog.Builder(this).setMessage(message).setCancelable(false).setNeutralButton("OK", listener).show()
    }

    protected fun showDialog(message: String, positiveOnClick: DialogInterface.OnClickListener, negativeOnClick: DialogInterface.OnClickListener): AlertDialog {
        return AlertDialog.Builder(this).setMessage(message).setCancelable(false).setPositiveButton("YES", positiveOnClick).setNegativeButton("NO", negativeOnClick).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
