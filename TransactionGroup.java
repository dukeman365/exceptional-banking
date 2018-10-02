
public class TransactionGroup {
  
  private enum EncodingType { BINARY_AMOUNT, INTEGER_AMOUNT, QUICK_WITHDRAW };
  private EncodingType type;
  private int[] values;

  public TransactionGroup(int[] groupEncoding) {
    this.type = EncodingType.values()[groupEncoding[0]];
    this.values = new int[groupEncoding.length-1];
    for(int i=0;i<values.length;i++)
      this.values[i] = groupEncoding[i+1];
  }
  
  public int getTransactionCount() {
    int transactionCount = 0;
    switch(this.type) {
      case BINARY_AMOUNT:
        int lastAmount = -1;
        for(int i=0;i<this.values.length;i++) {
          if(this.values[i] != lastAmount) {
            transactionCount++; 
            lastAmount = this.values[i];            
          }
        }
        break;
      case INTEGER_AMOUNT:
        transactionCount = values.length;
        break;
      case QUICK_WITHDRAW:
        for(int i=0;i<this.values.length;i++)
          transactionCount += this.values[i];        
    }
    return transactionCount;
  }
  
  public int getTransactionAmount(int transactionIndex) {
    int transactionCount = 0;
    switch(this.type) {
      case BINARY_AMOUNT:
        int lastAmount = -1;
        int amountCount = 0;
        for(int i=0;i<=this.values.length;i++) {
          if(i == this.values.length || this.values[i] != lastAmount)  { 
            if(transactionCount-1 == transactionIndex) {
              if(lastAmount == 0)
                return -1 * amountCount;
              else
                return +1 * amountCount;
            }
            transactionCount++;       
            lastAmount = this.values[i];
            amountCount = 1;
          } else
            amountCount++;
          lastAmount = this.values[i];
        } 
        break;
      case INTEGER_AMOUNT:
        return this.values[transactionIndex];
      case QUICK_WITHDRAW:
        final int[] QW_AMOUNTS = new int[] {-20, -40, -80, -100};
        for(int i=0;i<this.values.length;i++) 
          for(int j=0;j<this.values[i];j++)
            if(transactionCount == transactionIndex) 
              return QW_AMOUNTS[i]; 
            else 
              transactionCount++;
    }
    return -1;
  }  
}
