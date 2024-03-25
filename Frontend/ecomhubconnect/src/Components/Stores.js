import Nav from "./Nav";
import UserService from "../Services/UserService";
import React, { useEffect, useState } from "react";

const Stores = () => {

    const [storesData, setStoresData] = useState(null);
  const [error, setError] = useState(null);
  const [responseData, setResponseData] = useState(null);

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

  // const handleClick = async (storeid) => {
  //   try {
  //     const response = await fetch(`http://localhost:8080/woocommerce/syncorders/${storeid}`);
  //     if (!response.ok) {
  //       throw new Error('Failed to fetch data');
  //     }
  //     const data = await response.text();
  //     setResponseData(data);
  //   } catch (error) {
  //     setError(error);
  //   }
  // };


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
                    <div key={store.storeid} >
                      <p onClick={() => handleClick(store.storeid)} >{store.domain}</p>
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