let buttonClickCounter = 0;
let addedProducts = [];
document.addEventListener("DOMContentLoaded", ()=>{
    if (window.location.pathname.includes("register-sale.html")) {
        populateCustomerSelectList();
        populateProductSelectList(getItems().productNameSelectList);
        setPriceBasedOnProductSelection(getItems().productNameSelectList, null)
        totalPriceCalculation(getItems().productNameSelectList,getItems().productQuantity, null);
        handleAddSaleItemsButton();
        destroySaleItemRow();
        totalAmountCalculation()
        disableProductInOtherSelectLists(getItems().productNameSelectList)
        registerSale();
       
    }
})



function searchSale() {
    const searchInput = getItems().salesSearchInput;

    const table = getItems().salesTable;
    const rows = table.getElementsByTagName('tr');

    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();


        Array.from(rows).forEach(row => {
            const tds = row.getElementsByTagName('td');


            
            // Check if row has enough td elements
            if (tds.length >= 5) {

               
                
                const saleDate         = tds[0].textContent.toLowerCase();
                const customerName     = tds[1].textContent.toLowerCase();
                const totalAmount      = tds[2].textContent.toLowerCase();
                const totalQuantity    = tds[3].textContent.toLowerCase();
                const totalUniqueItems = tds[4].textContent.toLowerCase();
                const items            = tds[5].textContent.toLowerCase();




                if (saleDate.includes(searchTerm) || customerName.includes(searchTerm) || totalAmount.includes(searchTerm)
                ||totalQuantity.includes(searchTerm) || items.includes(searchTerm)||totalUniqueItems.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        });
    });
    
}




async function populateSales(){
    getAllSales().then(sale=>{

        const salesTableBody = getItems().salesTableBody;
        sale.forEach(sale=>{
          
        const row = document.createElement("tr");
        
  

        const saleDate = document.createElement("td");
        const customerName = document.createElement("td");
        const totalItemsInOrder = document.createElement("td");
        const allProductsInOrder = document.createElement("td");
        const uniqueItemsInOrder = document.createElement("td");
        const totalAmountSold = document.createElement("td");
        
        saleDate.textContent=sale.saleDate;
        customerName.textContent=sale.name;
        totalItemsInOrder.textContent=sale.totalItemsInOrder;
        allProductsInOrder.textContent=sale.items;
        uniqueItemsInOrder.textContent=sale.uniqueItemsInOrder;
        totalAmountSold.textContent=sale.totalAmountSold.toLocaleString()

        row.appendChild(saleDate)
        row.appendChild(customerName);
        row.appendChild(totalAmountSold)
        row.appendChild(totalItemsInOrder);
        row.appendChild(uniqueItemsInOrder);
        row.appendChild(allProductsInOrder)
        
        salesTableBody.appendChild(row)
        })
        
        searchSale()
        
    })
    
}

async function populateCustomerSelectList() {
    try {
        const customers = await getAllCustomers();
        customers.forEach((customer)=>{
            const option = document.createElement("option");
            
            option.value = customer.id;
            option.textContent=customer.customerFirstName + " " + customer.customerLastName + " - " + customer.customerStreetName + " " + customer.customerHouseNumber
            
            getItems().customerSelectList.appendChild(option);
        })

    }catch(error){
        console.error('Error: ', error.message)
    }
}

async function populateProductSelectList(productNameSelect){
    try {
        const products = await getAllProducts();
      
            products.forEach((product)=>{

                const option = document.createElement("option");
                option.value = product.id;
                option.textContent=product.name + " - " + product.manufacturerName
                
               productNameSelect.appendChild(option)
            })
        
    }catch(error){
        console.error('Error: ', error.message)
    }
}


function disableProductInOtherSelectLists(selectList) {
    selectList.addEventListener('change', () => {
        const selectedValue = selectList.value;
        
        
        for(let i = 0; i < addedProducts.length; i++){
            if(addedProducts[i].value === selectedValue){
                alert("Product already selected in another list!");
                selectList.value = "-1"; // Reset the value to the default "Select Product"
                addedProducts.splice(i+1, 1);
                return; 
            }
        }

        // Update the tst array with the new selection
        const existingIndex = addedProducts.findIndex(item => item.element === selectList.id);
        if (existingIndex !== -1) {
            // Update existing entry
            addedProducts[existingIndex].value = selectedValue;
        } else {
            // Add new entry
            addedProducts.push({
                element: selectList.id,
                value: selectedValue
            });
        }

        
    });
}


function setPriceBasedOnProductSelection(productNameSelect, sequence){
   productNameSelect.addEventListener("change", async (event) => {
        const selectedValue = event.target.value;

        try {
            const productInfo = await getProduct(selectedValue);
            if(sequence === null) {
                getItems().productPricePerItem.value = productInfo[0].price.toLocaleString()

            }else{
                document.getElementById(`price${sequence}`).value= productInfo[0].price.toLocaleString()
            }
        } catch (error) {
            console.error('error: ', error)
        }
    })
}

function totalPriceCalculation(productNameSelectList,productQuantity, sequence) {
 

    // let productQuantity;
    // if(sequence === null) {
    //     productQuantity = getItems().productQuantity;
    // }else{
    //     productQuantity = document.getElementById(`quantity${sequence}`);
    // }
    
    
    productNameSelectList.addEventListener("change", () => {
        updateTotalPrice(productQuantity, productNameSelectList, sequence);
    });
    productQuantity.addEventListener("change", () => {
        
        updateTotalPrice(productQuantity, productNameSelectList, sequence);
    });


}

async function updateTotalPrice(productQuantity, productNameSelectList, sequence) {
    const quantity = parseFloat(productQuantity.value);
    
    const productInfo =  await getProduct(productNameSelectList.value)

    const pricePerItem = parseFloat(productInfo[0].price)

    

    const totalPrice = quantity * pricePerItem;
    
    //for the first sale item which is shown by default
    if(sequence === null) {
        getItems().productTotalPrice.value = totalPrice.toLocaleString();
    }else{
        document.getElementById(`totalPrice${sequence}`).value = totalPrice.toLocaleString(); 
    }

    if (sequence === null) {
        getItems().productTotalPrice.dispatchEvent(new Event('input', { bubbles: true }));
    } else {
        document.getElementById(`totalPrice${sequence}`).dispatchEvent(new Event('input', { bubbles: true }));
    }
}


function totalAmountCalculation() {
    const totalAmountElement = getItems().totalAmount;

    // Attach input event listener to dynamically added sale items
    getItems().saleItemsDiv.addEventListener("input", () => {
        let totalAmount = 0;

        // Loop through all totalPrice fields and sum them up
        const totalPriceInputs = getItems().allTotalPrices;
        totalPriceInputs.forEach((input) => {
            totalAmount += parseFloat(input.value.replace(/,/g, '')) || 0;
        });

        // Update the total amount wherever you want to display it
        totalAmountElement.value = totalAmount.toLocaleString();
    });
}





function handleAddSaleItemsButton(){
    
    getItems().addSaleItemButton.addEventListener("click", ()=>{
        buttonClickCounter++;
        createSaleItemRow(buttonClickCounter)
        
    })
    
}

function destroySaleItemRow(){
    getItems().removeSaleItemButton.addEventListener("click", ()=>{
        buttonClickCounter--;

        
        if(buttonClickCounter < 0 ){
            alert("You must have atleast one product added to the sale!")
            buttonClickCounter = 0;
            return;
        }
        
 
        
        const sequenceToDelete = getItems().saleItemDivs.length-1;
        
        const saleItemDivToRemove = getItems().saleItemDivs[sequenceToDelete];
        
        getItems().saleItemsDiv.removeChild(saleItemDivToRemove);
    })
}


function createSaleItemRow(sequence) {
    const saleItemsDiv = getItems().saleItemsDiv;

    //Create a new div for the sale item
    const saleItemDiv = document.createElement("div");
    saleItemDiv.className="sale-item"

    //Product Name:
    const productNameLabel = document.createElement("label");
    productNameLabel.textContent="Product Name: ";
    productNameLabel.htmlFor = `productSelectList${sequence}`;
    const productNameSelect = createProductNameSelect(sequence);

    // Quantity:
    const quantityLabel = document.createElement("label");
    quantityLabel.textContent="Quantity: "
    quantityLabel.htmlFor=`quantity${sequence}`
    const quantityInput = createQuantityInput(sequence);
    //
    // Price per Item
    const priceLabel = document.createElement("label");
    priceLabel.htmlFor=`price${sequence}`
    priceLabel.textContent = "Price per item:";
    const priceInput = createPriceInput(sequence);
    //
    // // Total Price
    const totalPriceLabel = document.createElement("label");
    totalPriceLabel.htmlFor=`totalPrice${sequence}`
    totalPriceLabel.textContent = "Total Price:";
    const totalPriceInput = createTotalPriceInput(sequence);
    totalPriceCalculation(productNameSelect,quantityInput, sequence)
  
    
    
    totalAmountCalculation();

    saleItemDiv.appendChild(productNameLabel);
    saleItemDiv.appendChild(productNameSelect);
    saleItemDiv.appendChild(quantityLabel);
    saleItemDiv.appendChild(quantityInput);
    saleItemDiv.appendChild(priceLabel);
    saleItemDiv.appendChild(priceInput);
    saleItemDiv.appendChild(totalPriceLabel);
    saleItemDiv.appendChild(totalPriceInput);

    // Append the sale item div to the sale items section
    saleItemsDiv.appendChild(saleItemDiv);

}

function createProductNameSelect(sequence) {
    const productNameSelect = document.createElement("select");
    productNameSelect.name = `productSelectList${sequence}`;
    productNameSelect.id = `productSelectList${sequence}`;
    productNameSelect.classList.add("productSelectList")
    productNameSelect.required = true;

    // Populate product options (you may fetch these dynamically)
    const productOption = document.createElement("option");
    productOption.value = "-1";
    productOption.textContent = "Select Product";
    
    populateProductSelectList(productNameSelect)
    setPriceBasedOnProductSelection(productNameSelect, sequence)
    disableProductInOtherSelectLists(productNameSelect)
    
    productNameSelect.appendChild(productOption);




    // Add more options as needed...

    return productNameSelect;
}





function createQuantityInput(sequence) {
    const quantityInput = document.createElement("input");
    quantityInput.type = "number";
    quantityInput.required = true;
    quantityInput.className = "quantity small-input";
    quantityInput.name = `quantity${sequence}`;
    quantityInput.id = `quantity${sequence}`;
    
    return quantityInput;
}
function createPriceInput(sequence) {
    const priceInput = document.createElement("input");
    priceInput.type = "text"; // Change to "number" if needed
    priceInput.className = "price small-input";
    priceInput.name = `price${sequence}`;
    priceInput.id = `price${sequence}`;
    priceInput.step = "0.01";
    priceInput.readOnly = true;
    return priceInput;
}
function createTotalPriceInput(sequence) {
    const totalPriceInput = document.createElement("input");
    totalPriceInput.type = "text"; // Change to "number" if needed
    totalPriceInput.className = "totalPrice small-input";
    totalPriceInput.name = `totalPrice${sequence}`;
    totalPriceInput.id = `totalPrice${sequence}`;
    totalPriceInput.step = "0.01";
    totalPriceInput.readOnly = true;
    return totalPriceInput;
}

function buildRegisterJson(){

    const saleProducts = [];

    getItems().saleItemDivs.forEach(element => {
        const productSelectList = element.querySelector('.productSelectList');
        const quantityInput = element.querySelector('.quantity');

        saleProducts.push({
            product: {
                id: productSelectList.value
            },
            quantity: quantityInput.value
        })
    });
    
    let saleJson ={
        "customer":{
            id: getItems().customerSelectList.value
        },
        "saleProducts": saleProducts
    }
    
    return saleJson;
}

function registerSale() {
    getItems().registerSaleButton.addEventListener("click", async (event) => {
        event.preventDefault(); // Prevent the default form submission

        
        const saleJson = buildRegisterJson();
        

        if (!areRequiredFieldsFilledIn()) {
            return;
        }else {

            console.log("Generated JSON:", saleJson);


            try {
                await addSale(saleJson);

                // Show the success message to the user
                showMessage('Sale added successfully!', 'success')

            } catch (error) {
                const errorMessage = JSON.parse(error.message);
                console.log(errorMessage.message);

                // Show the error message to the user
                showMessage(errorMessage.message, 'failed');

            }
        }
    });
    
}

function areRequiredFieldsFilledIn() {
    const customerSelectList = getItems().customerSelectList;
    let isValid = true;

    if (customerSelectList.value === "-1") {
        alert("Please select a customer before registering the sale.");
        isValid = false;
    }

    getItems().saleItemDivs.forEach(element => {
        const productSelectList = element.querySelector('.productSelectList');
        const quantityInput = element.querySelector('.quantity');


        if (productSelectList.value === "-1") {
            alert("Please select a product");
            isValid = false;
        }

        if (quantityInput.value === "") {
            alert("Please select a quantity");
            isValid = false;
        }

        if (parseFloat(quantityInput.value) <= 0) {
            alert("Please select a quantity of 1 or more");
            isValid = false;
        }
    });

    return isValid;
}





//Central place to retrieve page elements
function getItems(){
    return {
        salesSearchInput: document.getElementById("sales-search-input"),
        addSale: document.getElementById("add-sale"),
        salesTable: document.getElementById("sales-table"),
        salesTableBody: document.querySelector("#sales-table tbody"),
        customerSelectList:document.getElementById("customerName"),
        productQuantity: document.getElementById("quantity"),
        productPricePerItem: document.getElementById("price"),
        productTotalPrice: document.getElementById("totalPrice"),
        productNameSelectList: document.getElementById("productSelectList"),
        addSaleItemButton: document.getElementById("addSaleItem"),
        saleItemsDiv: document.getElementById("saleItems"),
        productSelectListItems : document.getElementsByClassName("productSelectList"),
        removeSaleItemButton: document.getElementById("removeSaleItem"),
        saleItemDivs: document.querySelectorAll(".sale-item"),
        totalPricePerProductTimesQuantity: document.getElementsByClassName("totalPrice"),
        totalAmount: document.getElementById("totalAmount"),
        allTotalPrices: document.querySelectorAll(".totalPrice"),
        allProductSelectLists : document.querySelectorAll(".productSelectList"),
        registerSaleButton : document.getElementById("registerSale")
        
    }
}