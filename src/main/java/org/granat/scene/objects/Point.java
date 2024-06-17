package org.granat.scene.objects;

import lombok.Getter;
import lombok.Setter;
import org.granat.scene.enums.ColorGradation;

@Getter
@Setter
public class Point {

    //?-----------------------------------------------------------------------------------------------------------CONSTS

    @Getter
    private final static byte densityPosition = 0;
    @Getter
    private final static byte superstructuresPosition = 1;
    @Getter
    private final static byte girdersPosition = 2;
    @Getter
    private final static byte girdersHeightPosition = 3;

    //?-------------------------------------------------------------------------------------------------------------DATA

    private double[] coordinates;

    private double intensity;

    private double[] color;

    /**
     * [0] - значение плотности для данной точки
     */
    private long[] parameters;

    /**
     * [0] - фильтр плотности для данной точки
     */
    private boolean[] filters;

    //?----------------------------------------------------------------------------------------------------------GETTERS

    public double[] getCoordinates() throws NullPointerException {
        if (this.coordinates != null)
            return this.coordinates;
        System.out.println("Warning: point was not correctly initialized. Class: " + this.getClass());
        return new double[] {0.0, 0.0, 0.0};
    }

    public double[] getColor() {
        if (this.color != null) return this.color;

        this.color = new double[3];
        ColorGradation.INTENSITY.setColor(this, new double[0]);
        return this.color;
    }

    public boolean getDensityFilterValue() {
        if (this.filters != null && this.filters.length > densityPosition)
            return filters[densityPosition];
        return false;
    }

    public long getDensityParameterValue() {
        if (this.parameters != null && this.parameters.length > densityPosition)
            return parameters[densityPosition];
        return 1;
    }

    public long getSuperstructuresParameterValue() {
        if (this.parameters != null && this.parameters.length > superstructuresPosition)
            return parameters[superstructuresPosition];
        return -1;
    }

    public long getGirdersParameterValue() {
        if (this.parameters != null && this.parameters.length > girdersPosition)
            return parameters[girdersPosition];
        return -1;
    }

    public long getGirdersHeightParameterValue() {
        if (this.parameters != null && this.parameters.length > girdersHeightPosition)
            return parameters[girdersHeightPosition];
        return 0;
    }

    public boolean getFilterValue(int position) {
        if (position > -1 && this.filters != null && this.filters.length > position)
            return filters[position];
        if (position == -1) return true;
        return false;
    }

    public double getParameterValue(int position) {
        if (position > -1 && this.parameters != null && this.parameters.length > position)
            return parameters[position];
        return -1.0;
    }

    //?----------------------------------------------------------------------------------------------------------SETTERS

    //-----------------------------------------------------------------------------------------------------------PRIVATE

    private boolean notNullParameterValue(int position, long value) {
        if (this.parameters == null) {
            this.parameters = new long[position + 1];
            this.parameters[position] = value;
            return false;
        } else if (this.parameters.length < position + 1) {
            long[] newParameters = new long[position + 1];
            System.arraycopy(this.parameters, 0, newParameters, 0, this.parameters.length);
            newParameters[position] = value;
            this.parameters = newParameters;
            return false;
        }

        return true;
    }

    private void setParameterValue(int position, long value) {
        if (this.notNullParameterValue(position, value)) {
            this.parameters[position] = value;
        }
    }

    private void addParameterValue(int position, long value) {
        if (this.notNullParameterValue(position, value)) {
            this.parameters[position] += value;
        }
    }

    private boolean notNullFilterValue(int position, boolean value) {
        if (this.filters == null) {
            this.filters = new boolean[position + 1];
            this.filters[position] = value;
            return false;
        } else if (this.filters.length < position + 1) {
            boolean[] newFilters = new boolean[position + 1];
            System.arraycopy(this.filters, 0, newFilters, 0, this.filters.length);
            newFilters[position] = value;
            this.filters = newFilters;
            return false;
        }

        return true;
    }

    private void setFilterValue(int position, boolean value) {
        if (this.notNullFilterValue(position, value)) {
            filters[position] = value;
        }
    }

    //------------------------------------------------------------------------------------------------------------PUBLIC

    public void setDensityParameterValue(long value) {
        setParameterValue(densityPosition, value);
    }

    public void addDensityParameterValue(long value) {
        addParameterValue(densityPosition, value);
    }

    public void setDensityFilterValue(boolean value) {
        setFilterValue(densityPosition, value);
    }

    public void setSuperstructureParameterValue(int value) {
        setParameterValue(superstructuresPosition, value);
    }

    public void setLowerGirderClassValue(int value) {
        setParameterValue(girdersPosition, value);
    }

    public void setGirderHeightValue(long value) {
        setParameterValue(girdersHeightPosition, value);
    }

    public void setGirderHeightValue(boolean value) {
        setFilterValue(girdersHeightPosition, value);
    }
}