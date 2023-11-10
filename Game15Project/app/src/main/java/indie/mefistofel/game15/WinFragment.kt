package indie.mefistofel.game15

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

/**
 * Win game fragment controller.
 */
class WinFragment // Required empty public constructor
    : Fragment(), View.OnClickListener {
    private var mListener: IFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_win, container, false)
        val button = view.findViewById<View>(R.id.shuffle_button) as Button
        button.setOnClickListener(this)
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

    override fun onClick(v: View) {
        mListener!!.onFragmentInteraction(IFragmentInteractionListener.Action.shuffle)
    }
}