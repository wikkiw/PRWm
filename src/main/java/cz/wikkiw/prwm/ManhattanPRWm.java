package cz.wikkiw.prwm;

import cz.wikkiw.disipativechaos.DisipativeChaos;
import cz.wikkiw.fitnessfunctions.Ackley;
import cz.wikkiw.fitnessfunctions.FitnessFunction;
import cz.wikkiw.fitnessfunctions.objects.Boundary;
import cz.wikkiw.fitnessfunctions.objects.Individual;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author adam 5/11/2015
 */
public class ManhattanPRWm {

    private int dimension;
    private int stepCount;
    private double stepBoundary;
    private int[] startZone;
    private FitnessFunction ffunction;
    private double changeProbability;
    
    private Boundary boundary;
    private Individual best;
    private List<Individual> walkIndividuals;
    private DisipativeChaos chaos;

    public ManhattanPRWm(int dimension, int stepCount, double stepBoundary, int[] startZone, FitnessFunction ffunction, double changeProbability) {
        this.dimension = dimension;
        this.stepCount = stepCount;
        this.stepBoundary = stepBoundary;
        this.startZone = startZone;
        this.ffunction = ffunction;
        this.boundary = this.ffunction.getBoundary();
        this.changeProbability = changeProbability;
        this.chaos = new DisipativeChaos();
    }
    
    public void walk(){
        
        double[] position = new double[this.dimension];
        Random rnd = new Random();
        double r;
        double halfInterval = this.boundary.getRange()/2;
        Individual curInd;
        
        /**
         * First point
         */
        for(int i=0; i<this.dimension; i++){
            r = rnd.nextDouble() * halfInterval;
            if(this.startZone[i] == 1){
                position[i] = this.boundary.getMax() - r;
            } else {
                position[i] = this.boundary.getMin() + r;
            }
        }
        
        int rD = rnd.nextInt(this.dimension);
        
        /**
         * Set one feature to the edge of dimension
         */
        if(this.startZone[rD] == 1) {
            position[rD] = this.boundary.getMax();
        } else {
            position[rD] = this.boundary.getMin();
        }
        
        this.walkIndividuals = new ArrayList<>();
        curInd = new Individual(position, this.ffunction.getValue(position));
        this.walkIndividuals.add(curInd);
        this.best = curInd;
        
//        double maxStepLength = this.boundary.getRange() * this.stepBoundary;
        int changedDimension;
        
        double[] newPosition;
        int dim;
        changedDimension = -1;
        
        /**
         * step iteartion
         */
        for(int i=1; i<this.stepCount; i++) {

            newPosition = position.clone();
            
            /**
             * dimension iteration
             */
            dim = rnd.nextInt(this.dimension);
                
            r = this.stepBoundary;

            /**
             * Direction change (ping-pong) modification
             */
            if(rnd.nextDouble() <= this.changeProbability && dim != changedDimension){
                this.startZone[dim] = (this.startZone[dim]+1) % 2;
                changedDimension = dim;
            }

            if(this.startZone[dim] == 1){
                r = -r;
            }
            newPosition[dim] = position[dim] + r;
            
            if((newPosition[dim] + this.stepBoundary) > this.boundary.getMax()){
//                newPosition[dim] = this.boundary.getMax() - (newPosition[dim] - this.boundary.getMax());
                this.startZone[dim] = (this.startZone[dim]+1) % 2;
            }
            if((newPosition[dim]-this.stepBoundary) < this.boundary.getMin()){
//                newPosition[dim] = this.boundary.getMin() - (newPosition[dim] - this.boundary.getMin());
                this.startZone[dim] = (this.startZone[dim]+1) % 2;
            }
                
            
            
            curInd = new Individual(newPosition, this.ffunction.getValue(newPosition));
            this.walkIndividuals.add(curInd);
            if(curInd.getFitness() <= this.best.getFitness()){
                this.best = curInd;
            }
            position = newPosition.clone();
            
        }
        
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getStepBoundary() {
        return stepBoundary;
    }

    public void setStepBoundary(double stepBoundary) {
        this.stepBoundary = stepBoundary;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public int[] getStartZone() {
        return startZone;
    }

    public void setStartZone(int[] startZone) {
        this.startZone = startZone;
    }

    public FitnessFunction getFfunction() {
        return ffunction;
    }

    public void setFfunction(FitnessFunction ffunction) {
        this.ffunction = ffunction;
    }

    public double getChangeProbability() {
        return changeProbability;
    }

    public void setChangeProbability(double changeProbability) {
        this.changeProbability = changeProbability;
    }

    public Individual getBest() {
        return best;
    }

    public void setBest(Individual best) {
        this.best = best;
    }

    public List<Individual> getWalkIndividuals() {
        return walkIndividuals;
    }

    public void setWalkIndividuals(List<Individual> walkIndividuals) {
        this.walkIndividuals = walkIndividuals;
    }

    public DisipativeChaos getChaos() {
        return chaos;
    }

    public void setChaos(DisipativeChaos chaos) {
        this.chaos = chaos;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FitnessFunction ff = new Ackley();
        int dimension = 2;
        int stepCount = 10000;
        double stepBoundary = (ff.getBoundary().getRange()*dimension)/1000.0;
        System.out.println(stepBoundary);
        
        int[] startZone = {1,0};
        double changeProbability = 0.05;
        
        ManhattanPRWm mprwm = new ManhattanPRWm(dimension, stepCount, stepBoundary, startZone, ff, changeProbability);
        mprwm.walk();
        
        for(Individual ind : mprwm.walkIndividuals){
            System.out.println(ind);
        }
        System.out.println("========================");
        System.out.println(mprwm.best);
        
    }
    
}
