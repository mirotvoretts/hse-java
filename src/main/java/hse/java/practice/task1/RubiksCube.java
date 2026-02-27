package hse.java.practice.task1;

import java.util.Arrays;

import static hse.java.practice.task1.EdgeRotation.*;

/**
 * Необходимо реализовать интерфейс Cube
 * При повороте передней грани, меняются верх низ право и лево
 */
public class RubiksCube implements Cube {

    private static final int EDGES_COUNT = 6;

    private final Edge[] edges = new Edge[EDGES_COUNT];

    /**
     * Создать валидный собранный кубик
     * грани разместить по ордеру в енуме цветов
     * грань 0 -> цвет 0
     * грань 1 -> цвет 1
     * ...
     */
    public RubiksCube() {
        CubeColor[] colors = CubeColor.values();
        for (int i = 0; i < 6; i++) {
            edges[i] = new Edge(colors[i]);
        }
    }

    private void applyCycle(int[] cycle) {
        CubeColor temp = getSticker(cycle[cycle.length - 1]);
        for (int i = cycle.length - 1; i > 0; i--) {
            setSticker(cycle[i], getSticker(cycle[i - 1]));
        }
        setSticker(cycle[0], temp);
    }

    private CubeColor getSticker(int stickerIndex) {
        var index = stickerIndexToPartsIndex(stickerIndex);
        return edges[getEdgeIndexByStickerIndex(stickerIndex)].getParts()[index.row()][index.column()];
    }

    private void setSticker(int stickerIndex, CubeColor color) {
        var index = stickerIndexToPartsIndex(stickerIndex);
        edges[getEdgeIndexByStickerIndex(stickerIndex)].getParts()[index.row()][index.column()] = color;
    }

    private void rotate(RotateDirection direction, int[][] clockwiseRotation, int[][] counterClockwiseRotation) {
        int[][] rotation = (direction == RotateDirection.CLOCKWISE) ? clockwiseRotation : counterClockwiseRotation;
        for (int[] cycle : rotation) {
            applyCycle(cycle);
        }
    }

    @Override
    public void up(RotateDirection direction) {
        rotate(direction, EdgeRotation.UP_CLOCKWISE, EdgeRotation.UP_COUNTERCLOCKWISE);
    }

    @Override
    public void down(RotateDirection direction) {
        rotate(direction, EdgeRotation.DOWN_CLOCKWISE, EdgeRotation.DOWN_COUNTERCLOCKWISE);
    }

    @Override
    public void left(RotateDirection direction) {
        rotate(direction, EdgeRotation.LEFT_CLOCKWISE, EdgeRotation.LEFT_COUNTERCLOCKWISE);
    }

    @Override
    public void right(RotateDirection direction) {
        rotate(direction, EdgeRotation.RIGHT_CLOCKWISE, EdgeRotation.RIGHT_COUNTERCLOCKWISE);
    }

    @Override
    public void front(RotateDirection direction) {
        rotate(direction, EdgeRotation.FRONT_CLOCKWISE, EdgeRotation.FRONT_COUNTERCLOCKWISE);
    }

    @Override
    public void back(RotateDirection direction) {
        rotate(direction, EdgeRotation.BACK_CLOCKWISE, EdgeRotation.BACK_COUNTERCLOCKWISE);
    }

    public Edge[] getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return Arrays.toString(edges);
    }
}
