/***********************************************************************************************************************
 * Language: Angular
 * Class: SurveyHomeComponent
 * Description: This component is at the presentation layer of the application and makes use of CustomerListService to
 *              get the Customer and Survey details and stores into local instance that can be comsumed by the HTML      *              component to be used in user interface.
 ***********************************************************************************************************************/

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

import { CustomerListService } from '../services/customer-list.service';

@Component({
  selector: 'app-survey-home',
  templateUrl: './survey-home.component.html',
  styleUrls: ['./survey-home.component.scss']
})
export class SurveyHomeComponent implements OnInit {  
  
  customers = []; //object to contain the customers list from the API
  username: string; //to store the username from the login form

  constructor(private customerListService: CustomerListService, private route: ActivatedRoute, private router: Router) { }

  //Angular life cycle hook Interface method, used for initiation, called after constructor
  ngOnInit() {

    // Access the request parameter 'username'.
    this.route.paramMap.subscribe(
      (params) => {
        this.username = params.get('username');
    });

    //Get the list of customers to display in the left navgation using the service CustomerListService
    this.customerListService.getCustomerList().subscribe(
      customers => {
        this.customers = customers;
      },
      error => console.log(error)
    )
  }

  //It clears the value of local instance "username" used by child components and redirects to login page
  signOut(){
    //Clear the username for the logged in user from the component variable.
    this.username = "";
    this.router.navigateByUrl('login');
  }

}
