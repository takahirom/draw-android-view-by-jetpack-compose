package com.github.takahirom.compose

import android.app.Activity
import android.os.Bundle
import androidx.compose.runtime.Recomposer
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : Activity() {
  private var composer: Recomposer = Recomposer(Dispatchers.Main)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    GlobalSnapshotManager.ensureStarted()
    val mainScope = MainScope()
    mainScope.launch(start = CoroutineStart.UNDISPATCHED) {
      composer.runRecomposeAndApplyChanges()
    }
    mainScope.launch {
      composer.state.collect {
        println("composer:$it")
      }
    }
    setContentView(runApp(this, composer))
  }
}
