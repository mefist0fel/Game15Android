package indie.mefistofel.game15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private FrameLayout gameLayout = null;
    private Button[] buttons = new Button[15];
    final int fieldSize = 4;
    final int EMPTY_FIELD = 0;
    final int NON_USABLE_FIELD = -1;

    private int[][] field = null;
    private int[][] reference = null;

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
        if (field == null) {
            Log.d("tag", "recreate");
            reference = setDefaultFieldValues();
            field = setDefaultFieldValues();
            shuffleFields();
        }
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
        // Set button delegates
        for (int i = 0; i < 15; i++) {
            final int buttonNumber = i + 1;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(buttonNumber);
                    setButtonsPositions(true);
                    checkWinConditions();
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

    int[][] setDefaultFieldValues()
    {
        int[][] fieldValue = new int[fieldSize + 2][];
        for(int i = 0; i < fieldSize + 2; i++) {
            fieldValue[i] = new int[fieldSize + 2];
            for(int j = 0; j < fieldSize + 2; j++) {
                fieldValue[i][j] = NON_USABLE_FIELD;
            }
        }
        int counter = 1;
        for(int i = 0; i < fieldSize; i++) {
            for(int j = 0; j < fieldSize; j++) {
                fieldValue[i + 1][j + 1] = counter;
                counter += 1;
            }
        }
        fieldValue[fieldSize][fieldSize] = EMPTY_FIELD;
        return fieldValue;
    }

    void shuffleFields()
    {
        Random random = new Random();
        for(int i = 0; i < 10000; i++)
        {
            findSwitchNeighbor(random.nextInt(4) + 1, random.nextInt(4) + 1);
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
        for (int i = 1; i < field.length - 1; i++) {
            for (int j = 1; j < field[i].length - 1; j++) {
                if (field[i][j] > 0) {
                    int x = (j - 1) * size + marginLeft;
                    int y = (i - 1) * size + marginTop;
                    setButtonPosition(buttons[field[i][j] - 1], x, y, size, withAnimation);
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

    void onButtonClick(int number) {
        for (int i = 1; i < field.length - 1; i++) {
            for (int j = 1; j < field[i].length - 1; j++) {
                if (field[i][j] == number) {
                    findSwitchNeighbor(i, j);
                    return;
                }
            }
        }
    }

    void findSwitchNeighbor(int x, int y) {
        trySwitchButton(x, y, x + 1, y);
        trySwitchButton(x, y, x, y + 1);
        trySwitchButton(x, y, x - 1, y);
        trySwitchButton(x, y, x, y - 1);
    }

    void trySwitchButton(int xFrom, int yFrom, int xTo, int yTo) {
        if (field[xTo][yTo] == 0) {
            field[xTo][yTo] = field[xFrom][yFrom];
            field[xFrom][yFrom] = 0;
        }
    }

    void checkWinConditions() {
        if (isFieldEqualToReference()) {
            Log.i("aa", "WIN");
        }
    }

    boolean isFieldEqualToReference() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (field[i + 1][j + 1] != reference[i + 1][j + 1]){
                    return false;
                }
            }
        }
        return true;
    }

}