import React, { useState } from 'react';
import Nav from './Nav';
import { useNavigate } from "react-router-dom";
import './../Styles/Register.css'; // Import CSS file

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

            // Check the response message
            if (responseData === 'Register successfully') {
                const confirmed = window.confirm('Registration successful. Proceed to My Stores page?');
                if (confirmed) {
                    navigate('/mystores');
                }
                // Registration was successful
                // Handle success
            } else {
                // Registration failed
                // Handle failure
            }

        } catch (error) {
            console.error('Error:', error.message);
            // Handle error
        }
    };

    return (
        <div>
            <Nav />
<div className="register-container">
            
            <h2>Register</h2>
            <form onSubmit={handleSubmit} className="register-form">
                <label>
                    First Name:
                    <input type="text" name="firstname" value={formData.firstname} onChange={handleChange} />
                </label>
                <label>
                    Last Name:
                    <input type="text" name="lastname" value={formData.lastname} onChange={handleChange} />
                </label>
                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} />
                </label>
                <label>
                    Mobile:
                    <input type="text" name="mobile" value={formData.mobile} onChange={handleChange} />
                </label>
                <label>
                    Password:
                    <input type="password" name="password" value={formData.password} onChange={handleChange} />
                </label>
                <button type="submit">Submit</button>
            </form>
        </div>
        </div>
        
    );
}

export default Register;
