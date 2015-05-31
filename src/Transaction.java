public class Transaction
	{
        private String lastName = "";
        private double offerNumber = 0.0;
        
        public Transaction()
        {
            return;
        }
        
        public Transaction(String name, double offer)
        {
            this.lastName = name;
            this.offerNumber = offer;
            return;
        }
        
        public void lastName(String name)
        {
            this.lastName = name;
            return;
        }
        
        public String lastName()
        {
            return this.lastName;
        }
        
        public void offerNumber(double offer)
        {
            this.offerNumber = offer;
            return;
        }
        
        public double offerNumber()
        {
            return this.offerNumber;
        }
    }