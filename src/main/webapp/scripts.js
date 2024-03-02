document.addEventListener("DOMContentLoaded", ()=>{
    populateManufacturers()
    manufacturerModal()
    populateChart();

    genericCollapse("manufacturers", "manufacturer-header", "man");
    genericCollapse("customers", "customer-header", "cus");
    genericCollapse("products", "product-header", "prod")
    genericCollapse("sales", "sales-header", "sle")
    genericCollapse("addresses", "adress-header", "adr")

    // handleDeleteButtonClick();



})




function populateManufacturers(){


    getAllManufacturers().then(data=>{
        const tableBody = document.querySelector("#manufacturersTable tbody");

        data.forEach(manufacturer => {
            const row = document.createElement("tr");
            const editColumn = document.createElement("td");
            const editIcon = document.createElement("i");
            editIcon.className = "fas fa-pencil-alt edit-manufacturer";
            editIcon.style.display = "block";
            editIcon.dataset.id = manufacturer.id;

            // Attach the event listener to the edit icon
            editIcon.addEventListener("click", (event) => {
                const clickedManufacturerId = event.target.dataset.id;
                // Your logic for handling the click event
                editManufacturer(manufacturer.id)
            });

            // Append the edit icon to the edit column
            editColumn.appendChild(editIcon);

            // Create the other columns with manufacturer details
            const nameColumn = document.createElement("td");
            nameColumn.textContent = manufacturer.name;

            const countryColumn = document.createElement("td");
            countryColumn.textContent = manufacturer.country;

            // Append all columns to the row
            row.appendChild(editColumn);
            row.appendChild(nameColumn);
            row.appendChild(countryColumn);
            tableBody.appendChild(row);



        })




        searchManufacturer();
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

    handleButtonActions();

}



function handleButtonActions(){
    const modal = document.getElementById("addModal");
    document.getElementById("addManufacturerForm").addEventListener("submit", (event) => {

        event.preventDefault()

        const clickedButton = event.submitter;
        const action = clickedButton.dataset.action;

        switch (action) {
            case 'add':
                // handleAddManufacturer();
                break;
            case 'update':
                handleUpdateManufacturer();
                break;
            case 'delete':
                handleDeleteButtonManufacturer();
                break;
        }


        // modal.style.display = "none";
    });
}


function editManufacturer(manufacturerId){

    const modal = document.getElementById("addModal");
    modal.style.display = "block";

    //show update and delete but hide add button
    document.getElementById("update-manufacturer").style.display="block"
    document.getElementById("delete-manufacturer").style.display="block"
    document.getElementById("add-manufacturer").style.display="none"
    retrieveData(manufacturerId)

    const idInput = modal.querySelector("#manufacturerId");
    idInput.value = manufacturerId;

}



function retrieveData(id){

    const modal = document.getElementById("addModal");
    const idInput = modal.querySelector("#manufacturerId");
    const name =   modal.querySelector("#manufacturer-name");
    const country = modal.querySelector("#country")
    getManufacturer(id).then((data)=>{
        name.value = data.name
        country.value = data.country
    })
}

function clearValues(){
    const modal = document.getElementById("addModal");
    const idInput = modal.querySelector("#manufacturerId");
    const name =   modal.querySelector("#manufacturer-name");
    const country = modal.querySelector("#country")

    document.getElementById("add-manufacturer").style.display="block"
    document.getElementById("update-manufacturer").style.display="block"
    document.getElementById("delete-manufacturer").style.display="block"


    idInput.value = null;
    name.value = null;
    country.value = null;
}


async function handleDeleteButtonManufacturer() {

    const manufacturerId = document.getElementById("manufacturerId").value;


    try {
        // Call the deleteManufacturer function
        const deletedManufacturer = await deleteManufacturer(manufacturerId);
        console.log(deletedManufacturer);

        // Show a success message or perform additional actions on success
        showMessage('Manufacturer deleted successfully!', 'success');
    } catch (error) {
        // Handle errors
        console.error('Error:', error.message);

        // Show the error message to the user
        showMessage(error, 'failed');

    }
}
async function handleUpdateManufacturer() {
    const manufacturerId = document.getElementById("manufacturerId").value;
    const updatedName = document.getElementById("manufacturer-name")
    const updatedCountry = document.getElementById("country");

    const updatedData = {
        name: updatedName,
        country: updatedCountry
    };
    try {
        // Call the updateManufacturer function
        const updatedManufacturer = await updateManufacturer(manufacturerId, updatedData);

        // Show a success message or perform additional actions on success
        showMessage('Manufacturer updated successfully!', 'success');
    } catch (error) {
        // Handle errors
        console.error('Error updating manufacturer:', error.message);

        // Show the error message to the user
        showMessage(error, 'failed');
    }

}


function closeMessageModal(){
    document.getElementById('messageModal').style.display = 'none';
    // window.location.href="http://localhost:63342/html/index.html"
    location.reload()
}

function showMessage(message, type) {
    const messageModal = document.getElementById('messageModal');
    const messageText = document.getElementById('messageText');

    messageText.textContent = message;
    messageModal.style.backgroundColor = type === 'success' ? '#4CAF50' : '#F44336';
    messageModal.style.display = 'block';

    if(type === "success") {
        setTimeout(() => {
            closeMessageModal();
        }, 5000);

    }
}