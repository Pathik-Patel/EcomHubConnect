import './App.css';
import Login from './Login';
import { Link } from 'react-router-dom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './Register';
import Logout from './Logout';
import Home from './Home';
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
