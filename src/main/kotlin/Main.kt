package com.github.takahirom.compose

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

fun runApp(context: Context, composer: CompositionContext): FrameLayout {
  val rootDocument = FrameLayout(context)
  rootDocument.setContent(composer) {
    CompositionLocalProvider(localContext provides context) {
      AndroidViewApp()
    }
  }
  return rootDocument
}

@Composable
private fun AndroidViewApp() {
  var count by remember { mutableStateOf(0) }
  FrameLayout {
    TextView(
      text = "Android View!!$count",
      onClick = {
        count++
      }
    )
  }
}

val localContext = compositionLocalOf<Context> { TODO() }

fun FrameLayout.setContent(
  parent: CompositionContext,
  content: @Composable () -> Unit
): Composition {
  return Composition(ViewApplier(this), parent).apply {
    setContent(content)
  }
}

@Composable
fun TextView(text: String, onClick: () -> Unit) {
  val context = localContext.current
  ComposeNode<TextView, ViewApplier>(
    factory = {
      TextView(context)
    },
    update = {
      set(text) {
        this.text = text
      }
      set(onClick) {
        setOnClickListener { onClick() }
      }
    },
  )
}

@Composable
fun FrameLayout(children: @Composable () -> Unit) {
  val context = localContext.current
  ComposeNode<FrameLayout, ViewApplier>(
    factory = {
      FrameLayout(context)
    },
    update = {},
    content = children,
  )
}

class ViewApplier(val view: FrameLayout) : AbstractApplier<View>(view) {
  override fun onClear() {
    (view as? ViewGroup)?.removeAllViews()
  }

  override fun insertBottomUp(index: Int, instance: View) {
    // NOT Supported
    TODO()
  }

  override fun insertTopDown(index: Int, instance: View) {
    (view as? ViewGroup)?.addView(instance, index)
  }

  override fun move(from: Int, to: Int, count: Int) {
    // NOT Supported
    TODO()
  }

  override fun remove(index: Int, count: Int) {
    (view as? ViewGroup)?.removeViews(index, count)
  }
}
