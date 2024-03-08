document.addEventListener("DOMContentLoaded", ()=>{
        populateManufacturers()
        manufacturerModal()
        // populateChart();

       
    
    genericCollapse("manufacturers", "manufacturer-header", "man");
    genericCollapse("customers", "customer-header", "cus");
    genericCollapse("products", "product-header", "prod")
    genericCollapse("sales", "sales-header", "sle")
    genericCollapse("addresses", "adress-header", "adr")

    messageButtonHandler()
    populateSales()
    populateProducts();
    populateCustomers()
    populateAddresses()
})


function genericCollapse(content, header, icon){
    const contentElement = document.getElementById(content);
    const headerElement = document.getElementById(header);
    const iconElement = document.getElementsByClassName(icon)[0];
    
    headerElement.addEventListener("click", () => {
        if (contentElement.style.display === "none" || contentElement.style.display === null) {
            contentElement.style.display = "block";
            iconElement.classList.remove('fa-chevron-up');
            iconElement.classList.add('fa-chevron-down');
        } else {
            contentElement.style.display = "none";
            iconElement.classList.remove('fa-chevron-down');
            iconElement.classList.add('fa-chevron-up');
        }
    })
}






function closeMessageModal(type){
    document.getElementById('messageModal').style.display = 'none';

    if (type === 'success') {
        window.location.href = "index.html";
    }
    
}

function messageButtonHandler(type){
    document.getElementById("message-button").addEventListener("click", ()=>{
        closeMessageModal(type)
    })
    
}


function showMessage(message, type) {
    const messageModal = document.getElementById('messageModal');
    const messageText = document.getElementById('messageText');

    messageText.textContent = message;
    messageModal.style.backgroundColor = type === 'success' ? '#4CAF50' : '#F44336';
    messageModal.style.display = 'block';

    // Automatically close the modal after 5 seconds for success messages
    if (type === "success") {
        messageButtonHandler(type);
        setTimeout(() => {
            window.location.href = "index.html"
        }, 5000);
        }
}




function redirectToSalePage(){
    window.location.href = "register-sale.html"
}


function searchProduct() {
    const searchInput = getMainPageItems().productSearchInput;

    const table = getMainPageItems().productTable;
    const rows = table.getElementsByTagName('tr');

    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();
        
        Array.from(rows).forEach(row => {
            const tds = row.getElementsByTagName('td');

            // Check if row has enough td elements
            if (tds.length >= 4) {
                const productName = tds[0].textContent.toLowerCase();
                const productPrice = tds[1].textContent.toLowerCase();
                const  totalSold = tds[2].textContent.toLowerCase();
                const totalValue = tds[3].textContent.toLowerCase();
                const manufacturer = tds[4].textContent.toLowerCase();

                if (productName.includes(searchTerm) || productPrice.includes(searchTerm) || totalSold.includes(searchTerm)
                    ||totalValue.includes(searchTerm)||manufacturer.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        });
    });
}

function populateProducts() {
    getAllProducts().then(product=>{
        const productTable = getMainPageItems().productTable;

        product.forEach(product=>{
            const row = document.createElement("tr");


            const productName = document.createElement("td")
            const productPrice = document.createElement("td")
            const totalSold = document.createElement("td")
            const totalValue = document.createElement("td")
            const manufacturerInfo = document.createElement("td")

            productName.textContent      = product.name;
            productPrice.textContent     = product.price;
            totalSold.textContent        = product.totalSold;
            totalValue.textContent       = product.totalValue.toLocaleString();
            manufacturerInfo.textContent = product.manufacturerName + " - " + product.manufacturerCountry


            row.appendChild(productName);
            row.appendChild(productPrice);
            row.appendChild(totalSold);
            row.appendChild(totalValue);
            row.appendChild(manufacturerInfo);

            productTable.appendChild(row);

            searchProduct();

        })
    })
}


function searchCustomer() {
    const searchInput = getMainPageItems().customerSearchInput;

    const table = getMainPageItems().customersTable;
    const rows = table.getElementsByTagName('tr');

    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();
        
        Array.from(rows).forEach(row => {
            const tds = row.getElementsByTagName('td');

            // Check if row has enough td elements
            if (tds.length >= 4) {
                const firstName = tds[0].textContent.toLowerCase();
                const lastName = tds[1].textContent.toLowerCase();
                const phoneNumber = tds[2].textContent.toLowerCase();
                const adressInfo = tds[3].textContent.toLowerCase();
                const totalSpent = tds[4].textContent.toLowerCase();

                if (firstName.includes(searchTerm) || lastName.includes(searchTerm) || phoneNumber.includes(searchTerm)
                ||adressInfo.includes(searchTerm)||totalSpent.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        });
    });
}

function populateCustomers() {
    getAllCustomers().then(customer=>{
        const customerTable = getMainPageItems().customersTable;
        
        
        customer.forEach(customer=>{
            const row = document.createElement("tr");
            
            const customerFirstName = document.createElement("td");
            const customerLastName = document.createElement("td");
            const customerPhoneNumber = document.createElement("td");
            const adress = document.createElement("td");
            const totalSpent = document.createElement("td");
            
            
            customerFirstName.textContent = customer.customerFirstName;
            customerLastName.textContent = customer.customerLastName;
            customerPhoneNumber.textContent = customer.customerPhoneNumber;
            adress.textContent = customer.customerStreetName + " " + customer.customerHouseNumber
            totalSpent.textContent = customer.totalSpent.toLocaleString();
            
            
            row.appendChild(customerFirstName)
            row.appendChild(customerLastName)
            row.appendChild(customerPhoneNumber)
            row.appendChild(adress)
            row.appendChild(totalSpent)
            
            customerTable.appendChild(row);
            
            searchCustomer();
        })
    })
}
function searchAdress() {
    const searchInput = getMainPageItems().adressSearchInput;

    const table = getMainPageItems().adressTable;
    const rows = table.getElementsByTagName('tr');

    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();

        Array.from(rows).forEach(row => {
            const tds = row.getElementsByTagName('td');

            // Check if row has enough td elements
            if (tds.length >= 2) {
                const streetName = tds[0].textContent.toLowerCase();
                const houseNumber = tds[1].textContent.toLowerCase();
                const neighborhood = tds[2].textContent.toLowerCase();

                if (streetName.includes(searchTerm) || houseNumber.includes(searchTerm) || neighborhood.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        });
    });
}


function populateAddresses() {
    getAllAddresses().then(adress=>{
        const adressTable = getMainPageItems().adressTable;
        
        adress.forEach(adress=>{
            const row = document.createElement("tr");
            
            const streetName = document.createElement("td")
            const houseNumber = document.createElement("td")
            const neighborhood = document.createElement("td")
            
            
            streetName.textContent = adress.streetname
            houseNumber.textContent = adress.housenumber
            neighborhood.textContent = adress.neighborhood
            
            row.appendChild(streetName)
            row.appendChild(houseNumber)
            row.appendChild(neighborhood)
            
            
            adressTable.appendChild(row);
            searchAdress();
        })
    })
}

function getMainPageItems() {
    return {
        productTable: document.getElementById("product-table"),
        customersTable: document.getElementById("customersTable"),
        adressTable: document.getElementById("adressTable"),
        adressSearchInput: document.getElementById("adress-search-input"),
        customerSearchInput: document.getElementById("customer-search-input"),
        productSearchInput: document.getElementById("product-search-input")
    }

}