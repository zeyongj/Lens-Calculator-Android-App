package ca.cmpt276.as2.dofcalculator.model;

/**
 * Represent a camera lens including the make, its maximum aperture, and focal length.
 */
public class Lens {
    private String make;
    private double maxAperture;
    private int focalLengthInMM;

    public Lens(String make, double maxAperture, int focalLengthInMM) {
        this.make = make;
        this.maxAperture = maxAperture;
        this.focalLengthInMM = focalLengthInMM;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public double getMaxAperture() {
        return maxAperture;
    }

    public void setMaxAperture(double maxAperture) {
        this.maxAperture = maxAperture;
    }

    public int getFocalLengthInMM() {
        return focalLengthInMM;
    }

    public void setFocalLengthInMM(int focalLengthInMM) {
        this.focalLengthInMM = focalLengthInMM;
    }

    public String getDescription() {
        return make + " " + focalLengthInMM + "mm F" + maxAperture;
    }
}
