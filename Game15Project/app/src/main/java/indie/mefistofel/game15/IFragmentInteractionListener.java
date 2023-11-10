package indie.mefistofel.game15;

/**
 * Fragment interaction callback interface for activity
 */

public interface IFragmentInteractionListener {
    enum Action {
        shuffle,
        win,
        info
    }

    void onFragmentInteraction(Action action);
}
