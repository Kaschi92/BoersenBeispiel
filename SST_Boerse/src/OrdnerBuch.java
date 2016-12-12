import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;


public class OrdnerBuch {

	private long timeStamp;
	
	private String aktienName;
	private int aktienID;
	private double newPrice;
	
	private ArrayList<Offer> purchase;
	private ArrayList<Offer> sale;
	
		public OrdnerBuch(String aktienName, int aktienID) {

			purchase = new ArrayList<Offer>();
			sale = new ArrayList<Offer>();
			timeStamp = System.currentTimeMillis();
			
			if(readIn()){
				System.out.println("Ordnerbuch "+ aktienName +" erfolgreich eingelesen");
			}else{
				if(createOrdnerBuchTable()){
					System.out.println("Ordnerbuch für die Aktie " + aktienName +" erstellt!");
				}else{
					System.out.println("Fehler beim Ordnerbucherstellen");
				}
			}			
		}
		
		public boolean addOffer(Offer offer){
				
			
			if(offer!=null){	
				
				if(offer.getType()==true) // verkauf
				{					
					sale.add(offer);
				}
				else if(offer.getType()==false) // einkauf
				{
					purchase.add(offer);
				}		
				
				// TODO hinzufügen der dynamodb einträge
				return true;
			}			
			return false;
		}
		
		public void exeCuteOffers(){
			
			// logik für die berechnung des preises
			calcPrice();
			
			int newOwnerId = 0; 
			for (Offer offer : purchase) {
				offer.Activate(System.currentTimeMillis(), newOwnerId,newPrice);
			}
			
			for (Offer offer : sale) {
				offer.Activate(System.currentTimeMillis(), -1,newPrice);
			}
			
			// die id des neuen besitzers des Offers
			
			
			
			
		}
		
		private void calcPrice() {
			// TODO Auto-generated method stub
			
		}

		public int getPCount(){
			return purchase.size();
		}
		
		public int getSCount(){
			return sale.size();
		}
		
		public boolean writeOut(){
			try {
				AmazonDynamoDBSample.createTable(this.aktienName,new CreateTableRequest().withTableName("User")
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
		
		public boolean readIn(){
			// tablename = aktienName
			//TODO OrdnerBuch einlesen
			return true;
		}
		
		private boolean createOrdnerBuchTable(){
			
			// tablename = aktienName
			//TODO OrdnerBucheintrag in tabelle
			
			return false;
		}
		
	
}
