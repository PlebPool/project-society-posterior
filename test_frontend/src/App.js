import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";

function App() {
  const [mock, setMock] = useState("")

  const thing = () => {
    axios.get("https://localhost:8000/mock", {withCredentials: true}).then((res) => {
      setMock(res.data)
    })
  }

  const logout = (e) => {
    e.preventDefault()
    axios.post("https://localhost:8000/logout", null, {withCredentials: true}).then((res) => {
      console.log(res)
    })
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <h1>{mock}</h1>
        <button onClick={thing}>Click me</button>
        <a href={"https://localhost:8000/login/oauth2/google?redirect=https://localhost:3000/loggedIn"}><button>Login</button></a>
        <iframe name={"logout-frame"} id={"logout-frame"} style={{display: "none"}} title={"title"}/>
        <form action={"https://localhost:8000/logout"} method={"post"} target={"logout-frame"}>
          <input type={"hidden"} name={"_csrf"} value={"4969a6f4-e665-45c9-81fd-e2770b7e576c"}/>
          <input type={"submit"} value={"Logout"}/>
        </form>
      </header>
    </div>
  );
}

export default App;
