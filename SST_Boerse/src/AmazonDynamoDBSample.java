/*
 * Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.datapipeline.model.Query;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class AmazonDynamoDBSample {

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (C:\\Users\\Wiesorium\\.aws\\credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonDynamoDBClient dynamoDB;

//    static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
//            new ProfileCredentialsProvider()));
//
//    static SimpleDateFormat dateFormatter = new SimpleDateFormat(
//            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//
//    static String productCatalogTableName = "ProductCatalog";
//    static String forumTableName = "Forum";
//    static String threadTableName = "Thread";
//    static String replyTableName = "Reply";
    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
     * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\Wiesorium\\.aws\\credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\Wiesorium\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    public static void main(String[] args) throws Exception  {
    	
    	/* Init - kA */
    	
        init();
                
        
        /* Delete Tables (if needed) */
        
//        try{
//        deleteTable("User");
//        deleteTable("Aktien");
//        }
//        catch(Exception e){
//        	
//        }
        
        /* Tables Erstellen (if needed) */
              
//        createTable("User", new CreateTableRequest().withTableName("User")
//            .withKeySchema(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH))
//            .withAttributeDefinitions(new AttributeDefinition().withAttributeName("ID").withAttributeType(ScalarAttributeType.N))
//            .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(4L).withWriteCapacityUnits(5L)));
//        
//        
//       
//        createTable("Aktien",new CreateTableRequest().withTableName("Aktien")
//                .withKeySchema(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH))
//                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("ID").withAttributeType(ScalarAttributeType.N))
//                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(4L).withWriteCapacityUnits(5L)));
//                   
        
        /* User hinzufügen */

        User newUser = new User("Martin","Wieser",1,11);
        addUser(newUser);
        newUser = new User("Stephanie","Kaschnitz",2,21);
        addUser(newUser);
        newUser = new User("Christopher","Wieland",3,31);
        addUser(newUser);
        
        /* Aktien hinzufügen */
        
        Aktie newAktie = new Aktie(1, 1000, 10, "GOOGLE");
        addAktie(newAktie);
        newAktie = new Aktie(2, 1000, 10, "FACEBOOK");
        addAktie(newAktie);
        newAktie = new Aktie(3, 1000, 10, "Apfel");
        addAktie(newAktie);
        
        /* Abfragen des Tables */
        
        printResults(scan("Aktien"));
        printResults(scan("User"));
    
    }
    
    private static void printResults(ScanResult sr){
    	int ct = sr.getCount();
    	
    	for(int a = 0; a<ct; a++){
    		System.out.println(sr.getItems().get(a).toString());
    	}
    		
    }
    
    private static void addAktie(Aktie newAktie) {

    	String tableName = "Aktien";
        Map<String, AttributeValue> item = newAktie.getAktienItem();
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        
        System.out.println(newAktie.toString() + " added!");
		
	}

	private static ScanResult scan(String tableName){
//      /*    Query     */

      // Scan items for movies with a year attribute greater than 1985
      HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
      Condition condition = new Condition()
          .withComparisonOperator(ComparisonOperator.GT.toString())
          .withAttributeValueList(new AttributeValue().withN("0"));
      scanFilter.put("ID", condition);
      ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
      ScanResult scanResult = dynamoDB.scan(scanRequest);
      System.out.println("Result (Was habe ich in der Tabelle "+ tableName +" gefunden?): " + scanResult);
      
      return scanResult;
      
    }
    
    private static void addUser(User newUser){
    	
    	String tableName = "User";
        Map<String, AttributeValue> item = newUser.getUserItem();
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        
        System.out.println(newUser.toString() + " added!");
        
    }
    
    private static void createTable(String _tablename, CreateTableRequest _createTableRequest) throws Exception
    {
    	System.out.println("\n*** Creating table "+ _tablename + " ***");

    	String tableName = _tablename;
    	
    	 try {
        // Create a table with a primary hash key named 'name', which holds a string
        CreateTableRequest createTableRequest = _createTableRequest;

        // Create table if it does not exist yet
        TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
        // wait for the table to move into ACTIVE state
        TableUtils.waitUntilActive(dynamoDB, tableName);

        // Describe our new table
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
     
    	 } catch (AmazonServiceException ase) {
             System.out.println("Caught an AmazonServiceException, which means your request made it "
                     + "to AWS, but was rejected with an error response for some reason.");
             System.out.println("Error Message:    " + ase.getMessage());
             System.out.println("HTTP Status Code: " + ase.getStatusCode());
             System.out.println("AWS Error Code:   " + ase.getErrorCode());
             System.out.println("Error Type:       " + ase.getErrorType());
             System.out.println("Request ID:       " + ase.getRequestId());
         } catch (AmazonClientException ace) {
             System.out.println("Caught an AmazonClientException, which means the client encountered "
                     + "a serious internal problem while trying to communicate with AWS, "
                     + "such as not being able to access the network.");
             System.out.println("Error Message: " + ace.getMessage());
         }

    }

    private static void deleteTable(String table){
    	dynamoDB.deleteTable(table);
    }
    
    
    void dummycommentfunction(){
    	
    	
//    	private static void createTable() throws Exception
//        {
//        	System.out.println("\n*** Creating table ***");
//
//        	String tableName = "User";
//        	
//        	 try {
//            // Create a table with a primary hash key named 'name', which holds a string
//            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
//                .withKeySchema(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH))
//                .withAttributeDefinitions(new AttributeDefinition().withAttributeName("ID").withAttributeType(ScalarAttributeType.N))
//                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(4L).withWriteCapacityUnits(5L));
//
//            // Create table if it does not exist yet
//            TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
//            // wait for the table to move into ACTIVE state
//            TableUtils.waitUntilActive(dynamoDB, tableName);
//
//            // Describe our new table
//            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
//            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
//            System.out.println("Table Description: " + tableDescription);
//         
//            System.out.println("\n*** Created table ***");
//            
//        	 } catch (AmazonServiceException ase) {
//                 System.out.println("Caught an AmazonServiceException, which means your request made it "
//                         + "to AWS, but was rejected with an error response for some reason.");
//                 System.out.println("Error Message:    " + ase.getMessage());
//                 System.out.println("HTTP Status Code: " + ase.getStatusCode());
//                 System.out.println("AWS Error Code:   " + ase.getErrorCode());
//                 System.out.println("Error Type:       " + ase.getErrorType());
//                 System.out.println("Request ID:       " + ase.getRequestId());
//             } catch (AmazonClientException ace) {
//                 System.out.println("Caught an AmazonClientException, which means the client encountered "
//                         + "a serious internal problem while trying to communicate with AWS, "
//                         + "such as not being able to access the network.");
//                 System.out.println("Error Message: " + ace.getMessage());
//             }
//
//        }
//      dynamoDB.deleteTable("User");
//      try {
//          String tableName = "User";
//
//          /*    Table erstellen     */
//          
//          // Create a table with a primary hash key named 'name', which holds a string
//          CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
//              .withKeySchema(
//              		new KeySchemaElement().withAttributeName("Vorname").withKeyType(KeyType.HASH))
//              .withAttributeDefinitions(
//              		new AttributeDefinition().withAttributeName("Vorname").withAttributeType(ScalarAttributeType.S))
//              .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));
//
//          // Create table if it does not exist yet
//          TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
//          // wait for the table to move into ACTIVE state
//          TableUtils.waitUntilActive(dynamoDB, tableName);
//          
////          createTable();
//
//          // Describe our new table
//          DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
//          TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
//          System.out.println("Table Description: " + tableDescription);
//
//          // Add an item (newitem erstellt die hashmap)
//          Map<String, AttributeValue> item = newUser(25,"martin","wieser", "RS", "11101991");
//          PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
//          PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
//          
//          System.out.println("Result: " + putItemResult);
////
////          // Add another item (newitem erstellt die hashmap
////          
////          item = newUser("Christopher","Wieland", "SBG", "61655166");
////        
////          putItemRequest = new PutItemRequest(tableName, item);
////          putItemResult = dynamoDB.putItem(putItemRequest);
////          System.out.println("Result: " + putItemResult);
////          System.out.println("Result: " + putItemRequest);
//
//          
//          
//          /*    Query     */
//          /*
//          // Scan items for movies with a year attribute greater than 1985
//          HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//          Condition condition = new Condition()
//              .withComparisonOperator(ComparisonOperator.GT.toString())
//              .withAttributeValueList(new AttributeValue().withN("6"));
//          scanFilter.put("ID", condition);
//          ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
//          ScanResult scanResult = dynamoDB.scan(scanRequest);
//          System.out.println("Result (Was habe ich gefunden): " + scanResult);
//          
//          System.out.println(scanResult.getItems().get(0).toString());
//          System.out.println(scanResult.getItems().get(2).toString());
//          System.out.println(scanResult.getItems().get(1).toString());
//          */
//
//      } catch (AmazonServiceException ase) {
//          System.out.println("Caught an AmazonServiceException, which means your request made it "
//                  + "to AWS, but was rejected with an error response for some reason.");
//          System.out.println("Error Message:    " + ase.getMessage());
//          System.out.println("HTTP Status Code: " + ase.getStatusCode());
//          System.out.println("AWS Error Code:   " + ase.getErrorCode());
//          System.out.println("Error Type:       " + ase.getErrorType());
//          System.out.println("Request ID:       " + ase.getRequestId());
//      } catch (AmazonClientException ace) {
//          System.out.println("Caught an AmazonClientException, which means the client encountered "
//                  + "a serious internal problem while trying to communicate with AWS, "
//                  + "such as not being able to access the network.");
//          System.out.println("Error Message: " + ace.getMessage());
//      }
    	
    	
    	 /*
        private static void ListMyTables()
        {
        	System.out.println("\n*** listing tables ***");
            string lastTableNameEvaluated = null;
            do
            {
                var request = new ListTablesRequest
                {
                    Limit = 2,
                    ExclusiveStartTableName = lastTableNameEvaluated
                };

                var response = client.ListTables(request);
                foreach (string name in response.TableNames)
                    Console.WriteLine(name);

                lastTableNameEvaluated = response.LastEvaluatedTableName;

            } while (lastTableNameEvaluated != null);
        }

        private static void GetTableInformation()
        {
        	System.out.println("\n*** Retrieving table information ***");
            var request = new DescribeTableRequest
            {
                TableName = tableName
            };

            var response = client.DescribeTable(request);

            TableDescription description = response.Table;
            Console.WriteLine("Name: {0}", description.TableName);
            Console.WriteLine("# of items: {0}", description.ItemCount);
            Console.WriteLine("Provision Throughput (reads/sec): {0}",
                             description.ProvisionedThroughput.ReadCapacityUnits);
            Console.WriteLine("Provision Throughput (writes/sec): {0}",
                             description.ProvisionedThroughput.WriteCapacityUnits);

        }

        private static void UpdateExampleTable()
        {
        	System.out.println("\n*** Updating table ***");
            var request = new UpdateTableRequest()
            {
                TableName = tableName,
                ProvisionedThroughput = new ProvisionedThroughput()
                {
                    ReadCapacityUnits = 6,
                    WriteCapacityUnits = 7
                }
            };

            var response = client.UpdateTable(request);

            WaitUntilTableReady(tableName);
        }

        private static void DeleteExampleTable()
        {
        	System.out.println("\n*** Deleting table ***");
            var request = new DeleteTableRequest
            {
                TableName = tableName
            };

            var response = client.DeleteTable(request);

            System.out.println("Table is being deleted...");
        }

        private static void WaitUntilTableReady(string tableName)
        {
            string status = null;
            // Let us wait until table is created. Call DescribeTable.
            do
            {
                System.Threading.Thread.Sleep(5000); // Wait 5 seconds.
                try
                {
                    var res = client.DescribeTable(new DescribeTableRequest
                    {
                        TableName = tableName
                    });

                    Console.WriteLine("Table name: {0}, status: {1}",
                                   res.Table.TableName,
                                   res.Table.TableStatus);
                    status = res.Table.TableStatus;
                }
                catch (ResourceNotFoundException)
                {
                    // DescribeTable is eventually consistent. So you might
                    // get resource not found. So we handle the potential exception.
                }
            } while (status != "ACTIVE");
        }
    */
        
//        private static void deleteTable(String tableName) {
//            Table table = dynamoDB.getTable(tableName);
//            try {
//                System.out.println("Issuing DeleteTable request for " + tableName);
//                table.delete();
//                System.out.println("Waiting for " + tableName
//                    + " to be deleted...this may take a while...");
//                table.waitForDelete();
    //
//            } catch (Exception e) {
//                System.err.println("DeleteTable request failed for " + tableName);
//                System.err.println(e.getMessage());
//            }
//        }

//        private static void createTable(
//            String tableName, long readCapacityUnits, long writeCapacityUnits, 
//            String partitionKeyName, String partitionKeyType) {
    //
//            createTable(tableName, readCapacityUnits, writeCapacityUnits,
//                partitionKeyName, partitionKeyType, null, null);
//        }

//        private static void createTable(
//            String tableName, long readCapacityUnits, long writeCapacityUnits, 
//            String partitionKeyName, String partitionKeyType, 
//            String sortKeyName, String sortKeyType) {
    //
//            try {
    //
//                ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
//                keySchema.add(new KeySchemaElement()
//                    .withAttributeName(partitionKeyName)
//                    .withKeyType(KeyType.HASH)); //Partition key
//                
//                ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
//                attributeDefinitions.add(new AttributeDefinition()
//                    .withAttributeName(partitionKeyName)
//                    .withAttributeType(partitionKeyType));
    //
//                if (sortKeyName != null) {
//                    keySchema.add(new KeySchemaElement()
//                        .withAttributeName(sortKeyName)
//                        .withKeyType(KeyType.RANGE)); //Sort key
//                    attributeDefinitions.add(new AttributeDefinition()
//                        .withAttributeName(sortKeyName)
//                        .withAttributeType(sortKeyType));
//                }
    //
//                CreateTableRequest request = new CreateTableRequest()
//                        .withTableName(tableName)
//                        .withKeySchema(keySchema)
//                        .withProvisionedThroughput( new ProvisionedThroughput()
//                            .withReadCapacityUnits(readCapacityUnits)
//                            .withWriteCapacityUnits(writeCapacityUnits));
    //
//                // If this is the Reply table, define a local secondary index
//                if (replyTableName.equals(tableName)) {
//                    
//                    attributeDefinitions.add(new AttributeDefinition()
//                        .withAttributeName("PostedBy")
//                        .withAttributeType("S"));
    //
//                    ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
//                    localSecondaryIndexes.add(new LocalSecondaryIndex()
//                        .withIndexName("PostedBy-Index")
//                        .withKeySchema(
//                            new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH),  //Partition key
//                            new KeySchemaElement() .withAttributeName("PostedBy") .withKeyType(KeyType.RANGE))  //Sort key
//                        .withProjection(new Projection() .withProjectionType(ProjectionType.KEYS_ONLY)));
    //
//                    request.setLocalSecondaryIndexes(localSecondaryIndexes);
//                }
    //
//                request.setAttributeDefinitions(attributeDefinitions);
    //
//                System.out.println("Issuing CreateTable request for " + tableName);
//                Table table = dynamoDB.createTable(request);
//                System.out.println("Waiting for " + tableName
//                    + " to be created...this may take a while...");
//                table.waitForActive();
    //
//            } catch (Exception e) {
//                System.err.println("CreateTable request failed for " + tableName);
//                System.err.println(e.getMessage());
//            }
//        }
    }
    
   
}
