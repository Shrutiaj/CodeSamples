/************************************************************************************************************************
 * Language: Java
 * Framework: Spring Boot
 * Class: CustomerService
 * Description: This class is at the service layer of the application and loads JDBC driver to connect to the MySQL                     database and fetch the list of customers and their corresponding surveys. This code exposes the REST API                to be consumed at the presentation layer of the front-end application. It structures the data in the                    JSON format and provides it to the front-end application.
 ***********************************************************************************************************************/

package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


@Service
@SuppressWarnings("unchecked")
public class CustomerService {
	JSONArray customersList;
	int prevCustomer_id;
	
	public CustomerService() {
	}
	
	//method to return the list of customers
	public JSONArray getCustomers(){
		customersList = new JSONArray();
		prepareCustomerSurveyData();
		return customersList;
	}
	
	private void prepareCustomerSurveyData() {
		try{ 
			//Load JDBC driver for MySQL database
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			
			//Create connection object with the driver, server hosting the database and username and password to connect to database.
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/SurveyApp","root","password");
			
			//Create statement object to execute query on and get data from database
			Statement stmt=con.createStatement();
			
			//Execute query and store fetched data to the ResultSet object
			ResultSet customerSurveyList=stmt.executeQuery("SELECT Customers.customer_id, Customers.customer_name, Surveys.survey_name, Surveys.survey_id FROM Customers LEFT JOIN Surveys ON Customers.customer_id = Surveys.customer_id ORDER BY Customers.customer_id"); 
			
			//Using the simple json google plugin to format data in JSON format 
			JSONArray surveysList = new JSONArray();
			JSONObject customer = new JSONObject();
			JSONObject surveys = new JSONObject();
			
			//Iterate over the ResultSet to fetch data and add to the JSON objects created above.
			while(customerSurveyList.next()) {
				
				//Check if it is the first result set entry and add to customers array 
				if(customerSurveyList.isFirst()) {
					customer.put("customer_id",customerSurveyList.getInt("customer_id"));
					customer.put("customer_name",customerSurveyList.getString("customer_name"));
					prevCustomer_id = customerSurveyList.getInt("customer_id");
				}
				
				//check if customer has surveys
				if(customerSurveyList.getString("survey_name")!= null) {
					//Check if it is same customer, if yes add to its survey list
					if(prevCustomer_id == customerSurveyList.getInt("customer_id")){
						surveys = new JSONObject();
						surveys.put("survey_name",customerSurveyList.getString("survey_name"));
						surveys.put("survey_id",customerSurveyList.getString("survey_id"));
						surveysList.add(surveys);
					}
					//if not add survey-list to customer and customer to the customer-list
					else {
						if(surveysList.size() > 0) {
							customer.put("surveys", surveysList);
							customersList.add(customer);
						}
						
						customer = new JSONObject(); //create new customer object
						
						//add new customer data to the newly created object
						customer.put("customer_id",customerSurveyList.getInt("customer_id"));
						customer.put("customer_name",customerSurveyList.getString("customer_name"));
						
						//clear survey objects to enter current customer's surveys
						surveysList = new JSONArray();
						surveys = new JSONObject(); 						
						surveys.put("survey_name",customerSurveyList.getString("survey_name"));
						surveys.put("survey_id",customerSurveyList.getString("survey_id")); //add survey to the new survey array object
						surveysList.add(surveys);
						
						//change the previous customer id to current customer
						prevCustomer_id = customerSurveyList.getInt("customer_id");
					}
				}
				//check if customer doesn't have surveys
				else {
					//add earlier survey-list to customer and customer to the customer-list
					if(surveysList.size() > 0) {
						customer.put("surveys", surveysList);
					}
					//Add previous customer to the customer-list
					customersList.add(customer);
					
					//Clear all the JSON objects to enter current customer into customer-list
					surveysList = new JSONArray();
					surveys = new JSONObject();
					customer = new JSONObject();
					
					//add current customer to the customer-list
					customer.put("customer_id",customerSurveyList.getInt("customer_id"));
					customer.put("customer_name",customerSurveyList.getString("customer_name"));
					customersList.add(customer);
					
					//clear current customer data to add next customer's data
					customer = new JSONObject();
				}
			}
			if(surveysList.size() > 0) {
				customer.put("surveys", surveysList);
			}
			customersList.add(customer);
			con.close();
		}
		catch(Exception e){ 
			System.out.println(e);
		}
	}
}