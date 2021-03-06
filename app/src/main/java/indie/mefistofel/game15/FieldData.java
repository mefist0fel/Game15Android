package indie.mefistofel.game15;

import java.util.Random;

/**
 * Created by Mefistofel on 06.12.16.
 * base game field class
 */

class FieldData {
    final int fieldSize = 4;
    final int EMPTY_FIELD = 0;
    private final int NON_USABLE_FIELD = -1;

    private static FieldData instance = null;

    private int[][] field = null;
    private int[][] reference = null;

    private boolean equalWinReference = false;

    static FieldData getInstance () {
        if (instance == null) {
            instance = new FieldData();
        }
        return instance;
    }

    int getField(int x, int y) {
        if (x >= 0 && x < fieldSize && y >= 0 && y < fieldSize) {
            return field[x + 1][y + 1];
        }
        return NON_USABLE_FIELD;
    }

    void shuffleFields() {
        field = setDefaultFieldValues();
        Random random = new Random();
        for(int i = 0; i < 10000; i++)
        {
            findSwitchNeighbor(random.nextInt(4), random.nextInt(4));
        }
        equalWinReference = false;
    }

    void findSwitchNeighbor(int x, int y) {
        x += 1;
        y += 1;
        trySwitchButton(x, y, x + 1, y);
        trySwitchButton(x, y, x, y + 1);
        trySwitchButton(x, y, x - 1, y);
        trySwitchButton(x, y, x, y - 1);
    }

    boolean checkWinConditions() {
        equalWinReference = isFieldEqualToReference();
        return equalWinReference;
    }

    boolean isFieldWin() {
        return equalWinReference;
    }

    private FieldData () {
        reference = setDefaultFieldValues();
        field = setDefaultFieldValues();
        shuffleFields();
    }

    private int[][] setDefaultFieldValues()
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

    private void trySwitchButton(int xFrom, int yFrom, int xTo, int yTo) {
        if (field[xTo][yTo] == EMPTY_FIELD) {
            field[xTo][yTo] = field[xFrom][yFrom];
            field[xFrom][yFrom] = EMPTY_FIELD;
        }
    }

    private boolean isFieldEqualToReference() {
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
