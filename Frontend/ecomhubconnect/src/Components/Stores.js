import Nav from "./Nav";
import UserService from "../Services/UserService";
import React, { useEffect, useState } from "react";

const Stores = () => {

    const [storesData, setStoresData] = useState(null);
  const [error, setError] = useState(null);

    let loggedinUser;
  try{
    loggedinUser = sessionStorage.getItem('loggedinUserFirstName');
  }
  catch{
    loggedinUser = false;
  }

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
  
 
 
        return (
            <div>
              <Nav />
              <p>My Stores</p>
              {storesData ? (
                <div>
                  {storesData.map((store) => (
                    <div key={store.storeid}>
                      <p>{store.domain}</p>
                      {/* Render other store details here */}
                    </div>
                  ))}
                </div>
              ) : error ? (
                <p>Error: {error.message}</p>
              ) : (
                <p>Loading...</p>
              )}
            </div>
          );
     
}
 
export default Stores;