public class Centroid
    {
        private double mX = 0.0;
        private double mY = 0.0;
        
        public Centroid()
        {
            return;
        }
        
        public Centroid(double X, double Y)
        {
            this.mX = X;
            this.mY = Y;
            return;
        }
        
        public void X(double X)
        {
            this.mX = X;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double Y)
        {
            this.mY = Y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
    }