import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./../Styles/nav.css"; // Import CSS file
import Dropdown from 'react-bootstrap/Dropdown';

const Nav = () => {
  let loggedinUser;
  try {
    loggedinUser = sessionStorage.getItem("loggedinUserFirstName");
  } catch {
    loggedinUser = false;
  }

  // State to manage the visibility of the dropdown menu
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  // Function to toggle the visibility of the dropdown menu
  const toggleDropdown = () => {
    console.log(isDropdownOpen);
    setIsDropdownOpen(!isDropdownOpen);
    console.log(isDropdownOpen);
  };

  // Close dropdown when clicking outside of it
  const closeDropdown = () => {
    setIsDropdownOpen(false);
  };

  return (
    <nav className="navbar">
      <ul className="nav-list">

      {!loggedinUser && (
        <>
        <li className="nav-item">
          <Link to="/login" className="nav-link">
            Sign in
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/register" className="nav-link">
            Sign up
          </Link>
        </li>
        </>
      )}
        
        
        {loggedinUser && (
          <>
          <li className="nav-item">
              <Link to="/logout" className="nav-link">Logout</Link>
            </li>
            <li className="nav-item">
              <Link to="/addstore" className="nav-link">
                Add Store
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/mystores" className="nav-link">
                My Stores
              </Link>
            </li>
            
          </>
        )}
      </ul>
      {loggedinUser && (
        <div className="usernav">
        <h3 className="user-greeting">Welcome, {loggedinUser}</h3>
        <Dropdown>
                <Dropdown.Toggle variant="success" id="dropdown-basic" >
                  <img src="user.png" alt="User" />
                </Dropdown.Toggle>

                <Dropdown.Menu>
                  <Dropdown.Item href="#/action-1"><Link to="/editprofile" className="nav-link">
                    Edit Profile
                  </Link></Dropdown.Item>
                  <Dropdown.Item href="#/action-2"> <Link to="/changepassword" className="nav-link">
                    Change Password
                  </Link></Dropdown.Item>
                  <Dropdown.Item href="#/action-3"><Link to="/settings" className="nav-link">
                    Settings
                  </Link></Dropdown.Item>
                  <Dropdown.Item href="#/action-3"><Link to="/settings" className="nav-link">
                    Logout
                  </Link></Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
        </div>
        
      )}
    </nav>
  );
};

export default Nav;
