import UserService from "../Services/UserService";
import Nav from "./Nav";
const Logout = () => {

    const logout = () => {
        UserService.LogoutUser();
    }

    return (
        
        

        <div>
            <Nav/>
            <p>This is Logout Page</p>
            <button onClick={logout}>Logout</button>
        </div>
        
      );
}
 
export default Logout;