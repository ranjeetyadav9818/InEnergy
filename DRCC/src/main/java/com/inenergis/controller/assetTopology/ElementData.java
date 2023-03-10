package com.inenergis.controller.assetTopology;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;

import java.util.StringJoiner;

/**
 * Created by egamas on 24/11/2017.
 */
@Data
@Builder
public class ElementData {
    private int position;

    private String label;

    @Setter(AccessLevel.NONE)
    private Integer sourceX;

    @Setter(AccessLevel.NONE)
    private Integer sourceY;

    private static final int ELEMENT_WIDTH_EM = 15;
    private static final int ELEMENT_HEIGHT_EM = 4;
    private int elementLeftMarginEm = 4;
    private int elementTopMarginEm = 4;
    private int elementMarginEmX = 15;
    private int elementMarginEmY = 4;

    public String getX() {return (elementLeftMarginEm + sourceX * (ELEMENT_WIDTH_EM + elementMarginEmX)) + "em"; }

    public String getY() {
        return (elementTopMarginEm + sourceY * (ELEMENT_HEIGHT_EM + elementMarginEmY)) + "em";
    }

    /**
     * Get coordinates to place the elements according to their positions. Do not use with the root element.
     *
     * @return
     */
    public void placeElementByPosition(int numElements) {
        Double SQUARE_SIDE_LENGTH = Math.sqrt((double) numElements);
        sourceX = position == 0 ? 0 : position % SQUARE_SIDE_LENGTH.intValue();
        sourceY = position == 0 ? 0 : position / SQUARE_SIDE_LENGTH.intValue();
        if (numElements >= 7) {
            elementLeftMarginEm = 4;
            elementTopMarginEm = 2;
            elementMarginEmX = 25;
            elementMarginEmY = 3;
        } else {
            elementLeftMarginEm = 4;
            elementTopMarginEm = 4;
            elementMarginEmX = 25;
            elementMarginEmY = 4;
        }
    }

    public ImmutablePair<Integer, Integer> getTargetEndpoint(ElementData target) {
        return new ImmutablePair<>(sourceX.compareTo(target.getSourceX()), sourceY.compareTo(target.getSourceY()));
    }

    public EndPointAnchor translateEndpoint(ElementData target) {
        final ImmutablePair<Integer, Integer> endPointIndex = getTargetEndpoint(target);
        if (endPointIndex.getLeft() == 0 && endPointIndex.getRight() == 0) {
            return EndPointAnchor.TOP;
        }
        StringJoiner arrow = new StringJoiner("_");
        final String yArrow = translateY(endPointIndex.getRight());
        if (StringUtils.isNotEmpty(yArrow)) {
            arrow.add(yArrow);
        }
        final String xArrow = translateX(endPointIndex.getLeft());
        if (StringUtils.isNotEmpty(xArrow)) {
            arrow.add(xArrow);
        }
        return EndPointAnchor.valueOf(arrow.toString());
    }

    private static String translateY(Integer y) {
        if (y == 0) {
            return StringUtils.EMPTY;
        }
        return y < 0 ? "BOTTOM" : "TOP";
    }

    private static String translateX(Integer x) {
        if (x == 0) {
            return StringUtils.EMPTY;
        }
        return x < 0 ? "RIGHT" : "LEFT";
    }

    @Override
    public String toString() {
        return label;
    }
}
