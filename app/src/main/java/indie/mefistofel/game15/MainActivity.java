package indie.mefistofel.game15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout gameLayout = null;
    private Button[] buttons = new Button[15];

    private FieldData field = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachButtonViews();
        showButtons(false);
        gameLayout = (FrameLayout) findViewById(R.id.game_frame);
        gameLayout.post(new Runnable() {
            @Override
            public void run() {
                setButtonsPositions(false);
                showButtons(true);
            }
        });
        field = FieldData.getInstance();
    }

    void attachButtonViews() {
        buttons[0] = (Button) findViewById(R.id.button1);
        buttons[1] = (Button) findViewById(R.id.button2);
        buttons[2] = (Button) findViewById(R.id.button3);
        buttons[3] = (Button) findViewById(R.id.button4);
        buttons[4] = (Button) findViewById(R.id.button5);
        buttons[5] = (Button) findViewById(R.id.button6);
        buttons[6] = (Button) findViewById(R.id.button7);
        buttons[7] = (Button) findViewById(R.id.button8);
        buttons[8] = (Button) findViewById(R.id.button9);
        buttons[9] = (Button) findViewById(R.id.button10);
        buttons[10] = (Button) findViewById(R.id.button11);
        buttons[11] = (Button) findViewById(R.id.button12);
        buttons[12] = (Button) findViewById(R.id.button13);
        buttons[13] = (Button) findViewById(R.id.button14);
        buttons[14] = (Button) findViewById(R.id.button15);
        buttons[14] = (Button) findViewById(R.id.button15);
        // Set button delegates
        for (int i = 0; i < 15; i++) {
            final int buttonId = i + 1;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(buttonId);
                    setButtonsPositions(true);
                    FieldData.getInstance().checkWinConditions();
                }
            });
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

}