import React from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MenuPreviewPage from "./pages/MenuPreviewPage";
import MenuModificationPage from "./pages/MenuModificationPage";

function App() {
  return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<MenuPreviewPage/>}/>
            <Route path="/menuModification" element={<MenuModificationPage/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
