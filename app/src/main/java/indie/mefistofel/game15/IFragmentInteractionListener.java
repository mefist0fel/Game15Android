package indie.mefistofel.game15;

/**
 * Fragment interaction callback interface for activity
 */

interface IFragmentInteractionListener {
    enum Action {
        shuffle,
        win,
        info
    }

    void onFragmentInteraction(Action action);
}
