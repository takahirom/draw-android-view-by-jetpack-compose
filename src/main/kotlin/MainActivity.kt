package com.github.takahirom.compose

import GlobalSnapshotManager
import android.app.Activity
import android.os.Bundle
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {
    private var composer: Recomposer = Recomposer(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalSnapshotManager.ensureStarted()
        val mainScope = MainScope()
        mainScope.launch(start = CoroutineStart.UNDISPATCHED) {
            withContext(coroutineContext + DefaultMonotonicFrameClock) {
                composer.runRecomposeAndApplyChanges()
            }
        }
        mainScope.launch {
            composer.state.collect {
                println("composer:$it")
            }
        }
        setContentView(runApp(this, composer))
    }
}
