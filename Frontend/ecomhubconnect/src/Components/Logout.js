import UserService from "../Services/UserService";
import Nav from "./Nav";
import { useState, useEffect } from "react";
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Logout = () => {
    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [showResponseModal, setShowResponseModal] = useState(false);
    const [modalTitle, setModalTitle] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const navigate = useNavigate();
    const [redirectToLogin, setredirectToLogin] = useState(false); // Define redirectToHome state

    useEffect(() => {
        // Show confirm modal when component is loaded
        setShowConfirmModal(true);
    }, []);

    const handleCloseConfirmModal = () => {
        setShowConfirmModal(false);
        // Redirect to previous page when user closes the confirm modal
        navigate(-1);
    };

    const handleCloseResponseModal = () => {
        setShowResponseModal(false);
        if (redirectToLogin) {
            navigate('/login');
        }
    };

    const handleLogout = () => {
        UserService.LogoutUser()
            .then(response => {
                setModalTitle('Logout Successful');
                setShowConfirmModal(false);
                setResponseMessage(response);
                setShowResponseModal(true);
                setredirectToLogin(true);
            })
            .catch(error => {
                setModalTitle('Logout Failed');
                setResponseMessage(error.message);
                setShowResponseModal(true);
            });
    };

    return (
        <div>
            <Nav />
            <Modal show={showConfirmModal} onHide={handleCloseConfirmModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Logout</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to logout?
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseConfirmModal}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleLogout}>
                        Logout
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showResponseModal} onHide={handleCloseResponseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{modalTitle}</Modal.Title>
                </Modal.Header>
                <Modal.Body>{responseMessage}</Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleCloseResponseModal}>
                        Okay
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default Logout;
