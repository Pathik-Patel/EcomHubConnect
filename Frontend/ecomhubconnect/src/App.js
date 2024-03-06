import './App.css';
import Login from './Components/Login';
import { Link } from 'react-router-dom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './Components/Register';
import Logout from './Components/Logout';
import Home from './Components/Home';
function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/" element={<Home/>} />
        </Routes>
        <Routes>
          <Route path="/login" element={<Login/>} />
        </Routes>
        <Routes>
          <Route path="/register" element={<Register/>} />
        </Routes>
        <Routes>
          <Route path="/logout" element={<Logout/>} />
        </Routes>
        <Routes>
          <Route path="/home" element={<Home/>} />
        </Routes>
        
      </div>
    </Router>
  );
}

export default App;
