package Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Report implements Serializable{

//	Attributes
	
	static int reportId;
	{
		reportId=0;
	}
	Product product;
	Double sellingCost;
	int quantity;
	String size;
	LocalDateTime dateTime;
	private static final long serialVersionUID = 1L;
	
//	Constructors
	
	public Report() {
		
	}
	
	public Report(Product product,String size,int quantity,Double sellingCost) {
		reportId++;
		this.product=product;
		this.quantity=quantity;
		this.sellingCost = sellingCost;
		this.size=size;
		this.dateTime = LocalDateTime.now();;
	}

//	Setters and Getters
	
	public Double getSellingCost() {
		return sellingCost;
	}

	public static int getReportId() {
		return reportId;
	}

	public static void setReportId(int reportId) {
		Report.reportId = reportId;
	}

	public void setSellingCost(Double sellingCost) {
		this.sellingCost = sellingCost;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

//	Methods
	
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", product=" + (product != null ? product.getModel() : "null") +
                ", sellingCost=" + sellingCost +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

}//Class Reports ends here
