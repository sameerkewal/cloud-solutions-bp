async function getAllManufacturers() {
    try {
        // Make a GET request using fetch
        const response = await fetch('http://localhost:8080/cloud_solutions_bp_war_exploded/api/manufacturer/get-all-manufacturers');

        // Check if the response status is OK (status code 200)
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        // Parse the response JSON
        // Do something with the data
        const manufacturers = await response.json();
        return manufacturers
    } catch (error) {
        // Handle errors
        console.log('error: ', error)
    }
}

    async function getManufacturer(id) {

        try {
            // Make a GET request using fetch
            const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/manufacturer/get-manufacturer?id=${id}`);

            // Check if the response status is OK (status code 200)
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            // Parse the response JSON
            // Do something with the data
            const manufacturers = await response.json();
            return manufacturers
        } catch (error) {
            // Handle errors
            console.error('Error:', error.message);
        }
    }


    async function deleteManufacturer(manufacturerIdToDelete) {


        // Make a GET request using fetch
        const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/manufacturer/${manufacturerIdToDelete}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                // Add any additional headers as needed
            },
        })

        // Check if the response status is OK (status code 200)
        if (!response.ok) {
            const errorResponse = await response.json();// Log the error message from the server
            throw new Error(JSON.stringify({
                code: response.status,
                message: errorResponse.error
            }));
        }

        // Parse the response JSON
        // Do something with the data
        return await response.json()

    }

    async function updateManufacturer(manufacturerId, updatedManufacturerData) {

        const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/manufacturer/${manufacturerId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedManufacturerData),
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

    async function addManufacturer(newManufacturer) {
        const response = await fetch(`http://localhost:8080/cloud_solutions_bp_war_exploded/api/manufacturer`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newManufacturer),
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





