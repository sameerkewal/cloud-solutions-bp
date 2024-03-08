async function getAllSales(){
    try {
        // Make a GET request using fetch
        const response = await fetch('http://localhost:8080/cloud_solutions_bp_war_exploded/api/sale/get-all-sales');

        // Check if the response status is OK (status code 200)
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        // Parse the response JSON
        // Do something with the data
        return await response.json()
    } catch (error) {
        // Handle errors
        console.log('error: ', error)
    }

}


async function addSale(newSale){
    const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/sale`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newSale),
    });


    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(JSON.stringify({
            code: response.status,
            message: errorResponse.error
        }));
    }

    return await response.json();
}

async function getAllCustomers(){

        // Make a GET request using fetch
        const response = await fetch('http://localhost:8080/cloud_solutions_bp_war_exploded/api/customer/get-all-customers');

        // Check if the response status is OK (status code 200)
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        // Parse the response JSON
        // Do something with the data
        return await response.json()

}


async function getAllProducts(){
    const response = await fetch('http://localhost:8080/cloud_solutions_bp_war_exploded/api/product/get-all-products');

    // Check if the response status is OK (status code 200)
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    // Parse the response JSON
    // Do something with the data
    return await response.json()
}

async function getProduct(id){
    const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/product/get-product?id=${id}`);

    // Check if the response status is OK (status code 200)
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    // Parse the response JSON
    // Do something with the data
    return await response.json()
}








