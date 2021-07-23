package com.aaaaa.a2k.widget

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.*
import coil.load
import com.aaaaa.a2k.R
import com.bumptech.glide.Glide
import kotlin.math.abs


fun dp2px(ctx: Context, dp: Float): Int {
    val scale: Float = ctx.getResources().getDisplayMetrics().density
    return (dp * scale + 0.5f).toInt()
}

class FActivity :AppCompatActivity() {


    val parentFrame
        get()=window.decorView.findViewById<FrameLayout>(android.R.id.content)

    val width
        get() = ScreenUtils.getScreenWidth()

    val height
        get() = ScreenUtils.getScreenHeight()

    lateinit var float:View

    var hideAnimatior:AnimatorSet?=null


    var insetsBottom:Int?=null
    var insetsTop:Int?=null
    var insetsLeft:Int?=null
    var insetsRight:Int?=null


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

                val originalLeftMargin=old.leftMargin
                //float.layoutParams=old

                val targetTX=-float.measuredWidth *0.5f
                //float.translationX= -float.measuredWidth *0.5f

                val a1=ValueAnimator.ofInt(originalLeftMargin,0).apply {
                    duration=500
                    addUpdateListener {
                        val params = float.layoutParams as FrameLayout.LayoutParams
                        params.leftMargin=it.animatedValue as Int
                        float.layoutParams=params
                    }
                }

                val a2=ValueAnimator.ofFloat(float.translationX,targetTX).apply {
                    duration=500
                    addUpdateListener {
                        float.translationX=it.animatedValue as Float
                    }
                }
                hideAnimatior=AnimatorSet().apply {
                    play(a1)
                        .with(a2)
                    start()
                }

            }else{
                val old = float.layoutParams as FrameLayout.LayoutParams
                //old.leftMargin=width-float.measuredWidth
                val originalLeftMargin=old.leftMargin
                //float.layoutParams=old
                val targetTX=float.measuredWidth *0.5f

                //float.translationX= float.measuredWidth *0.5f


                val a1=ValueAnimator.ofInt(originalLeftMargin,width-float.measuredWidth).apply {
                    duration=500
                    addUpdateListener {
                        val params = float.layoutParams as FrameLayout.LayoutParams
                        params.leftMargin=it.animatedValue as Int
                        float.layoutParams=params
                    }
                }

                val a2=ValueAnimator.ofFloat(float.translationX,targetTX).apply {
                    duration=500
                    addUpdateListener {
                        addUpdateListener {
                            float.translationX=it.animatedValue as Float
                        }
                    }
                }
                hideAnimatior=AnimatorSet().apply {
                    play(a1)
                        .with(a2)
                    start()
                }
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
        //setContentView(R.layout.activity_f)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()
        val layoutParams=FrameLayout.LayoutParams(150,150)
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP  
        val new=ImageView(this).also { float=it }
        new.scaleType=ImageView.ScaleType.FIT_XY
        new.elevation= dp2px(this,2f).toFloat()
        new.apply {
         /*   Glide.with(context.applicationContext).asGif()
                .load(
                    "https://media1.giphy.com/media/BzyTuYCmvSORqs1ABM/200w.webp?cid=ecf05e47sa0l7xcseyni16vu7mjgxa19ge3dw2wjwpujaqqz&rid=200w.webp&ct=g")

                //.apply(sharedOptions)
                .into(this);
            //load("")*/
            setImageResource(R.mipmap.steam)
        }
        //new.setBackgroundColor(getColor(android.R.color.darker_gray))
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

            val topBorder=if(insetsTop!=null) insetsTop!! else 0

            val bottomBorder=if(insetsBottom!=null) height-insetsBottom!! else height


            if(newt>bottomBorder-new.measuredHeight)
                newt=bottomBorder-new.measuredHeight

            if(newt<topBorder)
                newt=topBorder


            val leftBorder=if(insetsLeft!=null) insetsLeft!! else 0

            val rightBorder=if(insetsRight!=null) width-insetsRight!! else width

            if(newl>rightBorder-float.measuredWidth)
                newl=rightBorder-float.measuredWidth

            if(newl<leftBorder)
                newl=leftBorder

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

            if(hideAnimatior!=null && hideAnimatior!!.isRunning)
                return@setOnTouchListener false


            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideSystemUI()
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
                    handler.postDelayed(hideF,1000)
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

        handler.postDelayed(hideF,1000)

        ViewCompat.setOnApplyWindowInsetsListener(float) { view, windowInsets ->
            val insets: Insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
           /* view.updateLayoutParams<MarginLayoutParams>(
                leftMargin = insets.left,
                bottomMargin = insets.bottom,
                rightMargin = insets.right,
            )*/

            if(insets.left>0)
            insetsLeft = insets.left
            if(insets.top>0)
            insetsTop = insets.top
            if(insets.right>0)
            insetsRight = insets.right
            if(insets.bottom>0)
            insetsBottom=insets.bottom

            view.updateLayoutParams<ViewGroup.MarginLayoutParams>{
                val old = new.layoutParams as FrameLayout.LayoutParams
                var newl=old.leftMargin.also { Log.e( "fuck ","original margin:${it}" ) }
                var newt=old.topMargin.also { Log.e( "fuck ","top margin:${it}" ) }

                val topBorder=if(insetsTop!=null) insetsTop!! else 0

                val bottomBorder=if(insetsBottom!=null) height-insetsBottom!! else height


                if(newt>bottomBorder-new.measuredHeight)
                    newt=bottomBorder-new.measuredHeight

                if(newt<topBorder)
                    newt=topBorder


                val leftBorder=if(insetsLeft!=null) insetsLeft!! else 0

                val rightBorder=if(insetsRight!=null) width-insetsRight!! else width

                if(newl>rightBorder-float.measuredWidth)
                    newl=rightBorder-float.measuredWidth

                if(newl<leftBorder)
                    newl=leftBorder

                old.leftMargin = newl
                old.topMargin = newt
                new.layoutParams = old
                Log.e( "insetsListener","left margin ${old.leftMargin} ,top margin ${old.topMargin}" )
            }




            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

    }

    val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    val MIN_TAP_TIME = 1000


    override fun onDestroy() {
        super.onDestroy()
        hideAnimatior?.cancel()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus)
            hideSystemUI()
    }

}