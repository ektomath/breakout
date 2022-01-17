package breakout.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPositionable {

        private double x;
        private double y;
        private double width;
        private double height;
        private double dx;
        private double dy;

        public AbstractPositionable(double x, double y, double width, double height, double dx, double dy) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.dx = dx;
            this.dy = dy;
        }

        public boolean intersects(AbstractPositionable otherthing) {
            boolean above = otherthing.getMaxY() < getY();
            boolean below = otherthing.getY() > getMaxY();
            boolean leftOf = otherthing.getMaxX() < getX();
            boolean rightOf = otherthing.getX() > getMaxX();
            return !(above || below || leftOf || rightOf);
        }

        public double getX() { return x;}
        public double getMaxX() {
            return x + width;
        }
        public double getY() {return y;}
        public double getMaxY() {
            return y + height;
        }
        public double getWidth() {return width;}
        public double getHeight() {return height;}
        public double getDx() {return dx;}
        public double getDy() {return dy;}
        public void setX(double x) {this.x = x;}
        public void setY(double y) {this.y = y;}
        public void setDx(double dx) {
            this.dx = dx;
        }
        public void setDy(double dy) {
            this.dy = dy;
        }

    }



