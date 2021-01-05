package ca.cmpt276.as2.dofcalculator.model;

/**
 * Compute depth of field distances for a given lens and camera setup.
 * Math Sources:
 *  https://www.bhphotovideo.com/explora/photography/tips-and-solutions/depth-field-part-2
 */
public class DoFCalculator {
    private static final double MM_PER_M = 1000.0;

    private double circleOfConfusion;
    private Lens lens;
    private double aperture;
    private double distanceInMM;

    public DoFCalculator(double circleOfConfusion, Lens lens, double aperture, double distanceInM) {
        this.circleOfConfusion = circleOfConfusion;
        this.lens = lens;
        this.aperture = aperture;
        this.distanceInMM = distanceInM * MM_PER_M;
    }

    public double getHpyerfocalDistanceInM() {
        double focLenMM = lens.getFocalLengthInMM();
        return (focLenMM * focLenMM) / (aperture * circleOfConfusion)
                / MM_PER_M;
    }
    public double getNearFocalPointInM() {
        double hyperMM = getHpyerfocalDistanceInM() * MM_PER_M;
        double focLenMM = lens.getFocalLengthInMM();
        return (hyperMM * distanceInMM)
                / (hyperMM + (distanceInMM - focLenMM))
                / MM_PER_M;
    }
    public double getFarFocalPointInM() {
        double hyperMM = getHpyerfocalDistanceInM() * MM_PER_M;
        double focLenMM = lens.getFocalLengthInMM();

        // Check for beyond the hyperfocal point
        if (hyperMM <= distanceInMM) {
            return Double.POSITIVE_INFINITY;
        }

        return (hyperMM * distanceInMM)
                /
                (hyperMM - (distanceInMM - focLenMM))
                / MM_PER_M;
    }
    public double getDepthOfFieldInM() {
        return getFarFocalPointInM() - getNearFocalPointInM();
    }
}
