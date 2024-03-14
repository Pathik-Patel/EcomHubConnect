import './App.css';
import Login from './Components/Login';
import { Link } from 'react-router-dom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './Components/Register';
import Logout from './Components/Logout';
import Home from './Components/Home';
import StoresForm from './Components/StoresForm';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal, Button } from "react-bootstrap";
import Stores from './Components/Stores';

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Home/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register/>} />
          <Route path="/logout" element={<Logout/>} />
          <Route path="/home" element={<Home/>} />
          <Route path="/addstore" element={<StoresForm/>} />
          <Route path="/mystores" element={<Stores/>} />
          <Route path="*" element={<p>Path not resolved</p>} />
        </Routes>
        
        
        
      </div>
    </Router>
  );
}

export default App;
