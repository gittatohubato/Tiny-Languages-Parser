package controller;

import Parser.Node;
import Parser.Tree;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import shape.Circle;
import shape.Line;

import java.util.Objects;

import static jdk.nashorn.internal.objects.Global.Infinity;

public final class GraphicsTree {
    public HBox hBox = new HBox(new Canvas(8000,8000));
    public Canvas canvas = (Canvas) hBox.getChildren().get(hBox.getChildren().size()-1);
    private Node root;
    private Circle insertCircle;
    public static double xExtreme = 0;
    public static double xExtremeCircle = 0;
    public static double yMaxRight = 0;
    public static double yMaxRightCircle = 0;

    public GraphicsTree(Node root) {

        this.root = root;
        canvas.widthProperty().addListener((evt) -> {
            this.drawTree();
        });
        canvas.heightProperty().addListener((evt) -> {
            this.drawTree();
        });
        this.createTree();
    }

    public void createTree() {

        this.drawTree();
    }

    protected void drawTree() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0.0D, 0.0D, width, height);
        if (this.root != null) {
            double treeHeight = this.root.getHeight(this.root);
            double treeWidth = this.root.getWidth(this.root);
            System.out.println("tree width = "+treeWidth);
            treeWidth = (treeWidth<5)? treeWidth*100 : treeWidth*80;
            treeHeight = (treeHeight<5)? treeHeight*35 : treeHeight*13;//14.0625D;
            System.out.println("tree width = "+treeHeight);
            this.drawTree(gc, this.root, 0.0D, treeWidth, 0.0D, treeHeight);
            this.drawCircles(gc, this.root, 0.0D, treeWidth, 0.0D, treeHeight);
        }
    }

    protected void drawTree(GraphicsContext gc, Node treeNode, double xMin, double xMax, double yMin, double yMax) {
        Line newLine = new Line();
        Point2D linePoint1;
        Point2D linePoint2;

        double xMinLeft = xMin;
        double xMaxLeft = xMax;
        double xMinCenter = xMin;
        double xMaxCenter = xMax;
        double xMinRight = xMin;
        double xMaxRight = xMax;
        double xMinNeighbour = xMax;
        double xMaxNeighbour = xMax+(xMax-xMin);
        System.out.println("temp = "+(xMax-xMin));

        if(xExtreme < xMax) xExtreme=xMax;
        if(yMaxRight<yMin || yMaxRight<yMax)  yMaxRight = Math.max(yMin, yMax);

        if(treeNode.leftChild != null && treeNode.midChild != null && treeNode.rightChild != null) {
            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 3.0D);
            xMinCenter = xMaxLeft;
            xMaxCenter = xMaxLeft + ((xMax - xMin) / 3.0D);
            xMinRight = xMaxCenter;
            xMaxRight = xMaxCenter + ((xMax - xMin) / 3.0D);
        }
        else if(treeNode.leftChild != null && treeNode.midChild != null) {
            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 2.0D);
            xMinCenter = xMaxLeft;
            xMaxCenter = xMaxLeft + ((xMax - xMin) / 2.0D);
        }
        else if(treeNode.leftChild != null && treeNode.rightChild != null) {

            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 2.0D);
            xMinRight = xMaxLeft;
            xMaxRight = xMaxLeft + ((xMax - xMin) / 2.0D);
        }

        if (treeNode.rightChild != null) {
            newLine.setHighlighter(false);
            if (treeNode.rightChild.highlightFlag) {
                newLine.setHighlighter(true);
            }

            linePoint1 = new Point2D((xMin + xMax) / 2.0D, yMin + yMax / 2.0D);
            linePoint2 = new Point2D((xMaxRight + xMinRight) / 2.0D, yMin + yMax + yMax / 2.0D);
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);
            this.drawTree(gc, treeNode.rightChild, xMinRight, xMaxRight, yMin + yMax, yMax);
        }

        if (treeNode.leftChild != null) {
            newLine.setHighlighter(false);
            if (treeNode.leftChild.highlightFlag) {
                newLine.setHighlighter(true);
            }

            linePoint1 = new Point2D((xMin + xMax) / 2.0D, yMin + yMax / 2.0D);

            double yMinTemp = yMin;
            double yMaxTemp = yMax;
            if(treeNode.leftChild.neighbour != null){
                yMinTemp = 50;
                yMaxTemp = yMaxRight;
            }

            linePoint2 = new Point2D((xMaxLeft+xMinLeft)/2.0D, (yMinTemp + yMaxTemp + yMaxTemp / 2.0D));
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);
            this.drawTree(gc, treeNode.leftChild,xMinLeft, xMaxLeft, yMinTemp + yMaxTemp, yMaxTemp);
        }

        if (treeNode.midChild != null) {
            newLine.setHighlighter(false);
            if (treeNode.midChild.highlightFlag) {
                newLine.setHighlighter(true);
            }

            linePoint1 = new Point2D((xMin + xMax) / 2.0D, yMin + yMax / 2.0D);

            double yMinTemp = yMin;
            double yMaxTemp = yMax;
            if(treeNode.midChild.neighbour != null){
                yMinTemp = 50;
                yMaxTemp = yMaxRight;
            }

            linePoint2 = new Point2D((xMaxCenter+xMinCenter) / 2.0D, yMinTemp + yMaxTemp + yMaxTemp / 2.0D);
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);
            this.drawTree(gc, treeNode.midChild,xMinCenter, xMaxCenter, yMinTemp + yMaxTemp, yMaxTemp);
        }

        if (treeNode.neighbour != null) {
            newLine.setHighlighter(false);

            if (treeNode.neighbour.highlightFlag) {
                newLine.setHighlighter(true);
            }

            if(xMinNeighbour < xExtreme){
                double temp = xMax-xMin;
                System.out.println(temp);
                xMinNeighbour = xExtreme;
                xMaxNeighbour = xExtreme+temp;
            }

            yMaxRight = 0;
            linePoint1 = new Point2D((xMin + xMax) / 2.0D, yMin + yMax / 2.0D);
            linePoint2 = new Point2D((xMaxNeighbour+xMinNeighbour) /2.0D, yMin + yMax / 2.0D);
            newLine.setPoint(linePoint1, linePoint2);
            newLine.draw(gc);
            this.drawTree(gc, treeNode.neighbour, xMinNeighbour, xMaxNeighbour, yMin, yMax);
        }
    }

    public void drawCircles(GraphicsContext gc, Node treeNode, double xMin, double xMax, double yMin, double yMax) {

        Point2D point = new Point2D((xMin + xMax) / 2.0D, yMin + yMax / 2.0D);

        double xMinLeft = xMin;
        double xMaxLeft = xMax;
        double xMinCenter = xMin;
        double xMaxCenter = xMax;
        double xMinRight = xMin;
        double xMaxRight = xMax;
        double xMinNeighbour = xMax;
        double xMaxNeighbour = xMax+(xMax-xMin);//*1.5;

        if(xExtremeCircle < xMax) xExtremeCircle=xMax;
        if(yMaxRightCircle<yMin || yMaxRightCircle<yMax)  yMaxRightCircle = Math.max(yMin, yMax);

        if(treeNode.leftChild != null && treeNode.midChild != null && treeNode.rightChild != null) {
            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 3.0D);
            xMinCenter = xMaxLeft;
            xMaxCenter = xMaxLeft + ((xMax - xMin) / 3.0D);
            xMinRight = xMaxCenter;
            xMaxRight = xMaxCenter + ((xMax - xMin) / 3.0D);
        }
        else if(treeNode.leftChild != null && treeNode.midChild != null ) {
            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 2.0D);
            xMinCenter = xMaxLeft;
            xMaxCenter = xMaxLeft + ((xMax - xMin) / 2.0D);
        }
        else if(treeNode.leftChild != null && treeNode.rightChild != null ) {
            xMinLeft = xMin;
            xMaxLeft = xMin + ((xMax - xMin) / 2.0D);
            xMinRight = xMaxLeft;
            xMaxRight = xMaxLeft + ((xMax - xMin) / 2.0D);
        }
        if (!treeNode.highlightFlag && !Objects.equals(treeNode.root, this.insertCircle)) {
            treeNode.root.setHighlighter(false);
        } else {
            this.insertCircle = null;
            treeNode.highlightFlag = false;
            treeNode.root.setHighlighter(true);
        }
        treeNode.root.setPoint(point);

        treeNode.root.draw(gc, treeNode.shape);

        if (treeNode.rightChild != null) {
            this.drawCircles(gc, treeNode.rightChild,xMinRight, xMaxRight, yMin + yMax, yMax);
        }

        if (treeNode.leftChild != null) {
            double yMinTemp = yMin;
            double yMaxTemp = yMax;
            if(treeNode.leftChild.neighbour != null){
                yMinTemp = 50;
                yMaxTemp = yMaxRightCircle;
            }
            this.drawCircles(gc, treeNode.leftChild,xMinLeft, xMaxLeft, yMinTemp + yMaxTemp, yMaxTemp);
        }
        if (treeNode.midChild != null) {
            double yMinTemp = yMin;
            double yMaxTemp = yMax;
            if(treeNode.midChild.neighbour != null){
                yMinTemp = 50;
                yMaxTemp = yMaxRightCircle;
            }
            this.drawCircles(gc, treeNode.midChild,xMinCenter, xMaxCenter, yMinTemp + yMaxTemp, yMaxTemp);
        }
        if (treeNode.neighbour != null) {
            yMaxRightCircle = 0;
            if(xMinNeighbour < xExtremeCircle){
                double temp = xMax-xMin;
                xMinNeighbour = xExtremeCircle;
                xMaxNeighbour = xExtremeCircle+temp;
            }
            this.drawCircles(gc, treeNode.neighbour, xMinNeighbour,xMaxNeighbour, yMin, yMax);
        }

    }
}
