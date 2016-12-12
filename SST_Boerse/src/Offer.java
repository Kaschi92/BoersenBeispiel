import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;


public class Offer {
	
	private int aktienID = -1;
	private int Aktion�rID = -1;
	private boolean type;
	private double price = 0.0;
	private long activate = 0;
	
	
	Offer(boolean type, double price, int aktion�rsID, int aktienID){
		this.type = type;
		this.price = price;
		this.Aktion�rID = aktion�rsID; // die id vom aktion�r der verkaufen oder kaufen will
		this.aktienID = aktienID;
		
	}
		
	public boolean getType(){
		return type;
	}
	
	public boolean Activate(long activated, int newAktion�rID, double ekWert){
		this.activate = activated; // timestamp vom ordnerbuch
		
		if(newAktion�rID>-1){
			
			/* von aktion�rid ---> zu newOwnerID */
			
		}
		// activate funktion
		
		

		
		
		// nachschauen ob bei dem user das depod vorhanden ist
		
		// wenn ja weitere hinzuf�gen sonst depod erstellen
		
		return true;
	}
	
	public double getPrice(){
		return price;
	}
	
    public Map<String, AttributeValue> getOfferItem() {
        
    	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        
        item.put("ID", new AttributeValue().withN(Integer.toString(aktienID)));
        item.put("Aktion�rID", new AttributeValue(Integer.toString(Aktion�rID)));
        item.put("price", new AttributeValue().withN(Double.toString(price)));
        item.put("type", new AttributeValue(Boolean.toString(type)));
        item.put("activate", new AttributeValue("-1"));
        
        return item;
    }



}
