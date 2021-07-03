package com.aaaaa.a2k.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.aaaaa.a2k.R
import kotlin.math.abs


fun dp2px(ctx: Context, dp: Float): Int {
    val scale: Float = ctx.getResources().getDisplayMetrics().density
    return (dp * scale + 0.5f).toInt()
}

class FActivity :AppCompatActivity() {


    val  parentFrame
            get()=window.decorView.findViewById<FrameLayout>(android.R.id.content)

    val width
    get() = ScreenUtils.getAppScreenWidth()

    val height
    get() = ScreenUtils.getAppScreenHeight()

    lateinit var float:View


    val hideF=object :Runnable{


        fun floatLeft(): Boolean {
            val screenCenter=width/2
            val viewCenterX=float.measuredWidth/2+float.marginLeft
            return viewCenterX<screenCenter

        }
        override fun run() {
            Log.e("fuck","hide float ball called" )
            float.alpha=0.5f

            if(floatLeft()){
                val old = float.layoutParams as FrameLayout.LayoutParams
                old.leftMargin=0
                float.layoutParams=old
                float.translationX= -float.measuredWidth *0.5f
            }else{
                val old = float.layoutParams as FrameLayout.LayoutParams
                old.leftMargin=width-float.measuredWidth
                float.layoutParams=old
                float.translationX= float.measuredWidth *0.5f
            }
        }

    }

    fun floatNormal(){
        float.alpha=1f
        float.translationX=0f
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f)
        val layoutParams=FrameLayout.LayoutParams(100,100)
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP  
        val new=FrameLayout(this).also { float=it }
        new.setBackgroundColor(getColor(android.R.color.darker_gray))
        new.layoutParams=layoutParams

        var downX=0
        var downY=0
        var mLastX=0
        var mLastY=0

        data class ViewState(var leftMargin:Int,var topMargin:Int)

        var viewState:ViewState?=null

        val MIN_DISTANCE_MOVE = 4

        val distance = dp2px(new.context,1f) *MIN_DISTANCE_MOVE


        fun onDis(dx: Int, dy: Int, viewState: ViewState?) {
            val old = new.layoutParams as FrameLayout.LayoutParams
            var newl=dx+viewState!!.leftMargin
            var newt=dy+viewState.topMargin

            if(newt>height-new.measuredHeight)
                newt=height-new.measuredHeight
            if(newt<0)
                newt=0


            if(newl>width-new.measuredWidth)
                newl=width-new.measuredWidth

            if(newl<0)
                newl=0

            old.leftMargin = newl
            old.topMargin = newt
            new.layoutParams = old
        }
        new.setOnTouchListener { v, event ->
          /*  TouchProxy(object : TouchProxy.OnTouchEventListener {
                override fun onMove(x: Int, y: Int, dx: Int, dy: Int) {
                    val old = v.layoutParams as FrameLayout.LayoutParams
                    Log.e( "onMove: ","dx dy:{${dx},${dy}}" )
                    old.leftMargin += dx
                    old.topMargin += dy

                    Log.e( "onMove: ","leftMargin rightMargin:{${old.leftMargin },${old.topMargin}}" )
                    v.layoutParams = old
                }

                override fun onUp(x: Int, y: Int) {

                }

                override fun onDown(x: Int, y: Int) {

                }
            }).onTouchEvent(v,event)*/
            val x =event.rawX.toInt()
            val y =event.rawY.toInt()


            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.removeCallbacks(hideF)
                    floatNormal()
                    viewState=ViewState(v.marginLeft,v.marginTop)
                    downX = x
                    downY = y
                    mLastY = y
                    mLastX = x
                }
                MotionEvent.ACTION_MOVE -> {
                    mLastY = y
                    mLastX = x

                    val dx=mLastX-downX
                    val dy=mLastY-downY
                    onDis(dx,dy,viewState)
                }
                MotionEvent.ACTION_UP -> {
                    handler.postDelayed(hideF,2000)
                    if( abs(x-downX) <distance &&    abs(y-downY) <distance
                            && event.eventTime - event.downTime < MIN_TAP_TIME){
                        v.performClick()
                    }
                }
                else -> {
                }
            }

            return@setOnTouchListener true
        }
        new.setOnClickListener {
            Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
        }

        parentFrame.addView(new)

        handler.postDelayed(hideF,1500)

    }

    val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    val MIN_TAP_TIME = 1000


}