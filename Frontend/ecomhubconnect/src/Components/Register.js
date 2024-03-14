import React, { useState } from 'react';
import Nav from './Nav';
import { useNavigate } from "react-router-dom";
function Register() {

    const navigate = useNavigate();


    const [formData, setFormData] = useState({
        firstname: '',
        lastname: '',
        email: '',
        mobile: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        try {
            const response = await fetch('http://localhost:8080/saveUser', {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const responseData = await response.text(); // Assuming response body is text
            // const responseData = 'Register successfully'
            console.log('Response:', responseData);

            // Check the response message
            if (responseData === 'Register successfully') {

                const confirmed = window.confirm('Registration successful. Proceed to home page?');
                if (confirmed) {
                    console.log('Registration successful');
                     navigate('/home');
                }
                // Registration was successful
                
                // Handle success
            } else {
                // Registration failed
                console.log('Registration failed');
                // Handle failure
            }

            // const responseData = await response.json();
            // console.log('Response:', responseData);
            // Handle response data as needed

        } catch (error) {
            console.error('Error:', error.message);
            // Handle error
        }
    };

    return (
       
        <div>
             <Nav/>
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input type="text" name="firstname" value={formData.firstname} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Last Name:
                    <input type="text" name="lastname" value={formData.lastname} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Mobile:
                    <input type="text" name="mobile" value={formData.mobile} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" name="password" value={formData.password} onChange={handleChange} />
                </label>
                <br />
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default Register;
