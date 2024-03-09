import UserService from "../Services/UserService";
import Nav from "./Nav";

const Home = () => {

    const home = () => {
        UserService.userdetails();
    }

    return (
        
        
        <div>
            <Nav/>
            <p>This is Home Page</p>
            <button onClick={home}>Fetch User Details</button>
        </div>
        
        
      );
}
 
export default Home;