package indie.mefistofel.game15;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game implementation fragment
 */
public class GameFragment extends Fragment implements IFragmentKey {
    private IFragmentInteractionListener mListener;

    private FrameLayout gameLayout = null;
    private Button[] buttons = new Button[15];

    private FieldData field = null;

    public GameFragment() {} // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        attachButtonViews(view);
        showButtons(false);
        gameLayout = (FrameLayout) view.findViewById(R.id.game_frame);
        gameLayout.post(new Runnable() {
            @Override
            public void run() {
                setButtonsPositions(false);
                showButtons(true);
            }
        });
        field = FieldData.getInstance();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentInteractionListener) {
            mListener = (IFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {

        }
    }

    void attachButtonViews(View view) {
        buttons[0] = (Button) view.findViewById(R.id.button1);
        buttons[1] = (Button) view.findViewById(R.id.button2);
        buttons[2] = (Button) view.findViewById(R.id.button3);
        buttons[3] = (Button) view.findViewById(R.id.button4);
        buttons[4] = (Button) view.findViewById(R.id.button5);
        buttons[5] = (Button) view.findViewById(R.id.button6);
        buttons[6] = (Button) view.findViewById(R.id.button7);
        buttons[7] = (Button) view.findViewById(R.id.button8);
        buttons[8] = (Button) view.findViewById(R.id.button9);
        buttons[9] = (Button) view.findViewById(R.id.button10);
        buttons[10] = (Button) view.findViewById(R.id.button11);
        buttons[11] = (Button) view.findViewById(R.id.button12);
        buttons[12] = (Button) view.findViewById(R.id.button13);
        buttons[13] = (Button) view.findViewById(R.id.button14);
        buttons[14] = (Button) view.findViewById(R.id.button15);
        buttons[14] = (Button) view.findViewById(R.id.button15);
        // Set button delegates
        for (int i = 0; i < 15; i++) {
            final int buttonId = i + 1;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (field.isFieldWin()) {
                        return;
                    }
                    onButtonClick(buttonId);
                    setButtonsPositions(true);
                    if (FieldData.getInstance().checkWinConditions()) {
                        showWinEffect();
                    }
                }
            });
        }
    }

    private void showWinEffect() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWinButtonAnimation();
                    }
                });
            }
        }, 120);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onFragmentInteraction(IFragmentInteractionListener.Action.win);
                }
            }
        }, 1600);
    }

    private void showWinButtonAnimation() {
        final int amplitude = 30;
        for (int i = 0; i < 15; i++) {
            TranslateAnimation anim;
            if (gameLayout.getWidth() > gameLayout.getHeight()) {
                anim = new TranslateAnimation(0, amplitude, 0, 0);
                anim.setStartOffset(i / 4 * 40);
            } else {
                anim = new TranslateAnimation(0, 0, 0, amplitude);
                anim.setStartOffset(i % 4 * 40);
            }
            anim.setDuration(300);
            anim.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    return -(float)Math.sin(2f * Math.PI * input);
                }
            });
            buttons[i].startAnimation(anim);
        }
    }

    void showButtons(boolean visible) {
        for (int i = 0; i < 15; i++) {
            if (visible) {
                buttons[i].setVisibility(View.VISIBLE);
            } else {
                buttons[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    void setButtonsPositions(boolean withAnimation) {
        int height = gameLayout.getHeight();
        int width = gameLayout.getWidth();
        int marginTop = 0;
        int marginLeft = 0;
        int size;
        if (width < height) {
            size = width / 4;
            marginTop = (height - width) / 2;
        } else {
            size = height / 4;
            marginLeft = (width - height) / 2;
        }
        for (int i = 0; i < field.fieldSize; i++) {
            for (int j = 0; j < field.fieldSize; j++) {
                int fieldValue = field.getField(i, j);
                int buttonId = fieldValue - 1;
                if (buttonId >= 0 && buttonId < buttons.length) {
                    int x = j * size + marginLeft;
                    int y = i * size + marginTop;
                    setButtonPosition(buttons[buttonId], x, y, size, withAnimation);
                }
            }
        }
    }

    void setButtonPosition(Button button, int x, int y, int size, boolean withAnimation) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)button.getLayoutParams();
        int xAnimation = params.leftMargin - x;
        int yAnimation = params.topMargin - y;
        params.width = size;
        params.height = size + 5;
        params.topMargin = y;
        params.leftMargin = x;
        button.setLayoutParams(params);
        if (withAnimation) {
            TranslateAnimation anim = new TranslateAnimation(xAnimation, 0, yAnimation, 0);
            anim.setDuration(60);
            anim.setFillAfter(true);
            button.startAnimation(anim);
        }
    }

    void onButtonClick(int fieldValue) {
        for (int i = 0; i < field.fieldSize; i++) {
            for (int j = 0; j < field.fieldSize; j++) {
                if (field.getField(i, j) == fieldValue) {
                    field.findSwitchNeighbor(i, j);
                    return;
                }
            }
        }
    }

    @Override
    public void OnKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Move(1, 0);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Move(-1, 0);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Move(0, -1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Move(0, 1);
                break;
        }
    }

    private void Move(int x, int y) {
        TryMove(x, y);
        setButtonsPositions(true);
        if (FieldData.getInstance().checkWinConditions()) {
            showWinEffect();
        }
    }

    private void TryMove(int x, int y) {
        for (int i = 0; i < field.fieldSize; i++) {
            for (int j = 0; j < field.fieldSize; j++) {
                if (field.getField(i + x, j + y) == field.EMPTY_FIELD) {
                    field.findSwitchNeighbor(i, j);
                    return;
                }
            }
        }
    }
}
