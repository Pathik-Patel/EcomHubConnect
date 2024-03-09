import ApiBoilerPlateService from "./ApiBoilerPlateService";

const UserService = {

    login: async (credentials) => {

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
            localStorage.setItem('sessionId', responseData.sessionid);
            console.log("Login Successful");
        }
        catch {
            return response;
        }
    },

    LogoutUser: async () => {
        const sessionId = window.localStorage.getItem('sessionId');
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
                // Handle successful logout
                console.log('Logout successful');
                // Clear session ID from local storage or perform any other cleanup
                localStorage.removeItem('sessionId');
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
        const sessionId = window.localStorage.getItem('sessionId');
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
    }
}

export default UserService;