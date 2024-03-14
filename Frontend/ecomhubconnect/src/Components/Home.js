import UserService from "../Services/UserService";
import Nav from "./Nav";

const Home = () => {
    let loggedinUser;
  try{
    loggedinUser = sessionStorage.getItem('loggedinUserFirstName');
  }
  catch{
    loggedinUser = false;
  }

    const home = () => {
        UserService.userdetails();
    }

    return (
        
        
        <div>
            <Nav/>
            <p>This is Home Page</p>
            {loggedinUser && <button onClick={home}>Fetch User Details</button>}
        </div>
        
        
      );
}
 
export default Home;