import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


public class UserDepod {

	
	private int id;
	private String name; // überall dazu bei einträge von Aktienanteilen
	private double balance; // toDo
	
	public UserDepod(int userID, String name, double balance){
		this.id = userID;
		this.name = name;
		this.balance = balance;
	}
	
	public UserDepod(int userID, String name){
		this.id = userID;
		this.name = name;
		this.balance = 0.0;
	}
	
	public void mdfBalance(double value){
		balance = balance + value;
	}
	
	public boolean readIn(){
		//TODO
		return false;
	}
	
	public boolean writeOut(){
		try {
			AmazonDynamoDBSample.createTable(Integer.toString(this.id),new CreateTableRequest().withTableName("User")
			        .withKeySchema(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH))
			        .withAttributeDefinitions(new AttributeDefinition().withAttributeName("ID").withAttributeType(ScalarAttributeType.N))
			        .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(4L).withWriteCapacityUnits(5L)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		// tablename = aktienName
		return false;
	}
	
	
}
