import Nav from "./Nav";
import UserService from "../Services/UserService";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./../Styles/stores.css"; // Import CSS file

const Stores = () => {
  const [storesData, setStoresData] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchStores();
  }, []);

  const fetchStores = () => {
    UserService.stores()
      .then((response) => {
        setStoresData(response);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const handleClick = (storeid) => {
    UserService.syncorders(storeid)
      .then((response) => {
        navigate('/orders', { state: { orders: response } });
      })
      .catch((error) => {
        setError(error);
      });
  };

  // Function to handle edit button click
  const handleEdit = (storeid) => {
    // Add functionality for edit button here
    console.log("Edit clicked for store ID:", storeid);
  };

  return (
    <div>
      <Nav />
      <div className="stores-container">
        <h2>My Stores</h2>
        {storesData ? (
          <div className="stores-list">
            {storesData.map((store) => (
              <div key={store.storeid} className="store-item">
                <div className="store-details store-clickable" onClick={() => handleClick(store.storeid)}>
                  <p className="store-domain">{store.domain}</p>
                  {/* Edit button */}
                  <button className="edit-button" onClick={() => handleEdit(store.storeid)}>Edit</button>
                </div>
                {/* Clicking on store item also triggers navigation */}
                
              </div>
            ))}
          </div>
        ) : error ? (
          <p>Error: {error.message}</p>
        ) : (
          <p>Loading...</p>
        )}
      </div>
    </div>
  );
};

export default Stores;
