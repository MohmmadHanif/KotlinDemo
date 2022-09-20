package com.example.logindemoinkotlin.dataclass

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemoinkotlin.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class swipeGester(context: Context) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val p = Paint()

    val deleteColor =ContextCompat.getColor(context,R.color.delete)
    val updateColor =ContextCompat.getColor(context,R.color.update)
    val deleteIcon = R.drawable.ic_baseline_delete_24
    val updateIcon = R.drawable.ic_baseline_update_24
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {


            /*val colorAlert =
                ContextCompat.getColor(this@ShowUserListActivity, R.color.black)
            val defaultWhiteColor =
                ContextCompat.getColor(this@ShowUserListActivity, R.color.white)
            val teal200 =
                ContextCompat.getColor(this@ShowUserListActivity, R.color.black)

            val desiredValue = 18f
            ItemDecorator.Builder(c, recyclerView, viewHolder, dX, actionState).set(
                iconHorizontalMargin = desiredValue,
                backgroundColorFromStartToEnd = colorAlert,
                backgroundColorFromEndToStart = teal200,
                textFromStartToEnd = getString(R.string.update),
                textFromEndToStart = getString(R.string.update),
                textColorFromStartToEnd = defaultWhiteColor,
                textColorFromEndToStart = defaultWhiteColor,
                iconTintColorFromStartToEnd = defaultWhiteColor,
                iconTintColorFromEndToStart = defaultWhiteColor,
                textSizeFromStartToEnd = 16f,
                textSizeFromEndToStart = 16f,
                iconResIdFromStartToEnd = R.drawable.ic_baseline_delete_24,
                iconResIdFromEndToStart = R.drawable.ic_baseline_delete_24
            )*/

            RecyclerViewSwipeDecorator.Builder(c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive)
                .addSwipeLeftBackgroundColor(deleteColor)
                .addSwipeLeftActionIcon(deleteIcon)
                .addSwipeRightBackgroundColor(updateColor)
                .addSwipeRightActionIcon(updateIcon)
                .create()
                .decorate()


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}
