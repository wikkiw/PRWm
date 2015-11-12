package cz.wikkiw.prwm;

import cz.wikkiw.fitnessfunctions.Ackley;
import cz.wikkiw.fitnessfunctions.FitnessFunction;
import cz.wikkiw.fitnessfunctions.Griewank;
import cz.wikkiw.fitnessfunctions.Quadric;
import cz.wikkiw.fitnessfunctions.Rosenbrock;
import cz.wikkiw.fitnessfunctions.Salomon;
import cz.wikkiw.fitnessfunctions.Schwefel;
import cz.wikkiw.fitnessfunctions.f1;
import cz.wikkiw.fitnessfunctions.f10;
import cz.wikkiw.fitnessfunctions.f11;
import cz.wikkiw.fitnessfunctions.f12;
import cz.wikkiw.fitnessfunctions.f13;
import cz.wikkiw.fitnessfunctions.f14;
import cz.wikkiw.fitnessfunctions.f15;
import cz.wikkiw.fitnessfunctions.f2;
import cz.wikkiw.fitnessfunctions.f3;
import cz.wikkiw.fitnessfunctions.f4;
import cz.wikkiw.fitnessfunctions.f5;
import cz.wikkiw.fitnessfunctions.f6;
import cz.wikkiw.fitnessfunctions.f7;
import cz.wikkiw.fitnessfunctions.f8;
import cz.wikkiw.fitnessfunctions.f9;
import cz.wikkiw.fitnessfunctions.objects.Boundary;
import cz.wikkiw.fitnessfunctions.objects.Individual;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adam on 11/11/2015
 */
public class EdgeWalk {

    private int dimension;
    private int stepCount;
    private FitnessFunction ffunction;
    
    private Boundary boundary;
    private Individual best;
    private List<Individual> walkIndividuals;

    public EdgeWalk(int dimension, int stepCount, FitnessFunction ffunction) {
        this.dimension = dimension;
        this.stepCount = stepCount;
        this.ffunction = ffunction;
        this.boundary = this.ffunction.getBoundary();
    }
    
    public void walk(){
        
        double[] position = new double[this.dimension];
        Random rnd = new Random();
        Individual curInd;
        
        
        int edgedDim = rnd.nextInt(this.dimension);
        double start = (rnd.nextDouble() * this.boundary.getRange()) + this.boundary.getMin();
        double end = (rnd.nextDouble() * this.boundary.getRange()) + this.boundary.getMin();
        double diff = (end-start)/(double) (this.stepCount-1);
        double baseDiff = this.boundary.getRange()/(double) (this.stepCount-1);
//        
//        System.out.println("Start: " + start);
//        System.out.println("End: " + end);
//        System.out.println("Diff: " + diff);
//        System.out.println("BaseDiff: " + baseDiff);
        /**
         * First point
         */
        for(int i=0; i<this.dimension; i++){
            
            if(i == edgedDim){
                position[i] = start;
            }
            else{
                position[i] = this.boundary.getMin();
            }
            
        }
        
        this.walkIndividuals = new ArrayList<>();
        curInd = new Individual(position, this.ffunction.getValue(position));
        this.walkIndividuals.add(curInd);
        this.best = curInd;

        
        double[] newPosition;
        /**
         * step iteartion
         */
        for(int i=1; i<this.stepCount; i++) {

            newPosition = new double[this.dimension];
            
            /**
             * dimension iteration
             */
            for(int dim=0; dim<this.dimension; dim++ ){
                
                if(dim == edgedDim){
                    newPosition[dim] = position[dim] + diff;
                }
                else{
                    newPosition[dim] = position[dim] + baseDiff;
                }
                
                if(newPosition[dim] > this.boundary.getMax()){
                    newPosition[dim] = this.boundary.getMax();
                }
                if(newPosition[dim] < this.boundary.getMin()){
                    newPosition[dim] = this.boundary.getMin();
                }
                
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

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }
    
    public FitnessFunction getFfunction() {
        return ffunction;
    }

    public void setFfunction(FitnessFunction ffunction) {
        this.ffunction = ffunction;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        int dimension = 10;
        int stepCount = 1000;
        int cuts = dimension;
        
        List<FitnessFunction> list = new ArrayList<>();
        list.add(new Ackley());
        list.add(new Griewank());
        list.add(new Quadric());
        list.add(new Rosenbrock());
        list.add(new Schwefel());
        list.add(new Salomon());
        list.add(new f1());
        list.add(new f2());
        list.add(new f3());
        list.add(new f4());
        list.add(new f5());
        list.add(new f6());
        list.add(new f7());
        list.add(new f8());
        list.add(new f9());
        list.add(new f10());
        list.add(new f11());
        list.add(new f12());
        list.add(new f13());
        list.add(new f14());
        list.add(new f15());
        
        EdgeWalk edge;
        PrintWriter writer;
        
        for(FitnessFunction ff : list){
            
            ff.init(dimension);
            
            try {
                writer = new PrintWriter(ff.getName()+"-cut.txt", "UTF-8");

                writer.print("{");

                for(int cut=0; cut < cuts; cut++){

                    edge = new EdgeWalk(dimension, stepCount, ff);
                    edge.walk();

                    writer.print("{");

                    for(int i=0; i < edge.walkIndividuals.size(); i++){

                        writer.print(String.format(Locale.US, "%.10f",edge.walkIndividuals.get(i).getFitness()));

                        if(i != edge.walkIndividuals.size()-1){
                            writer.print(",");
                        }

                    }

                    writer.print("}");

                    if(cut != cuts-1){
                        writer.print(",");
                    }

                }

                writer.print("}");

                writer.close();

            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(EdgeWalk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
    }
    
}
