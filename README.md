# CodeSamples

These files are the part of the project "Survey Application", where upon login user can see and navigate the list of different organizations and their corresponding surveys, take the surveys and submit and update them. This application is developed in Angular and it makes use of the REST API developed using Java/J2EE alongwith Maven and Spring boot framework. The REST API application pulls the code from MySQL server database and provides it to the Angular application.

The application Architecture is as follows:
  * The architecture is built using four layers - UI layer, presentation layer, service layer and the backend layer.
  * Angular app constitiues the UI and presentation layer.
  * The REST APi developed using Java constitiues the service layer
  * MySQL database constitues the backend layer.
  
List of files included:
  * survey-home.component.ts -   This component is at the presentation layer of the application and makes use of                                                CustomerListService to get the Customer and Survey details and stores into local instance                                      that can be comsumed by the HTML
  * survey-home.component.html - This template is at the UI layer of the application and makes use of properties from                                          typescript class SurveyHomeComponent to get the Customer and Survey details stored in local                                    instance.
  * CustomerService.java -       This class is at the service layer of the application and loads JDBC driver to connect to the                                  MySQL database and fetch the list of customers and their corresponding surveys.
  * pom.xml -                    Configuration file for Maven dependencies.
