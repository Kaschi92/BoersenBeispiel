import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;


public class Offer {
	
	private int aktienID = -1;
	private int AktionärID = -1;
	private boolean type;
	private double price = 0.0;
	private long activate = 0;
	
	
	Offer(boolean type, double price, int aktionärsID, int aktienID){
		this.type = type;
		this.price = price;
		this.AktionärID = aktionärsID; // die id vom aktionär der verkaufen oder kaufen will
		this.aktienID = aktienID;
		
	}
		
	public boolean getType(){
		return type;
	}
	
	public boolean Activate(long activated, int newAktionärID, double ekWert){
		this.activate = activated; // timestamp vom ordnerbuch
		
		if(newAktionärID>-1){
			
			/* von aktionärid ---> zu newOwnerID */
			
		}
		// activate funktion
		
		

		
		
		// nachschauen ob bei dem user das depod vorhanden ist
		
		// wenn ja weitere hinzufügen sonst depod erstellen
		
		return true;
	}
	
	public double getPrice(){
		return price;
	}
	
    public Map<String, AttributeValue> getOfferItem() {
        
    	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        
        item.put("ID", new AttributeValue().withN(Integer.toString(aktienID)));
        item.put("AktionärID", new AttributeValue(Integer.toString(AktionärID)));
        item.put("price", new AttributeValue().withN(Double.toString(price)));
        item.put("type", new AttributeValue(Boolean.toString(type)));
        item.put("activate", new AttributeValue("-1"));
        
        return item;
    }



}
