async function getAllAddresses(){
    const response = await fetch('http://localhost:8080/cloud_solutions_bp_war_exploded/api/adress/get-all-addresses');

    // Check if the response status is OK (status code 200)
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    // Parse the response JSON
    // Do something with the data
    return await response.json()
}

