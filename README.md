# How the GridLayoutManager average each item.
Default effect
![image.png](https://upload-images.jianshu.io/upload_images/3290652-75fdeb830aad8b1a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



Expected effect
![image.png](https://upload-images.jianshu.io/upload_images/3290652-6ef3db3130a8d1ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**Expect every row like ConstraintLayout chain spread inside**
![image.png](https://upload-images.jianshu.io/upload_images/3290652-57cac4485a0f2bc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**Dash line is GridLayoutManger default each item's left**

How to implement？You need know blew point.
-  Dash line  is spanIndex*spanWidth
- item.left= Dash line location+ItemDecoration.left

So through set itemDecoration.left ,we could implement the average effect.

The Code
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
            // Core line:
            // left = expect left- default left
            outRect.left =  ((itemWidth + spanMargin) * columnIndex - spanWidth * columnIndex).toInt()
            if (spanSizeLookup.getSpanGroupIndex(adapterPosition, spanCount) > 0) {
                outRect.top = dp2px(15f).toInt()
            }
        }
    }
}
```


Demo 地址: https://github.com/pokercc/GridLayoutMangerDemo