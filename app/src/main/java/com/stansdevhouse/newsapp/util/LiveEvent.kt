package com.stansdevhouse.newsapp.util

import android.util.ArraySet
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A LiveData that emits value only "once"
 */

class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // if there is a pending value set up the new wrapper with that pending value, it should
        // get notified right away
        val wrapper = ObserverWrapper(observer, pending.getAndSet(false))

        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove<Observer<out Any?>?>(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        if (observers.size == 0) {
            pending.set(true) // if there are no observers mark a pending flag so new observers will pick up the event
        } else {
            pending.set(false) // if there are some,then the existing observers consume the data and new ones have to wait for the next setValue
            observers.forEach { it.newValue() }
        }

        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>, initialPending: Boolean = false) : Observer<T> {

        private var pending = initialPending

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}
