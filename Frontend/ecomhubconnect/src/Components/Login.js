import Nav from "./Nav";
import { useState } from "react";
import UserService from "../Services/UserService";
const Login = () => {

    const [formData, setFormData] = useState({
        username: '',
        password: ''
      });

    const handleChange = (e) => {
    setFormData({
        ...formData,
        [e.target.name]: e.target.value
    });
    };

    const submit = (e) => {

        e.preventDefault();
        UserService.login(formData)
        .then(response => {
            // console.log('User created:', response);
            // Do something with the response if needed
        })
        .catch(error => {
            // console.error('Error creating user:', error);
            // Handle error
        });
        
    }

    return (
        
        <div>
            <Nav/>
            <form onSubmit={submit}>
                <input name="username" type="text" value={formData.username} onChange={handleChange}/>
                <input name="password" type="password" value={formData.password} onChange={handleChange}/>
                <button type="submit">Submit</button>
            </form>
        </div>
        
      );
}
 
export default Login;