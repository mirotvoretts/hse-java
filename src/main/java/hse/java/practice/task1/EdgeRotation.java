package hse.java.practice.task1;

/**
 * Рассматриваем кубик Рубика как подгруппу симметрической группы S48,
 * которая действует на 48 стикерах (по 8 стикеров на каждой из 6 граней, центры неподвижны).
 * Поворот грани = это перестановка, которая циклически меняет 4 стикера на грани и 12 стикеров на соседних гранях.
 * НА ВИКИ ПОДРОБНЕЕ.
 */
public class EdgeRotation {
    private static final int STICKERS_ON_EDGE = 8;

    public static final int[][] LEFT_CLOCKWISE = {
            {16, 18, 23, 21},
            {17, 20, 22, 19},
            {0, 32, 8, 47},
            {3, 35, 11, 44},
            {5, 37, 13, 42}
    };
    public static final int[][] LEFT_COUNTERCLOCKWISE = reverseDirection(LEFT_CLOCKWISE);

    public static final int[][] FRONT_CLOCKWISE = {
            {32, 34, 39, 37},
            {33, 36, 38, 35},
            {5, 24, 10, 23},
            {6, 27, 9, 20},
            {7, 29, 8, 18}
    };
    public static final int[][] FRONT_COUNTERCLOCKWISE = reverseDirection(FRONT_CLOCKWISE);

    public static final int[][] RIGHT_CLOCKWISE = {
            {24, 26, 31, 29},
            {25, 28, 30, 27},
            {2, 45, 10, 34},
            {4, 43, 12, 36},
            {7, 40, 15, 39}
    };
    public static final int[][] RIGHT_COUNTERCLOCKWISE = reverseDirection(RIGHT_CLOCKWISE);

    public static final int[][] BACK_CLOCKWISE = {
            {40, 42, 47, 45},
            {41, 44, 46, 43},
            {2, 16, 15, 31},
            {1, 19, 14, 28},
            {0, 21, 13, 26}
    };
    public static final int[][] BACK_COUNTERCLOCKWISE = reverseDirection(BACK_CLOCKWISE);

    public static final int[][] UP_CLOCKWISE = {
            {0, 2, 7, 5},
            {1, 4, 6, 3},
            {40, 24, 32, 16},
            {41, 25, 33, 17},
            {42, 26, 34, 18}
    };
    public static final int[][] UP_COUNTERCLOCKWISE = reverseDirection(UP_CLOCKWISE);

    public static final int[][] DOWN_CLOCKWISE = {
            {8, 10, 15, 13},
            {9, 12, 14, 11},
            {37, 29, 45, 21},
            {38, 30, 46, 22},
            {39, 31, 47, 23}
    };
    public static final int[][] DOWN_COUNTERCLOCKWISE = reverseDirection(DOWN_CLOCKWISE);

    private EdgeRotation() {
    }

    private static int[][] reverseDirection(int[][] rotation) {
        var result = new int[rotation.length][];

        for (int i = 0; i < rotation.length; i++) {
            var row = rotation[i];
            var reversedRow = new int[row.length];
            reversedRow[0] = row[0];
            for (int j = 1; j < row.length; j++) {
                reversedRow[j] = row[row.length - j];
            }
            result[i] = reversedRow;
        }

        return result;
    }

    /**
     * По индексу стикера возвращает индекс грани, на которой он находится.
     *
     * @param stickerIndex индекс стикера от 0 до 47 включительно
     * @return индекс грани от 0 до 5 включительно
     */
    public static int getEdgeIndexByStickerIndex(int stickerIndex) {
        return stickerIndex / STICKERS_ON_EDGE;
    }

    /**
     * По индексу стикера возвращает индекс части грани, на которой он находится.
     *
     * @param stickerIndex индекс стикера от 0 до 47 включительно
     * @return пара индексов, где первый индекс - это строка, а второй - это столбец
     */
    public static PartsIndex stickerIndexToPartsIndex(int stickerIndex) {
        int stickerIndexOnEdge = stickerIndex % STICKERS_ON_EDGE;
        int row;
        int column;
        if (stickerIndexOnEdge < 3) {
            row = 0;
            column = stickerIndexOnEdge;
        } else if (stickerIndexOnEdge < 5) {
            row = 1;
            column = (stickerIndexOnEdge == 3) ? 0 : 2;
        } else {
            row = 2;
            column = stickerIndexOnEdge - 5;
        }
        return new PartsIndex(row, column);
    }
}
