import Nav from "./Nav";
import { useState } from "react";
import UserService from "../Services/UserService";
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import './../Styles/login.css'; // Import CSS file

const Login = () => {
    const [show, setShow] = useState(false);
    const [modalTitle, setModalTitle] = useState('');
    const navigate = useNavigate();
    const [redirectToHome, setRedirectToHome] = useState(false);

    const handleClose = () => {
        setShow(false);
        if (redirectToHome) {
            navigate('/mystores');
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
            })
            .catch(error => {
                setModalTitle('Login Failed');
                handleShow();
            });
    }

    const handleForgotPassword = () => {
        // Add functionality for forgot password here
        console.log("Forgot Password clicked");
    };
    
    return (
        <div>
            <Nav />
            <div className="login-container">
                <form onSubmit={submit}>
                    <input name="username" type="text" value={formData.username} onChange={handleChange} placeholder="Username" />
                    <input name="password" type="password" value={formData.password} onChange={handleChange} placeholder="Password" />
                    <button type="submit">Submit</button>
                </form>
                <div className="forgot-password">
                    <span onClick={handleForgotPassword}>Forgot Password?</span>
                </div>

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
        </div>
    );
}

export default Login;
