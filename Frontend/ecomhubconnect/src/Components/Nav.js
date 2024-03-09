import { Link } from "react-router-dom";
const Nav = () => {
    return (
        
        <nav>
      <ul>
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/login">Login</Link>
        </li>
        <li>
          <Link to="/register">Register</Link>
        </li>
        <li>
          <Link to="/logout">Logout</Link>
        </li>
        <li>
          <Link to="/addstore">Add Store</Link>
        </li>
      </ul>
    </nav>
        
      );
}
 
export default Nav;