import { Component, OnInit } from '@angular/core';

export interface Product {
   id: string|number,
   designation: string,
   price : number
   isAvailable?:boolean;
}

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  products:Array<Product>=[];

  constructor() { }

  ngOnInit(): void {
    this.products=[
      {id:1,designation:"Samsung Galaxy S9", price:5000},
      {id:2,designation:"HP Note Book", price:7450},
      {id:3,designation:"Macbook Pro 2015", price:95000},
      {id:4,designation:"Huawei Y9 Prime", price:4490}
    ]
  }

}
