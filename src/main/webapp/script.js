document.addEventListener("DOMContentLoaded", ()=>{
    populateManufacturers();
    populateChart();
    genericCollapse("manufacturers", "manufacturer-header", "man");
    genericCollapse("customers", "customer-header", "cus");
    genericCollapse("products", "product-header", "prod")
    genericCollapse("sales", "sales-header", "sle")
    genericCollapse("addresses", "adress-header", "adr")
    manufacturerModal()
    editManufacturer()



    expandHeader();
})



function expandHeader() {
    document.querySelectorAll('.collapsible-header').forEach(header => {
        header.addEventListener('click', () => {
            const content = header.nextElementSibling;
            content.style.display = content.style.display === 'none' ? 'block' : 'none';
        });
    });


    document.querySelectorAll('.collapsible-header').forEach(header => {
        header.addEventListener('click', () => {
            const content = header.nextElementSibling;
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
            }
        });
    });
}

function populateManufacturers(){
    const manufacturersData = [
        { id: 1, name: "Samsung", country: "Country 1", website: "https://example.com/manufacturer1" },
        { id: 2, name: "Apple", country: "Country 2", website: "https://example.com/manufacturer2" },
        { id: 69, name: "Acer", country: "Country 3", website: "https://example.com/manufacturer3" }
    ];


        const tableBody = document.querySelector("#manufacturersTable tbody");

        manufacturersData.forEach(manufacturer => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>
                    <i class="fas fa-pencil-alt edit-manufacturer" style="display: block;" data-id="${manufacturer.id}"></i>
                </td>
                <td>${manufacturer.name}</td>
                <td>${manufacturer.country}</td>
            `;
            tableBody.appendChild(row);
        });

        searchManufacturer();
            

}

function collapseManufacturer() {

    

    const manufacturers = document.getElementById("manufacturers");
    const manufacturerHeader = document.getElementById("manufacturer-header");

    manufacturerHeader.addEventListener("click", () => {

    console.log(manufacturers.style.display)

    // Check if manufacturers is initially hidden
    if (manufacturers.style.display === "none" || manufacturers.style.display === null) {
        // If hidden, show it when manufacturer header is clicked
            manufacturers.style.display = "block"; // Set display to "block" to make it visible
        
    } else {
        // If visible, hide it when manufacturer header is clicked
            manufacturers.style.display = "none"; // Set display to "none" to hide it

    }
})
}

function genericCollapse(content, header, icon){
    const contentElement = document.getElementById(content);
    const headerElement = document.getElementById(header);
    const iconElement = document.getElementsByClassName(icon)[0];



    console.log(contentElement.style.display);

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







function searchManufacturer() {
    const searchInput = document.getElementById('searchInput');
    const table = document.getElementById('manufacturersTable');
    const rows = table.getElementsByTagName('tr');


    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();

        Array.from(rows).forEach(row => {
            const tds = row.getElementsByTagName('td');

            // Check if row has enough td elements
            if (tds.length >= 3) {
                const manufacturerName = tds[0].textContent.toLowerCase();
                const manufacturerCountry = tds[1].textContent.toLowerCase();
                const manufacturerWebsite = tds[2].textContent.toLowerCase();

                if (manufacturerName.includes(searchTerm) || manufacturerCountry.includes(searchTerm) || manufacturerWebsite.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        });
    });
}

function populateChart(){
              
    const ordersData = [{ week: "Week 1", orders: 2000 }];

    const ctx = document.getElementById('ordersChart').getContext('2d');
                const chart = new Chart(ctx, {
                    type: 'pie', // Change this line
                    data: {
                        labels: ordersData.map(data => data.week),
                        datasets: [{
                            label: 'Orders',
                            data: ordersData.map(data => data.orders),
                            backgroundColor: [
                                'rgba(0, 99, 132, 0.2)',
                                'rgba(54, 162, 235, 0.2)',
                                'rgba(255, 206, 86, 0.2)',
                                'rgba(75, 192, 192, 0.2)'
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)'
                            ],
                            borderWidth: 1
                        }]
                    },
                });

        
}


function manufacturerModal(){
    const modal = document.getElementById("addModal");
    const idInput = modal.querySelector("#manufacturerId");

    // Get the button that opens the modal
    const addButton = document.getElementById("addManufacturer")

    // Get the <span> element that closes the modal
    const closeButton = document.querySelector(".close");

    // When the user clicks the button, open the modal
    addButton.addEventListener("click", () => {
      modal.style.display = "block";

      //show add button and hide update and delete
      document.getElementById("add-manufacturer").style.display="block"
      document.getElementById("update-manufacturer").style.display="none";
      document.getElementById("delete-manufacturer").style.display="none";
    });

    // When the user clicks on <span> (x), close the modal
    closeButton.addEventListener("click", () => {
      modal.style.display = "none";
      clearValues();
     
    });

    

    // When the user clicks anywhere outside of the modal, close it
    window.addEventListener("click", (event) => {
      if (event.target === modal) {
        modal.style.display = "none";
        clearValues();
      }
    });

    // Handle form submission to add a new manufacturer
    document.getElementById("addManufacturerForm").addEventListener("submit", (event) => {
      event.preventDefault();
      // Add your logic to handle form submission and add a new manufacturer here
      // For example, you can retrieve the form data and perform an AJAX request
      // to add the manufacturer to your database
      // After successful addition, you can close the modal
      modal.style.display = "none";
    });
}


function editManufacturer(){
    const editButtons = document.querySelectorAll(".edit-manufacturer");

    editButtons.forEach(button => {
        button.addEventListener("click", (event) => {
            const manufacturerId = event.target.dataset.id;
            const modal = document.getElementById("addModal");
            modal.style.display = "block";

            //show update and delete but hide add button
            document.getElementById("update-manufacturer").style.display="block"
            document.getElementById("delete-manufacturer").style.display="block"
            document.getElementById("add-manufacturer").style.display="none"
            retrieveData(manufacturerId)

            const idInput = modal.querySelector("#manufacturerId");
            idInput.value = manufacturerId;
        });
    });
   

    

}



function retrieveData(id){

    const modal = document.getElementById("addModal");
    const idInput = modal.querySelector("#manufacturerId");
    const name =   modal.querySelector("#name");
    const country = modal.querySelector("country")
    console.log(idInput)
    if(id === "1"){
        name.value = "testing123"
        console.log("testing123");
    }else if (id === "2") {
        name.value = "testing124"
    } else {
        
    }
}

function clearValues(){
    const modal = document.getElementById("addModal");
    const idInput = modal.querySelector("#manufacturerId");
    const name =   modal.querySelector("#name");
    const country = modal.querySelector("#country")

    document.getElementById("add-manufacturer").style.display="block"
    document.getElementById("update-manufacturer").style.display="block"
    document.getElementById("delete-manufacturer").style.display="block"


    idInput.value = null;
    name.value = null;
    country.value = null;
}