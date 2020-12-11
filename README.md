# GridLayoutManager 如何均分
默认效果是这样
![image.png](https://upload-images.jianshu.io/upload_images/3290652-75fdeb830aad8b1a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



期望的效果是这样
![image.png](https://upload-images.jianshu.io/upload_images/3290652-6ef3db3130a8d1ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**期望是每一行类似ConstraintLayout中的chain spread inside**
![image.png](https://upload-images.jianshu.io/upload_images/3290652-57cac4485a0f2bc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**虚线是GridLayoutManger 默认的每个item的left**

怎么实现呢，了解一下下面的计算细则？
-  虚线的位置是spanIndex*spanWidth
- item.left=虚线的位置+ItemDecoration.left
所以通过设置itemDecoration.left 就可以完成

下面直接给出计算的代码
```kotlin
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
        val itemWidth = view.resources.getDimension(R.dimen.course_practice_result_item_size)
        val spanWidth = parentWidth / spanCount
        if (spanCount == 1) return
        val spanMargin = (parentWidth - itemWidth * spanCount) / (spanCount - 1)
        val spanSizeLookup = layoutManager.spanSizeLookup

        for (child in parent) {
            val adapterPosition = parent.getChildAdapterPosition(child)
            val columnIndex = spanSizeLookup.getSpanIndex(adapterPosition, spanCount)
            // 核心代码:
            // left = 期望的left- 默认的left
            outRect.left =  ((itemWidth + spanMargin) * columnIndex - spanWidth * columnIndex).toInt()
            if (spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount) > 0) {
                outRect.top = dp2px(15f).toInt()
            }
        }
    }
}
```


Demo 地址: https://github.com/pokercc/GridLayoutMangerDemo