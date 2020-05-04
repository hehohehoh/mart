package mysmallmart;

public class ProductVO implements Comparable<ProductVO>{

	private int product_id;
	private String p_name;
	private String manufacture;
	private int nowStock;
	private int buy_price;
	private int sell_price;
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public int getNowStock() {
		return nowStock;
	}
	public void setNowStock(int nowStock) {
		this.nowStock = nowStock;
	}
	public int getBuy_price() {
		return buy_price;
	}
	public void setBuy_price(int buy_price) {
		this.buy_price = buy_price;
	}
	public int getSell_price() {
		return sell_price;
	}
	public void setSell_price(int sell_price) {
		this.sell_price = sell_price;
	}
	
	@Override
	public int compareTo(ProductVO vo) {
		
		if(this.product_id > vo.product_id)
			return 1;
		else if(this.product_id == vo.product_id)
			return 0;
		else
			return -1;
		
	}
}
