import Nav from "./Nav";
import { useState } from "react";
import UserService from "../Services/UserService";
import $ from 'jquery';
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
const Login = () => {

    const [show, setShow] = useState(false);
  const [modalTitle, setModalTitle] = useState('');
  const navigate = useNavigate();
  const [redirectToHome, setRedirectToHome] = useState(false); // Define redirectToHome state
  

  
  const handleClose = () => {
    setShow(false);
    if (redirectToHome) {
        navigate('/home');
    }
  };
  const handleShow = () => setShow(true);

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
                setModalTitle('Login Successful');
        handleShow();
        setRedirectToHome(true);
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
            <Nav />
            <form onSubmit={submit}>
                <input name="username" type="text" value={formData.username} onChange={handleChange} />
                <input name="password" type="password" value={formData.password} onChange={handleChange} />
                <button type="submit">Submit</button>
            </form>

            {/* <Button variant="primary" onClick={submit}>
        Click modal
      </Button> */}
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

export default Login;