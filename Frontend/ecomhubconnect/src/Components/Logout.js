import UserService from "../Services/UserService";
import Nav from "./Nav";
import { useState } from "react";
import $ from 'jquery';
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
const Logout = () => {


    const [show, setShow] = useState(false);
    const [modalTitle, setModalTitle] = useState('');
    const navigate = useNavigate();
    const [redirectToLogin, setredirectToLogin] = useState(false); // Define redirectToHome state
    
  
    
    const handleClose = () => {
      setShow(false);
      if (redirectToLogin) {
          navigate('/login');
      }
    };
    const handleShow = () => setShow(true);

    
    const logout = () => {

        UserService.LogoutUser()
            .then(response => {
                setModalTitle('Logout Successful');
        handleShow();
        setredirectToLogin(true);
                // console.log('User created:', response);
                // Do something with the response if needed
            })
            .catch(error => {
                setModalTitle('Login Failed');
        handleShow();
                // console.error('Error creating user:', error);
                // Handle error
            });
    }

    return (
        
        

        <div>
            <Nav/>
            <p>This is Logout Page</p>
            <button onClick={logout}>Logout</button>

            <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>{modalTitle}</Modal.Title>
        </Modal.Header>
        <Modal.Footer>
          <Button variant="primary" onClick={handleClose}>
            Okay
          </Button>
        </Modal.Footer>
      </Modal>

        </div>
        
      );
}
 
export default Logout;