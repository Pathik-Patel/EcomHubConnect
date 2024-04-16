import React, { useState } from 'react';
import Nav from './Nav';
import './../Styles/storesform.css'; // Import the CSS file

const StoresForm = () => {
  const [formData, setFormData] = useState({
    domain: '',
    consumerKey: '',
    consumerSecret: '',
    storename:''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const sessionId = sessionStorage.getItem('sessionId');
      const response = await fetch('http://localhost:8080/woocommerce/addstore', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'AUTHORIZATION': sessionId    
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        throw new Error('Failed to add store');
      }

      console.log('Store added successfully');
      // Optionally, you can perform any other actions upon successful submission
    } catch (error) {
      console.error('Error:', error.message);
      // Handle errors accordingly
    }
  };

  return (
    <div>
      <Nav/>
      <div className="form-container">
        <form onSubmit={handleSubmit}>
        <div>
            <label htmlFor="domain">Name of Store:</label>
            <input
              type="text"
              id="storename"
              name="storename"
              value={formData.storename}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="domain">Domain:</label>
            <input
              type="text"
              id="domain"
              name="domain"
              value={formData.domain}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="consumerKey">Consumer Key:</label>
            <input
              type="text"
              id="consumerKey"
              name="consumerKey"
              value={formData.consumerKey}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="consumerSecret">Consumer Secret:</label>
            <input
              type="text"
              id="consumerSecret"
              name="consumerSecret"
              value={formData.consumerSecret}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit">Submit</button>
        </form>
      </div>
    </div>
  );
};

export default StoresForm;
