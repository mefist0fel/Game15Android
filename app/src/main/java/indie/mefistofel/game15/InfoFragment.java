package indie.mefistofel.game15;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import indie.mefistofel.game15.IFragmentInteractionListener;
import indie.mefistofel.game15.R;

/**
 * Info (about) fragment.
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
}
