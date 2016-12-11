import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;


public class Aktie {

	private int id;
	private int count;
	private double kurs;
	private String name;	
	
	Aktie(int _id, int _count, double _kurs, String _name){
		
		this.id = _id;
		this.count = _count;
		this.kurs = _kurs;
		this.name = _name;		
		
	}
	
	
    public Map<String, AttributeValue> getAktienItem() {
        
    	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        
        item.put("ID", new AttributeValue().withN(Integer.toString(this.id)));
        item.put("Aktien-Name", new AttributeValue(this.name));
        item.put("Anzahl", new AttributeValue().withN(Double.toString(this.kurs)));
        item.put("Kurs", new AttributeValue().withN(Double.toString(this.count)));
        
        return item;
    }
	
}
