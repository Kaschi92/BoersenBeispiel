import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class User {

	private String firstname;
	private String lastname;
	private int id;
	private int depotid;
	
	
	User(String _firstname, String _lastname, int _id, int _depodid){
		firstname = _firstname;
		lastname = _lastname;
		id = _id;
		depotid = _depodid;
	}
	
    public Map<String, AttributeValue> getUserItem() {
        
    	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        
        item.put("ID", new AttributeValue().withN(Integer.toString(this.id)));
        item.put("DepotID", new AttributeValue().withN(Integer.toString(this.depotid)));
        item.put("Vorname", new AttributeValue(this.firstname));
        item.put("Nachname", new AttributeValue(this.lastname));
        
        return item;
    }
	

}
