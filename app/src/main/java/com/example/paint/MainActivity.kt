package com.example.paint

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create PaintView (drawing area)
        val paintView = PaintView(this)

        // Inflate color button layout
        val colorLayout = layoutInflater.inflate(R.layout.fragment_paint_colors, null)

        // Combine PaintView and Color Buttons into a vertical layout
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(colorLayout)
            addView(paintView)
        }

        setContentView(mainLayout)

        // Set up button click listeners to change paint color
        colorLayout.findViewById<View>(R.id.btn_red).setOnClickListener { paintView.changeColor(Color.RED) }
        colorLayout.findViewById<View>(R.id.btn_blue).setOnClickListener { paintView.changeColor(Color.BLUE) }
        colorLayout.findViewById<View>(R.id.btn_green).setOnClickListener { paintView.changeColor(Color.GREEN) }
        colorLayout.findViewById<View>(R.id.btn_yellow).setOnClickListener { paintView.changeColor(Color.YELLOW) }
        colorLayout.findViewById<View>(R.id.btn_black).setOnClickListener { paintView.changeColor(Color.BLACK) }
    }

    class PaintView(context: Context) : View(context) {
        private val paint = Paint()
        private val paths = mutableListOf<Pair<CustomPath, Int>>()
        private var currentColor = Color.BLACK
        private var currentPath: CustomPath? = null

        init {
            paint.isAntiAlias = true
            paint.strokeWidth = 20f
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath = CustomPath().apply { moveTo(event.x, event.y) }
                }
                MotionEvent.ACTION_MOVE -> {
                    currentPath?.lineTo(event.x, event.y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    currentPath?.let { paths.add(Pair(it, currentColor)) }
                    currentPath = null
                }
            }
            return true
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.WHITE)
            for ((path, color) in paths) {
                paint.color = color
                canvas.drawPath(path, paint)
            }
            currentPath?.let {
                paint.color = currentColor
                canvas.drawPath(it, paint)
            }
        }

        fun changeColor(color: Int) {
            currentColor = color
        }
    }

    class CustomPath : android.graphics.Path()
}