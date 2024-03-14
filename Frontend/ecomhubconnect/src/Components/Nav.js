import { Link } from "react-router-dom";
const Nav = () => {
  let loggedinUser;
  try{
    loggedinUser = sessionStorage.getItem('loggedinUserFirstName')
  }
  catch{
    loggedinUser = false;
  }
    return (
        
        <nav>
      <ul>
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/login">Signin</Link>
        </li>
        <li>
          <Link to="/register">Signup</Link>
        </li>
        {loggedinUser && <li>
          <Link to="/logout">Logout</Link>
        </li>}
        {loggedinUser && <li>
          <Link to="/addstore">Add Store</Link>
        </li>}
        {loggedinUser && <li>
          <Link to="/mystores">My Stores</Link>
        </li>}
        
      </ul>
      {loggedinUser && <h3>{loggedinUser}</h3>}
    </nav>
        
      );
}
 
export default Nav;