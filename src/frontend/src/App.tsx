import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MenuPreviewPage from "./pages/MenuPreviewPage";
import MenuModificationPage from "./pages/MenuModificationPage";
import ResourceModificationPage from "./pages/ResourceModificationPage";

function App() {
  return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<MenuPreviewPage/>}/>
            <Route path="/menuModification" element={<MenuModificationPage/>}/>
            <Route path="/resourceModification" element={<ResourceModificationPage/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
