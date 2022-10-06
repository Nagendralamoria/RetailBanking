import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { exit } from 'process';
import { AuthService } from '../authenticate/auth.service';
import { EmpService } from './emp.service';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {
  depositmsg: string;
  servicemsg: string;
  deletemsg: string;
  viewCustomer: string;
  Accounterror: string;
  xyz=[];

  constructor(private empService: EmpService,
    private router: Router,
    private authService:AuthService) { }

  ngOnInit() {
  }

  createAccount(id){
    let val=id.value;
    console.log(id);
    this.empService.getDetails(val).subscribe((out)=>{
      this.router.navigate(['/createAccount',val]);
    },error=>{
      this.Accounterror="Invalid CustomerID";
      id.resetForm();
    })
  }

  onDisplay(id){
    let val=id.value;
    this.empService.viewCustomer(val).subscribe((out)=>{
      console.log(out);
      this.router.navigate(['/viewCustomer',val]);
    },error=>{
      this.viewCustomer="Customer UserId DOES NOT EXISTS";
    })

  }
 
  onDelete(id){
    console.log(id);
    this.empService.onDelete(id.value).subscribe((out)=>{
      console.log(out);
      this.deletemsg="Deleted Successfully";
    },error=>{
      this.deletemsg="Customer UserId DOES NOT EXISTS";
    });
  }

  onDeposit(f){
    this.empService.onDeposit(f.value)
    .subscribe((val)=>{
      console.log(val);
      this.depositmsg="Deposit Successfully Done..";
      f.resetForm();
    },error=>{
      this.depositmsg="Invalid Account Id..";
    })
  }

  onServiceCharge(){
    this.empService.onServiceCharge().subscribe((val)=>{
      this.xyz.push(val);
      var n = this.xyz[0].length;
      var count=0;
      for(var i=0;i<n;i++){
      if(val[i].currentBalance <2000){
       
        count++;
        exit;
       
      }
    }if(count!=0){
      this.servicemsg="Service Charge Deducted";
    }else{
      this.servicemsg="No Low Amount";
    }

    },error=>{
      this.servicemsg="Error in Service Charge Deduction";
      console.log(error);
    })
  }

}
