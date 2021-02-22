package pokercc.android.gridlayoutmangerdemo

import android.content.res.Resources
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pokercc.android.gridlayoutmangerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding.defaultGrid) {
            adapter = createAdapter()
            layoutManager = createLayoutManager()
            addItemDecoration(DebugItemDecoration())
            addItemDecoration(DefaultItemDecoration())
        }
        with(binding.averageGrid) {
            adapter = createAdapter()
            layoutManager = createLayoutManager()
            addItemDecoration(DebugItemDecoration())
            addItemDecoration(AverageGridItemDecoration())
        }
    }

    private fun RecyclerView.createLayoutManager() =
        GridLayoutManager(context, 5)

    private fun createAdapter() = SampleAdapter((0..16).map { it % 2 == 0 })
}

private class DebugItemDecoration : RecyclerView.ItemDecoration() {
    private val linePaint = Paint().apply {
        strokeWidth = 1f
        color = Color.BLACK
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val parentWidth = parent.measuredWidth - parent.paddingStart - parent.paddingEnd
        val spanWidth = parentWidth / spanCount
        with(c) {
            for (i in 0..spanCount) {
                val x = spanWidth * i.toFloat()
                drawLine(x, 0f, x, parent.height.toFloat(), linePaint)
            }
        }
    }
}

private class DefaultItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount) > 0) {
            outRect.top = dp2px(15f).toInt()
        }

    }
}

private class AverageGridItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val parentWidth = parent.measuredWidth - parent.paddingStart - parent.paddingEnd
        // 每一条的宽度的获取，我这里是直接写死了一个值.
        // 当然你也可以动态计算或者测量出来，但是不能直接使用view.getWidth(),因为此时有可能还没有完成布局
        val itemWidth = view.resources.getDimension(R.dimen.course_practice_result_item_size)
        val spanWidth = parentWidth / spanCount
        if (spanCount == 1) return
        val spanMargin = (parentWidth - itemWidth * spanCount) / (spanCount - 1)
        val spanSizeLookup = layoutManager.spanSizeLookup
        val adapterPosition = parent.getChildAdapterPosition(view)
        val columnIndex = spanSizeLookup.getSpanIndex(adapterPosition, spanCount)
        // 核心代码:
        // 左边的间距 = 期望的left- 默认的left
        outRect.left =
            ((itemWidth + spanMargin) * columnIndex - spanWidth * columnIndex).toInt()
        // 不是第一行的情况，设置上边距
        if (spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount) > 0) {
            outRect.top = dp2px(15f).toInt()
        }

    }
}

private fun dp2px(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        Resources.getSystem().displayMetrics
    )
}