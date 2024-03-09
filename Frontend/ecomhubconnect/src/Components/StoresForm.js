import React, { useState } from 'react';

const StoresForm = () => {
  const [formData, setFormData] = useState({
    domain: '',
    consumerKey: '',
    consumerSecret: ''
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
      const sessionId = window.localStorage.getItem('sessionId');
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
    <form onSubmit={handleSubmit}>
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
  );
};

export default StoresForm;
