const UserService = {
    createUser: async (formData) => {
        try {
            const response = await fetch('http://localhost:8080/userLogin', {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log(response);
            // const data = await response.json();
            // return data;
        } catch (error) {
            console.error('Error:', error);
            // Handle error
        }
    }
};

export default UserService;