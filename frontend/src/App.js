import {BrowserRouter as Router, Routes, Route} from "react-router-dom"
import Home from "./components/Home.js"
import Simulation from "./components/Simulation";
import AddElevatorForm from "./components/AddElevatorForm";
import UpdateElevatorForm from "./components/UpdateElevatorForm";

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/simulation" element={<Simulation />} />
              <Route path="/addElevator" element={<AddElevatorForm />} />
              <Route path="/update" element={<UpdateElevatorForm />} />
          </Routes>
      </Router>
  );
}

export default App;
