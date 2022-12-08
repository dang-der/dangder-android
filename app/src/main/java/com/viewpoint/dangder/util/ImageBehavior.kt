package com.viewpoint.dangder.util


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.viewpoint.dangder.R


class ImageBehavior(private val context: Context, private val attrs: AttributeSet) :
    CoordinatorLayout.Behavior<ImageView>(context, attrs) {

    private var mCustomFinalHeight = 0f
    private var mStartXPosition = 0
    private var mStartToolbarPosition = 0f
    private var mStartYPosition = 0
    private var mFinalYPosition = 0
    private var mStartHeight = 0
    private var mFinalXPosition = 0
    private var mChangeBehaviorPoint = 0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        return dependency is androidx.appcompat.widget.Toolbar
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {

        maybeInitProperties(child, dependency)

        val maxScrollDistance = mStartToolbarPosition.toInt()
        val expandedPercentageFactor: Float =dependency.y / maxScrollDistance

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            val heightFactor: Float =
                (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint
            val distanceXToSubtract: Float = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + child.height / 2
            val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + child.height / 2
            child.x = mStartXPosition - distanceXToSubtract
            child.y = mStartYPosition - distanceYToSubtract
            val heightToSubtract: Float = (mStartHeight - mCustomFinalHeight) * heightFactor
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = (mStartHeight - heightToSubtract).toInt()
            lp.height = (mStartHeight - heightToSubtract).toInt()
            child.layoutParams = lp
        } else {
            val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + mStartHeight / 2
            child.x = mStartXPosition - (child.width / 2).toFloat()
            child.y = mStartYPosition - distanceYToSubtract
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = mStartHeight
            lp.height = mStartHeight
            child.layoutParams = lp
        }
        return true
    }

    private fun maybeInitProperties(child: ImageView, dependency: View) {
        if (mStartYPosition == 0) mStartYPosition = dependency.y.toInt()
        if (mFinalYPosition == 0) mFinalYPosition = dependency.height / 2
        if (mStartHeight == 0) mStartHeight = child.getHeight()
        if (mStartXPosition == 0) mStartXPosition = (child.getX() + child.getWidth() / 2).toInt()
        if (mFinalXPosition == 0) mFinalXPosition = context.getResources()
            .getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + mCustomFinalHeight.toInt() / 2
        if (mStartToolbarPosition == 0f) mStartToolbarPosition = dependency.y
        if (mChangeBehaviorPoint == 0f) {
            mChangeBehaviorPoint =
                (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition))
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId: Int =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

}