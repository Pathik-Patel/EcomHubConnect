import ApiBoilerPlateService from "./ApiBoilerPlateService";

const UserService = {

    login: async (credentials) => {

        // return "successful";

        const response = await fetch('http://localhost:8080/login', {
            method: "POST",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        });
        console.log(response);
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('User not found');
            } else {
                throw new Error('Network response was not ok');
            }


        }
        try {
            const responseData = await response.json();
            sessionStorage.setItem('sessionId', responseData.sessionid);
            sessionStorage.setItem('loggedinUserEmail', responseData.username);
            sessionStorage.setItem('loggedinUserFirstName', responseData.firstname);
            
            console.log("Login Successful");
        }
        catch {
            return response;
        }
    },

    LogoutUser: async () => {
        const sessionId = sessionStorage.getItem('sessionId');
        const response = await fetch('http://localhost:8080/logout', {
            method: "GET",
            mode: 'cors',
            headers: {
                'AUTHORIZATION': sessionId
            },
        });
        console.log(response);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        try {
            const responseBody = await response.text();
            if (responseBody === 'Logout successful') {
                
                console.log('Logout successful');
                
                sessionStorage.removeItem('sessionId');
                sessionStorage.removeItem('loggedinUserEmail');
                sessionStorage.removeItem('loggedinUserFirstName');
                
            } else {
                // Handle logout error
                throw new Error('Logout failed');
            }
        } catch (error) {
            // Handle other errors
            throw new Error('Error handling logout response');
        }
    },

    userdetails: async () => {
        const sessionId = sessionStorage.getItem('sessionId');
        const response = await fetch('http://localhost:8080/userdetails', {
            method: "GET",
            mode: 'cors',
            headers: {
                'AUTHORIZATION': sessionId
            },
        });
    
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    
        // Check the content type of the response
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            // Parse JSON response
            try {
                const responseData = await response.json();
                console.log('JSON Response:', responseData);
                // Handle JSON response data here
            } catch (error) {
                throw new Error('Error parsing JSON response');
            }
        } else {
            // Parse text response
            try {
                const responseText = await response.text();
                console.log('Text Response:', responseText);
                // Handle text response data here
            } catch (error) {
                throw new Error('Error parsing text response');
            }
        }
    },


    stores: async () => {
        const sessionId = sessionStorage.getItem('sessionId');
        const response = await fetch('http://localhost:8080/woocommerce/stores', {
            method: "GET",
            mode: 'cors',
            headers: {
                'AUTHORIZATION': sessionId
            },
        });
    
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    
        // Check the content type of the response
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            // Parse JSON response
            try {
                const responseData = await response.json();
                console.log('JSON Response:', responseData);
                return responseData;
                // Handle JSON response data here
            } catch (error) {
                console.error('Error parsing JSON response:', error);
                throw new Error('Error parsing JSON response');
            }
            // try {
            //     const responseText = await response.text();
            //     console.log('Text Response:', responseText);
            //     // Handle text response data here
            // } catch (error) {
            //     throw new Error('Error parsing text response');
            // }   
        } else {
            // Parse text response
            try {
                const responseText = await response.text();
                console.log('Text Response:', responseText);
                // Handle text response data here
            } catch (error) {
                throw new Error('Error parsing text response');
            }
        }
    },

    syncorders: async (storeid) => {
        const sessionId = sessionStorage.getItem('sessionId');
        const response = await fetch(`http://localhost:8080/woocommerce/syncorders/${storeid}`, {
            method: "GET",
            mode: 'cors',
            headers: {
                'AUTHORIZATION': sessionId
            },
        });
    
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    
        // Check the content type of the response
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            // Parse JSON response
            try {
                const responseData = await response.json();
                console.log('JSON Response:', responseData);
                return responseData;
                // Handle JSON response data here
            } catch (error) {
                console.error('Error parsing JSON response:', error);
                throw new Error('Error parsing JSON response');
            }
            // try {
            //     const responseText = await response.text();
            //     console.log('Text Response:', responseText);
            //     // Handle text response data here
            // } catch (error) {
            //     throw new Error('Error parsing text response');
            // }   
        } else {
            // Parse text response
            try {
                const responseText = await response.text();
                console.log('Text Response:', responseText);
                // Handle text response data here
            } catch (error) {
                throw new Error('Error parsing text response');
            }
        }
    }
}

export default UserService;