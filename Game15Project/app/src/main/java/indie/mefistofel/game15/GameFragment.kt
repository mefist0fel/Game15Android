package indie.mefistofel.game15

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import java.util.Timer
import java.util.TimerTask

/**
 * Game implementation fragment
 */
class GameFragment // Required empty public constructor
    : Fragment(), IFragmentKey {
    private var mListener: IFragmentInteractionListener? = null
    private var gameLayout: FrameLayout? = null
    private val buttons = arrayOfNulls<Button>(15)
    private var field: FieldData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        attachButtonViews(view)
        showButtons(false)
        gameLayout = view.findViewById<View>(R.id.game_frame) as FrameLayout
        gameLayout!!.post {
            setButtonsPositions(false)
            showButtons(true)
        }
        field = FieldData.getInstance()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is IFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement IFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun attachButtonViews(view: View) {
        buttons[0] = view.findViewById<View>(R.id.button1) as Button
        buttons[1] = view.findViewById<View>(R.id.button2) as Button
        buttons[2] = view.findViewById<View>(R.id.button3) as Button
        buttons[3] = view.findViewById<View>(R.id.button4) as Button
        buttons[4] = view.findViewById<View>(R.id.button5) as Button
        buttons[5] = view.findViewById<View>(R.id.button6) as Button
        buttons[6] = view.findViewById<View>(R.id.button7) as Button
        buttons[7] = view.findViewById<View>(R.id.button8) as Button
        buttons[8] = view.findViewById<View>(R.id.button9) as Button
        buttons[9] = view.findViewById<View>(R.id.button10) as Button
        buttons[10] = view.findViewById<View>(R.id.button11) as Button
        buttons[11] = view.findViewById<View>(R.id.button12) as Button
        buttons[12] = view.findViewById<View>(R.id.button13) as Button
        buttons[13] = view.findViewById<View>(R.id.button14) as Button
        buttons[14] = view.findViewById<View>(R.id.button15) as Button
        buttons[14] = view.findViewById<View>(R.id.button15) as Button
        // Set button delegates
        for (i in 0..14) {
            val buttonId = i + 1
            buttons[i]!!.setOnClickListener(View.OnClickListener {
                if (field!!.isFieldWin) {
                    return@OnClickListener
                }
                onButtonClick(buttonId)
                setButtonsPositions(true)
                if (FieldData.getInstance().checkWinConditions()) {
                    showWinEffect()
                }
            })
        }
    }

    private fun showWinEffect() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread { showWinButtonAnimation() }
            }
        }, 120)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (mListener != null) {
                    mListener!!.onFragmentInteraction(IFragmentInteractionListener.Action.win)
                }
            }
        }, 1600)
    }

    private fun showWinButtonAnimation() {
        val amplitude = 30
        for (i in 0..14) {
            var anim: TranslateAnimation
            if (gameLayout!!.width > gameLayout!!.height) {
                anim = TranslateAnimation(0f, amplitude.toFloat(), 0f, 0f)
                anim.startOffset = (i / 4 * 40).toLong()
            } else {
                anim = TranslateAnimation(0f, 0f, 0f, amplitude.toFloat())
                anim.startOffset = (i % 4 * 40).toLong()
            }
            anim.duration = 300
            anim.interpolator = Interpolator { input -> -Math.sin(2f * Math.PI * input).toFloat() }
            buttons[i]!!.startAnimation(anim)
        }
    }

    fun showButtons(visible: Boolean) {
        for (i in 0..14) {
            if (visible) {
                buttons[i]!!.visibility = View.VISIBLE
            } else {
                buttons[i]!!.visibility = View.INVISIBLE
            }
        }
    }

    private fun setButtonsPositions(withAnimation: Boolean) {
        val height = gameLayout!!.height
        val width = gameLayout!!.width
        var marginTop = 0
        var marginLeft = 0
        val size: Int
        if (width < height) {
            size = width / 4
            marginTop = (height - width) / 2
        } else {
            size = height / 4
            marginLeft = (width - height) / 2
        }
        for (i in 0 until field!!.fieldSize) {
            for (j in 0 until field!!.fieldSize) {
                val fieldValue = field!!.getField(i, j)
                val buttonId = fieldValue - 1
                if (buttonId >= 0 && buttonId < buttons.size) {
                    val x = j * size + marginLeft
                    val y = i * size + marginTop
                    setButtonPosition(buttons[buttonId], x, y, size, withAnimation)
                }
            }
        }
    }

    private fun setButtonPosition(button: Button?, x: Int, y: Int, size: Int, withAnimation: Boolean) {
        val params = button!!.layoutParams as FrameLayout.LayoutParams
        val xAnimation = params.leftMargin - x
        val yAnimation = params.topMargin - y
        params.width = size
        params.height = size + 5
        params.topMargin = y
        params.leftMargin = x
        button.layoutParams = params
        if (withAnimation) {
            val anim = TranslateAnimation(xAnimation.toFloat(), 0f, yAnimation.toFloat(), 0f)
            anim.duration = 60
            anim.fillAfter = true
            button.startAnimation(anim)
        }
    }

    private fun onButtonClick(fieldValue: Int) {
        for (i in 0 until field!!.fieldSize) {
            for (j in 0 until field!!.fieldSize) {
                if (field!!.getField(i, j) == fieldValue) {
                    field!!.findSwitchNeighbor(i, j)
                    return
                }
            }
        }
    }

    override fun OnKeyDown(keyCode: Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> move(1, 0)
            KeyEvent.KEYCODE_DPAD_RIGHT -> move(-1, 0)
            KeyEvent.KEYCODE_DPAD_UP -> move(0, -1)
            KeyEvent.KEYCODE_DPAD_DOWN -> move(0, 1)
        }
    }

    private fun move(x: Int, y: Int) {
        tryMove(x, y)
        setButtonsPositions(true)
        if (FieldData.getInstance().checkWinConditions()) {
            showWinEffect()
        }
    }

    private fun tryMove(x: Int, y: Int) {
        for (i in 0 until field!!.fieldSize) {
            for (j in 0 until field!!.fieldSize) {
                if (field!!.getField(i + x, j + y) == field!!.EMPTY_FIELD) {
                    field!!.findSwitchNeighbor(i, j)
                    return
                }
            }
        }
    }
}